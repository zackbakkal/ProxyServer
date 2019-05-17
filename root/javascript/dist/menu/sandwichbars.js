"use strict";

/**
 * File Name: sandwichbars.js
 * Author: Zakaria Bakkal
 * Version: 1
 * Date: April 06, 2019
 * Description: This script handles the menu on hand-held devices
 *              that have max-width 767px. The menu first looks like
 *              three horizontal bars, and when it is clicked the 
 *              the menu appears and the menu icon looks like an X.
 */
var sandwichbars; // The div that hosts the sandwich bars

var menu; // The div that hosts the menu

var bar1; // The top bar in the sandwichbars

var bar2; // The middle bar in the sandwichbars

var bar3; // The bottom bar in the sandwichbars

/*
* This function is called when the page has finished loading.
*/

function start() {
  // Retrieve the element from the DOM with the id named sandwichbars
  sandwichbars = document.getElementById("sandwichbars");

  // Retrieve the element from the DOM with the id named menu and
  // set its display attribute in the style to 'none'

  menu = document.getElementById("menu");
  menu.style.display = "none";

  // Check if the user is using a device with max-width of 768px, if
  // so we hide the menu
  if (screen.width < 768) {
    menu.style.display = "none";
  } else {
    // otherwise we put it back to its initial value (displayed)
    menu.style.display = "";
  }

  // Retrieve the element from the DOM with the id name bar1 and
  // set it class to bar1
  bar1 = document.getElementById("bar1");
  bar1.setAttribute("class", "bar1");

  // Retrieve the element from the DOM with the id name bar2 and
  // set it class to bar2
  bar2 = document.getElementById("bar2");
  bar2.setAttribute("class", "bar2");

  // Retrieve the element from the DOM with the id name bar3 and
  // set it class to bar3
  bar3 = document.getElementById("bar3");
  bar3.setAttribute("class", "bar3");

  // Add an event listner to the element retrieved above. When the element is clicked
  // call the function named changeLook
  sandwichbars.addEventListener("click", function () {
    // Calls the changeLook function and pass the sandwichbars element to it which
    // is represented by the keyword this.
    changeLook("change");

    // Remove the display property from the menu style so that it is shown
    // after we click again
    menu.style.removeProperty("display");
  }, false);

}

/* 
* Change how the sandwich bars menu look.
*/
function changeLook(className) {
  // Toggles between the bar1 class and the change class
  toggleClass(bar1, className + 1);

  //bar1.classList.toggle(className + 1);
  // Toggles between the bar2 class and the change class

  toggleClass(bar2, className + 2);

  // Toggles between the bar3 class and the change class
  toggleClass(bar3, className + 3);

  // Add an event listner to the sanwichbar so that if it is clicked when the 
  // menu is shown the menu will hide
  sandwichbars.addEventListener("click", function () {
    // Calls the resetSandwichBar function to change the menu button to
    // a sandwich look
    resetSandwichBar();
  }, false);
}

/*
* For Internet Explorer compatibility we use this function to toggle between
* classes.
*/
function toggleClass(element, className) {
  if (element.classList) {
    // Toggles between the bar1 class and the change class
    element.classList.toggle(className);
  } else {
    // For IE9
    // Retrieve the class attribute content and split the
    // words at the space to get an array of class names
    var classes = element.className.split(" ");

    // Retrieve the index of the class we want to toggle
    var i = classes.indexOf(className);

    // Check if that class exists
    if (i >= 0) //If so, remove it from the class list
      classes.splice(i, 1); else // other wise we add it to the class list
      classes.push(className); // update the elements class names

    element.className = classes.join(" ");
  }
}

/*
* Resets the sandwich bar menu button to look like a sandwich.
*/
function resetSandwichBar() {
  /*
  * Here the menu is shown as 3 bars
  */
  // Set the bar1 class name to bar1
  bar1.setAttribute("class", "bar1");
  // Set the bar1 class name to bar2
  bar2.setAttribute("class", "bar2");
  // Set the bar1 class name to bar3
  bar3.setAttribute("class", "bar3");

  // Hide the menu
  menu.style.display = "none";

  // Add an event listner to the sandwichbars menu. When the element is clicked
  // call the function named changeLook
  sandwichbars.addEventListener("click", function () {
    // Calls the changeLook function and pass the sandwichbars element to it which
    // is represented by the keyword this.
    changeLook("change");

    // Remove the display property from the menu style so that it is shown
    // after we click again
    menu.style.removeProperty("display");
  }, false);
}

// Add an event listner to the window after the page is loaded
window.addEventListener("load", start, false);