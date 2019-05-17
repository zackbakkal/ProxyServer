"use strict";

/**
 * File Name: Product.js
 * Class Name: Product
 * Author: Zakaria Bakkal
 * Version: 3
 * Date: April 20, 2019
 * Description: This class represents the items that users are
 *              interested in adding to their carts.
 */

function _classCallCheck(instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
}

function _defineProperties(target, props) {
  for (var i = 0; i < props.length; i++) {
    var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor);
  }
}

function _createClass(Constructor, protoProps, staticProps) {
  if (protoProps) {
    _defineProperties(Constructor.prototype, protoProps);
  }
  if (staticProps) {
    _defineProperties(Constructor, staticProps);
  }
  return Constructor;
}

var Product =
  /*#__PURE__*/
  function () {
    /*
    * Class Constructor used to instantiate objects.
    * 
    * name: product name
    * price: product price
    */
    function Product(name, price) {
      _classCallCheck(this, Product);

      // invokes the setters
      this.name = name;
      this.price = price;
    }

    _createClass(Product, [{
      key: "name",
      get: function get() {
        return this._name;
      },
      set: function set(name) {
        this._name = name;
      }
    },
    {
      key: "price",
      get: function get() {
        return parseFloat(this._price.toFixed(2));
      },
      set: function set(price) {
        this._price = parseFloat(price.toFixed(2));
      }
    },
    /*
    * Returns this product object as an array. Help store data
    * in localStorage and their retrieval.
    */
    {
      key: "data",
      get: function get() {
        return new Array(this.name, this.price);
      }
    }]);

    return Product;
  }();