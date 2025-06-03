import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchOrdersAction } from "../redux/OrderService/Action.js";

function Orders() {
    const dispatch = useDispatch();
    const { orders, loading, error } = useSelector((state) => state.orderService);

    useEffect(() => {
        dispatch(fetchOrdersAction());
    }, [dispatch]);

    if (loading) {
        return <div className="text-center p-8 text-gray-500">Loading orders...</div>;
    }

    if (error) {
        return <div className="text-center p-8 text-red-500">Error: {error}</div>;
    }

    return (
        <div className="min-h-screen p-8" style={{ backgroundColor: "#f5e9dc" }}>
            <div className="max-w-4xl mx-auto bg-white rounded-lg shadow-md p-6">
                <h1 className="text-3xl font-bold text-[#3E2723] mb-6">Order History</h1>
                {orders.length === 0 ? (
                    <p className="text-center text-gray-500">No orders found</p>
                ) : (
                    <div className="space-y-6">
                        {orders.map((order) => (
                            <div key={order.id} className="border rounded-lg p-4 shadow-sm">
                                <div className="flex justify-between items-start mb-4">
                                    <div>
                                        <h2 className="text-xl font-semibold text-[#3E2723]">Order # {order.id}</h2>
                                        <p className="text-gray-500 text-sm">
                                            {new Date(order.orderDate).toLocaleDateString()}
                                        </p>
                                    </div>
                                    <div className="text-right">
                                        <p className={`font-medium ${order.status === 'PAID' ? 'text-green-600' : 'text-red-600'}`}>
                                            {order.status}
                                        </p>
                                        <p className="text-lg font-bold text-[#3E2723]">
                                            {order.totalAmount.toFixed(2)} zł
                                        </p>
                                    </div>
                                </div>

                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                                    <div className="border p-3 rounded">
                                        <h3 className="font-semibold mb-2 text-[#3E2723]">Shipping Details</h3>
                                        <p>{order.shippingDetails.shipping_street}</p>
                                        <p>{order.shippingDetails.shipping_city}</p>
                                        <p>{order.shippingDetails.shipping_zipCode}</p>
                                        <p>{order.shippingDetails.shipping_country}</p>
                                    </div>

                                    <div className="border p-3 rounded">
                                        <h3 className="font-semibold mb-2 text-[#3E2723]">Payment Details</h3>
                                        <p>Method: {order.paymentDetails.payment_method.replace('_', ' ')}</p>
                                        <p>Status: {order.paymentDetails.payment_status}</p>
                                        <p>Transaction ID: {order.paymentDetails.payment_transactionId}</p>
                                    </div>
                                </div>

                                <div className="border-t pt-4">
                                    <h3 className="font-semibold mb-3 text-[#3E2723]">Order Items</h3>
                                    {order.orderItems.map((item) => (
                                        <div key={item.productId} className="flex justify-between items-center py-2">
                                            <div>
                                                <p className="font-medium text-[#3E2723]">{item.productName}</p>
                                                <p className="text-sm text-gray-500">
                                                    Quantity: {item.quantity}
                                                </p>
                                            </div>
                                            <div className="text-right">
                                                <p>{item.unitPrice.toFixed(2)} zł each</p>
                                                <p className="font-medium text-[#3E2723]">
                                                    {(item.unitPrice * item.quantity).toFixed(2)} zł
                                                </p>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export { Orders };
