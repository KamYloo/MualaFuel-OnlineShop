import React from "react";
import { useDispatch, useSelector } from "react-redux";
import toast from "react-hot-toast";
import { updateProductAction } from "../../redux/ProductService/Action.js";
import { ProductForm } from "./ProductForm.jsx";

function EditProductForm({ product, onClose }) {
    const dispatch = useDispatch();
    const { saving, error } = useSelector((state) => state.productService);

    const handleSubmit = (formData) => {
        const data = new FormData();
        data.append("id", product.id);
        data.append("name", formData.name);
        data.append("description", formData.description);
        data.append("price", formData.price);
        data.append("brand", formData.brand);
        data.append("alcoholType", formData.alcoholType);
        data.append("quantity", formData.quantity);
        data.append("alcoholContent", formData.alcoholContent);
        data.append("capacityInMilliliters", formData.capacityInMilliliters);
        if (formData.imageFile) {
            data.append("image", formData.imageFile);}
        dispatch(updateProductAction(data))
            .then(() => {
                toast.success("Product updated successfully");
                onClose();
            })
            .catch(() => {
                toast.error("Failed to update product");
            });
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg shadow-md p-6 w-full max-w-md sm:max-w-lg md:max-w-xl">
                <h2 className="text-2xl font-bold text-[#3E2723] mb-4 text-center">
                    Edit Product
                </h2>
                {error && <p className="text-red-500 text-center mb-2">{error}</p>}
                <ProductForm
                    initialData={product}
                    onSubmit={handleSubmit}
                    submitText="Update Product"
                    isSubmitting={saving}
                    onCancel={onClose}
                />
            </div>
        </div>
    );
}

export { EditProductForm };