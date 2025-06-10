import {
    FETCH_ORDERS_REQUEST,
    FETCH_ORDERS_SUCCESS,
    FETCH_ORDERS_ERROR,
    FETCH_ADMIN_ORDERS_REQUEST,
    CANCEL_ADMIN_ORDER_REQUEST,
    UPDATE_ADMIN_ORDER_REQUEST,
    FETCH_ADMIN_ORDERS_SUCCESS,
    UPDATE_ADMIN_ORDER_SUCCESS,
    CANCEL_ADMIN_ORDER_SUCCESS,
    FETCH_ADMIN_ORDERS_ERROR,
    UPDATE_ADMIN_ORDER_ERROR, CANCEL_ADMIN_ORDER_ERROR,
} from "./ActionType.js";

const initialState = {
    loading: false,
    orders: [],
    error: null,
    updateLoading: false,
    cancelLoading: false,
};

export const orderReducer = (state = initialState, action) => {
    switch (action.type) {
        case FETCH_ORDERS_REQUEST:
            return { ...state, loading: true, error: null };
        case FETCH_ORDERS_SUCCESS:
            return { ...state, loading: false, orders: action.payload };
        case FETCH_ORDERS_ERROR:
            return { ...state, loading: false, error: action.payload };

        case FETCH_ADMIN_ORDERS_REQUEST:
            return { ...state, loading: true, error: null };
        case FETCH_ADMIN_ORDERS_SUCCESS:
            return { ...state, loading: false, orders: action.payload };
        case FETCH_ADMIN_ORDERS_ERROR:
            return { ...state, loading: false, error: action.payload };

        case UPDATE_ADMIN_ORDER_REQUEST:
            return { ...state, updateLoading: true, error: null };
        case UPDATE_ADMIN_ORDER_SUCCESS:
            return { ...state, updateLoading: false };
        case UPDATE_ADMIN_ORDER_ERROR:
            return { ...state, updateLoading: false, error: action.payload };

        case CANCEL_ADMIN_ORDER_REQUEST:
            return { ...state, cancelLoading: true, error: null };
        case CANCEL_ADMIN_ORDER_SUCCESS:
            return { ...state, cancelLoading: false };
        case CANCEL_ADMIN_ORDER_ERROR:
            return { ...state, cancelLoading: false, error: action.payload };

        default:
            return state;
    }
};