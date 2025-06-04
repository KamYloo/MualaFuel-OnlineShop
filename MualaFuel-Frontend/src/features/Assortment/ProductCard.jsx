import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { deleteProductAction } from "../../redux/ProductService/Action.js";
import default_product_image from "../../assets/default_product_image.png";
import { EditProductForm } from "./EditProductForm.jsx";

export function ProductCard({ product }) {
  const [error, setError] = useState(null);
  const [showEditModal, setShowEditModal] = useState(false);

  const dispatch = useDispatch();
  const { reqUser } = useSelector((state) => state.auth);
  const isAdmin = reqUser && Array.from(reqUser.roles).some((role) => role.name === "ADMIN");

  const handleDelete = () => {
    dispatch(deleteProductAction(product.id));
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
            />
          ) : (
            <img
              src={default_product_image}
              alt="Default product"
              className="h-32 w-32 object-contain cursor-pointer"
            />
          )}
        </div>
        <div className="p-4 flex flex-col flex-grow">
          <div className="flex justify-between items-start mb-2">
            <h3 className="font-semibold text-lg truncate text-[#3E2723]">{product.name}</h3>
            <span className="bg-[#3E2723] text-white text-xs font-medium px-2 py-1 rounded">
              {product.brand}
            </span>
          </div>
          <p className="text-gray-600 text-sm mb-4 line-clamp-2 flex-grow">
            {product.description}
          </p>
          <div className="flex justify-between items-center mt-auto">
            <span className="font-bold text-lg text-[#3E2723]">{product.price} zł</span>
            <div className="flex flex-col items-end">
              <button
                className="bg-[#3E2723] hover:bg-[#4E3423] text-white px-3 py-1 rounded text-sm transition-colors"
              >
                Add to Cart
              </button>
              {error && <span className="text-red-500 text-xs mt-1">{error}</span>}
              {isAdmin && (
                <div className="flex gap-2 mt-2">
                  <button
                    onClick={handleDelete}
                    className="px-2 py-1 bg-[#8B0000] hover:bg-[#7A0000] text-white rounded transition-colors"
                  >
                    Delete
                  </button>
                  <button
                    onClick={() => setShowEditModal(true)}
                    className="px-2 py-1 bg-[#3E2723] hover:bg-[#4E3423] text-white rounded transition-colors"
                  >
                    Edit
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
      {showEditModal && (
        <EditProductForm
          product={product}
          onClose={() => setShowEditModal(false)}
        />
      )}
    </>
  );
}