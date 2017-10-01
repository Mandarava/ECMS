$(document).ready(function () {
    initSchedulerTable();
});
var schedulerTable;
function initSchedulerTable() {
    schedulerTable =
        $('#scheduler_table').DataTable({
            "ajax": "/scheduler/list",
            "columns": [
                {"data": "jobId"},
                {"data": "jobName"},
                {"data": "jobGroup"},
                {"data": "cronExpression"},
                {"data": "description"},
                {
                    "data": "targetMethod",
                    render: function (data, type, row) {
                        return row.targetObject + '.'
                               + row.targetMethod;
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
            "aoColumnDefs": [
                {
                    "targets": -1,
                    "bSortable": false,
                    render: function (data, type, row) {
                        return '<a type="button" class="btn btn-sm" href="#" id="resume">恢复</a>'
                               + '<a type="button" class="btn btn-sm" href="#" id="pause">暂停</a>'
                               + '<a type="button" class="btn btn-sm" href="#" id="delete">删除</a>'
                               + '<br>'
                               + '<a type="button" class="btn btn-sm" href="#" id="trigger">立即执行</a>'
                               + '<a type="button" class="btn btn-sm" href="#" id="edit" data-toggle="modal" data-target="#editTaskModal">编辑</a>';
                    }
                },
                {
                    "targets": 0,
                    "visible": false,
                    "searchable": false
                }
            ]
        });
}

$('#scheduler_table tbody').on('click', 'a#resume', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('POST', '/scheduler/resume', data);
}).on('click', 'a#pause', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('POST', '/scheduler/pause', data);
}).on('click', 'a#delete', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('POST', '/scheduler/delete', data);
}).on('click', 'a#trigger', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    ajaxPost('POST', '/scheduler/trigger', data);
}).on('click', 'a#edit', function () {
    var data = $('#scheduler_table').DataTable().row($(this).parents('tr')).data();
    $('#editCronExpressionTxt').val(data.cronExpression);
    $('#editJobId').val(data.jobId);
    $('#editJobGroup').val(data.jobGroup);
    $('#editJobName').val(data.jobName);
});

$('#saveTask').on('click', function () {
    var jobName = $('#jobNameTxt').val();
    var jobGroup = $('#jobGroupTxt').val();
    var cronExpression = $('#cronExpressionTxt').val();
    var description = $('#descriptionTxt').val();
    var targetObject = $('#targetObjectTxt').val();
    var targetMethod = $('#targetMethodTxt').val();
    var misfireInstruction = $('#misfireInstructionSelect option:selected').val();
    var recovery = $('#recoverySelect option:selected').val();
    var durable = $('#durableSelect option:selected').val();
    var concurrent = $('#concurrentSelect option:selected').val();
    var cluster = $('#clusterSelect option:selected').val();
    var data = {
        "jobName": jobName,
        "jobGroup": jobGroup,
        "cronExpression": cronExpression,
        "description": description,
        "targetObject": targetObject,
        "targetMethod": targetMethod,
        "misfireInstruction": misfireInstruction,
        "recovery": recovery,
        "durable": durable,
        "concurrent": concurrent,
        "cluster": cluster
    };
    var successCallback = function (data) {
        if (data.code === 200) {
            $('#jobNameTxt').val('');
            $('#jobGroupTxt').val('');
            $('#cronExpressionTxt').val('');
            $('#descriptionTxt').val('');
            $('#targetObjectTxt').val('');
            $('#targetMethodTxt').val('');
            $('#misfireInstructionSelect option:first').prop("selected", 'selected');
            $('#recoverySelect option:first').prop("selected", 'selected');
            $('#durableSelect option:first').prop("selected", 'selected');
            $('#concurrentSelect option:first').prop("selected", 'selected');
            $('#clusterSelect option:first').prop("selected", 'selected');

            $('#newTaskModal').modal('hide');
            schedulerTable.ajax.reload( null, false );
        } else {
            alert('处理出错！');
        }

    };
    ajaxPost("POST", "/scheduler/add", data, successCallback);
});

$('#editTaskSave').on('click', function () {
    var cronExpression = $('#editCronExpressionTxt').val();
    var jobId = $('#editJobId').val();
    var jobName = $('#editJobName').val();
    var jobGroup = $('#editJobGroup').val();
    var data = {
        "jobId": jobId,
        "jobName": jobName,
        "jobGroup": jobGroup,
        "cronExpression": cronExpression
    };
    var successCallback = function (data) {
        if (data.code === 200) {
            $('#editCronExpressionTxt').val('');
            $('#editJobId').val('');
            $('#editJobName').val('');
            $('#editJobGroup').val('');
            $('#editTaskModal').modal('hide');
            schedulerTable.ajax.reload( null, false );
        } else {
            alert('处理出错！');
        }

    };
    ajaxPost("POST", "/scheduler/reschedule", data, successCallback);
});

function ajaxPost(type, url, data, callback) {
    $.ajax({
           type: type,
           url: url,
           cache: false,
           async: true,
           dataType: "json",
           data: data,
           success: callback,
           error: function (e) {
               console.log('failure');
           }
       });
}