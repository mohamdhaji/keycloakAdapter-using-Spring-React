import {
  LOGIN_USER,
  LOGOUT_USER,
  UPDATE_DATA_USER,
  GET_CART_ITEMS_USER,
  REMOVE_CART_ITEM_USER,
  ADD_TO_CART_USER,
} from "../actions/types";
import _ from "lodash";

const initialState = {
  validToken: false,
};

const booleanActionPayload = (payload) => {
  if (!_.isEmpty(payload)) {
    return true;
  } else {
    return false;
  }
};

export default function (state = initialState, action) {
  switch (action.type) {
    case LOGIN_USER:
      return {
        ...state,
        validToken: booleanActionPayload(action.payload),
        userRoles: action.payload.realm_access,
        userRole: action.payload.role,
        userName: action.payload.given_name,
        userLastname: action.payload.family_name,
        userEmail: action.payload.email,
      };
    case UPDATE_DATA_USER:
      return { ...state, updateUser: action.payload };

    case LOGOUT_USER:
      return {
        validToken: booleanActionPayload(action.payload),
      };

    case ADD_TO_CART_USER:
      return {
        ...state,
        success: action.payload,
      };
    case GET_CART_ITEMS_USER:
      return { ...state, cartDetail: action.payload };
    case REMOVE_CART_ITEM_USER:
      return {
        ...state,
        cartDetail: action.payload,
      };

    default:
      return state;
  }
}
