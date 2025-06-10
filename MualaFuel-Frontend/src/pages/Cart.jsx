import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { 
    fetchCartAction, 
    removeItemAction, 
    updateQuantityAction, 
    placeOrderFromCartAction 
} from "../redux/CartService/Action.js";
import Spinner from "../components/Spinner.jsx";
import ErrorOverlay from "../components/ErrorOverlay.jsx";

function Cart() {
    const dispatch = useDispatch();
    const { cart, loading, error, orderPlaceSuccess, removing, updateLoading} = useSelector((state) => state.cartService);
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

    useEffect(() => {
        dispatch(fetchCartAction());
    }, [dispatch, removing, orderPlaceSuccess, updateLoading]);

    const handleCheckout = (e) => {
        e.preventDefault();
        const transactionId = `txn_${Math.random().toString(36).substr(2, 9)}`;
        const orderRequest = {
            shippingDetails: orderData.shippingDetails,
            paymentDetails: {
                ...orderData.paymentDetails,
                payment_transactionId: transactionId
            }
        };
        dispatch(placeOrderFromCartAction(orderRequest));
        setShowCheckoutForm(false);
    };

    if (loading) return <Spinner size={350} />
    if (error) return <ErrorOverlay size={350} message={error}/>

    return (
        <div className="min-h-screen p-8" style={{ backgroundColor: "#f5e9dc" }}>
            <div className="bg-white rounded-lg shadow-md p-6 max-w-4xl mx-auto">
                <h2 className="text-3xl font-bold text-[#3E2723] mb-6">Your Shopping Cart</h2>
                {cart.items.length === 0 ? (
                    <p className="text-gray-500 text-center">Your cart is currently empty</p>
                ) : (
                    <>
                        <div className="space-y-4">
                            {cart.items.map((item) => (
                                <div key={item.productId} className="bg-gray-50 rounded-lg shadow-sm p-4">
                                    <div className="flex justify-between items-center">
                                        <h3 className="text-xl font-semibold text-[#3E2723]">{item.productName}</h3>
                                        <div className="flex items-center gap-4">
                                            <p className="text-lg font-medium text-[#3E2723]">
                                                ${item.totalPrice.toFixed(2)}
                                            </p>
                                            <button
                                                onClick={() => dispatch(removeItemAction(item.productId))}
                                                className="bg-[#3E2723] text-white px-3 py-1 rounded hover:bg-[#4E3423] transition-colors"
                                            >
                                                Remove
                                            </button>
                                        </div>
                                    </div>
                                    <div className="flex items-center gap-4 mt-2 text-gray-600">
                                        <p>Price: ${item.price.toFixed(2)}</p>
                                        <div className="flex items-center border rounded">
                                            <button
                                                onClick={() => dispatch(updateQuantityAction(item.productId, item.quantity - 1))}
                                                className="px-3 py-1 bg-white border-r hover:bg-gray-100 transition-colors"
                                            >
                                                -
                                            </button>
                                            <span className="px-3">{item.quantity}</span>
                                            <button
                                                onClick={() => dispatch(updateQuantityAction(item.productId, item.quantity + 1))}
                                                className="px-3 py-1 bg-white border-l hover:bg-gray-100 transition-colors"
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
                                <h3 className="text-2xl font-bold text-[#3E2723]">Total Price:</h3>
                                <p className="text-2xl font-bold text-[#3E2723]">
                                    ${cart.totalPrice.toFixed(2)}
                                </p>
                            </div>
                            <button
                                onClick={() => setShowCheckoutForm(true)}
                                className="w-full bg-[#3E2723] hover:bg-[#4E3423] text-white py-2 px-4 rounded transition-colors"
                            >
                                Proceed to Checkout
                            </button>
                        </div>
                    </>
                )}
            </div>

            {showCheckoutForm && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
                    <div className="bg-white rounded-lg shadow-md p-6 w-full max-w-md">
                        <h2 className="text-2xl font-bold text-[#3E2723] mb-4">Checkout</h2>
                        <form onSubmit={handleCheckout}>
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
                                    className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded transition-colors"
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="bg-[#3E2723] hover:bg-[#4E3423] text-white px-4 py-2 rounded transition-colors"
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