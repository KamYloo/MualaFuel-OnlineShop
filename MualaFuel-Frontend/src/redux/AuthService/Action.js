import {dispatchAction} from "../api.js"
import {
    LOGIN_ERROR,
    LOGIN_REQUEST,
    LOGIN_SUCCESS, LOGOUT_ERROR, LOGOUT_REQUEST, LOGOUT_SUCCESS,
    REGISTER_ERROR,
    REGISTER_REQUEST,
    REGISTER_SUCCESS, REQUEST_USER, REQUEST_USER_ERROR, REQUEST_USER_SUCCESS
} from "./ActionType.js";

export const registerAction = (data) => async (dispatch) => {
    await dispatchAction(dispatch, REGISTER_REQUEST, REGISTER_SUCCESS, REGISTER_ERROR, '/auth/register',
        {
            method: 'POST',
            body: JSON.stringify(data),
        }
    );
};

export const loginAction = (data) => async (dispatch) => {
    await dispatchAction(dispatch, LOGIN_REQUEST, LOGIN_SUCCESS, LOGIN_ERROR, '/auth/login',
        {
            method: 'POST',
            body: JSON.stringify(data),
        }
    );
};

export const logoutAction = () => async (dispatch) => {
    await dispatchAction(dispatch, LOGOUT_REQUEST, LOGOUT_SUCCESS, LOGOUT_ERROR, '/auth/logout',
        {
            method: 'POST',
        }
    );
};

export const currentUser = () => async (dispatch) => {
    await dispatchAction(dispatch, REQUEST_USER, REQUEST_USER_SUCCESS, REQUEST_USER_ERROR, `/auth/check`, {
        method: 'GET',
        credentials: 'include',
    });
}