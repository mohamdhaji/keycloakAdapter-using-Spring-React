import axios from "axios";
import {
  GET_PRODUCTS_BY_SELL,
  GET_PRODUCTS_BY_ARRIVAL,
  ADD_TYPE,
  GET_PRODUCTS_TO_SHOP,
  ADD_PRODUCT,
  CLEAR_PRODUCT,
  GET_PRODUCT_DETAIL,
  CLEAR_PRODUCT_DETAIL,
  GET_TYPES,
  GET_PRODUCTS,
} from "./types";

import { PRODUCT_SERVER } from "../components/utils/misc";
import { TYPE_SERVER } from "../components/utils/misc";

export function getProductDetail(id) {
  const request = axios.get(`${PRODUCT_SERVER}/${id}`).then((response) => {
    return response.data;
  });

  return {
    type: GET_PRODUCT_DETAIL,
    payload: request,
  };
}

export function clearProductDetail() {
  return {
    type: CLEAR_PRODUCT_DETAIL,
    payload: "",
  };
}

export function getProductsBySell() {
  //?sortBy=sold&order=desc&limit=100
  const request = axios
    .get(`${PRODUCT_SERVER}/findBySold`)
    .then((response) => response.data);

  return {
    type: GET_PRODUCTS_BY_SELL,
    payload: request,
  };
}

export function getProductsByArrival() {
  const request = axios
    .get(`${PRODUCT_SERVER}/findByArrival`)
    .then((response) => response.data);

  return {
    type: GET_PRODUCTS_BY_ARRIVAL,
    payload: request,
  };
}

export function getAllProducts() {
  const request = axios
    .get(`${PRODUCT_SERVER}/all`)
    .then((response) => response.data);

  return {
    type: GET_PRODUCTS,
    payload: request,
  };
}

export function getProductsToShop(
  skip,
  limit,
  filters = [],
  previousState = []
) {
  const data = {
    limit,
    skip,
    filters,
  };

  const request = axios
    .post(`${PRODUCT_SERVER}/shop`, data)
    .then((response) => {
      let newState = [...previousState, ...response.data];
      return {
        size: response.data.length,
        products: newState,
      };
    });

  return {
    type: GET_PRODUCTS_TO_SHOP,
    payload: request,
  };
}

export function addProduct(datatoSubmit) {
  const request = axios
    .post(`${PRODUCT_SERVER}`, datatoSubmit)
    .then((response) => response.data);

  return {
    type: ADD_PRODUCT,
    payload: request,
  };
}

export function clearProduct() {
  return {
    type: CLEAR_PRODUCT,
    payload: "",
  };
}

////////////////////////////////////
//////        Types
////////////////////////////////////

export function getTypes() {
  const request = axios
    .get(`${TYPE_SERVER}/all`)
    .then((response) => response.data);

  return {
    type: GET_TYPES,
    payload: request,
  };
}

export function addType(dataToSubmit, existingTypes) {
  const request = axios
    .post(`${TYPE_SERVER}`, dataToSubmit)
    .then((response) => {
      let types = [...existingTypes, response.data];
      return {
        success: response.data.name ? true : false,
        types,
      };
    });

  return {
    type: ADD_TYPE,
    payload: request,
  };
}
