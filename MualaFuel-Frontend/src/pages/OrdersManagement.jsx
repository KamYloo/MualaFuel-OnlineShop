import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { cancelAdminOrderAction, fetchAdminOrdersAction, updateAdminOrderAction } from "../redux/OrderService/Action.js";
import toast from "react-hot-toast";
import Spinner from "../components/Spinner.jsx";
import ErrorOverlay from "../components/ErrorOverlay.jsx";
import {OrderCard} from "../features/Orders/OrderCard.jsx";

function OrdersManagement() {
    const dispatch = useDispatch();
    const { orders, loading, error } = useSelector(state => state.orderService);

    useEffect(() => {
        dispatch(fetchAdminOrdersAction());
    }, [dispatch]);

    const handleUpdate = orderId => {
        dispatch(updateAdminOrderAction(orderId))
            .then(() => toast.success("Order status updated"))
            .catch(() => toast.error(error));
    };

    const handleCancel = orderId => {
        if (window.confirm("Are you sure you want to cancel this order?")) {
            dispatch(cancelAdminOrderAction(orderId))
                .then(() => toast.success("Order cancelled"))
                .catch(() => toast.error(error));
        }
    };

    if (loading) return <Spinner size={350} />
    if (error) return <ErrorOverlay size={350} message={error}/>

    return (
        <div className="min-h-screen p-8" style={{ backgroundColor: "#f5e9dc" }}>
            <div className="max-w-4xl mx-auto bg-white rounded-lg shadow-md p-6">
                <h1 className="text-3xl font-bold text-[#3E2723] mb-6">Admin Orders Panel</h1>
                {orders.length === 0 ? (
                    <p className="text-center text-gray-500">No orders found</p>
                ) : (
                    <div className="space-y-6">
                        {orders.map(order => (
                            <OrderCard
                                key={order.id}
                                order={order}
                                variant="admin"
                                onUpdate={handleUpdate}
                                onCancel={handleCancel}
                            />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export { OrdersManagement };