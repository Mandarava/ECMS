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
public class ParallelJumpTaskCmd implements Command<Void> {

    protected String executionId;
    protected String processInstanceId;
    protected ActivityImpl desActivity;
    protected ActivityImpl currentActivity;
    protected Map<String, Object> paramvar;
    protected String rejectReason;

    public ParallelJumpTaskCmd(String executionId, String processInstanceId,
                               ActivityImpl desActivity, Map<String, Object> paramvar,
                               ActivityImpl currentActivity, String rejectReason) {
        this.executionId = executionId;
        this.desActivity = desActivity;
        this.paramvar = paramvar;
        this.currentActivity = currentActivity;
        this.processInstanceId = processInstanceId;
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
            } else {
                id = executionEntity.getId();
            }
        }
        executionEntity.setVariables(paramvar);
        executionEntity.setEventSource(this.currentActivity);
        executionEntity.setActivity(this.currentActivity);
        // 根据executionId 获取Task
        Iterator<TaskEntity> localIterator = Context.getCommandContext().getTaskEntityManager()
                .findTasksByExecutionId(this.executionId).iterator();
        while (localIterator.hasNext()) {
            TaskEntity taskEntity = localIterator.next();
            // 触发任务监听
            taskEntity.fireEvent(TaskListener.EVENTNAME_COMPLETE);
            // 删除任务的原因
            Context.getCommandContext().getTaskEntityManager().deleteTask(taskEntity, rejectReason, false);
        }
        List<ExecutionEntity> list = executionEntityManager.findChildExecutionsByParentExecutionId(processInstanceId);
        for (ExecutionEntity entity : list) {
            entity.remove();
        }
        executionEntity.executeActivity(this.desActivity);
        return null;
    }

}
