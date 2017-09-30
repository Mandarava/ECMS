$(document).ready(function () {
    $.ajax({
               type: "GET",
               url: "/ECMS/scheduler/list",
               cache: false,
               async: true,
               success: function (data) {
                   if (data) {
                       for (var i = 0; i < data.length; i++) {
                           $("#triggers").append("<tr>");
                           $("#triggers").append("<td>" + data[i].jobName + "</td>");
                           $("#triggers").append("<td>" + data[i].jobGroup + "</td>");
                           $("#triggers").append("<td>" + data[i].cronExpression + "</td>");
                           $("#triggers").append("<td>" + data[i].description + "</td>");
                           $("#triggers").append("<td>" + data[i].targetObject + "</td>");
                           $("#triggers").append("<td>" + data[i].targetMethod + "</td>");
                           $("#triggers").append("<td>" + data[i].misfireInstruction + "</td>");
                           $("#triggers").append("<td>" + data[i].recovery + "</td>");
                           $("#triggers").append("<td>" + data[i].durable + "</td>");
                           $("#triggers").append("<td>" + data[i].concurrent + "</td>");
                           $("#triggers").append("<td>" + data[i].cluster + "</td>");
                           $("#triggers").append("</tr>");
                       }
                   }
               },
               error: function (e) {
                   alert("error!");
               }
           });
});