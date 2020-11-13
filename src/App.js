import React, { Component } from "react";
import "./App.css";
import Routes from "./routes";

import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import "./Resources/css/styles.css";

import store from "./store";

import setJWTToken from "./components/securityUtils/setJWTToken";
import { LOGIN_USER } from "./actions/types";
import { logout } from "./actions/user_actions";
import jwt_decode from "jwt-decode";
// only executed at first time this component in rendered , also whenever you reload(refresh) the app .
let isAuth =
  localStorage.isAuth == null
    ? false
    : localStorage.isAuth.toLowerCase() === "true";

if (isAuth) {
  const access_token = localStorage.accessToken;
  const decoded_jwtToken = jwt_decode(access_token);

  const user = { ...decoded_jwtToken, role: localStorage.role };
  setJWTToken(access_token);
  store.dispatch({
    type: LOGIN_USER,
    payload: user,
  });

  const currentTime = Date.now() / 1000;
  if (localStorage.expiresIn < currentTime) {
    console.log("App.fffff");

    store.dispatch(logout());
    window.location.href = "/";
  }
}
class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <BrowserRouter>
          <Routes />
        </BrowserRouter>
      </Provider>
    );
  }
}

export default App;
