import {
    FETCH_CART_REQUEST,
    FETCH_CART_SUCCESS,
    FETCH_CART_ERROR,
    REMOVE_ITEM_REQUEST,
    REMOVE_ITEM_SUCCESS,
    REMOVE_ITEM_ERROR,
    UPDATE_QUANTITY_REQUEST,
    UPDATE_QUANTITY_SUCCESS,
    UPDATE_QUANTITY_ERROR,
    PLACE_ORDER_FROM_CART_REQUEST,
    PLACE_ORDER_FROM_CART_SUCCESS,
    PLACE_ORDER_FROM_CART_ERROR,
    ADD_ITEM_REQUEST,
    ADD_ITEM_SUCCESS,
    ADD_ITEM_ERROR,
} from "./ActionType.js";

const initialState = {
    loading: false,
    error: null,
    cart: {
        items: [],
        totalPrice: 0,
    },
    removing: false,
    updateLoading: false,
    orderPlacing: false,
    orderPlaceSuccess: null,
    addLoading: false,
};

export const cartReducer = (state = initialState, action) => {
    switch (action.type) {
        case FETCH_CART_REQUEST:
            return { ...state, loading: true, error: null };
        case FETCH_CART_SUCCESS:
            return { ...state, loading: false, cart: action.payload };
        case FETCH_CART_ERROR:
            return { ...state, loading: false, error: action.payload };

        case REMOVE_ITEM_REQUEST:
            return { ...state, removing: true };
        case REMOVE_ITEM_SUCCESS:
            return { ...state, removing: false };
        case REMOVE_ITEM_ERROR:
            return { ...state, removing: false, error: action.payload };

        case UPDATE_QUANTITY_REQUEST:
            return { ...state, updateLoading: true };
        case UPDATE_QUANTITY_SUCCESS:
            return { ...state, updateLoading: false };
        case UPDATE_QUANTITY_ERROR:
            return { ...state, updateLoading: false, error: action.payload };

        case PLACE_ORDER_FROM_CART_REQUEST:
            return { ...state, orderPlacing: true, orderPlaceSuccess: null };
        case PLACE_ORDER_FROM_CART_SUCCESS:
            return { ...state, orderPlacing: false, orderPlaceSuccess: action.payload };
        case PLACE_ORDER_FROM_CART_ERROR:
            return { ...state, orderPlacing: false, error: action.payload };

        case ADD_ITEM_REQUEST:
            return { ...state, addLoading: true, error: null };
        case ADD_ITEM_SUCCESS:
            return { ...state, addLoading: false };
        case ADD_ITEM_ERROR:
            return { ...state, addLoading: false, error: action.payload };

        default:
            return state;
    }
};