/**
 * Created by zt on 2017/2/7.
 */
$('#submitOrder').on('click', function () {
    $.ajax({
               type: "POST",
               url: "/fund/order/add",
               dataType: "json",
               cache: false,
               async: true,
               data: {
                   code: $("#fundId option:selected").attr('id'),
                   buyDate: $('#buy_date').val(),
                   buyAmount: $('#buy_amount').val(),
                   buyNetValue: $('#buy_net_value').val(),
                   share: $('#share').val(),
                   fee: $('#fee').val(),
                   orderType: $("#order_type option:selected").attr('id')
               },
               success: function (data) {
                   if (data.hasOwnProperty('flag')) {
                       if (data.flag === 1) {
                           $('#buy_date').val('');
                           $('#buy_amount').val('');
                           $('#buy_net_value').val('');
                           $('#share').val('');
                           $('#fee').val('');
                       }
                   }
               },
               error: function (e) {
                   alert("error!");
               }
           })
});