import React from "react";

function AuthForm({ fields, onSubmit, submitText }) {
    return (
        <form onSubmit={onSubmit} className="w-full space-y-4 bg-[#6D4C41] p-6 rounded-lg shadow-lg">
            {fields.map((field) => (
                <div key={field.id}>
                    <label htmlFor={field.id} className="block text-sm font-medium text-white mb-2">
                        {field.label}
                    </label>
                    <input
                        id={field.id}
                        type={field.type}
                        placeholder={field.placeholder}
                        value={field.value || ""}
                        onChange={field.onChange}
                        className="w-full px-4 py-3 bg-white rounded-lg text-[#5D4037] placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-amber-300 transition"
                    />
                    {field.error && (
                        <span className="text-red-500 text-xs">{field.error}</span>
                    )}
                </div>
            ))}
            <button
                type="submit"
                className="w-full py-3 bg-amber-500 text-white font-semibold rounded-xl hover:bg-amber-600 shadow-md transform hover:-translate-y-0.5 transition"
            >
                {submitText}
            </button>
        </form>
    );
}

export default AuthForm;