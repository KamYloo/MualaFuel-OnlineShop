import React, {useEffect} from "react";
import {findProducts} from "../api/productService.js";
import 'rc-slider/assets/index.css';
import {InputSlider} from "../features/Assortyment/InputSlider.jsx";
import {ProductCard} from "../features/Assortyment/ProductCard.jsx";

function Assortment() {

    const [name, setName ] = React.useState("");
    const [brand, setBrand] = React.useState("");
    const [alcoholType, setAlcoholType] = React.useState([]);
    const [price, setPrice] = React.useState([0, 1000]);
    const [alcoholContent, setAlcoholContent] = React.useState([0, 100]);
    const [capacity, setCapacity] = React.useState([0, 1000]);

    const [products, setProducts] = React.useState([]);

    useEffect(() => {
        handleSubmit();
    }, []);

    const handleSubmit = () =>{
        findProducts(
            {
                name: name,
                brand: brand,
                alcoholType: alcoholType,
                minPrice: price[0],
                maxPrice: price[1],
                minAlcoholContent: alcoholContent[0],
                maxAlcoholContent: alcoholContent[1],
                minCapacity: capacity[0],
                maxCapacity: capacity[1],
            },
            {
                page: 0,
                size: 10
            }
        ).then((result) => {
            setProducts(result.content);
            console.log(result);
        }).catch((err) => {
            console.log(err);
        });
    }

    const handleCheckboxChange = (alcoholTypeSel) => {
        setAlcoholType(prevSelected => {
            if (prevSelected.includes(alcoholTypeSel)) {
                return prevSelected.filter(type => type !== alcoholTypeSel);
            } else {
                return [...prevSelected, alcoholTypeSel];
            }
        });
        console.log(alcoholType);
    };

    const availableAlcohols = [
        "WODKA", "WHISKEY", "RUM", "GIN", "TEQUILA", "WINE", "BEER", "LIQUEUR", "OTHER"
    ]

    return (
        <div className="flex flex-col lg:flex-row px-4 sm:px-8 md:px-12 lg:px-16 xl:px-24 my-6 sm:my-8 md:my-10 lg:my-12 gap-4">
            <div className="bg-amber-100 w-full lg:w-1/4 p-3 rounded-lg">
                <div className="space-y-6">
                    <div className="flex flex-col">
                        <label className="font-medium mb-1">Name</label>
                        <input
                            type="text"
                            onChange={(e) => setName(e.target.value)}
                            className="p-2 border rounded"
                        />
                    </div>

                    <div className="flex flex-col">
                        <label className="font-medium mb-1">Brand</label>
                        <input
                            type="text"
                            onChange={(e) => setBrand(e.target.value)}
                            className="p-2 border rounded"
                        />
                    </div>

                    <div>
                        <label className="font-medium mb-1 block">Alcohol Type</label>
                        <div className="space-y-2">
                            {availableAlcohols.map((alcoholType, index) => (
                                <div key={index} className="flex items-center">
                                    <input
                                        type="checkbox"
                                        id={`alcohol-${index}`}
                                        name={alcoholType.charAt(0).toUpperCase() + alcoholType.slice(1).toLowerCase()}
                                        value={alcoholType}
                                        onChange={() => handleCheckboxChange(alcoholType)}
                                        className="mr-2"
                                    />
                                    <label htmlFor={`alcohol-${index}`}>
                                        {alcoholType.charAt(0).toUpperCase() + alcoholType.slice(1).toLowerCase()}
                                    </label>
                                </div>
                            ))}
                        </div>
                    </div>

                    <div>
                        <label className="font-medium mb-1 block">Price</label>
                        <InputSlider
                            value={price}
                            setValue={setPrice}
                            min={0}
                            max={1000}
                        />
                    </div>

                    <div>
                        <label className="font-medium mb-1 block">Alcohol Content</label>
                        <InputSlider
                            value={alcoholContent}
                            setValue={setAlcoholContent}
                            min={0}
                            max={100}
                        />
                    </div>

                    <div>
                        <label className="font-medium mb-1 block">Capacity</label>
                        <InputSlider
                            value={capacity}
                            setValue={setCapacity}
                            min={0}
                            max={1000}
                        />
                    </div>

                    <div className="flex justify-center pt-2">
                        <button
                            className="w-full sm:w-32 border rounded-lg shadow bg-blue-100 hover:bg-blue-200 py-2 transition-colors"
                            onClick={handleSubmit}>
                            Search
                        </button>
                    </div>
                </div>
            </div>

            <div className="w-full lg:w-3/4">
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                    {products.map((product, index) => (
                        <ProductCard key={index} product={product} />
                    ))}
                </div>
            </div>
        </div>
    );
}

export {Assortment};