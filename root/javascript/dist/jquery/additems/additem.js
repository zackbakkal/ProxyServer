"use strict";

/**
 * File Name: additem.js
 * Author: Zakaria Bakkal
 * Version: 3
 * Date: April 20, 2019
 * Description: This script handles adding an item to the cart
 */

/*
* This function loads after the page finishes loading
*/
$(function () {
  if (localStorage.getItem("mycart") == null) {
    localStorage.setItem("mycart", JSON.stringify(new Cart().data));
    localStorage.setItem("carttotalqty", 0);
    $("#carttotalqty").text("0");
  } else {
    var qty = localStorage.getItem("carttotalqty");
    $("#carttotalqty").text(qty);
  }

  // represents the item we add to the cart
  var item;

  // Retrieve the addbutton element parent and add an event listner when it is
  // clicked

  $("#addbutton").parent().click(function () {

    // depending on the value of the id attribute of the addbutton element
    // we create the appropriate item
    switch ($("#addbutton").parent().attr("id")) {
      case "addarganoil":
        item = new Product("Argan Oil", 24.99);
        break;

      case "addblacksoap":
        item = new Product("Exfoliating Soap", 19.99);
        break;

      case "addlavaclay":
        item = new Product("Lava Clay", 14.99);
        break;
    }

    // load the cart from localStorage
    var myCart = new Cart();
    myCart.loadCart(JSON.parse(localStorage.getItem("mycart")));

    // add the item to the cart
    myCart.addProduct(item);

    // used to show cart total quantity next to the cart icon
    localStorage.setItem("carttotalqty", myCart.totalQty);

    // set the quantity value next to the cart icon
    $("#carttotalqty").text(myCart.totalQty);

    // show an idicator that an item is added to the cart
    $.fn.indicateItemAdded();
  });
});

/*
* I left this function written in javascript because
* it seems faster than when it is implemented using JQuery
*/
$.fn.indicateItemAdded = function () {
  // create an indicator element, with a +1 text inside it
  var indicator = document.createElement("span");
  indicator.setAttribute("id", "indicator");
  indicator.style.position = "absolute";
  indicator.style.display = "inline";
  indicator.style.width = "auto";
  indicator.style.height = "auto";
  indicator.style.borderRadius = "50px";
  indicator.style.backgroundColor = "#E65100";
  indicator.style.fontWeight = "bold";
  indicator.style.margin = "0";
  indicator.style.padding = "0";
  indicator.style.transition = "all 2s ease-in";
  indicator.innerHTML = "+1";
  indicator.style.border = "solid 1px black";

  // Retrieve the addbutton element of the page
  var addButton = document.getElementById("addbutton");
  // insert the indicator before the 2nd child element in addbutton
  // which is the suggestions
  addButton.append(indicator);

  // change the indicator class that makes it move upwards.
  // setting a time out will make the script wait until the 
  // indicator element is created.
  setTimeout(function () {
    indicator.className = "indicatorup";
  });

  // wait 2 seconds then remove the indicator element
  setTimeout(function () {
    addButton.removeChild(addButton.childNodes[1]);
  }, 2000);
}