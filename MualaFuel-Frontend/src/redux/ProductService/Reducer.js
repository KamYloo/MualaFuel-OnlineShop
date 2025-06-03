import {
  SAVE_REQUEST,
  SAVE_SUCCESS,
  SAVE_ERROR,
  FETCH_PRODUCTS_REQUEST,
  FETCH_PRODUCTS_SUCCESS,
  FETCH_PRODUCTS_ERROR,
  DELETE_PRODUCT_REQUEST,
  DELETE_PRODUCT_SUCCESS,
  DELETE_PRODUCT_ERROR,
  UPDATE_PRODUCT_REQUEST,
  UPDATE_PRODUCT_SUCCESS,
  UPDATE_PRODUCT_ERROR,
} from "./ActionType.js";

const initialState = {
  saving: false,
  product: null,
  loading: false,
  products: [],
  totalPages: 0,
  currentPage: 0,
  error: null,
};

export const productReducer = (state = initialState, action) => {
  switch (action.type) {
    case SAVE_REQUEST:
      return { ...state, saving: true, error: null };
    case SAVE_SUCCESS:
      return {
        ...state,
        saving: false,
        product: action.payload,
        products: [action.payload, ...state.products],
      };
    case SAVE_ERROR:
      return { ...state, saving: false, error: action.payload };

    case FETCH_PRODUCTS_REQUEST:
      return { ...state, loading: true, error: null };
      case FETCH_PRODUCTS_SUCCESS:
          return {
              ...state,
              loading: false,
              products: action.payload.content,
              totalPages: action.payload.totalPages,
              currentPage: action.payload.number,
          };

      case FETCH_PRODUCTS_ERROR:
      return { ...state, loading: false, error: action.payload };

    case DELETE_PRODUCT_REQUEST:
      return { ...state, loading: true, error: null };
    case DELETE_PRODUCT_SUCCESS:
      return {
        ...state,
        loading: false,
        products: state.products.filter((p) => p.id !== action.payload),
      };
    case DELETE_PRODUCT_ERROR:
      return { ...state, loading: false, error: action.payload };

    case UPDATE_PRODUCT_REQUEST:
      return { ...state, loading: true, error: null };
    case UPDATE_PRODUCT_SUCCESS:
      return {
        ...state,
        loading: false,
        products: state.products.map((p) =>
          p.id === action.payload.id ? action.payload : p
        ),
      };
    case UPDATE_PRODUCT_ERROR:
      return { ...state, loading: false, error: action.payload };

    default:
      return state;
  }
};
