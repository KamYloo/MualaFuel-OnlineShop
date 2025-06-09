import React from "react";

export function OrderCard({ order, variant = "user", onUpdate, onCancel }) {
    return (
        <div className="border rounded-lg p-4 shadow-sm">
            <div className="flex justify-between items-start mb-4">
                <div>
                    <h2 className="text-xl font-semibold text-[#3E2723]">
                        Order # {order.id}
                    </h2>
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

            {variant === "user" && (
                <div className="border-t pt-4">
                    <h3 className="font-semibold mb-3 text-[#3E2723]">Order Items</h3>
                    {order.orderItems.map((item) => (
                        <div key={item.productId} className="flex justify-between items-center py-2">
                            <div>
                                <p className="font-medium text-[#3E2723]">{item.productName}</p>
                                <p className="text-sm text-gray-500">Quantity: {item.quantity}</p>
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
            )}

            {variant === "admin" && (
                <div className="border-t pt-4 flex justify-end gap-4">
                    <button
                        onClick={() => onUpdate(order.id)}
                        className="bg-[#3E2723] hover:bg-[#4E2723] text-white px-4 py-2 rounded transition-colors"
                    >
                        Update Status
                    </button>
                    <button
                        onClick={() => onCancel(order.id)}
                        className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded transition-colors"
                    >
                        Cancel Order
                    </button>
                </div>
            )}
        </div>
    );
}