import apiClient from "./api.js";

export const findProducts = async (productSearch, pageable) => {
    const response = await apiClient.post(`/product/find`, productSearch, {
            params: {
                page: pageable.page,
                size: pageable.size,
                sort: pageable.sort
            }
        }
    );
    return response.data;
};

