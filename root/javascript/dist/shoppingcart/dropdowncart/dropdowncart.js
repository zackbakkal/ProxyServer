/**
 * File Name: dropdowncart.js
 * Author: Zakaria Bakkal
 * Version: 1
 * Date: April 17, 2019
 * Description: This script handles the shopping cart as a drop down
 *              when the user hovers over the cart icon.
 */

var cartIcon;       // The div that hosts the cart icon
var cart;           // the shopping cart          
var totalQty;       // the shopping cart total quantity
var totalPrice;     // the shopping cart total price
var myCart;         // the drop down cart

/*
* This function is called when the page has finished loading.
*/
function start() {
    // Retrieve the element from the DOM with the id named carticonimg
    cartIcon = document.getElementById("carticonimg");

    // Retrieve the element from the DOM with the id named cart and
    // set its display attribute height and width to 0
    if (document.getElementById("cart")) {
        cart = document.getElementById("cart");
        cart.style.height = "0";
        cart.style.width = "0";

        // Add an event listner to the element retrieved above.
        // When the mouse is over the cart icon the function is executed
        cartIcon.addEventListener("mouseover",
            function () {
                // give the drop down cart a border style
                cart.style.borderStyle = "outset";
                // give a color to the border color
                cart.style.borderColor = "rgb(248, 164, 118)";
                // make the height auto to fit content
                cart.style.height = "auto";
                // give the cart a 400px width
                cart.style.width = "400px";

                // Calls the showCart function
                showCart();
            }
            , false);
    }

}

/* 
* Displays the drop down cart
*/
function showCart() {

    // the drop down cart will only function on computer screens larger than 767px
    if (screen.width > 767) {
        // instantiate a cart object and load it from local storage
        myCart = new Cart();
        myCart.loadCart(JSON.parse(localStorage.getItem("mycart")));

        // display the items on the drop down cart
        displayItems();

        // Add an event listner to the cart icon when the mouse is out
        cartIcon.addEventListener("mouseout",
            function () {
                // reset the height and width to 0 and hide the border
                cart.style.height = "0";
                cart.style.width = "0";
                cart.style.borderColor = "white";
                cart.style.borderStyle = "hidden";

                // remove all itmes from the drop down cart
                while (cart.firstElementChild) {
                    cart.removeChild(cart.firstElementChild);
                }
            }
            , false);
    }
}

/*
* Displays the items on the drop down cart
*/
function displayItems() {
    // remove any children of drop down cart
    if (cart) {
        while (cart.firstElementChild) {
            cart.removeChild(cart.firstElementChild);
        }

        // create span element for the total pquantity and price
        // and reset them to 0
        totalQty = document.createElement("span");
        totalQty.innerHTML = 0;
        totalPrice = document.createElement("span");
        totalPrice.innerHTML = (0).toFixed(2);

        // check if the cart has any products added to it
        if (myCart._totalQty > 0) {

            // create a div respresenting the header of the cart
            var header = document.createElement("div");
            // add column names to the header
            var itemName = document.createElement("span");
            itemName.innerHTML = "Name";
            header.append(itemName);

            var itemPrice = document.createElement("span");
            itemPrice.innerHTML = "Price";
            header.append(itemPrice);

            var qty = document.createElement("span");
            qty.innerHTML = "Qty";
            header.append(qty);

            var total = document.createElement("span");
            total.innerHTML = "Total";
            header.append(total);

            // add the header to the cart
            cart.append(header);

            // Display added items on the cart
            myCart._rows.forEach(row => {
                displayItem(row);
            });

            // Display the shopping cart total qty of items
            totalQty.innerHTML = myCart._totalQty;

            // Display the shopping cart total price of items
            totalPrice.innerHTML = parseFloat((myCart._totalPrice).toFixed(2));

            // create a div that holds the totals
            var totals = document.createElement("div");
            totals.append(totalQty);
            totals.append(totalPrice);

            // ad the totals to the cart
            cart.append(totals);
        } else {    // the cart is empty
            var div = document.createElement("div");
            var emptyMessage = document.createTextNode("Your Cart is Empty");
            div.append(emptyMessage);

            cart.append(div);
        }
    }

}

/*
* Displays the item passed as an argument
*/
function displayItem(row) {

    // Create a table row
    var cartRow = document.createElement("div");

    // create a table data cell for item name, and add
    // the name to the innerHtml, then append the cell to the row
    var name = document.createElement("span");
    name.innerHTML = row._product._name;
    cartRow.append(name);

    // create a table data cell for item price, and add
    // the price to the innerHtml, then append the cell to the row
    var price = document.createElement("span");
    price.innerHTML = row._product._price;
    cartRow.append(price);

    // create a table data cell for item qty, and add
    // the qty to the innerHtml, then append the cell to the row
    var qty = document.createElement("span");
    qty.innerHTML = row._qty;
    cartRow.append(qty);

    // create a table data cell for item total price, and add
    // the total price to the innerHtml, then append the cell to the row
    var total = document.createElement("span");
    total.innerHTML = parseFloat((row._totalPrice).toFixed(2));
    cartRow.append(total);

    // Add the row to the tableBody
    cart.append(cartRow);
    cart.append(document.createElement("br"));
}

// Add an event listner to the window after the page is loaded
window.addEventListener("load", start, false);