import React, { Component } from "react";
import { Link, withRouter } from "react-router-dom";

import { connect } from "react-redux";
import { logout } from "../../../actions/user_actions";

class Header extends Component {
  state = {
    page: [
      {
        name: "Home",
        linkTo: "/",
        public: true,
      },
      {
        name: "shop",
        linkTo: "/shop",
        public: true,
      },
    ],
    user: [
      // {
      //   name: "My Cart",
      //   linkTo: "/user/cart",
      //   public: false,
      // },
      {
        name: "My Account",
        linkTo: "/user/dashboard",
        public: false,
      },
      {
        name: "Log in",
        linkTo: "/register_login",
        public: true,
      },
      {
        name: "Log out",
        linkTo: "/user/logout",
        public: false,
      },
    ],
  };

  logoutHandler = () => {
    this.props.dispatch(logout()).then((response) => {
      if (this.props.user.validToken === false) {
        this.props.history.push("/");
      }
    });
  };

  // cartLink = (item, i) => {
  //   const user = this.props.user;

  //   return (
  //     <div className="cart_link" key={i}>
  //       <span>{user.cartDetail ? user.cartDetail.length : 0}</span>
  //       <Link to={item.linkTo}>{item.name}</Link>
  //     </div>
  //   );
  // };

  defaultLink = (item, i) =>
    item.name === "Log out" ? (
      <div
        className="log_out_link"
        key={i}
        onClick={() => this.logoutHandler()}
      >
        {item.name}
      </div>
    ) : (
      <Link to={item.linkTo} key={i}>
        {item.name}
      </Link>
    );

  showLinks = (type) => {
    let list = [];

    type.forEach((item) => {
      if (!this.props.user.validToken) {
        if (item.public === true) {
          list.push(item);
        }
      } else {
        if (item.name !== "Log in") {
          list.push(item);
        }
      }
    });

    return list.map((item, i) => {
      if (item.name !== "My Cart") {
        return this.defaultLink(item, i);
      } else {
        return this.cartLink(item, i);
      }
    });
  };

  render() {
    return (
      <header className="bck_b_light">
        <div className="container">
          <div className="left">
            <div className="logo">Fashion Week</div>
          </div>
          <div className="right">
            <div className="top">{this.showLinks(this.state.user)}</div>
            <div className="bottom">{this.showLinks(this.state.page)}</div>
          </div>
        </div>
      </header>
    );
  }
}

function mapStateToProps(state) {
  return {
    user: state.user,
  };
}

export default connect(mapStateToProps)(withRouter(Header));
