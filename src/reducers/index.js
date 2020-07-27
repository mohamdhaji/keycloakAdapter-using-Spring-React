import { combineReducers } from "redux";
import user from "./user_reducer";
import products from "./products_reducer";
import site from "./site_reducer";
import errorReducer from "./errorReducer";

const rootReducer = combineReducers({
  user,
  products,
  site,
  errors: errorReducer,
});

export default rootReducer;
