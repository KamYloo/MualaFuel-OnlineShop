import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Home } from "./pages/Home.jsx";
import { Toaster } from "react-hot-toast";



function AppRoutes() {
    const renderLayout = (Component) => (
        <div className='App'>
            {/*<div className='navbar'>*/}
            {/*    <Navbar />*/}
            {/*</div>*/}
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
            </Routes>
            <Toaster
                position="bottom-right"
                reverseOrder={false}
            />
        </Router>
    );
}

export default AppRoutes;