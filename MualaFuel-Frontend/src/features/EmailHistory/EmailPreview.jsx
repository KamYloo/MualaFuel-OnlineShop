import React from 'react';

export const EmailPreview = ({ show, onClose, htmlContent }) => {
    if (!show) return null;
    return (
        <div
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
            onClick={onClose}
        >
            <div
                className="bg-white p-5 rounded-lg max-w-4xl max-h-[80%] overflow-y-auto"
                onClick={(e) => e.stopPropagation()}
            >
                <div dangerouslySetInnerHTML={{ __html: htmlContent }} />
                <button
                    onClick={onClose}
                    className="block mx-auto mt-6 bg-[#3E2723] text-white px-4 py-2 rounded-lg hover:bg-[#4E3423] transition-colors"
                >
                    Close
                </button>
            </div>
        </div>
    );
};