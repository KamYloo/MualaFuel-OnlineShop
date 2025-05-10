import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Home } from "./pages/Home.jsx";
import { Toaster } from "react-hot-toast";
import {Navbar} from "./components/Navbar.jsx";
import {Login} from "./pages/Login.jsx";
import {Registration} from "./pages/Registration.jsx";
import {Assortment} from "./pages/Assortment.jsx";
import {Orders} from "./pages/Orders.jsx";
import {Cart} from "./pages/Cart.jsx";



function AppRoutes() {
    const renderLayout = (Component) => (
        <div className='App'>
            <div className='navbar'>
                <Navbar />
            </div>
            <div className='content-wrapper'>
                <Component />
            </div>
        </div>
    );

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/home" />} />
                <Route path="/home" element={renderLayout(Home)} />
                <Route path="/login" element={<Login />} />
                <Route path="/registration" element={<Registration />} />
                <Route path="/home" element={renderLayout(Home)} />
                <Route path="/assortment" element={renderLayout(Assortment)} />
                <Route path="/orders" element={renderLayout(Orders)} />
                <Route path="/cart" element={renderLayout(Cart)} />
            </Routes>
            <Toaster
                position="bottom-right"
                reverseOrder={false}
            />
        </Router>
    );
}

export default AppRoutes;