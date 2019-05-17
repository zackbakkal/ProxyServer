/**
 * File Name: approved.js
 * Author: Zakaria Bakkal
 * Version: 1
 * Date: April 26, 2019
 * Description: This script handles the checkout process of the
 *              customer's order.
 */

/*
* This function is fired after the document is ready
*/
$(function () {
    // retrieve the order from local storage
    var order = JSON.parse(localStorage.getItem("order"));
    // print the order details on the page
    $("#id").text(order.create_time);
    $("#total").text(order.purchase_units[0].amount.currency_code + " " + order.purchase_units[0].amount.value);
    $("#name").text(order.purchase_units[0].shipping.name.full_name);
    $("#line1").text(order.purchase_units[0].shipping.option.address.address_line_1);
    $("#line2").text(order.purchase_units[0].shipping.option.address.address_line_2);
    $("#province").text(order.purchase_units[0].shipping.option.address.admin_area_1);
    $("#city").text(order.purchase_units[0].shipping.option.address.admin_area_2);
    $("#country").text(order.purchase_units[0].shipping.option.address.country_code);
    $("#zipcode").text(order.purchase_units[0].shipping.option.address.postal_code);
});