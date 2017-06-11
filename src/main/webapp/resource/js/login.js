/**
 * Created by zt on 2016/12/20.
 */
$('#login').on('click', function () {
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey($('#rsa_public_key').val());
    var encrypted = encrypt.encrypt($('#password').val());
    $('#password').val(encrypted);
    $('#form').submit();
});
