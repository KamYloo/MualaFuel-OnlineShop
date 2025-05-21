import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import toast from "react-hot-toast";
import { sendContactAction } from "../redux/EmailService/Action.js";

function ContactForm() {
    const dispatch = useDispatch();
    const { sending, sendSuccess, error } = useSelector((state) => state.emailServ);

    const [formValues, setFormValues] = useState({
        name: "",
        email: "",
        subject: "",
        message: ""
    });
    const [formErrors, setFormErrors] = useState({});

    useEffect(() => {
        if (error) toast.error(error);
        if (sendSuccess) {
            toast.success(sendSuccess);
            setFormValues({
                name: "",
                email: "",
                subject: "",
                message: ""
            });
        }
    }, [error, sendSuccess]);

    const handleChange = (e) => {
        const { id, value } = e.target;
        setFormValues((prev) => ({ ...prev, [id]: value }));
    };

    const validate = () => {
        const errs = {};
        if (!formValues.name) errs.name = "Name is required.";
        if (!formValues.email) errs.email = "Email is required.";
        if (!formValues.subject) errs.subject = "Subject is required.";
        if (!formValues.message) errs.message = "Message is required.";
        return errs;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const errs = validate();
        if (Object.keys(errs).length) {
            setFormErrors(errs);
        } else {
            setFormErrors({});
            dispatch(sendContactAction(formValues));
        }
    };

    return (
        <div className="w-[56rem] mt-16 p-10 bg-[#fff8f0] rounded-lg shadow-md">
            <h2 className="text-3xl font-bold mb-6 text-center text-[#3E2723]">Contact Us</h2>
            <form onSubmit={handleSubmit} className="space-y-6">
                <div>
                    <label htmlFor="name" className="block font-medium mb-2 text-[#3E2723]">Name</label>
                    <input
                        id="name"
                        type="text"
                        value={formValues.name}
                        onChange={handleChange}
                        className="w-full p-3 border border-[#3E2723] rounded focus:outline-none focus:ring-2 focus:ring-[#3E2723]"
                    />
                    {formErrors.name && <p className="text-red-500 text-sm mt-1">{formErrors.name}</p>}
                </div>
                <div>
                    <label htmlFor="email" className="block font-medium mb-2 text-[#3E2723]">Email</label>
                    <input
                        id="email"
                        type="email"
                        value={formValues.email}
                        onChange={handleChange}
                        className="w-full p-3 border border-[#3E2723] rounded focus:outline-none focus:ring-2 focus:ring-[#3E2723]"
                    />
                    {formErrors.email && <p className="text-red-500 text-sm mt-1">{formErrors.email}</p>}
                </div>
                <div>
                    <label htmlFor="subject" className="block font-medium mb-2 text-[#3E2723]">Subject</label>
                    <input
                        id="subject"
                        type="text"
                        value={formValues.subject}
                        onChange={handleChange}
                        className="w-full p-3 border border-[#3E2723] rounded focus:outline-none focus:ring-2 focus:ring-[#3E2723]"
                    />
                    {formErrors.subject && <p className="text-red-500 text-sm mt-1">{formErrors.subject}</p>}
                </div>
                <div>
                    <label htmlFor="message" className="block font-medium mb-2 text-[#3E2723]">Message</label>
                    <textarea
                        id="message"
                        value={formValues.message}
                        onChange={handleChange}
                        className="w-full p-3 border border-[#3E2723] rounded focus:outline-none focus:ring-2 focus:ring-[#3E2723]"
                        rows="6"
                    />
                    {formErrors.message && <p className="text-red-500 text-sm mt-1">{formErrors.message}</p>}
                </div>
                <button
                    type="submit"
                    disabled={sending}
                    className="w-full bg-[#3E2723] text-white py-3 rounded hover:bg-[#4E3423] transition-colors"
                >
                    {sending ? "Sending..." : "Send Message"}
                </button>
            </form>
        </div>
    );
}

export default ContactForm;