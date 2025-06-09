import React from 'react';
import {Navbar} from "../components/Navbar.jsx";
import {Outlet} from "react-router-dom";

export const Layout = () => {
    return (
        <div className='App'>
            <div className='navbar sticky top-0 z-50'>
                <Navbar/>
            </div>
            <div className='content-wrapper relative flex-1 overflow-y-auto'>
                <Outlet/>
            </div>
        </div>
    )
};