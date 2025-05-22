import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { AiOutlineMenuFold } from "react-icons/ai";
import { MdCancel } from "react-icons/md";
import { FaUserCircle, FaShoppingCart } from "react-icons/fa";
import { BiDotsVerticalRounded } from "react-icons/bi";
import {useDispatch, useSelector} from "react-redux";
import toast from "react-hot-toast";
import logo from "../assets/logo.png";
import {logoutAction} from "../redux/AuthService/Action.js";

function Navbar() {
    const [isPanelVisible, setPanelVisible] = useState(false);
    const [isMenuOpen, setMenuOpen] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const {auth} = useSelector(store => store);
    const [activeTab, setActiveTab] = useState("home");

    const togglePanel = () => {
        setPanelVisible(!isPanelVisible);
    };

    const toggleMenu = () => {
        setMenuOpen(!isMenuOpen);
    };

    const handleLogout = () => {
        dispatch(logoutAction())
            .then(() => {
                navigate("/login");
                toast.success(auth.logout || 'Logged out successfully');
            })
            .catch(() => {
                toast.error("Failed to logout. Please try again.");
            })
    };

    const handleTabClick = (tab) => {
        setActiveTab(tab.path);
        navigate(`/${tab.path}`);
        setPanelVisible(false);
    };

    useEffect(() => {
        const currentPath = location.pathname.replace("/", "") || "home";
        setActiveTab(currentPath);
    }, [location]);

    const tabs = [
        { name: "Home", path: "home" },
        { name: "Assortment", path: "assortment" },
        { name: "Orders", path: "orders" },
    ];

    // Add Emails tab for ADMIN and SUPPORT roles
    if (auth.reqUser) {
        const roles = Array.from(auth.reqUser.roles || []);
        if (roles.some(role => role.name === "ADMIN" || role.name === "SUPPORT")) {
            tabs.push({ name: "Emails", path: "emailsHistory" });
        }
    }

    return (
        <header className="bg-[#3E2723] h-24 flex items-center px-8 relative shadow-md shadow-[#00000088]">
            <div className="flex items-center">
                <img src={logo} alt="Logo" className="w-16 h-16 mr-2 rounded-full"/>
                <p className="text-white font-bold text-3xl">MualaFuel</p>
            </div>

            <div className="flex lg:hidden ml-auto z-[2]">
                <button onClick={togglePanel} className="text-white text-3xl">
                    {isPanelVisible ? <MdCancel/> : <AiOutlineMenuFold/>}
                </button>
            </div>

            <nav
                className={`fixed top-0 right-0 h-full w-3/5 bg-[#3E2723] transform transition-transform duration-500 ease-in-out 
                ${isPanelVisible ? "translate-x-0" : "translate-x-full"} lg:translate-x-0 lg:static lg:flex lg:items-center lg:w-auto ml-auto`}
            >
                <ul className="flex flex-col lg:flex-row gap-4 lg:gap-6 list-none text-center lg:text-left mt-20 lg:mt-0">
                    {tabs.map((tab, index) => (
                        <li
                            key={index}
                            className={`relative group ${activeTab === tab.path ? "text-amber-300" : "text-white"}`}
                            onClick={() => handleTabClick(tab)}
                        >
                            <Link
                                to={`/${tab.path}`}
                                className={`font-bold text-lg transition-colors duration-300 group-hover:text-amber-300 ${activeTab === tab.path ? "text-amber-300" : ""}`}
                            >
                                {tab.name}
                            </Link>
                            <div
                                className={`absolute left-0 bottom-[-5px] h-[3px] rounded transition-all duration-300 bg-amber-300 ${activeTab === tab.path ? "w-full" : "w-0"} group-hover:w-full`}
                            />
                        </li>
                    ))}
                    <li className="relative group text-white" onClick={() => { setPanelVisible(false); navigate("/cart"); }}>
                        <FaShoppingCart className="text-2xl transition-colors duration-300 group-hover:text-amber-300 cursor-pointer"/>
                    </li>
                </ul>

                <div className="lg:hidden flex flex-col items-center gap-4 mt-4 absolute bottom-10 left-1/2 transform -translate-x-1/2">
                    {auth.reqUser ? (
                        <>
                            <p className="text-white font-bold text-lg">
                                {auth.reqUser?.firstName} {auth.reqUser?.lastName}
                            </p>
                            <div className="flex gap-2">
                                {auth.reqUser?.roles &&
                                    Array.from(auth.reqUser.roles).map((role, id) => (
                                        <span key={id} className="text-xs text-amber-300">{role.name}</span>
                                    ))
                                }
                            </div>
                            <button
                                onClick={handleLogout}
                                className="text-white font-bold text-lg border border-white px-4 py-2 rounded"
                            >
                                Logout
                            </button>
                        </>
                    ) : (
                        <div className="flex items-center" onClick={() => navigate("/login")}>
                            <p className="text-white font-bold text-lg cursor-pointer">Login</p>
                        </div>
                    )}
                </div>
            </nav>

            <div className="hidden lg:flex items-center gap-4 ml-20 relative">
                {auth.reqUser ? (
                    <>
                        <div className="flex flex-col">
                            <p
                                className="text-white font-bold text-lg cursor-pointer"
                                onClick={() => navigate(`/user-profile/${auth.reqUser.nickName}`)}
                            >
                                {auth.reqUser?.firstName} {auth.reqUser?.lastName}
                            </p>
                            <div className="flex gap-2">
                                {auth.reqUser?.roles &&
                                    Array.from(auth.reqUser.roles).map((role, id) => (
                                        <span key={id} className="text-xs text-amber-300">{role.name}</span>
                                    ))
                                }
                            </div>
                        </div>
                        <button onClick={toggleMenu} className="text-white text-xl focus:outline-none">
                            <BiDotsVerticalRounded/>
                        </button>
                        {isMenuOpen && (
                            <div className="absolute top-[7vh] right-0 mt-2 bg-[#3E2723] shadow-lg rounded w-40">
                                <button
                                    onClick={handleLogout}
                                    className="w-full text-center px-4 py-2 text-white hover:bg-[#4E3423]"
                                >
                                    Logout
                                </button>
                            </div>
                        )}
                    </>
                ) : (
                    <div className="flex items-center" onClick={() => navigate("/login")}>
                        <p className="text-white font-bold text-lg cursor-pointer">Login</p>
                    </div>
                )}
            </div>
        </header>
    );
}

export { Navbar };