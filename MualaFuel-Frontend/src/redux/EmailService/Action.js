import {dispatchAction} from "../api.js";
import {
    DELETE_EMAIL_ERROR,
    DELETE_EMAIL_REQUEST,
    DELETE_EMAIL_SUCCESS,
    FETCH_EMAIL_BODY_ERROR,
    FETCH_EMAIL_BODY_REQUEST,
    FETCH_EMAIL_BODY_SUCCESS,
    FETCH_EMAILS_ERROR,
    FETCH_EMAILS_REQUEST,
    FETCH_EMAILS_SUCCESS,
    SEND_CONTACT_ERROR,
    SEND_CONTACT_REQUEST,
    SEND_CONTACT_SUCCESS
} from "./ActionType.js";

export const sendContactAction = (data) => async (dispatch) => {
    await dispatchAction(
        dispatch,
        SEND_CONTACT_REQUEST,
        SEND_CONTACT_SUCCESS,
        SEND_CONTACT_ERROR,
        '/contact',
        {
            method: 'POST',
            body: JSON.stringify(data),
        }
    );
};


export const fetchEmails = (params) => async (dispatch) => {
    await dispatchAction(dispatch, FETCH_EMAILS_REQUEST, FETCH_EMAILS_SUCCESS, FETCH_EMAILS_ERROR, `/emailHistory/all`, {
        method: 'GET',
        params
    });
}

export const fetchEmailBody  = (id) => async (dispatch) => {
    await dispatchAction(dispatch, FETCH_EMAIL_BODY_REQUEST, FETCH_EMAIL_BODY_SUCCESS, FETCH_EMAIL_BODY_ERROR, `/emailHistory/${id}/body`, {
        method: 'GET',
    });
}

export const deleteEmail = (id) => async (dispatch) => {
    await dispatchAction(dispatch, DELETE_EMAIL_REQUEST, DELETE_EMAIL_SUCCESS, DELETE_EMAIL_ERROR, `/emailHistory/${id}`, {
        method: 'DELETE',
    });
}