import {
  GET_PRODUCTS_BY_SELL,
  GET_PRODUCTS_BY_ARRIVAL,
  GET_TYPES,
  ADD_TYPE,
  GET_PRODUCTS_TO_SHOP,
  ADD_PRODUCT,
  CLEAR_PRODUCT,
  GET_PRODUCT_DETAIL,
  CLEAR_PRODUCT_DETAIL,
  GET_PRODUCTS,
} from "../actions/types";

export default function (state = {}, action) {
  switch (action.type) {
    case GET_PRODUCTS_BY_SELL:
      return { ...state, bySell: action.payload };
    case GET_PRODUCTS_BY_ARRIVAL:
      return { ...state, byArrival: action.payload };
    case GET_TYPES:
      return { ...state, types: action.payload };
    case GET_PRODUCTS:
      return {
        ...state,
        toShop: action.payload,
        toShopSize: action.payload.size,
      };
    case ADD_TYPE:
      return {
        ...state,
        addType: action.payload.success,
        types: action.payload.types,
      };

    case GET_PRODUCTS_TO_SHOP:
      return {
        ...state,
        toShop: action.payload.products,
        toShopSize: action.payload.size,
      };
    case ADD_PRODUCT:
      return { ...state, addProduct: action.payload };
    case CLEAR_PRODUCT:
      return { ...state, addProduct: action.payload };
    case GET_PRODUCT_DETAIL:
      return { ...state, prodDetail: action.payload };
    case CLEAR_PRODUCT_DETAIL:
      return { ...state, prodDetail: action.payload };
    default:
      return state;
  }
}
