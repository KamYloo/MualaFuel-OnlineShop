import { applyMiddleware, combineReducers, legacy_createStore } from "redux"
import { thunk } from "redux-thunk"
import { authReducer } from "./AuthService/Reducer.js";
import { emailReducer } from "./EmailService/Reducer.js";


const rootReducer = combineReducers({
    auth: authReducer,
    emailServ: emailReducer,
})

export const store = legacy_createStore(rootReducer, applyMiddleware(thunk))