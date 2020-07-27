import { createStore, applyMiddleware, compose } from "redux";
import ReduxThunk from "redux-thunk";
import rootReducer from "./reducers";
import promiseMiddleware from "redux-promise";

let store;

const createStoreWithMiddleware = applyMiddleware(
  promiseMiddleware,
  ReduxThunk
)(createStore);

store = createStoreWithMiddleware(
  rootReducer,
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
);

export default store;
