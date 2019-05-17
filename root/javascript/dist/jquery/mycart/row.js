"use strict";

/**
 * File Name: row.js
 * Class Name: Row
 * Author: Zakaria Bakkal
 * Version: 3
 * Date: April 20, 2019
 * Description: This class represents a row of the shopping cart.
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

var Row =
  /*#__PURE__*/
  function () {
    /*
    * Class Constructor used to instantiate objects.
    * 
    * product: product added
    */
    function Row(product) {
      _classCallCheck(this, Row);

      this.product = product;
      this.qty = 1;
      this.totalPrice = this.product.price;
    }

    _createClass(Row, [{
      key: "incrementQty",
      /*
      * Increments the row qty variable by 1
      */
      value: function incrementQty() {
        return this._qty++;
      }
    },
    /*
    * Decrements the row qty variable by 1
    */
    {
      key: "decrementQty",
      value: function decrementQty() {
        return this._qty--;
      }
    },
    /*
    * Increments the row totalPrice variable by the product's price
    */
    {
      key: "incrementTotalPrice",
      value: function incrementTotalPrice() {
        this.totalPrice += this.product.price;
      }
    },
    /*
    * Decrements the row totalPrice variable by the product's price
    */
    {
      key: "decrementTotalPrice",
      value: function decrementTotalPrice() {
        this.totalPrice -= this.product.price;
      }
    },
    {
      key: "product",
      get: function get() {
        return this._product;
      },
      set: function set(product) {
        this._product = product;
      }
    },
    {
      key: "qty",
      get: function get() {
        return this._qty;
      },
      set: function set(qty) {
        this._qty = qty;
      }
    },
    {
      key: "totalPrice",
      get: function get() {
        return parseFloat(this._totalPrice.toFixed(2));
      }
      /*
      * Returns this row object as an array. Help store data
      * in localStorage and their retrieval.
      */
      ,
      set: function set(totalPrice) {
        this._totalPrice = parseFloat(totalPrice.toFixed(2));
      }
    },
    {
      key: "data",
      get: function get() {
        return new Array(this.product.name, this.product.price, this.qty, this.totalPrice);
      }
    }]);

    return Row;
  }();