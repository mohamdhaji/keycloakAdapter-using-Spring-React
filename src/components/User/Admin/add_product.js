import React, { Component } from "react";
import UserLayout from "../../../hoc/user";

import FormField from "../../utils/Form/formfield";
import {
  update,
  generateData,
  isFormValid,
  populateOptionFields,
  resetFields,
} from "../../utils/Form/formActions";
import FileUpload from "../../utils/Form/fileupload";

import { connect } from "react-redux";
import {
  getTypes,
  addProduct,
  clearProduct,
} from "../../../actions/products_actions";

class AddProduct extends Component {
  state = {
    formError: false,
    formSuccess: false,
    formdata: {
      productName: {
        element: "input",
        value: "",
        config: {
          label: "Product name",
          name: "name_input",
          type: "text",
          placeholder: "Enter your name",
        },
        validation: {
          required: true,
        },
        valid: false,
        touched: false,
        validationMessage: "",
        showlabel: true,
      },
      sold: {
        element: "input",
        value: "",
        config: {
          label: "Product Sold Times",
          name: "sold_input",
          type: "text",
          placeholder: "Enter sold times",
        },
        validation: {
          required: true,
        },
        valid: false,
        touched: false,
        validationMessage: "",
        showlabel: true,
      },
      description: {
        element: "textarea",
        value: "",
        config: {
          label: "Product description",
          name: "description_input",
          type: "text",
          placeholder: "Enter your description",
        },
        validation: {
          required: true,
        },
        valid: false,
        touched: false,
        validationMessage: "",
        showlabel: true,
      },
      price: {
        element: "input",
        value: "",
        config: {
          label: "Product price",
          name: "price_input",
          type: "number",
          placeholder: "Enter your price",
        },
        validation: {
          required: true,
        },
        valid: false,
        touched: false,
        validationMessage: "",
        showlabel: true,
      },
      typeName: {
        element: "select",
        value: "",
        config: {
          label: "Product Type",
          name: "types_input",
          options: [],
        },
        validation: {
          required: true,
        },
        valid: false,
        touched: false,
        validationMessage: "",
        showlabel: true,
      },
      shipping: {
        element: "select",
        bool: true,
        value: "",
        config: {
          label: "Shipping",
          name: "shipping_input",
          options: [
            { key: true, value: true },
            { key: false, value: false },
          ],
        },
        validation: {
          required: true,
        },
        valid: false,
        touched: false,
        validationMessage: "",
        showlabel: true,
      },
      available: {
        element: "select",
        value: "",
        bool: true,
        config: {
          label: "Available, in stock",
          name: "available_input",
          options: [
            { key: true, value: true },
            { key: false, value: false },
          ],
        },
        validation: {
          required: true,
        },
        valid: false,
        touched: false,
        validationMessage: "",
        showlabel: true,
      },

      publish: {
        element: "select",
        value: "",
        bool: true,
        config: {
          label: "Publish",
          name: "publish_input",
          options: [
            { key: true, value: true },
            { key: false, value: false },
          ],
        },
        validation: {
          required: true,
        },
        valid: false,
        touched: false,
        validationMessage: "",
        showlabel: true,
      },
      images: {
        value: [],
        validation: {
          required: false,
        },
        valid: true,
        touched: false,
        validationMessage: "",
        showlabel: false,
      },
    },
  };

  updateFields = (newFormdata) => {
    this.setState({
      formdata: newFormdata,
    });
  };

  updateForm = (element) => {
    console.log(element.event.target.value);
    const newFormdata = update(element, this.state.formdata, "products");
    this.setState({
      formError: false,
      formdata: newFormdata,
    });
  };

  resetFieldHandler = () => {
    const newFormData = resetFields(this.state.formdata, "products");

    this.setState({
      formdata: newFormData,
      formSuccess: true,
    });
    setTimeout(() => {
      this.setState(
        {
          formSuccess: false,
        },
        () => {
          this.props.dispatch(clearProduct());
        }
      );
    }, 3000);
  };

  submitForm = (event) => {
    event.preventDefault();

    let dataToSubmit = generateData(this.state.formdata, "products");
    let formIsValid = isFormValid(this.state.formdata, "products");

    if (formIsValid) {
      this.props.dispatch(addProduct(dataToSubmit)).then(() => {
        if (this.props.products.addProduct.success) {
          this.resetFieldHandler();
        } else {
          this.setState({ formError: true });
        }
      });
    } else {
      this.setState({
        formError: true,
      });
    }
  };

  componentDidMount() {
    const formdata = this.state.formdata;

    this.props.dispatch(getTypes()).then((response) => {
      const newFormData = populateOptionFields(
        formdata,
        this.props.products.types,
        "typeName"
      );
      this.updateFields(newFormData);
    });
  }

  imagesHandler = (images) => {
    const newFormData = {
      ...this.state.formdata,
    };
    newFormData["images"].value = images;
    newFormData["images"].valid = true;

    this.setState({
      formdata: newFormData,
    });
  };

  render() {
    return (
      <UserLayout>
        <div>
          <h1>Add product</h1>

          <form onSubmit={(event) => this.submitForm(event)}>
            <FileUpload
              imagesHandler={(images) => this.imagesHandler(images)}
              reset={this.state.formSuccess}
            />

            <FormField
              id={"productName"}
              formdata={this.state.formdata.productName}
              change={(element) => this.updateForm(element)}
            />

            <FormField
              id={"sold"}
              formdata={this.state.formdata.sold}
              change={(element) => this.updateForm(element)}
            />

            <div className="form_devider"></div>

            <FormField
              id={"description"}
              formdata={this.state.formdata.description}
              change={(element) => this.updateForm(element)}
            />

            <FormField
              id={"price"}
              formdata={this.state.formdata.price}
              change={(element) => this.updateForm(element)}
            />

            <div className="form_devider"></div>

            <FormField
              id={"typeName"}
              formdata={this.state.formdata.typeName}
              change={(element) => this.updateForm(element)}
            />

            <FormField
              id={"shipping"}
              formdata={this.state.formdata.shipping}
              change={(element) => this.updateForm(element)}
            />

            <FormField
              id={"available"}
              formdata={this.state.formdata.available}
              change={(element) => this.updateForm(element)}
            />

            <div className="form_devider"></div>

            <FormField
              id={"publish"}
              formdata={this.state.formdata.publish}
              change={(element) => this.updateForm(element)}
            />

            {this.state.formSuccess ? (
              <div className="form_success">Success</div>
            ) : null}

            {this.state.formError ? (
              <div className="error_label">Please check your data</div>
            ) : null}
            <button onClick={(event) => this.submitForm(event)}>
              Add product
            </button>
          </form>
        </div>
      </UserLayout>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    products: state.products,
  };
};

export default connect(mapStateToProps)(AddProduct);
