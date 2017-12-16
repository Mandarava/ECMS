/**
 * Created by zt on 2016/12/20.
 */
$('#login').on('click', function () {
    $('#login').attr('disabled', 'disabled');
    // load_rsa_public_key 请求RSA公钥
    $.ajax({
       type: "GET",
       url: "/rsa/public_key",
       cache: false,
       async: false,
       dataType: "json",
       success: function (data) {
           $('#rsa_public_key').val(data);
           doLogin();
       },
       error: function (e) {
           alert("login failed!");
           $('#login').removeAttr('disabled');
       }
   });
});

function doLogin() {
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey($('#rsa_public_key').val());
    var encrypted = encrypt.encrypt($('#password').val());
    $('#password').val(encrypted);
    $('#form').submit();
}
