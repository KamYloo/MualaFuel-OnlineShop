import React from "react";
import banner from "../assets/banner.png";
import ContactForm from "../components/ContactForm.jsx";

function Home() {
    return (
        <div className="min-h-screen p-8" style={{ backgroundColor: "#f5e9dc" }}>
            <div className="flex flex-col items-center">
                <header className="text-center mb-12">
                    <img src={banner} alt="Logo" className="w-128 h-48 mx-auto mb-4 rounded-xl shadow-lg" />
                    <h1 className="text-5xl font-bold text-[#3E2723]">MualaFuel</h1>
                </header>
                <section className="max-w-4xl text-center mb-12">
                    <p className="text-xl text-[#3E2723] mb-6">
                        Discover our finest selection of drinks. Experience the exceptional taste of our top three featured beverages:
                    </p>
                    <div className="flex flex-col md:flex-row justify-center gap-8 mb-12">
                        <div className="bg-white rounded-lg shadow-md p-6 flex-1">
                            <h2 className="text-2xl font-semibold text-[#3E2723] mb-2">Premium Lager</h2>
                            <p className="text-gray-600">Crisp and refreshing, perfect for hot days.</p>
                        </div>
                        <div className="bg-white rounded-lg shadow-md p-6 flex-1">
                            <h2 className="text-2xl font-semibold text-[#3E2723] mb-2">Classic Ale</h2>
                            <p className="text-gray-600">Rich flavor with a bitter finish.</p>
                        </div>
                        <div className="bg-white rounded-lg shadow-md p-6 flex-1">
                            <h2 className="text-2xl font-semibold text-[#3E2723] mb-2">Dark Stout</h2>
                            <p className="text-gray-600">Smooth and creamy with roasted malt.</p>
                        </div>
                    </div>
                    <button className="bg-[#3E2723] text-white px-8 py-4 rounded-lg text-xl hover:bg-[#4E3423] transition-colors">
                        Discover Something
                    </button>
                </section>
            </div>
            <div className="max-w-full mt-16 flex justify-center items-center">
                <ContactForm />
            </div>
        </div>
    );
}

export { Home };