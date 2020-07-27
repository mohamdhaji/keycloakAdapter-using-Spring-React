import MyButton from "../utils/button";
import Login from "./login";

import React, { Component } from "react";
import { connect } from "react-redux";

class RegisterLogin extends Component {
  componentDidMount() {
    if (this.props.user.validToken) {
      this.props.history.push("/user/dashboard");
    }
  }

  render() {
    return (
      <div className="page_wrapper">
        <div className="container">
          <div className="register_login_container">
            <div className="left">
              <h1>New Customers</h1>
              <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
                enim ad minim veniam, quis nostrud exercitation ullamco laboris
                nisi ut aliquip ex ea commodo consequat.{" "}
              </p>
              <MyButton
                type="default"
                title="Create an account"
                linkTo="/register"
                addStyles={{
                  margin: "10px 0 0 0",
                }}
              />
            </div>
            <div className="right">
              <h2>Registered customers</h2>
              <p>If you have an account please log in.</p>
              <Login />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    user: state.user,
  };
}

export default connect(mapStateToProps)(RegisterLogin);
