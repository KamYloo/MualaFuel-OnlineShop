import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import background from "../assets/background.png";
import AuthForm from "../components/AuthForm";
import { useDispatch, useSelector } from "react-redux";
import toast from "react-hot-toast";
import {registerAction} from "../redux/AuthService/Action.js";

function Registration() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { loading, error, registerResponse } = useSelector((s) => s.auth);

    const [formValues, setFormValues] = useState({
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        confirmPassword: "",
    });
    const [formErrors, setFormErrors] = useState({});

    const registrationFields = [
        {
            id: "firstname",
            label: "Firstname",
            type: "text",
            placeholder: "Enter your firstname",
            value: formValues.firstname,
            onChange: (e) => handleChange(e),
            error: formErrors.firstname,
        },
        {
            id: "lastname",
            label: "Lastname",
            type: "text",
            placeholder: "Enter your lastname",
            value: formValues.lastname,
            onChange: (e) => handleChange(e),
            error: formErrors.lastname,
        },
        {
            id: "email",
            label: "Email",
            type: "email",
            placeholder: "Enter your email",
            value: formValues.email,
            onChange: (e) => handleChange(e),
            error: formErrors.email,
        },
        {
            id: "password",
            label: "Password",
            type: "password",
            placeholder: "Enter your password",
            value: formValues.password,
            onChange: (e) => handleChange(e),
            error: formErrors.password,
        },
        {
            id: "confirmPassword",
            label: "Confirm Password",
            type: "password",
            placeholder: "Confirm your password",
            value: formValues.confirmPassword,
            onChange: (e) => handleChange(e),
            error: formErrors.confirmPassword,
        },
    ];

    useEffect(() => {
        if (error) toast.error(error);
    }, [error]);

    useEffect(() => {
        if (registerResponse) {
            toast.success("You have registered successfully.");
            navigate("/login");
        }
    }, [registerResponse, navigate]);

    const handleChange = (e) => {
        const { id, value } = e.target;
        setFormValues((prev) => ({ ...prev, [id]: value }));
    };

    const validate = () => {
        const errs = {};
        if (!formValues.firstname) errs.firstname = "Firstname is required.";
        if (!formValues.lastname) errs.lastname = "Lastname is required.";
        if (!formValues.email) errs.email = "Email is required.";
        else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formValues.email)) errs.email = "Invalid email format.";
        if (!formValues.password) errs.password = "Password is required.";
        else if (formValues.password.length < 6) errs.password = "Password must be at least 6 characters.";
        if (formValues.password !== formValues.confirmPassword)
            errs.confirmPassword = "Passwords do not match.";
        return errs;
    };

    const handleRegistration = (e) => {
        e.preventDefault();
        const errs = validate();
        if (Object.keys(errs).length) {
            setFormErrors(errs);
        } else {
            setFormErrors({});
            dispatch(
                registerAction({
                    firstname: formValues.firstname,
                    lastname: formValues.lastname,
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
            <div className="w-full max-w-7xl bg-[#5D4037] rounded-2xl shadow-2xl overflow-hidden flex flex-col md:flex-row">
                <div className="w-full md:w-1/2 p-8 text-white flex flex-col items-center">
                    <h1 className="w-full text-2xl md:text-5xl font-extrabold mb-8 text-center drop-shadow-2xl">
                        MualaFuel Registration
                    </h1>
                    <AuthForm
                        fields={registrationFields}
                        onSubmit={handleRegistration}
                        submitText={loading ? "Registering…" : "Register"}
                    />
                    <p className="mt-4 text-sm">
                        Already have an account?{' '}
                        <Link to="/login" className="text-amber-300 hover:underline">
                            Log In
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

export { Registration };
