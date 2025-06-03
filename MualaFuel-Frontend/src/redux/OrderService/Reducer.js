import {
    FETCH_ORDERS_REQUEST,
    FETCH_ORDERS_SUCCESS,
    FETCH_ORDERS_ERROR,
} from "./ActionType.js";

const initialState = {
    loading: false,
    orders: [],
    error: null,
};

export const orderReducer = (state = initialState, action) => {
    switch (action.type) {
        case FETCH_ORDERS_REQUEST:
            return { ...state, loading: true, error: null };
        case FETCH_ORDERS_SUCCESS:
            return { ...state, loading: false, orders: action.payload };
        case FETCH_ORDERS_ERROR:
            return { ...state, loading: false, error: action.payload };
        default:
            return state;
    }
};