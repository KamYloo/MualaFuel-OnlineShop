import {LOGIN_SUCCESS, LOGOUT_SUCCESS} from "./AuthService/ActionType.js";

export const BASE_API_URL = "http://localhost:8080/api"

export const fetchWithAuth = async (url, options = {}, errorType) => {
    const fullUrl = `${BASE_API_URL}${url}`;
    const isForm = options.body instanceof FormData;
    const headers = {
        ...(isForm ? {} : { 'Content-Type': 'application/json' }),
        ...(options.headers || {}),
    };

    try {
        const response = await fetch(fullUrl, {
            ...options,
            headers,
            credentials: 'include',
        });


        if (response.status === 401) {
            window.location.href = '/login';
            return { error: true, message: 'Unauthorized. Please log in.' };
        }

        if (!response.ok) {
            const data = await response.json().catch(() => ({}));
            const msg = data.message || data.error || (data.validationErrors && data.validationErrors.join(', ')) || 'Request failed';
            return { error: true, message: msg };
        }

        const contentType = response.headers.get('content-type') || '';
        return contentType.includes('application/json') ? response.json() : response.text();
    } catch (err) {
        console.error(`Error in ${errorType}:`, err);
        throw new Error(err.message);
    }
};

export const dispatchAction = async (
    dispatch,
    requestType,
    successType,
    errorType,
    url,
    options = {}
) => {
    dispatch({ type: requestType });
    try {
        const result = await fetchWithAuth(url, options, requestType);
        if (result.error) {
            dispatch({ type: errorType, payload: result.message });
            throw new Error(result.message);
        }
        dispatch({ type: successType, payload: result });
        if (successType === LOGIN_SUCCESS) {
            localStorage.setItem("isLoggedIn", "1");
        }

        if (successType === LOGOUT_SUCCESS) {
            localStorage.removeItem("isLoggedIn");
        }
        return result;
    } catch (err) {
        dispatch({ type: errorType, payload: err.message });
        throw err;
    }
};
