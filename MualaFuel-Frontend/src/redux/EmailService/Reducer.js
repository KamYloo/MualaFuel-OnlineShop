import {
    DELETE_EMAIL_ERROR,
    DELETE_EMAIL_REQUEST, DELETE_EMAIL_SUCCESS,
    FETCH_EMAILS_ERROR,
    FETCH_EMAILS_REQUEST,
    FETCH_EMAILS_SUCCESS,
    SEND_CONTACT_ERROR,
    SEND_CONTACT_REQUEST,
    SEND_CONTACT_SUCCESS
} from "./ActionType.js";

const initialState = {
    sending: false,
    sendSuccess: null,
    error: null,
    loading: false,
    emails: [],
    pageInfo: { totalPages: 0, page: 0, size: 10 },
    deleting: false,
    deleteError: null,
};

export const emailReducer = (state = initialState, action) => {
    switch (action.type) {
        case SEND_CONTACT_REQUEST:
            return { ...state, sending: true, error: null, sendSuccess: null };
        case SEND_CONTACT_SUCCESS:
            return { ...state, sending: false, sendSuccess: action.payload };
        case SEND_CONTACT_ERROR:
            return { ...state, sending: false, error: action.payload };


        case FETCH_EMAILS_REQUEST:
            return { ...state, loading: true, error: null };

        case FETCH_EMAILS_SUCCESS:
            return {
                ...state,
                loading: false,
                emails: action.payload.content,
                pageInfo: {
                    totalPages: action.payload.totalPages,
                    page: action.payload.number,
                    size: action.payload.size,
                },
            };

        case FETCH_EMAILS_ERROR:
            return { ...state, loading: false, error: action.payload };

        case DELETE_EMAIL_REQUEST:
            return { ...state, deleting: true, deleteError: null };

        case DELETE_EMAIL_SUCCESS:
            return {
                ...state,
                deleting: false,
                emails: state.emails.filter(email => email.id !== action.payload),
            };

        case DELETE_EMAIL_ERROR:
            return { ...state, deleting: false, deleteError: action.payload };
        default:
            return state;
    }
};