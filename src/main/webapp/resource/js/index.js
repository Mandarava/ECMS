/**
 * Created by zt on 2016/12/20.
 */
$("#button_submit").click(function () {
    var fundId = $("#fundId option:selected").attr('id');
    var profit = $("#profit").val();
    var time = $('#profit_date').val();
    var profit = {code: fundId, profit: profit, time: time};
    $.ajax({
               type: "POST",
               url: "/fund/profit/addProfit",
               data: profit,
               success: function (data) {
                   if (data.hasOwnProperty('flag')) {
                       if (data.flag === 1) {
                           $("#profit").val('');
                           $("#dailyGainPercent").val('');
                       } else {
                           alert("insert failed!!!!!")
                       }
                   }
               },
               error: function (e) {
                   alert("出错!");
               }
           });
});

$('#insertOrUpdateFundNetValue').click(function () {
    $.ajax({
               type: "POST",
               url: "/fund/net/insertOrUpdateFundNetData",
               async: true,
               cache: false,
               success: function (data) {
                   if (data.hasOwnProperty('flag')) {
                       if (data.flag === 1) {
                           alert("数据更新成功！");
                       } else {
                           alert("数据更新失败！");
                       }
                   }
               },
               error: function (e) {
                   alert("error!");
               }
           })
});

$('#insertOrUpdateFundValue').click(function () {
    $.ajax({
               type: "POST",
               url: "/fund/insertOrUpdateFundData",
               async: true,
               cache: false,
               success: function (data) {
                   if (data.hasOwnProperty('flag')) {
                       if (data.flag === 1) {
                           alert("数据更新成功！");
                       } else {
                           alert("数据更新失败！");
                       }
                   }
               },
               error: function (e) {
                   alert("error!");
               }
           })
});

$('#producer').click(function () {
    $.ajax({
               type: "GET",
               url: "/fund/net/producer",
               async: true,
               cache: false,
               success: function (data) {

               },
               error: function (e) {
                   alert("error!");
               }
           })
});