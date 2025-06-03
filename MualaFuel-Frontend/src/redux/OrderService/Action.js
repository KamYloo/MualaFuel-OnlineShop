import { dispatchAction } from "../api.js";
import {
    FETCH_ORDERS_REQUEST,
    FETCH_ORDERS_SUCCESS,
    FETCH_ORDERS_ERROR,
} from "./ActionType.js";

export const fetchOrdersAction = () => async (dispatch) => {
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