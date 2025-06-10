import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchProductsAction } from "../redux/ProductService/Action.js";
import { InputSlider } from "../features/Assortment/InputSlider.jsx";
import { ProductCard } from "../features/Assortment/ProductCard.jsx";
import Spinner from "../components/Spinner.jsx";
import ErrorOverlay from "../components/ErrorOverlay.jsx";

function Assortment() {
  const dispatch = useDispatch();
  const { products, loading, error, totalPages, currentPage } = useSelector(
    (state) => state.productService
  );

  const [name, setName] = useState("");
  const [brand, setBrand] = useState("");
  const [alcoholType, setAlcoholType] = useState([]);
  const [price, setPrice] = useState([0, 1000]);
  const [alcoholContent, setAlcoholContent] = useState([0, 100]);
  const [capacity, setCapacity] = useState([0, 1000]);

  useEffect(() => {
    handleSearch(0);
  }, []);

  const handleSearch = (page = 0) => {
    const criteria = {
      name,
      brand,
      alcoholType,
      minPrice: price[0],
      maxPrice: price[1],
      minAlcoholContent: alcoholContent[0],
      maxAlcoholContent: alcoholContent[1],
      minCapacity: capacity[0],
      maxCapacity: capacity[1],
    };
    dispatch(fetchProductsAction(criteria, page, 9));
  };

  const handleCheckboxChange = (selected) => {
    setAlcoholType((prev) =>
      prev.includes(selected) ? prev.filter((x) => x !== selected) : [...prev, selected]
    );
  };

  const availableAlcohols = [
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

  return (
    <div className="min-h-screen p-8" style={{ backgroundColor: "#f5e9dc" }}>
      <div className="max-w-7xl mx-auto flex flex-col lg:flex-row gap-4">
        <div className="w-full lg:w-1/4 bg-white rounded-xl shadow-md p-6 max-h-[900px] overflow-y-auto">
          <div className="space-y-6">
            <div className="flex flex-col">
              <label className="font-medium mb-1 text-[#3E2723]">Name</label>
              <input
                type="text"
                onChange={(e) => setName(e.target.value)}
                className="p-2 border rounded focus:outline-none focus:ring focus:border-[#3E2723]"
              />
            </div>
            <div className="flex flex-col">
              <label className="font-medium mb-1 text-[#3E2723]">Brand</label>
              <input
                type="text"
                onChange={(e) => setBrand(e.target.value)}
                className="p-2 border rounded focus:outline-none focus:ring focus:border-[#3E2723]"
              />
            </div>
            <div>
              <label className="font-medium mb-1 block text-[#3E2723]">Alcohol Type</label>
              <div className="space-y-2">
                {availableAlcohols.map((alcohol, index) => (
                  <div key={index} className="flex items-center">
                    <input
                      type="checkbox"
                      id={`alcohol-${index}`}
                      value={alcohol}
                      onChange={() => handleCheckboxChange(alcohol)}
                      className="mr-2"
                    />
                    <label htmlFor={`alcohol-${index}`} className="text-gray-700">
                      {alcohol.charAt(0).toUpperCase() + alcohol.slice(1).toLowerCase()}
                    </label>
                  </div>
                ))}
              </div>
            </div>
            <div>
              <label className="font-medium mb-1 block text-[#3E2723]">Price</label>
              <InputSlider value={price} setValue={setPrice} min={0} max={1000} />
            </div>
            <div>
              <label className="font-medium mb-1 block text-[#3E2723]">Alcohol Content</label>
              <InputSlider value={alcoholContent} setValue={setAlcoholContent} min={0} max={100} />
            </div>
            <div>
              <label className="font-medium mb-1 block text-[#3E2723]">Capacity</label>
              <InputSlider value={capacity} setValue={setCapacity} min={0} max={1000} />
            </div>
            <div className="flex justify-center pt-2">
              <button
                className="w-full sm:w-32 bg-[#3E2723] hover:bg-[#4E3423] text-white px-4 py-2 rounded-lg transition-colors"
                onClick={() => handleSearch(0)}
              >
                Search
              </button>
            </div>
          </div>
        </div>
        <div className="w-full lg:w-3/4">
          {loading ? (
              <Spinner size={350} />
          ) : error ? (
              <ErrorOverlay size={350} message={error}/>
          ) : (
              <>
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                  {products.map((product, index) => (
                  <ProductCard key={index} product={product} />
                ))}
              </div>
              <div className="flex justify-center items-center mt-6 space-x-4">
                <button
                  disabled={currentPage === 0}
                  onClick={() => handleSearch(currentPage - 1)}
                  className="px-4 py-2 bg-[#3E2723] text-white rounded disabled:opacity-50"
                >
                  Previous
                </button>
                <span>
                  Page {currentPage + 1} of {totalPages}
                </span>
                <button
                  disabled={currentPage === totalPages - 1}
                  onClick={() => handleSearch(currentPage + 1)}
                  className="px-4 py-2 bg-[#3E2723] text-white rounded disabled:opacity-50"
                >
                  Next
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export { Assortment };