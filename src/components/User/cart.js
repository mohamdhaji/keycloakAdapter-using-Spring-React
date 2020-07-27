import React, { Component } from "react";
import UserLayout from "../../hoc/user";
import UserProductBlock from "../utils/User/product_block";

import { connect } from "react-redux";
import { getCartItems, removeCartItem } from "../../actions/user_actions";

import FontAwesomeIcon from "@fortawesome/react-fontawesome";
import faFrown from "@fortawesome/fontawesome-free-solid/faFrown";
import faSmile from "@fortawesome/fontawesome-free-solid/faSmile";

class UserCart extends Component {
  state = {
    loading: true,
    total: 0,
    showTotal: false,
    showSuccess: false,
  };

  componentDidMount() {
    let cartItems = [];
    let user = this.props.user;

    this.props.dispatch(getCartItems(user.userEmail)).then(() => {
      if (this.props.user.cartDetail.length > 0) {
        this.calculateTotal(this.props.user.cartDetail);
      }
    });
  }

  calculateTotal = (cartDetail) => {
    let total = 0;

    cartDetail.forEach((item) => {
      total += parseInt(item.price, 10) * item.userDemand;
    });

    this.setState({
      total,
      showTotal: true,
    });
  };

  removeFromCart = (uemail, id) => {
    this.props.dispatch(removeCartItem(uemail, id)).then(() => {
      if (this.props.user.cartDetail.length <= 0) {
        this.setState({
          showTotal: false,
        });
      } else {
        this.calculateTotal(this.props.user.cartDetail);
      }
    });
  };

  showNoItemMessage = () => (
    <div className="cart_no_items">
      <FontAwesomeIcon icon={faFrown} />
      <div>You have no items</div>
    </div>
  );

  // transactionError = (data) => {
  //   console.log("Paypal error");
  // };

  // transactionCanceled = () => {
  //   console.log("Transaction cancled");
  // };

  // transactionSuccess = (data) => {
  //   this.props
  //     .dispatch(
  //       onSuccessBuy({
  //         cartDetail: this.props.user.cartDetail,
  //         paymentData: data,
  //       })
  //     )
  //     .then(() => {
  //       if (this.props.user.successBuy) {
  //         this.setState({
  //           showTotal: false,
  //           showSuccess: true,
  //         });
  //       }
  //     });
  // };

  render() {
    let userEmail = this.props.user.userEmail;

    return (
      <UserLayout>
        <div>
          <h1>My cart</h1>
          <div className="user_cart">
            <UserProductBlock
              products={this.props.user}
              type="cart"
              removeItem={(id) => this.removeFromCart(userEmail, id)}
            />
            {this.state.showTotal ? (
              <div>
                <div className="user_cart_sum">
                  <div>Total amount: $ {this.state.total}</div>
                </div>
              </div>
            ) : this.state.showSuccess ? (
              <div className="cart_success">
                <FontAwesomeIcon icon={faSmile} />
                <div>THANK YOU</div>
                <div>YOUR ORDER IS NOW COMPLETE</div>
              </div>
            ) : (
              this.showNoItemMessage()
            )}
          </div>
          {this.state.showTotal ? (
            <div className="paypal_button_container">
              {/* <Paypal
                                    toPay={this.state.total}
                                    transactionError={(data)=> this.transactionError(data)}
                                    transactionCanceled={(data)=> this.transactionCanceled(data)}
                                    onSuccess={(data)=> this.transactionSuccess(data)}
                                /> */}
            </div>
          ) : null}
        </div>
      </UserLayout>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    user: state.user,
  };
};

export default connect(mapStateToProps)(UserCart);
