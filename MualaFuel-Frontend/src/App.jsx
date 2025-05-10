import React, {useEffect} from 'react'

import './App.css'
import AppRoutes from "./AppRoutes.jsx";
import {useDispatch} from "react-redux";
import {currentUser} from "./redux/AuthService/Action.js";

function App() {
    const dispatch = useDispatch();
    useEffect(() => {
        if (localStorage.getItem('isLoggedIn'))
         dispatch(currentUser());
    }, [dispatch]);
    return (
        <AppRoutes/>
    );
}

export default App