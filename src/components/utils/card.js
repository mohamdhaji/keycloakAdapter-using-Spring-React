import React, { Component } from "react";
import MyButton from "./button";

import { connect } from "react-redux";
import { addToCart } from "../../actions/user_actions";
import Dialog from "@material-ui/core/Dialog";

class Card extends Component {
  state = {
    success: false,
  };
  renderCardImage(images) {
    if (images.length > 0) {
      return images[0].url;
    } else {
      return "/images/image_not_availble.png";
    }
  }

  render() {
    const props = this.props;
    // const nameSplited= props.productName.split(" ");
    // const productName =nameSplited[0]+" "+nameSplited[1]

    return (
      <div className={`card_item_wrapper ${props.grid}`}>
        <div
          className="image"
          style={{
            background: `url(${this.renderCardImage(props.images)}) no-repeat`,
          }}
        >
          {" "}
        </div>
        <div className="action_container">
          <div className="tags">
            <div className="brand">{props.typeName}</div>
            <div className="name">{props.productName}</div>
            <div className="name">${props.price}</div>
          </div>

          {props.grid ? (
            <div className="description">
              <p>{props.description}</p>
            </div>
          ) : null}
          <div className="actions">
            <div className="button_wrapp">
              <MyButton
                type="default"
                altClass="card_link"
                title="View product"
                linkTo={`/product_detail/${props.id}`}
                addStyles={{
                  margin: "10px 0 0 0",
                }}
              />
            </div>
            <div className="button_wrapp">
              <MyButton
                type="bag_link"
                runAction={() => {
                  props.user.validToken
                    ? this.props
                        .dispatch(addToCart(props.user.userEmail, props.id))
                        .then((r) => {
                          this.setState({ success: r.payload.success });
                          window.setTimeout(() => {
                            this.setState({ success: false });
                          }, 2000); //any arbitary timeout
                        })
                    : console.log("you need to log in");
                }}
              />
            </div>
          </div>
        </div>
        <Dialog open={this.state.success}>
          <div className="dialog_alert">
            <div>product added to your cart successfully !!</div>
          </div>
        </Dialog>
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    user: state.user,
  };
};

export default connect(mapStateToProps)(Card);
