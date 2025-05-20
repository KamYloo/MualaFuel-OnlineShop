import React, { useState, useEffect } from "react";
import axios from "axios";

function Cart() {
    const [cartData, setCartData] = useState({
        items: [],
        totalPrice: 0
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showCheckoutForm, setShowCheckoutForm] = useState(false);
    const [orderData, setOrderData] = useState({
        shippingDetails: {
            shipping_country: "",
            shipping_city: "",
            shipping_zipCode: "",
            shipping_street: ""
        },
        paymentDetails: {
            payment_method: "CREDIT_CARD",
            payment_status: "PENDING"
        }
    });

    const fetchCartData = async () => {
        try {
            setLoading(true);
            const response = await axios.get("http://localhost:8080/api/cart", {
                withCredentials: true
            });
            setCartData(response.data);
            setError(null);
        } catch (error) {
            console.error("Error fetching cart data:", error);
            setError("Failed to load cart data");
        } finally {
            setLoading(false);
        }
    };

    const removeItem = async (productId) => {
        try {
            await axios.delete(`http://localhost:8080/api/cart/items/${productId}`, {
                withCredentials: true
            });
            fetchCartData();
        } catch (error) {
            console.error("Error removing item:", error);
        }
    };

    const updateQuantity = async (productId, newQuantity) => {
        if (newQuantity < 1) return;

        try {
            await axios.put(
                "http://localhost:8080/api/cart/items",
                { productId, quantity: newQuantity },
                { withCredentials: true }
            );
            fetchCartData();
        } catch (error) {
            console.error("Error updating quantity:", error);
        }
    };

    const handleCheckout = async () => {
        try {
            const transactionId = `txn_${Math.random().toString(36).substr(2, 9)}`;

            const orderRequest = {
                shippingDetails: orderData.shippingDetails,
                paymentDetails: {
                    ...orderData.paymentDetails,
                    payment_transactionId: transactionId
                }
            };

            const response = await axios.post(
                "http://localhost:8080/api/orders",
                orderRequest,
                {
                    withCredentials: true,
                    headers: {
                        "Content-Type": "application/json"
                    }
                }
            );

            console.log("Order placed successfully:", response.data);
            fetchCartData();
            setShowCheckoutForm(false);
            alert("Order placed successfully!");
        } catch (error) {
            console.error("Error placing order:", error);
            setError("Failed to place order");
        }
    };

    useEffect(() => {
        fetchCartData();
    }, []);

    if (loading) {
        return <div className="text-center p-8 text-gray-500">Loading cart...</div>;
    }

    if (error) {
        return <div className="text-center p-8 text-red-500">{error}</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-5">
            <h2 className="text-2xl font-bold mb-6">Your Shopping Cart</h2>
            {cartData.items.length === 0 ? (
                <p className="text-gray-500 text-center">Your cart is currently empty</p>
            ) : (
                <>
                    <div className="space-y-4">
                        {cartData.items.map((item) => (
                            <div key={item.productId} className="border-b border-gray-200 pb-4">
                                <div className="flex justify-between items-start">
                                    <h3 className="text-lg font-semibold">{item.productName}</h3>
                                    <div className="flex items-center gap-4">
                                        <p className="text-lg font-medium">
                                            ${item.totalPrice.toFixed(2)}
                                        </p>
                                        <button
                                            onClick={() => removeItem(item.productId)}
                                            className="text-red-500 hover:text-red-700"
                                        >
                                            Remove
                                        </button>
                                    </div>
                                </div>
                                <div className="flex items-center gap-4 mt-2 text-gray-600">
                                    <p>Price: ${item.price.toFixed(2)}</p>
                                    <div className="flex items-center border rounded">
                                        <button
                                            onClick={() => updateQuantity(item.productId, item.quantity - 1)}
                                            className="px-3 py-1 hover:bg-gray-100"
                                        >
                                            -
                                        </button>
                                        <span className="px-3">{item.quantity}</span>
                                        <button
                                            onClick={() => updateQuantity(item.productId, item.quantity + 1)}
                                            className="px-3 py-1 hover:bg-gray-100"
                                        >
                                            +
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="mt-6 pt-4 border-t border-gray-200">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-bold">Total Price:</h3>
                            <p className="text-xl font-bold">
                                ${cartData.totalPrice.toFixed(2)}
                            </p>
                        </div>
                        <button
                            onClick={() => setShowCheckoutForm(true)}
                            className="w-full bg-green-600 hover:bg-green-700 text-white py-2 px-4 rounded"
                        >
                            Proceed to Checkout
                        </button>
                    </div>
                </>
            )}

            {showCheckoutForm && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">Checkout</h2>
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            handleCheckout();
                        }}>
                            <div className="mb-4">
                                <label className="block text-gray-700 mb-2">Country</label>
                                <input
                                    type="text"
                                    className="w-full p-2 border rounded"
                                    value={orderData.shippingDetails.shipping_country}
                                    onChange={(e) => setOrderData({
                                        ...orderData,
                                        shippingDetails: {
                                            ...orderData.shippingDetails,
                                            shipping_country: e.target.value
                                        }
                                    })}
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 mb-2">City</label>
                                <input
                                    type="text"
                                    className="w-full p-2 border rounded"
                                    value={orderData.shippingDetails.shipping_city}
                                    onChange={(e) => setOrderData({
                                        ...orderData,
                                        shippingDetails: {
                                            ...orderData.shippingDetails,
                                            shipping_city: e.target.value
                                        }
                                    })}
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 mb-2">Zip Code</label>
                                <input
                                    type="text"
                                    className="w-full p-2 border rounded"
                                    value={orderData.shippingDetails.shipping_zipCode}
                                    onChange={(e) => setOrderData({
                                        ...orderData,
                                        shippingDetails: {
                                            ...orderData.shippingDetails,
                                            shipping_zipCode: e.target.value
                                        }
                                    })}
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 mb-2">Street Address</label>
                                <input
                                    type="text"
                                    className="w-full p-2 border rounded"
                                    value={orderData.shippingDetails.shipping_street}
                                    onChange={(e) => setOrderData({
                                        ...orderData,
                                        shippingDetails: {
                                            ...orderData.shippingDetails,
                                            shipping_street: e.target.value
                                        }
                                    })}
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 mb-2">Payment Method</label>
                                <select
                                    className="w-full p-2 border rounded"
                                    value={orderData.paymentDetails.payment_method}
                                    onChange={(e) => setOrderData({
                                        ...orderData,
                                        paymentDetails: {
                                            ...orderData.paymentDetails,
                                            payment_method: e.target.value
                                        }
                                    })}
                                    required
                                >
                                    <option value="CREDIT_CARD">Credit Card</option>
                                    <option value="BANK_TRANSFER">Bank Transfer</option>
                                    <option value="PAYPAL">PayPal</option>
                                </select>
                            </div>
                            <div className="flex justify-end gap-2">
                                <button
                                    type="button"
                                    onClick={() => setShowCheckoutForm(false)}
                                    className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded"
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded"
                                >
                                    Place Order
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export { Cart };