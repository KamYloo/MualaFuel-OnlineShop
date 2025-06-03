import { applyMiddleware, combineReducers, legacy_createStore } from "redux"
import { thunk } from "redux-thunk"
import { authReducer } from "./AuthService/Reducer.js";
import { emailReducer } from "./EmailService/Reducer.js";
import {orderReducer} from "./OrderService/Reducer.js";
import {cartReducer} from "./CartService/Reducer.js";
import {productReducer} from "./ProductService/Reducer.js";


const rootReducer = combineReducers({
    auth: authReducer,
    emailServ: emailReducer,
    orderService: orderReducer,
    cartService: cartReducer,
    productService: productReducer,
})

export const store = legacy_createStore(rootReducer, applyMiddleware(thunk))