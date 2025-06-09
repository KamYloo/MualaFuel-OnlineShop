import { dispatchAction } from "../api.js";
import {
    FETCH_ORDERS_REQUEST,
    FETCH_ORDERS_SUCCESS,
    FETCH_ORDERS_ERROR,
    FETCH_ADMIN_ORDERS_REQUEST,
    FETCH_ADMIN_ORDERS_SUCCESS,
    FETCH_ADMIN_ORDERS_ERROR,
    UPDATE_ADMIN_ORDER_REQUEST,
    UPDATE_ADMIN_ORDER_SUCCESS,
    UPDATE_ADMIN_ORDER_ERROR,
    CANCEL_ADMIN_ORDER_REQUEST,
    CANCEL_ADMIN_ORDER_SUCCESS, CANCEL_ADMIN_ORDER_ERROR,
} from "./ActionType.js";

export const fetchOwnOrdersAction = () => async (dispatch) => {
    await dispatchAction(
        dispatch,
        FETCH_ORDERS_REQUEST,
        FETCH_ORDERS_SUCCESS,
        FETCH_ORDERS_ERROR,
        '/orders',
        {
            method: 'GET'
        }
    );
};

export const fetchAdminOrdersAction = () => async (dispatch) => {
    await dispatchAction(
        dispatch,
        FETCH_ADMIN_ORDERS_REQUEST,
        FETCH_ADMIN_ORDERS_SUCCESS,
        FETCH_ADMIN_ORDERS_ERROR,
        "/admin/orders",
        { method: "GET" }
    );
};

export const updateAdminOrderAction = (orderId) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        UPDATE_ADMIN_ORDER_REQUEST,
        UPDATE_ADMIN_ORDER_SUCCESS,
        UPDATE_ADMIN_ORDER_ERROR,
        `/admin/orders/${orderId}`,
        { method: "PUT" }
    );
};

export const cancelAdminOrderAction = (orderId) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        CANCEL_ADMIN_ORDER_REQUEST,
        CANCEL_ADMIN_ORDER_SUCCESS,
        CANCEL_ADMIN_ORDER_ERROR,
        `/admin/orders/${orderId}`,
        { method: "DELETE" }
    );
};