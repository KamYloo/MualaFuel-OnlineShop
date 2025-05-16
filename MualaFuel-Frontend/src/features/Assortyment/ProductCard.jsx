import default_product_image from "../../assets/default_product_image.png";

export function ProductCard({ product }) {
    return (
        <div className="w-full h-full bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300 flex flex-col">
            <div className="aspect-square bg-gray-100 flex items-center justify-center">
                {product.imagePath ? (
                    <img
                        src={product.imagePath}
                        alt={product.name}
                        className="h-full w-full object-cover"
                    />
                ) : (
                    <img
                        src={default_product_image}
                        alt="Default product"
                        className="h-32 w-32 object-contain"
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
                    <button className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded text-sm transition-colors">
                        Add to cart
                    </button>
                </div>
            </div>
        </div>
    );
}