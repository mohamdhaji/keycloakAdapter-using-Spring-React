import React, { Component } from "react";
import PageTop from "../utils/page_top";

import ProdNfo from "./prodNfo";
import ProdImg from "./prodImg";
import Dialog from "@material-ui/core/Dialog";

import { connect } from "react-redux";
import { addToCart } from "../../actions/user_actions";
import {
  getProductDetail,
  clearProductDetail,
} from "../../actions/products_actions";

class ProductPage extends Component {
  state = {
    success: false,
  };
  componentDidMount() {
    const id = this.props.match.params.id;
    this.props.dispatch(getProductDetail(id)).then(() => {
      if (!this.props.products.prodDetail) {
        this.props.history.push("/");
      }
    });
  }

  componentWillUnmount() {
    this.props.dispatch(clearProductDetail());
  }

  addToCartHandler(id) {
    this.props.dispatch(addToCart(this.props.user.userEmail, id)).then((r) => {
      this.setState({ success: r.payload.success });
      window.setTimeout(() => {
        this.setState({ success: false });
      }, 2000); //any arbitary timeout
    });
  }

  render() {
    return (
      <div>
        <PageTop title="Product detail" />
        <div className="container">
          {this.props.products.prodDetail ? (
            <div className="product_detail_wrapper">
              <div className="left">
                <div style={{ width: "500px" }}>
                  <ProdImg detail={this.props.products.prodDetail} />
                </div>
              </div>
              <div className="right">
                <ProdNfo
                  addToCart={(id) => this.addToCartHandler(id)}
                  detail={this.props.products.prodDetail}
                />
              </div>
            </div>
          ) : (
            "Loading"
          )}
        </div>
        <Dialog open={this.state.success}>
          <div className="dialog_alert">
            <div>product added to your cart successfully !!</div>
          </div>
        </Dialog>
        ;
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    products: state.products,
    user: state.user,
  };
};

export default connect(mapStateToProps)(ProductPage);
