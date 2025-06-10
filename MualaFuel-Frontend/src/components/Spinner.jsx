import React from 'react';
import Lottie from 'lottie-react';
import beerAnimation from '../assets/beerAnimation.json';

const Spinner = ({ size = 300 }) => {
    const dim = typeof size === 'number' ? `${size}px` : size;
    return (
        <div
            className="absolute inset-0 z-40 flex items-center justify-center bg-[#70593F] bg-opacity-60"
        >
            <div style={{ width: dim, height: dim }}>
                <Lottie animationData={beerAnimation} loop style={{ width: '100%', height: '100%' }} />
            </div>
        </div>
    );
};

export default Spinner;
