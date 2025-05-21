import {dispatchAction} from "../api.js";
import {SEND_CONTACT_ERROR, SEND_CONTACT_REQUEST, SEND_CONTACT_SUCCESS} from "./ActionType.js";

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