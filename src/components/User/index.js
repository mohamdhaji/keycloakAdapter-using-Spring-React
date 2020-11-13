import React, { Component } from "react";
import UserLayout from "../../hoc/user";
import MyButton from "../utils/button";
// import UserHistoryBlock from "../utils/User/history_block";
import { connect } from "react-redux";
// import { getCartItems } from "../../actions/user_actions";
class UserDashboard extends Component {
  // componentDidMount() {
  //   this.props.dispatch(getCartItems(this.props.user.userEmail));
  // }
  render() {
    return (
      <UserLayout>
        <div>
          <div className="user_nfo_panel">
            <h1>User information</h1>
            <div>
              <span>{this.props.user.userName}</span>
              <span>{this.props.user.userLastname}</span>
              <span>{this.props.user.userEmail}</span>
            </div>
            <MyButton
              type="default"
              title="Edit account info"
              linkTo="/user/user_profile"
            />
          </div>

          {/* {user.userData.history ? (
            <div className="user_nfo_panel">
              <h1>History purchases</h1>
              <div className="user_product_block_wrapper">
                <UserHistoryBlock products={user.userData.history} />
              </div>
            </div>
          ) : null} */}
        </div>
      </UserLayout>
    );
  }
}

function mapStateToProps(state) {
  return {
    user: state.user,
  };
}

export default connect(mapStateToProps)(UserDashboard);
