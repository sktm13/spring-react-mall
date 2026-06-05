const API_SERVER_HOST = import.meta.env.VITE_API_SERVER;

/** 원본 이미지 반환 */
export const getProductImage = (fileName?: string) => {
    if (!fileName || fileName.trim() === "") {
        return `${API_SERVER_HOST}/api/products/view/default.jpeg`;
    }

    return `${API_SERVER_HOST}/api/products/view/${fileName}`;
};

/** 썸네일 이미지 반환 */
export const getThumbnailImage = (fileName?: string) => {
    if (!fileName || fileName.trim() === "") {
        return `${API_SERVER_HOST}/api/products/view/s_default.jpeg`;
    }

    return `${API_SERVER_HOST}/api/products/view/s_${fileName}`;
};