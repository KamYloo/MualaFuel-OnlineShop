import {SEND_CONTACT_ERROR, SEND_CONTACT_REQUEST, SEND_CONTACT_SUCCESS} from "./ActionType.js";

const initialState = {
    sending: false,
    sendSuccess: null,
    error: null,
};

export const emailReducer = (state = initialState, action) => {
    switch (action.type) {
        case SEND_CONTACT_REQUEST:
            return { ...state, sending: true, error: null, sendSuccess: null };
        case SEND_CONTACT_SUCCESS:
            return { ...state, sending: false, sendSuccess: action.payload };
        case SEND_CONTACT_ERROR:
            return { ...state, sending: false, error: action.payload };
        default:
            return state;
    }
};