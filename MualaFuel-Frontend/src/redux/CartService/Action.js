import {dispatchAction} from "../api.js";
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
} from "./ActionType.js";

export const fetchCartAction = () => async (dispatch) => {
    await dispatchAction(
        dispatch,
        FETCH_CART_REQUEST,
        FETCH_CART_SUCCESS,
        FETCH_CART_ERROR,
        '/cart',
        {
            method: 'GET'
        }
    );
};

export const removeItemAction = (productId) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        REMOVE_ITEM_REQUEST,
        REMOVE_ITEM_SUCCESS,
        REMOVE_ITEM_ERROR,
        `/cart/items/${productId}`,
        {
            method: 'DELETE'
        }
    );
};

export const updateQuantityAction = (productId, quantity) => async (dispatch) => {
    if (quantity < 1) return;
    await dispatchAction(
        dispatch,
        UPDATE_QUANTITY_REQUEST,
        UPDATE_QUANTITY_SUCCESS,
        UPDATE_QUANTITY_ERROR,
        '/cart/items',
        {
            method: 'PUT',
            body: JSON.stringify({ productId, quantity }),
        }
    );
};


export const placeOrderFromCartAction = (orderRequest) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        PLACE_ORDER_FROM_CART_REQUEST,
        PLACE_ORDER_FROM_CART_SUCCESS,
        PLACE_ORDER_FROM_CART_ERROR,
        '/orders',
        {
            method: 'POST',
            body: JSON.stringify(orderRequest),
        }
    );
};