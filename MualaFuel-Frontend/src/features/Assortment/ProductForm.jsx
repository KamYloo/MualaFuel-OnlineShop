import React, { useState, useEffect } from "react";

const alcoholTypes = [
    "WODKA",
    "WHISKEY",
    "RUM",
    "GIN",
    "TEQUILA",
    "WINE",
    "BEER",
    "LIQUEUR",
    "OTHER",
];

export function ProductForm({
    initialData = {},
    onSubmit,
    submitText,
    isSubmitting,
    onCancel,
}) {
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");
    const [brand, setBrand] = useState("");
    const [alcoholType, setAlcoholType] = useState("");
    const [quantity, setQuantity] = useState("");
    const [alcoholContent, setAlcoholContent] = useState("");
    const [capacityInMilliliters, setCapacityInMilliliters] = useState("");
    const [imageFile, setImageFile] = useState(null);

    useEffect(() => {
        if (Object.keys(initialData).length > 0) {
            setName(initialData.name || "");
            setDescription(initialData.description || "");
            setPrice(initialData.price || "");
            setBrand(initialData.brand || "");
            setAlcoholType(initialData.alcoholType || "");
            setQuantity(initialData.quantity || "");
            setAlcoholContent(initialData.alcoholContent || "");
            setCapacityInMilliliters(initialData.capacityInMilliliters || "");
        }
    }, [initialData]);

    const handleSubmit = (e) => {
        e.preventDefault();
        const data = {
            name,
            description,
            price,
            brand,
            alcoholType,
            quantity,
            alcoholContent,
            capacityInMilliliters,
            imageFile,
        };
        onSubmit(data);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                <div>
                    <label className="block text-gray-700 mb-1">Name</label>
                    <input
                        type="text"
                        className="w-full p-2 border rounded"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label className="block text-gray-700 mb-1">Brand</label>
                    <input
                        type="text"
                        className="w-full p-2 border rounded"
                        value={brand}
                        onChange={(e) => setBrand(e.target.value)}
                        required
                    />
                </div>
            </div>
            <div>
                <label className="block text-gray-700 mb-1">Description</label>
                <textarea
                    className="w-full p-2 border rounded"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                />
            </div>
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                <div>
                    <label className="block text-gray-700 mb-1">Price</label>
                    <input
                        type="number"
                        step="0.01"
                        className="w-full p-2 border rounded"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label className="block text-gray-700 mb-1">Quantity</label>
                    <input
                        type="number"
                        className="w-full p-2 border rounded"
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                        required
                    />
                </div>
            </div>
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                <div>
                    <label className="block text-gray-700 mb-1">Alcohol Content</label>
                    <input
                        type="number"
                        step="0.1"
                        className="w-full p-2 border rounded"
                        value={alcoholContent}
                        onChange={(e) => setAlcoholContent(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label className="block text-gray-700 mb-1">Capacity (ml)</label>
                    <input
                        type="number"
                        className="w-full p-2 border rounded"
                        value={capacityInMilliliters}
                        onChange={(e) => setCapacityInMilliliters(e.target.value)}
                        required
                    />
                </div>
            </div>
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                <div>
                    <label className="block text-gray-700 mb-1">Alcohol Type</label>
                    <select
                        className="w-full p-2 border rounded"
                        value={alcoholType}
                        onChange={(e) => setAlcoholType(e.target.value)}
                        required
                    >
                        <option value="">Select type</option>
                        {alcoholTypes.map((type, idx) => (
                            <option key={idx} value={type}>
                                {type}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label className="block text-gray-700 mb-1">Product Image</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={(e) => setImageFile(e.target.files[0])}
                    />
                </div>
            </div>
            <div className="flex flex-col sm:flex-row justify-center gap-4 pt-4">
                {onCancel && (
                    <button
                        type="button"
                        onClick={onCancel}
                        className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded transition-colors"
                    >
                        Cancel
                    </button>
                )}
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="bg-[#3E2723] hover:bg-[#4E3423] text-white px-4 py-2 rounded transition-colors"
                >
                    {isSubmitting ? "Saving..." : submitText}
                </button>
            </div>
        </form>
    );
}