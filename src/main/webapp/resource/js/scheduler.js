$(document).ready(function () {
    $('#scheduler_table').DataTable({
       "ajax": "/scheduler/list",
       "columns": [
           {"data": "jobName"},
           {"data": "jobGroup"},
           {"data": "cronExpression"},
           {"data": "description"},
           {
               "data": "targetMethod",
               render: function (data, type, row) {
                   return row.targetObject + '.' +  row.targetMethod;
               }
           },
           {
               "data": "misfireInstruction",
               render: function (data, type, row) {
                   if (1 === data) {
                       return "FIRE_ONCE_NOW";
                   } else if (2 === data) {
                       return "DO_NOTHING";
                   } else if (-1 === data) {
                       return "IGNORE_MISFIRE_POLICY";
                   } else if (0 === data) {
                       return "SMART_POLICY";
                   } else {
                       return "";
                   }
               }
           },
           {
               "data": "recovery",
               render: function (data, type, row) {
                   return data === true ? "是" : "否";
               }
           },
           {
               "data": "durable",
               render: function (data, type, row) {
                   return data === true ? "是" : "否";
               }
           },
           {
               "data": "concurrent",
               render: function (data, type, row) {
                   return data === true ? "是" : "否";
               }
           },
           {
               "data": "cluster",
               render: function (data, type, row) {
                   return data === true ? "集群" : "单机";
               }
           },
           {"data": null}
       ],
       "aoColumnDefs": [ {
           "targets": -1,
           "bSortable": false,
           render: function(data, type, row) {
               return '<a type="button" class="btn btn-sm" href="#" id="resume">恢复</a>'
                      + '<a type="button" class="btn btn-sm" href="#" id="pause">暂停</a>'
                      + '<a type="button" class="btn btn-sm" href="#" id="delete">删除</a>' + '<br>'
                      + '<a type="button" class="btn btn-sm" href="#" id="execute">立即执行</a>'
                      + '<a type="button" class="btn btn-sm" href="#" id="edit">编辑</a>';
           }
       }]
   });
});

$('#scheduler_table tbody').on( 'click', 'a#resume', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('POST','/scheduler/resume',data);
}).on( 'click', 'a#pause', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('POST','/scheduler/pause',data);
}).on( 'click', 'a#delete', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('DELETE','/scheduler/delete',data);
}).on( 'click', 'a#execute', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('POST','/scheduler/execute',data);
}).on( 'click', 'a#edit', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('POST','/scheduler/edit',data);
});

function ajaxPost(type, url, data) {
    $.ajax({
       type: type,
       url: url,
       cache: false,
       async: true,
       dataType: "json",
       data: data,
       success: function (data) {
           alert('操作成功');
       },
       error: function (e) {
           console.log(e);
       }
   });
}