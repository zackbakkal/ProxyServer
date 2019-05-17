"use strict";

/**
 * File Name: suggestions.js
 * Author: Zakaria Bakkal
 * Version: 1
 * Date: April 17, 2019
 * Description: This script handles item suggestions for the user.
 */
var addButton; // the add item button we listen to

var xhr; // used to make XMLHttpRequest

var suggestionsContainer; // the div containing the suggestions

var items; // the items the program suggests to the user

var suggested; // flag to control display of suggestions

/*
* This function loads after the page finishes loading
*/

function start() {
  // Retrieve the add button element
  addButton = document.getElementById("addbutton"); // Retrieve the suggestionContainer element

  suggestionsContainer = document.getElementById("suggestions"); // give it a width of 0

  suggestionsContainer.style.width = "0"; // Retrieve the items element

  items = document.getElementById("items"); // set the flag to false, meaning no suggestions made yet

  suggested = false; // add an event listner when it is clicked

  addButton.addEventListener("click", function () {
    // give the suggestion container width of 100%
    suggestionsContainer.style.width = "100%"; // check if already suggestions have been made

    if (!suggested) {
      // create a heading of type h3
      var h3 = document.createElement("h3");
      h3.innerHTML = "Items you might be interested in"; // insert the h3 heading before the first child of suggestions container

      suggestionsContainer.insertBefore(h3, suggestionsContainer.childNodes[0]); // display suggestions to the user, the argument helps 
      // identify which item we are adding to the cart, and 
      // which items to suggest

      showSuggestions(this.parentNode.id);
    }
  }, false);
}
/*
* Displays suggestions
*/


function showSuggestions(parentId) {
  // depending on the value of the parent's id we
  // make the corresponding suggestions
  switch (parentId) {
    case "addarganoil":
      getSuggections(["exfoliatingsoap", "lavaclay"]);
      break;

    case "addblacksoap":
      getSuggections(["arganoil", "lavaclay"]);
      break;

    case "addlavaclay":
      getSuggections(["arganoil", "exfoliatingsoap"]);
      break;
  }
}
/*
* Retrieves suggestions froma JSON file
*/


function getSuggections(suggestions) {
  // try to start an asynchronous request
  try {
    xhr = new XMLHttpRequest(); // create request object
    // register event handler

    xhr.onreadystatechange = function () {
      // for each suggestion in the array suggestions
      // add the suggestion
      suggestions.forEach(function (suggestion) {
        add(suggestion);
      }); // set the flag to true, meaning suggested

      suggested = true;
    }; // get the content of the JSON file


    xhr.open("GET", "json/suggestions.json", true); // send the request

    xhr.send(null);
  } catch (exception) {
    alert("Something went wrong.");
  }
}
/*
* Adds a suggestion to the suggestiuon container by calling 
* the display method
*/


function add(suggestion) {
  // if the AJAX request is successful
  if (xhr.readyState == 4 && xhr.status == 200) {
    // get the content of the JSON file
    var response = JSON.parse(xhr.responseText); // depending on the value of the suggestion argument
    // we display the right item

    switch (suggestion) {
      case "arganoil":
        display(response[0]);
        break;

      case "exfoliatingsoap":
        display(response[1]);
        break;

      case "lavaclay":
        display(response[2]);
        break;
    }
  }
}
/*
* Displays the item passed in the argument
*/


function display(item) {
  // Create a div that holds the item
  var container = document.createElement("div"); // style the container with round corners

  container.setAttribute("class", "round"); // create an image element

  var img = document.createElement("img"); // set its source and alt attributes from the content of the JSON file

  img.setAttribute("src", item.src);
  img.setAttribute("alt", item.alt); // create an anchor element

  var anchor = document.createElement("a"); // set its href attribute from the content of the JSON file

  anchor.setAttribute("href", item.href); // add the img element to the a element

  anchor.append(img); // add the a element to the div container

  container.append(anchor); // append the container to the items container

  items.append(container);
}

window.addEventListener("load", start, false);