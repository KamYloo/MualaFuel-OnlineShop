import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import toast from "react-hot-toast";
import { saveProductAction } from "../redux/ProductService/Action.js";

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

function AddProductForm({ onClose }) {
    const dispatch = useDispatch();
    const { saving, error } = useSelector((state) => state.productService);

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");
    const [brand, setBrand] = useState("");
    const [alcoholType, setAlcoholType] = useState("");
    const [quantity, setQuantity] = useState("");
    const [alcoholContent, setAlcoholContent] = useState("");
    const [capacityInMilliliters, setCapacityInMilliliters] = useState("");
    const [imageFile, setImageFile] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append("name", name);
        formData.append("description", description);
        formData.append("price", price);
        formData.append("brand", brand);
        formData.append("alcoholType", alcoholType);
        formData.append("quantity", quantity);
        formData.append("alcoholContent", alcoholContent);
        formData.append("capacityInMilliliters", capacityInMilliliters);
        if (imageFile) {
            formData.append("image", imageFile);
        }

        dispatch(saveProductAction(formData))
            .then(() => {
                toast.success("Product added successfully");
                onClose();
            })
            .catch(() => {
                toast.error("Failed to add product");
            });
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg shadow-md p-6 w-full max-w-md sm:max-w-lg md:max-w-xl">
                <h2 className="text-2xl font-bold text-[#3E2723] mb-4 text-center">
                    Add Product
                </h2>
                {error && <p className="text-red-500 text-center mb-2">{error}</p>}
                <form
                    onSubmit={handleSubmit}
                    encType="multipart/form-data"
                    className="space-y-4"
                >
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
                            <label className="block text-gray-700 mb-1">
                                Alcohol Type
                            </label>
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
                            <label className="block text-gray-700 mb-1">
                                Product Image
                            </label>
                            <input
                                type="file"
                                accept="image/*"
                                onChange={(e) => setImageFile(e.target.files[0])}
                            />
                        </div>
                    </div>

                    <div className="flex flex-col sm:flex-row justify-end gap-4 pt-4">
                        <button
                            type="button"
                            onClick={onClose}
                            className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded transition-colors"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={saving}
                            className="bg-[#3E2723] hover:bg-[#4E3423] text-white px-4 py-2 rounded transition-colors"
                        >
                            {saving ? "Saving..." : "Add Product"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export { AddProductForm };