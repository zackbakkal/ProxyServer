"use strict";

/**************************************************************
 * File Name: currentpagemenu.js
 * Author: Zakaria Bakkal
 * Version: 2
 * Date: April 20, 2019
 * This script assigns the class attribute the names black or
 * currentpagemenu. If the link is for the current page it will
 * show light gray otherwise it will show black.
 *************************************************************/
$(function () {
  // get the href from the url
  var location = window.location;
  var pathname = location.pathname;
  var href = pathname.substring(pathname.lastIndexOf('/') + 1);

  // Retrive the li element in the element with id name menu. And,
  // filter the result by comparing the href attribute of the child 
  // of each li element, which is an anchor element, with the path
  // of the current window.
  $("#menu li").filter(function () {
    $(this).attr("class", function () {
      if ($(this).find("a").attr("href") == href) {
        return "currentpagemenu";
      } else {
        return "black";
      }
    });
  });
});