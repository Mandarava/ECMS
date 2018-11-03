package com.finance.activiti.cmd;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.Map;

/**
 * 常规节点跳转,没有分支
 */
public class CommonJumpTaskCmd implements Command<Void> {

    private String processInstanceId;
    private ActivityImpl currentActivity;
    private ActivityImpl desActivity;
    private Map<String, Object> paramvar;
    private String rejectReason;

    /**
     * 构造参数 可以根据自己的业务需要添加更多的字段
     *
     * @param desActivity
     * @param paramvar
     * @param currentActivity
     */
    public CommonJumpTaskCmd(String processInstanceId, ActivityImpl currentActivity, ActivityImpl desActivity,
                             Map<String, Object> paramvar, String rejectReason) {
        this.processInstanceId = processInstanceId;
        this.currentActivity = currentActivity;
        this.desActivity = desActivity;
        this.paramvar = paramvar;
        this.rejectReason = rejectReason;
    }

    public Void execute(CommandContext commandContext) {
        // 获取当前流程的executionId，常规流程的执行实例ID与流程实例ID是相等的。
        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
        executionEntity.setVariables(paramvar);
        executionEntity.setExecutions(null);
        executionEntity.setEventSource(currentActivity);
        executionEntity.setActivity(currentActivity);
        commandContext.getHistoryManager().recordActivityEnd(executionEntity);
        for (TaskEntity taskEntity : Context.getCommandContext()
                .getTaskEntityManager().findTasksByProcessInstanceId(processInstanceId)) {
            if (taskEntity.isSuspended()) {
                throw new ActivitiException(getSuspendedTaskException());
            }
            // 触发任务监听
            taskEntity.fireEvent(TaskListener.EVENTNAME_COMPLETE);
//            if (Authentication.getAuthenticatedUserId() != null && processInstanceId != null) {
//                taskEntity.getProcessInstance().involveUser(Authentication.getAuthenticatedUserId(), IdentityLinkType.PARTICIPANT);
//            }
//            if(Context.getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
//                Context.getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(
//                        ActivitiEventBuilder.createEntityWithVariablesEvent(ActivitiEventType.TASK_COMPLETED, this, paramvar, false));
//            }
            commandContext.getTaskEntityManager().deleteTask(taskEntity, rejectReason, false);
            executionEntity.removeTask(taskEntity);
        }
//        executionEntity.destroyScope(rejectReason);
        executionEntity.executeActivity(desActivity);
        return null;
    }

    private String getSuspendedTaskException() {
        return "Cannot jump a suspended task";
    }

}
