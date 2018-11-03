package com.finance.activiti.listener;

import com.finance.activiti.service.ActivitiProcessService;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.el.FixedValue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("candidateUserListener")
@Scope(value = "prototype")
public class CandidateUserListener implements TaskListener {

    private static final long serialVersionUID = 449113379350548440L;
    private static final Logger logger = LoggerFactory.getLogger(CandidateUserListener.class);
    @Autowired
    private GroupUser groupUserCache;
    @Autowired
    private ActivitiProcessService activitiProcessService;

    private FixedValue roleName;

    @Override
    public void notify(DelegateTask delegateTask) {
        // 如已经有分配assignee则跳过，退回到的节点的处理人需要前一个节点分配的场合；
        if (StringUtils.isNotEmpty(delegateTask.getAssignee())) {
            return;
        }
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        List<HistoricActivityInstance> activitiList =
                activitiProcessService.findHistoricActivitiByInstanceId(delegateTask.getProcessInstanceId());
        String assignee = null;
        if (CollectionUtils.isNotEmpty(activitiList)) {
            // 如有退回，查找最后一次该节点的处理人
            for (int i = activitiList.size() - 1; i >= 0; i--) {
                HistoricActivityInstance historicActivityInstance = activitiList.get(i);
                String activityId = historicActivityInstance.getActivityId();
                if (taskDefinitionKey.equals(activityId)) {
                    assignee = historicActivityInstance.getAssignee();
                    break;
                }
            }
        }
        if (StringUtils.isNotEmpty(assignee)) {
            // 判断该用户当前是否还存在。
            User user = activitiProcessService.findUserByUserId(assignee);
            if (user != null) {
                delegateTask.setAssignee(assignee);
                // 分配用户结束，返回；
                return;
            }
        }
        User user = groupUserCache.getUserByRoleName(roleName.getExpressionText());
        if (user == null || StringUtils.isEmpty(user.getId())) {
            logger.error("查找不到" + roleName.getExpressionText() + "该角色的用户");
            throw new RuntimeException("查找不到" + roleName.getExpressionText() + "该角色的用户");
        }
        // delegateTask.addCandidateUser(user.getId());
        delegateTask.setAssignee(user.getId());
    }

}
