import axios from "axios";
import jwt_decode from "jwt-decode";

import {
  LOGIN_USER,
  LOGOUT_USER,
  GET_ERRORS,
  REGISTER_USER,
  ADD_TO_CART_USER,
  GET_CART_ITEMS_USER,
  REMOVE_CART_ITEM_USER,
  ON_SUCCESS_BUY_USER,
  UPDATE_DATA_USER,
  CLEAR_UPDATE_USER_DATA,
} from "./types";

import setJWTToken from "../components/securityUtils/setJWTToken";

import { U_SERVER, USER_SERVER } from "../components/utils/misc";

export function registerUser(dataToSubmit) {
  const request = axios
    .post(`${USER_SERVER}/create`, dataToSubmit)
    .then((response) => response.data);

  return {
    type: REGISTER_USER,
    payload: request,
  };
}

export const loginUser = async (LoginRequest) => {
  try {
    // post => Login Request
    const res = await axios.post(`${USER_SERVER}/token`, LoginRequest);
    // extract token from res.data
    const token = res.data;
    // store the token in the localStorage
    localStorage.setItem("accessToken", token.access_token);
    localStorage.setItem("refreshToken", token.refresh_token);
    localStorage.setItem("isAuth", token.isAuth);
    localStorage.setItem("expiresIn", token.expires_in);
    localStorage.setItem("refreshExpiresIn", token.refresh_expires_in);

    localStorage.setItem("role", token.role);

    // set our token in header ***
    setJWTToken(token.access_token);
    // decode token on React
    const decoded = jwt_decode(token.access_token);

    return {
      type: LOGIN_USER,
      payload: { ...decoded, role: token.role.toLowerCase() },
    };
  } catch (err) {
    console.log(err.response.data.error);
    return {
      type: GET_ERRORS,
      payload: err.response.data.error,
    };
  }
};

export const logout = async () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  localStorage.setItem("isAuth", false);
  localStorage.removeItem("expiresIn");
  localStorage.removeItem("refreshExpiresIn");
  localStorage.removeItem("role");

  setJWTToken(false);
  return {
    type: LOGOUT_USER,
    payload: {},
  };
};

export function addToCart(uemail, pid) {
  const request = axios
    .post(`${U_SERVER}/cart/${uemail}/${pid}`)
    .then((response) => response.data);

  return {
    type: ADD_TO_CART_USER,
    payload: request,
  };
}

export function getCartItems(uemail) {
  // const config = {
  //   headers: { Authorization: "Bearer " + localStorage.accessToken },
  // };

  const request = axios.get(`${U_SERVER}/cart/${uemail}`).then((response) => {
    return response.data.products;
  });

  return {
    type: GET_CART_ITEMS_USER,
    payload: request,
  };
}

export function removeCartItem(uemail, id) {
  const request = axios
    .delete(`${U_SERVER}/cart/${uemail}/${id}`)
    .then((response) => {
      return response.data.products;
    });

  return {
    type: REMOVE_CART_ITEM_USER,
    payload: request,
  };
}

export function updateUserData(dataToSubmit, useremail) {
  console.log(useremail);
  const request = axios
    .post(`${USER_SERVER}/updateuser/${useremail}`, dataToSubmit)
    .then((response) => {
      return response.data;
    });

  return {
    type: UPDATE_DATA_USER,
    payload: request,
  };
}

export function clearUpdateUser() {
  return {
    type: CLEAR_UPDATE_USER_DATA,
    payload: "",
  };
}
