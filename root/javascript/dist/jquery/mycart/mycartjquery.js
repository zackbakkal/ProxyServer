"use strict";

/**
 * File Name: mycartjquery.js
 * Author: Zakaria Bakkal
 * Version: 4
 * Date: April 20, 2019
 * Description: This script handles the shopping cart.
 *              it displays, adds and remove items from
 *              the shopping cart.
 */
var myCart;
var tableBody;
var totalQty;
var totalPrice;
$(function () {

  // Retrieve the tableBody element
  // Retrive the totalqty element and set it to 0
  $("#totalqty").text("0");
  // Retrieve the totalprice element and set it to 0.00
  $("#totalprice").text("0");
  // retrieve the cart from the local storage
  myCart = new Cart();
  myCart.loadCart(JSON.parse(localStorage.getItem("mycart")));
  // Retrieve the clear button element and add an event 
  // listner when clicked
  $("#clear").click(function () {
    $.fn.clearCart();
  });
  $.fn.displayItems();

  // when the customer clicks on the checkout button the checkout page is opened, on the condition that the shopping cart has items in it
  $("#checkout").click(function () {
    if (myCart._totalQty > 0) {
      window.open("checkout.html", "_self");
    }
  });
});

/*
* Displays items on the shopping cart
*/
$.fn.displayItems = function () {
  // remove any children of tableBody element
  if ($("#tablebody").children().length > 0) {
    $("#tablebody").empty();
  }

  // reset totals
  $("#totalqty").text("0");
  $("#totalprice").text("0.00");

  // check if the cart has any products added to it
  if (myCart._totalQty > 0) {
    // used to assign class names and ids to table rows
    var rowNumber = 0;
    // Display added items on the cart
    myCart._rows.forEach(function (row) {
      $.fn.displayItem(row, rowNumber);
      rowNumber++;
    });

    // Display the shopping cart total qty of items
    $("#totalqty").text(myCart._totalQty); // Display the shopping cart total price of items
    $("#totalprice").text(parseFloat(myCart._totalPrice.toFixed(2)));
  }
};

/*
* Displays the item passed as an argument
*/
$.fn.displayItem = function (row, rowNumber) {
  $("#tablebody")
    .append($("<tr></tr>")
      .append($("<td></td>")
        .text(row._product._name))
      .append($("<td></td>")
        .text(row._product._price))
      .append($("<td></td>")
        .append($("<span></span>")
          .text("-")
          .attr("class", "minus" + rowNumber)
          .attr("id", "minus" + rowNumber)
          .click(function () {
            // decrease the item at rowNumber
            $.fn.minusProduct(rowNumber);
            // redisplay the cart
            $.fn.displayItems();
          }))
        .append($("<p></p>")
          .text(row._qty)
          .attr("id", "qtyvalue"))
        .append($("<span></span>")
          .text("+")
          .attr("class", "plus" + rowNumber)
          .attr("id", "plus" + rowNumber)
          .click(function () {
            // increase item qty
            $.fn.plusProduct(rowNumber);
            // redisplay the cart
            $.fn.displayItems();
          })))
      .append($("<td></td>")
        .text(parseFloat(row._totalPrice.toFixed(2))))
      .append($("<td></td>")
        .text("X")
        .attr("class", "removeMe" + rowNumber)
        .attr("id", "removeMe" + rowNumber)
        .css("text-align", "center")
        .css("cursor", "pointer")
        .click(function () {
          $.fn.removeRow(rowNumber);
          // store the cart on local storage
          localStorage.setItem("mycart", JSON.stringify(myCart.data));
          // Display the shopping cart total qty of items
          $("#totalqty").text(myCart._totalQty);
          // Display the shopping cart total price of items
          $("#totalprice").text(parseFloat(myCart._totalPrice.toFixed(2)));
          // redisplay the cart
          $.fn.displayItems();
        }))
      .attr("id", rowNumber));
};

/*
* Removes an entire row from the cart
*/
$.fn.removeRow = function (rowNumber) {
  myCart.removeRow(rowNumber);
};

/*
* Decreases the quantity of an item
*/
$.fn.minusProduct = function (rowNumber) {
  myCart.minusProduct(rowNumber);
};

/*
* Increases the quantity of an item
*/
$.fn.plusProduct = function (rowNumber) {
  myCart.plusProduct(rowNumber);
};

/*
* Clears the shopping cart
*/
$.fn.clearCart = function () {
  // clear the cart
  myCart.clear();
  // remove all children of table body
  $("#tablebody").empty();
  // reset the total quantity
  $("#totalqty").text("0");
  //reset the total price
  $("#totalprice").text("0.00");
};