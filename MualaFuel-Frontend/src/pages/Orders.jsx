import React, { useState, useEffect } from "react";
import axios from "axios";

function Orders() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const response = await axios.get("http://localhost:8080/api/orders", {
                    withCredentials: true
                });
                setOrders(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchOrders();
    }, []);

    if (loading) {
        return <div className="text-center p-8 text-gray-500">Loading orders...</div>;
    }

    if (error) {
        return <div className="text-center p-8 text-red-500">Error: {error}</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-5">
            <h1 className="text-2xl font-bold mb-6">Order History</h1>

            {orders.length === 0 ? (
                <p className="text-center text-gray-500">No orders found</p>
            ) : (
                <div className="space-y-6">
                    {orders.map((order) => (
                        <div key={order.id} className="border rounded-lg p-4 shadow-sm">
                            <div className="flex justify-between items-start mb-4">
                                <div>
                                    <h2 className="text-lg font-semibold">Order # {order.id}</h2>
                                    <p className="text-gray-500 text-sm">
                                        {new Date(order.orderDate).toLocaleDateString()}
                                    </p>
                                </div>
                                <div className="text-right">
                                    <p className={`font-medium ${
                                        order.status === 'PAID' ? 'text-green-600' : 'text-red-600'
                                    }`}>
                                        {order.status}
                                    </p>
                                    <p className="text-lg font-bold">
                                        {order.totalAmount.toFixed(2)} zł
                                    </p>
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                                <div className="border p-3 rounded">
                                    <h3 className="font-semibold mb-2">Shipping Details</h3>
                                    <p>{order.shippingDetails.shipping_street}</p>
                                    <p>{order.shippingDetails.shipping_city}</p>
                                    <p>{order.shippingDetails.shipping_zipCode}</p>
                                    <p>{order.shippingDetails.shipping_country}</p>
                                </div>

                                <div className="border p-3 rounded">
                                    <h3 className="font-semibold mb-2">Payment Details</h3>
                                    <p>Method: {order.paymentDetails.payment_method.replace('_', ' ')}</p>
                                    <p>Status: {order.paymentDetails.payment_status}</p>
                                    <p>Transaction ID: {order.paymentDetails.payment_transactionId}</p>
                                </div>
                            </div>

                            <div className="border-t pt-4">
                                <h3 className="font-semibold mb-3">Order Items</h3>
                                {order.orderItems.map((item) => (
                                    <div key={item.productId} className="flex justify-between items-center py-2">
                                        <div>
                                            <p className="font-medium">{item.productName}</p>
                                            <p className="text-sm text-gray-500">
                                                Quantity: {item.quantity}
                                            </p>
                                        </div>
                                        <div className="text-right">
                                            <p>{item.unitPrice.toFixed(2)} zł each</p>
                                            <p className="font-medium">
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
    );
}

export { Orders };