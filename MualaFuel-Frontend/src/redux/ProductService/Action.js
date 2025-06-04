import { dispatchAction } from "../api.js";
import {
    SAVE_REQUEST,
    SAVE_SUCCESS,
    SAVE_ERROR,
    FETCH_PRODUCTS_REQUEST,
    FETCH_PRODUCTS_ERROR,
    FETCH_PRODUCTS_SUCCESS,
    DELETE_PRODUCT_REQUEST,
    DELETE_PRODUCT_SUCCESS, DELETE_PRODUCT_ERROR, UPDATE_PRODUCT_REQUEST, UPDATE_PRODUCT_SUCCESS, UPDATE_PRODUCT_ERROR,
} from "./ActionType.js";

export const saveProductAction = (formData) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        SAVE_REQUEST,
        SAVE_SUCCESS,
        SAVE_ERROR,
        "/product/save",
        {
            method: "POST",
            body: formData,
        }
    );
};

export const fetchProductsAction = (searchCriteria, page = 0, size = 9) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        FETCH_PRODUCTS_REQUEST,
        FETCH_PRODUCTS_SUCCESS,
        FETCH_PRODUCTS_ERROR,
        `/product/find?page=${page}&size=${size}`,
        {
            method: "POST",
            body: JSON.stringify(searchCriteria),
            headers: { "Content-Type": "application/json" },
        }
    );
};

export const deleteProductAction = (id) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        DELETE_PRODUCT_REQUEST,
        DELETE_PRODUCT_SUCCESS,
        DELETE_PRODUCT_ERROR,
        `/product/${id}`,
        {
            method: "DELETE",
        }
    );
};

export const updateProductAction = (productData) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        UPDATE_PRODUCT_REQUEST,
        UPDATE_PRODUCT_SUCCESS,
        UPDATE_PRODUCT_ERROR,
        "/product/update",
        {
            method: "PUT",
            body: productData,
        }
    );
};