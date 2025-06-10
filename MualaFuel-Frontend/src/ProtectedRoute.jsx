import { Navigate } from "react-router-dom";
import { useSelector } from "react-redux";
import React from "react";

export const ProtectedRoute = ({ children, allowedRoles }) => {
    const { reqUser, loading } = useSelector(state => state.auth);

    if (loading || reqUser === null) {
        return
    }

    const userRoles = Array.from(reqUser.roles || []).map(role => role.name);
    const hasAccess = userRoles.some(roleName => allowedRoles.includes(roleName));

    if (!hasAccess) {
        return <Navigate to="/home" replace />;
    }

    return children;
};