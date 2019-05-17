"use strict";

/**
 * File Name: cart.js
 * Author: Zakaria Bakkal
 * Version: 3
 * Date: April 20, 2019
 * Description: This class is used to represent a shopping cart.
 */

function _classCallCheck(instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
}

function _defineProperties(target, props) {
  for (var i = 0; i < props.length; i++) {
    var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) {
      descriptor.writable = true;
    }
    Object.defineProperty(target, descriptor.key, descriptor);
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

var Cart =
  /*#__PURE__*/
  function () {
    function Cart() {
      _classCallCheck(this, Cart);

      this.rows = new Array();
      this.totalQty = 0;
      this.totalPrice = 0;
    }
    _createClass(Cart, [{
      key: "loadCart",

      /*
      * Loads data from the argument storage to the cart.
      * storage: represents localStorage. It is an array with
      * 3 elements:
      *    -storage[0]: Represents an array of row objects
      *    -storage[1]: Represents the quantity of product objects in the cart
      *    -storage[2]: Represents the total price of the cart
      */
      value: function loadCart(storage) {
        if (storage != null) {
          this.rows = storage[0];
          this.totalQty = storage[1];
          this.totalPrice = parseFloat(storage[2].toFixed(2));
        }
      }
      /*
      * Adds a product to the cart. If a product is new to the cart a new row
      * is added, otherwise the quantity of the existing product is incremented.
      */

    },
    {
      key: "addProduct",
      value: function addProduct(product) {
        // Check a cart does not exists in localStorage, or
        // a cart exists but no products in it or,
        // a cart exists and the product is not in the cart
        if (localStorage.getItem("mycart") == null || this.totalQty == 0 || this.findProduct(product) == -1) {
          // add the new product to the cart
          this.rows.push(new Row(product));
        } else {
          // look for the product in the cart,
          // so that if it exists we increment
          // the quantity, otherwise we add it
          // to the cart.
          var index = this.findProduct(product);
          var foundRow = this.rows[index];

          //Check if the row is found then we increment the quantity
          if (foundRow) {
            // if the product is found
            // increment the product quantity by 1
            foundRow._qty++; // increment the total price of the row 
            // by product price

            foundRow._totalPrice += foundRow._product._price;
          }
        }

        // update the total price by adding the product price
        this.updateTotalPrice(product.price);

        // increment the total quantity by 1
        this.incrementTotalQty();
        this.updateLocalStorage();
      }
    },
    /*
    * Decrements the quantity of a product in the cart by 1.
    */
    {
      key: "minusProduct",
      value: function minusProduct(rowNumber) {
        // decrement row's quantity
        this.rows[rowNumber]._qty -= 1;
        // decrement rows's total price
        this.rows[rowNumber]._totalPrice -= this.rows[rowNumber]._product._price;
        // decrement cart's total quantity
        this.decrementTotalQty();
        // decrement cart's total price
        this.updateTotalPrice(-parseFloat(this.rows[rowNumber]._product._price.toFixed(2)));

        if (this.rows[rowNumber]._qty == 0) {
          this.removeRow(rowNumber);
        }

        this.updateLocalStorage();
      }
    },
    /*
    * increments the quantity of a product in the cart by 1.
     */
    {
      key: "plusProduct",
      value: function plusProduct(rowNumber) {
        // increment row's quantity
        this.rows[rowNumber]._qty += 1;
        // increment rows's total price
        this.rows[rowNumber]._totalPrice += this.rows[rowNumber]._product._price;
        // increment cart's total quantity
        this.incrementTotalQty();
        // increment cart's total price

        this.updateTotalPrice(parseFloat(this.rows[rowNumber]._product._price.toFixed(2)));
        this.updateLocalStorage();
      }
    },
    {
      key: "findProduct",
      value: function findProduct(product) {
        var index = -1;
        // Check if the cart is not empty

        if (this._rows.length > 0) {
          // iterate over the rows of the cart
          // and return the index of the product in
          // the array rows.
          for (var i = 0; i < this._rows.length; i++) {
            if (this._rows[i]._product._name == product.name) {
              return i;
            }
          }
        }

        // At this point no match is found (index = -1)
        return index;
      }
    },
    /*
    * Removes a row from rows variable.
    */
    {
      key: "removeRow",
      value: function removeRow(rowNumber) {
        // Subtruct the total quantity of the row from the total quantity of the cart
        this.totalQty -= myCart.rows[rowNumber]._qty;
        // Subtruct the total price of the row from the total price of the cart
        this.totalPrice -= parseFloat(myCart.rows[rowNumber]._totalPrice.toFixed(2));
        // remove the row at index rowNumber
        this.rows.splice(rowNumber, 1);
        this.updateLocalStorage();
      }
    },
    {
      key: "updateTotalPrice",
      value: function updateTotalPrice(value) {
        this.totalPrice += parseFloat(value.toFixed(2));
      }
    },
    {
      key: "updateTotalQty",
      value: function updateTotalQty(value) {
        this.totalQty += value;
      }
    },
    {
      key: "incrementTotalQty",
      value: function incrementTotalQty() {
        parseFloat((this.totalQty++).toFixed(2));
      }
    },
    {
      key: "decrementTotalQty",
      value: function decrementTotalQty() {
        parseFloat((this.totalQty--).toFixed(2));
      }
    },
    /*
    * Clears the cart completely. and clears the local storage.
    */
    {
      key: "clear",
      value: function clear() {
        this._rows = new Array();
        this.totalQty = 0;
        this._totalPrice = parseFloat(0).toFixed(2);
        localStorage.clear();
      }
    },
    {
      key: "updateLocalStorage",
      value: function updateLocalStorage() {
        localStorage.setItem("mycart", JSON.stringify(this.data));
        localStorage.setItem("carttotalqty", this.totalQty);
      }
    },
    {
      key: "rows",
      get: function get() {
        return this._rows;
      },
      // setters
      set: function set(rows) {
        this._rows = rows;
      }
    },
    {
      key: "totalPrice",
      get: function get() {
        return parseFloat(this._totalPrice.toFixed(2));
      },
      set: function set(totalPrice) {
        this._totalPrice = parseFloat(totalPrice.toFixed(2));
      }
    },
    {
      key: "totalQty",
      get: function get() {
        return this._totalQty;
      },
      set: function set(totalQty) {
        this._totalQty = totalQty;
      }
    },
    /*
    * Returns this cart object as an array. Help store data
    * in localStorage and their retrieval.
    */
    {
      key: "data",
      get: function get() {
        return new Array(this.rows, this.totalQty, parseFloat(this.totalPrice.toFixed(2)));
      }
    }]);

    return Cart;
  }();