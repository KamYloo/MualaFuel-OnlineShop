import axios from "axios";
import { useState } from "react";
import default_product_image from "../../assets/default_product_image.png";

export function ProductCard({ product }) {
    const [isAdding, setIsAdding] = useState(false);
    const [error, setError] = useState(null);
    const [showAddDialog, setShowAddDialog] = useState(false);
    const [quantity, setQuantity] = useState(1);

    const handleAddToCart = async () => {
        try {
            setIsAdding(true);
            setError(null);

            await axios.post(
                "http://localhost:8080/api/cart/items",
                {
                    productId: product.id,
                    quantity: quantity
                },
                {
                    withCredentials: true,
                    headers: {
                        "Content-Type": "application/json"
                    }
                }
            );

            console.log("Product added to cart!");
            setShowAddDialog(false);
            setQuantity(1);
        } catch (err) {
            setError(err.response?.data?.message || "Failed to add product to cart");
        } finally {
            setIsAdding(false);
        }
    };

    const incrementQuantity = () => {
        setQuantity(prev => prev + 1);
    };

    const decrementQuantity = () => {
        if (quantity > 1) {
            setQuantity(prev => prev - 1);
        }
    };

    return (
        <>
            <div className="w-full h-full bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300 flex flex-col">
                <div className="aspect-square bg-gray-100 flex items-center justify-center">
                    {product.imagePath ? (
                        <img
                            src={product.imagePath}
                            alt={product.name}
                            className="h-full w-full object-cover cursor-pointer"
                            onClick={() => setShowAddDialog(true)}
                        />
                    ) : (
                        <img
                            src={default_product_image}
                            alt="Default product"
                            className="h-32 w-32 object-contain cursor-pointer"
                            onClick={() => setShowAddDialog(true)}
                        />
                    )}
                </div>

                <div className="p-4 flex flex-col flex-grow">
                    <div className="flex justify-between items-start mb-2">
                        <h3 className="font-semibold text-lg truncate">{product.name}</h3>
                        <span className="bg-blue-100 text-blue-800 text-xs font-medium px-2 py-1 rounded">
                            {product.brand}
                        </span>
                    </div>

                    <p className="text-gray-600 text-sm mb-4 line-clamp-2 flex-grow">
                        {product.description}
                    </p>

                    <div className="flex justify-between items-center mt-auto">
                        <span className="font-bold text-lg">{product.price} zł</span>
                        <div className="flex flex-col items-end">
                            <button
                                onClick={() => setShowAddDialog(true)}
                                disabled={isAdding}
                                className={`bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded text-sm transition-colors ${
                                    isAdding ? "opacity-50 cursor-not-allowed" : ""
                                }`}
                            >
                                Add to cart
                            </button>
                            {error && (
                                <span className="text-red-500 text-xs mt-1">{error}</span>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            {showAddDialog && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <div className="flex gap-6">
                            <div className="w-1/3">
                                <img
                                    src={product.imagePath || default_product_image}
                                    alt={product.name}
                                    className="w-full h-auto object-cover rounded"
                                />
                            </div>
                            <div className="w-2/3">
                                <h2 className="text-xl font-bold mb-2">{product.name}</h2>
                                <p className="text-gray-600 mb-1"><span className="font-semibold">Brand:</span> {product.brand}</p>
                                <p className="text-gray-600 mb-1"><span className="font-semibold">Type:</span> {product.alcoholType}</p>
                                <p className="text-gray-600 mb-1"><span className="font-semibold">Alcohol:</span> {product.alcoholContent}%</p>
                                <p className="text-gray-600 mb-1"><span className="font-semibold">Size:</span> {product.capacityInMilliliters}ml</p>
                                <p className="text-gray-600 mb-4"><span className="font-semibold">Price:</span> {product.price} zł</p>

                                <div className="flex items-center justify-between mb-4">
                                    <div className="flex items-center border rounded">
                                        <button
                                            onClick={decrementQuantity}
                                            className="px-3 py-1 hover:bg-gray-100 text-lg"
                                            disabled={quantity <= 1}
                                        >
                                            -
                                        </button>
                                        <span className="px-4">{quantity}</span>
                                        <button
                                            onClick={incrementQuantity}
                                            className="px-3 py-1 hover:bg-gray-100 text-lg"
                                        >
                                            +
                                        </button>
                                    </div>
                                    <div className="text-lg font-semibold">
                                        Total: {(product.price * quantity).toFixed(2)} zł
                                    </div>
                                </div>

                                <div className="flex gap-2">
                                    <button
                                        onClick={() => {
                                            setShowAddDialog(false);
                                            setQuantity(1);
                                        }}
                                        className="flex-1 bg-gray-300 hover:bg-gray-400 text-gray-800 py-2 px-4 rounded"
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        onClick={handleAddToCart}
                                        disabled={isAdding}
                                        className={`flex-1 bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded ${
                                            isAdding ? "opacity-50 cursor-not-allowed" : ""
                                        }`}
                                    >
                                        {isAdding ? "Adding..." : "Add to Cart"}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}