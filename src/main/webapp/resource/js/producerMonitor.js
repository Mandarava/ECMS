/**
 * Created by zt on 2017/3/18.
 */
var pathName = window.document.location.pathname;
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
var host = window.document.location.host

var websocket;
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://" + host + projectName + "/producer-websocket");
} else if ('MozWebSocket' in window) {
    websocket = new MozWebSocket("ws://" + host + projectName + "/producer-websocket");
} else {
    websocket = new SockJS(projectName + '/sockjs/producer-websocket');
}
websocket.onopen = function (event) {

};
websocket.onmessage = function (event) {
    $("#numbers").html("");
    var data = JSON.parse(event.data);
    for (var i = 0; i < data.length; i++) {
        $("#numbers").append("<tr><td>" + data[i] + "</td></tr>");
    }
};
websocket.onerror = function (event) {
    alert('websocket error');
};
websocket.onclose = function (event) {
    alert('websocket close');
}

