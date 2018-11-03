package com.finance.activiti.cmd;

import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * untested
 */
public class MultiInstanceJumpTaskCmd implements Command<Void> {

    protected String executionId;
    protected String parentId;
    protected ActivityImpl desActivity;
    protected Map<String, Object> paramvar;
    protected ActivityImpl currentActivity;
    protected String rejectReason;

    public MultiInstanceJumpTaskCmd(String executionId, String parentId,
                                    ActivityImpl desActivity, Map<String, Object> paramvar,
                                    ActivityImpl currentActivity, String rejectReason) {
        this.executionId = executionId;
        this.parentId = parentId;
        this.desActivity = desActivity;
        this.paramvar = paramvar;
        this.currentActivity = currentActivity;
        this.rejectReason = rejectReason;

    }

    public Void execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = Context.getCommandContext().getExecutionEntityManager();
        ExecutionEntity executionEntity = executionEntityManager.findExecutionById(executionId);
        String id = null;
        if (executionEntity.getParent() != null) {
            executionEntity = executionEntity.getParent();
            if (executionEntity.getParent() != null) {
                executionEntity = executionEntity.getParent();
                id = executionEntity.getId();
            }
            id = executionEntity.getId();
        }
        executionEntity.setVariables(paramvar);
        executionEntity.setExecutions(null);
        executionEntity.setEventSource(this.currentActivity);
        executionEntity.setActivity(this.currentActivity);
        // 根据executionId 获取Task
        Iterator<TaskEntity> localIterator = Context.getCommandContext()
                .getTaskEntityManager().findTasksByProcessInstanceId(id).iterator();
        while (localIterator.hasNext()) {
            TaskEntity taskEntity = localIterator.next();
            System.err.println("==================" + taskEntity.getId());
            // 触发任务监听
            taskEntity.fireEvent(TaskListener.EVENTNAME_COMPLETE);
            // 删除任务的原因
            Context.getCommandContext().getTaskEntityManager()
                    .deleteTask(taskEntity, rejectReason, false);
        }
        List<ExecutionEntity> list = executionEntityManager.findChildExecutionsByParentExecutionId(parentId);
        for (ExecutionEntity entity : list) {
            ExecutionEntity findExecutionById = executionEntityManager.findExecutionById(entity.getId());
            List<ExecutionEntity> parent = executionEntityManager.findChildExecutionsByParentExecutionId(entity.getId());
            for (ExecutionEntity executionEntity3 : parent) {
                executionEntity3.remove();
                System.err.println(executionEntity3.getId() + "----------------->>>>>>>>>>");
                Context.getCommandContext().getHistoryManager().recordActivityEnd(executionEntity3);
            }
            entity.remove();
            Context.getCommandContext().getHistoryManager().recordActivityEnd(entity);
            System.err.println(findExecutionById + "----------------->>>>>>>>>>");
        }
        commandContext.getIdentityLinkEntityManager().deleteIdentityLinksByProcInstance(id);
        executionEntity.executeActivity(this.desActivity);
        return null;
    }

}
