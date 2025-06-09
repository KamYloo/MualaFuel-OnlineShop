import React from 'react';
import Lottie from 'lottie-react';
import errorAnimation from '../assets/errorAnimation.json';

const ErrorOverlay = ({ size = 250, message = 'Something went wrong' }) => {
    const dim = typeof size === 'number' ? `${size}px` : size;
    return (
        <div
            className="absolute inset-0 z-40 flex flex-col items-center justify-center bg-[#70593F] bg-opacity-60 p-4 text-center"
        >
            <div style={{ width: dim, height: dim }} className="mb-6">
                <Lottie animationData={errorAnimation} loop={false} style={{ width: '100%', height: '100%' }} />
            </div>
            <p className="text-white text-xl font-semibold">{message}</p>
        </div>
    );
};

export default ErrorOverlay;
