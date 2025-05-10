import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import toast from "react-hot-toast";
import logo from "../assets/logo.png";
import background from "../assets/background.png";
import AuthForm from "../components/AuthForm";
import {loginAction} from "../redux/AuthService/Action.js";

function Login() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { loading, error, loginResponse } = useSelector((s) => s.auth);

    const [formValues, setFormValues] = useState({
        email: "",
        password: "",
    });
    const [formErrors, setFormErrors] = useState({});

    const loginFields = [
        {
            id: "email",
            label: "Email",
            type: "email",
            placeholder: "Enter your email",
            value: formValues.email,
            onChange: handleChange,
            error: formErrors.email,
        },
        {
            id: "password",
            label: "Password",
            type: "password",
            placeholder: "Enter your password",
            value: formValues.password,
            onChange: handleChange,
            error: formErrors.password,
        },
    ];

    useEffect(() => {
        if (error) toast.error(error);
    }, [error]);

    useEffect(() => {
        if (loginResponse) {
            toast.success("Logged in successfully.");
            navigate("/");
        }
    }, [dispatch, loginResponse, navigate]);

    function handleChange(e) {
        const { id, value } = e.target;
        setFormValues((prev) => ({ ...prev, [id]: value }));
    }

    const validate = () => {
        const errs = {};
        if (!formValues.email) errs.email = "Email is required.";
        if (!formValues.password) errs.password = "Password is required.";
        return errs;
    };

    const handleLogin = (e) => {
        e.preventDefault();
        const errs = validate();
        if (Object.keys(errs).length) {
            setFormErrors(errs);
        } else {
            setFormErrors({});
            dispatch(
                loginAction({
                    email: formValues.email,
                    password: formValues.password,
                })
            );
        }
    };

    return (
        <div
            className="min-h-screen flex items-center justify-center p-4 bg-cover bg-center bg-no-repeat"
            style={{ backgroundImage: `url(${background})` }}
        >
            <div className="w-full max-w-4xl bg-[#5D4037] rounded-2xl shadow-2xl overflow-hidden flex flex-col md:flex-row">
                <div className="w-full md:w-1/2 p-10 text-white flex flex-col items-center">
                    <h1 className="w-full text-5xl md:text-6xl font-extrabold mb-8 text-center drop-shadow-2xl">
                        MualaFuel
                    </h1>
                    <AuthForm
                        fields={loginFields}
                        onSubmit={handleLogin}
                        submitText={loading ? "Logging in…" : "Log In"}
                    />
                    <p className="mt-4 text-sm">
                        Don't have an account?{" "}
                        <Link to="/registration" className="text-amber-300 hover:underline">
                            Sign Up
                        </Link>
                    </p>
                </div>
                <div className="w-full md:w-1/2 flex bg-[#f5e9dc]">
                    <img src={logo} alt="Logo" className="max-w-full h-auto shadow-lg" />
                </div>
            </div>
        </div>
    );
}

export { Login };