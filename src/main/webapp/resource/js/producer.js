/**
 * Created by zt on 2017/3/16.
 */
var timer;
$(document).ready(function () {
    $.ajax({
               type: "GET",
               url: "produce/start",
               cache: false,
               async: false,
               success: function (data) {
                   if (data === 'success') {
                       getData();
                       timer = window.setInterval(getData, 100);
                   }
               },
               error: function (e) {
                   alert("start producer failed!");
               }
           });
});

function getData() {
    $.ajax({
               type: "GET",
               url: "produce/data",
               cache: false,
               async: true,
               success: function (data) {
                   for (var i = 0; i < data.length; i++) {
                       $("#numbers").append("<tr><td>" + data[i] + "</td></tr>");
                   }
                   scorllBottom();
               },
               error: function (e) {
                   alert("error!");
                   window.clearInterval(timer);
               }
           });
}

function scorllBottom() {
    var h = document.documentElement.scrollHeight || document.body.scrollHeight;
    window.scrollTo(h, h);
}