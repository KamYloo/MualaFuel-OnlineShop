import React from 'react';
import {Navbar} from "../components/Navbar.jsx";
import {Outlet} from "react-router-dom";

export const Layout = () => {
    return (
        <div className='App'>
            <div className='navbar'>
                <Navbar/>
            </div>
            <div className='content-wrapper'>
                <Outlet/>
            </div>
        </div>
    )
};