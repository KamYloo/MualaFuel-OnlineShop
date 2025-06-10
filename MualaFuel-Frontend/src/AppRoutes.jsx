import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Home } from "./pages/Home.jsx";
import { Toaster } from "react-hot-toast";
import {Login} from "./pages/Login.jsx";
import {Registration} from "./pages/Registration.jsx";
import {Assortment} from "./pages/Assortment.jsx";
import {Orders} from "./pages/Orders.jsx";
import {Cart} from "./pages/Cart.jsx";
import {NoLayout} from "./layout/NoLayout.jsx";
import {Layout} from "./layout/Layout.jsx";
import {ProtectedRoute} from "./ProtectedRoute.jsx";
import {EmailHistory} from "./pages/EmailHistory.jsx";
import {OrdersManagement} from "./pages/OrdersManagement.jsx";
import {Product} from "./pages/Product.jsx";

function AppRoutes() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/home" />} />

                <Route element={<NoLayout/>}>
                    <Route path="/login" element={<Login />} />
                    <Route path="/registration" element={<Registration />} />
                </Route>

                <Route element={<Layout/>}>
                    <Route path="/home" element={<Home />} />
                    <Route path="/assortment" element={<Assortment />} />
                    <Route path="/product/:id" element={<Product />} />
                    <Route path="/orders" element={<Orders />} />
                    <Route path="/cart" element={<Cart />} />
                    <Route
                        path="/emailsHistory"
                        element={
                            <ProtectedRoute allowedRoles={["ADMIN", "SUPPORT"]}>
                                <EmailHistory />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/ordersManagement"
                        element={
                            <ProtectedRoute allowedRoles={["ADMIN", "SUPPORT"]}>
                                <OrdersManagement />
                            </ProtectedRoute>
                        }
                    />
                </Route>

            </Routes>
            <Toaster
                position="bottom-right"
                reverseOrder={false}
            />
        </Router>
    );
}

export default AppRoutes;