import React from "react";
import { Switch, Route } from "react-router-dom";

import Layout from "./hoc/layout";
import PageNotFound from "./components/utils/page_not_found";

import Home from "./components/Home";
import RegisterLogin from "./components/Register_login";
import Register from "./components/Register_login/register";
import UserDashboard from "./components/User";
import SecuredRoute from "./components/securityUtils/SecureRoute";
import Shop from "./components/Shop";
import AddProduct from "./components/User/Admin/add_product";
import ManageSite from "./components/User/Admin/manage_site";
import ManageCategories from "./components/User/Admin/manage_categories";
import UpdateProfile from "./components/User/update_profile";
import ProductPage from "./components/Product";
import UserCart from "./components/User/cart";

const Routes = () => {
  return (
    <Layout>
      <Switch>
        <Route path="/" exact component={Home}  />

        <Route path="/register_login" exact component={RegisterLogin} />
        <Route path="/register" exact component={Register} />
        <SecuredRoute path="/user/dashboard" exact component={UserDashboard} />
        <Route path="/shop" exact component={Shop} />

        <SecuredRoute path="/admin/add_product" exact component={ AddProduct} />
        <SecuredRoute path="/admin/site_info" exact component={ManageSite} />
        <SecuredRoute
          path="/admin/manage_categories"
          exact
          component={ManageCategories}
        />
        <SecuredRoute
          path="/user/user_profile"
          exact
          component={UpdateProfile}
        />
        <Route path="/product_detail/:id" exact component={ProductPage} />
        <SecuredRoute path="/user/cart" exact component={UserCart} />

        <Route component={PageNotFound} />
      </Switch>
    </Layout>
  );
};

export default Routes;
