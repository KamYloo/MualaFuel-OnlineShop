import React, {useEffect, useState} from "react";
import {useParams} from "react-router";
import axios from "axios";
import ErrorOverlay from "../components/ErrorOverlay.jsx";
import Spinner from "../components/Spinner.jsx";
import toast from "react-hot-toast";
import { useDispatch } from "react-redux";
import { addItemAction } from "../redux/CartService/Action.js"

function Product(){

    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isAdding, setIsAdding] = useState(false);
    const [quantity, setQuantity] = useState(1);
    const dispatch = useDispatch();

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/product/${id}`);
                setProduct(response.data);
            } catch (err) {
                setError(err.message || 'Failed to fetch product');
            } finally {
                setLoading(false);
            }
        };
        fetchProduct();
    }, [id]);

    const handleAddToCart = () => {
        setIsAdding(true);
        dispatch(addItemAction(product.id, quantity))
            .then(() => {
                setIsAdding(false);
                toast.success("Product added to cart successfully");
            })
            .catch(() => {
                toast.error("Failed to add product");
                setIsAdding(false);
            });
    };

    if (loading) return <Spinner size={350} />;
    if (error) return <ErrorOverlay size={350} message={error} />;
    if (!product) return <ErrorOverlay size={350} message="Product not found" />;

    return (
        <div className="min-h-screen p-8" style={{ backgroundColor: "#f5e9dc" }}>
            <div className="max-w-7xl mx-auto">
                <div className="bg-white rounded-xl shadow-md overflow-hidden">
                    <div className="md:flex">
                        <div className="md:w-1/3 p-6">
                            <div className="h-full flex items-center justify-center">
                                <img
                                    src={product.imagePath || '/placeholder-product.jpg'}
                                    alt={product.name}
                                    className="max-h-96 object-contain"
                                />
                            </div>
                        </div>
                        <div className="md:w-2/3 p-6">
                            <div className="uppercase tracking-wide text-sm text-[#3E2723] font-semibold">
                                {product.brand}
                            </div>
                            <h1 className="block mt-1 text-2xl font-medium text-gray-900">
                                {product.name}
                            </h1>
                            <div className="mt-4">
                                <span className="text-gray-900 text-xl">{product.price.toFixed(2)} zł</span>
                                <span className="ml-2 text-sm text-gray-600">
                  {product.capacityInMilliliters}ml
                </span>
                            </div>

                            <div className="mt-6 space-y-4">
                                <div>
                                    <h3 className="text-sm font-medium text-[#3E2723]">Description</h3>
                                    <p className="mt-1 text-sm text-gray-600">
                                        {product.description || 'No description available'}
                                    </p>
                                </div>

                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <h3 className="text-sm font-medium text-[#3E2723]">Alcohol Type</h3>
                                        <p className="mt-1 text-sm text-gray-600 capitalize">
                                            {product.alcoholType?.toLowerCase()}
                                        </p>
                                    </div>
                                    <div>
                                        <h3 className="text-sm font-medium text-[#3E2723]">Alcohol Content</h3>
                                        <p className="mt-1 text-sm text-gray-600">
                                            {product.alcoholContent}%
                                        </p>
                                    </div>
                                    <div>
                                        <h3 className="text-sm font-medium text-[#3E2723]">Quantity</h3>
                                        <p className="mt-1 text-sm text-gray-600">
                                            {product.quantity > 0 ? `${product.quantity} in stock` : 'Out of stock'}
                                        </p>
                                    </div>
                                </div>
                            </div>

                            <div className="mt-6 flex items-center">
                                <button
                                    className="bg-[#3E2723] hover:bg-[#4E3423] text-white px-6 py-3 rounded-lg transition-colors"
                                    disabled={product.quantity <= 0}
                                    onClick={handleAddToCart}
                                >
                                    {product.quantity > 0 ? 'Add to cart' : 'Out of stock'}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export { Product };