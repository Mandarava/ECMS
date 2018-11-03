package com.finance.activiti.service.impl;

import com.finance.activiti.cmd.CommonJumpTaskCmd;
import com.finance.activiti.cmd.UpdateHiTaskReasonCommand;
import com.finance.activiti.entity.CustomExtensionElements;
import com.finance.activiti.entity.ResourceAuthority;
import com.finance.activiti.entity.ResourceProcessDefinition;
import com.finance.activiti.entity.ResourceTaskDefinition;
import com.finance.activiti.entity.TodoTask;
import com.finance.activiti.parser.ExtensionUserTaskParseHandler;
import com.finance.activiti.service.ActivitiProcessService;
import com.github.pagehelper.Page;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;


@Service
public class ActivitiProcessServiceImpl implements ActivitiProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiProcessServiceImpl.class);
    private static final String APPLY_USERID = "applyUserId";
    private static final String ASSIGNEE = BpmnXMLConstants.ATTRIBUTE_TASK_USER_ASSIGNEE;
    private static final String CANDIDATE_USERS = BpmnXMLConstants.ATTRIBUTE_TASK_USER_CANDIDATEUSERS;
    private static final String CANDIDATE_GROUPS = BpmnXMLConstants.ATTRIBUTE_TASK_USER_CANDIDATEGROUPS;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private RuntimeService runtimeService;

    @Override
    @Transactional
    public String deployDiagram(String processDefinitionKey) {
        if (StringUtils.isEmpty(processDefinitionKey)) {
            throw new RuntimeException("没有processDefinitionKey");
        }
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        String classpathResourceUrl = "classpath:/deployments/" + processDefinitionKey + ".bar";
        Resource resource = resourceLoader.getResource(classpathResourceUrl);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("读取流程图失败。");
        }
        if (inputStream == null) {
            logger.warn("ignore deploy workflow module: {}", classpathResourceUrl);
        } else {
            logger.debug("finded workflow module: {}, deploy it!", classpathResourceUrl);
            ZipInputStream zis = new ZipInputStream(inputStream);
            Deployment deployment = repositoryService.createDeployment()
                    .name(processDefinitionKey)
                    .enableDuplicateFiltering()
                    .addZipInputStream(zis)
                    .deploy();
            return deployment.getId();
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteDeployment(String deploymentId, boolean cascade) {
        repositoryService.deleteDeployment(deploymentId, cascade);
    }

    @Override
    @Transactional
    public String startProcessInstanceByKey(String processDefinitionKey, String businessKey, String userId) {
        return this.startProcessInstanceByKey(processDefinitionKey, businessKey, userId, null);
    }

    @Override
    @Transactional
    public String startProcessInstanceByKey(String processDefinitionKey, String businessKey, String userId, Map<String, Object> variables) {
        // business key 不能重复
        this.businessKeyValidate(processDefinitionKey, businessKey);
        Map<String, Object> map = new HashMap<>();
        map.put(ASSIGNEE, null);
        map.put(CANDIDATE_USERS, null);
        map.put(CANDIDATE_GROUPS, null);
        if (variables != null) {
            map.putAll(variables);
        }
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(userId);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, map);
            return processInstance.getId();
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    /**
     * 检查该businessKey是否已经使用或者已经使用过了
     */
    private void businessKeyValidate(String processDefinitionKey, String businessKey) {
        if (StringUtils.isEmpty(processDefinitionKey) || StringUtils.isEmpty(businessKey)) {
            return;
        }
        // 查询正在使用中的案件
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .processInstanceBusinessKey(businessKey)
                .list();
        if (CollectionUtils.isNotEmpty(processInstanceList)) {
            throw new RuntimeException("businessKey " + businessKey + " 正在使用中");
        }
        // 查询使用过的案件
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().
                processDefinitionKey(processDefinitionKey).
                processInstanceBusinessKey(businessKey)
                .list();
        if (CollectionUtils.isNotEmpty(historicProcessInstanceList)) {
            throw new RuntimeException("businessKey " + businessKey + " 已经使用完成");
        }
    }

    @Override
    public List<TodoTask> findTodoTasks(String processDefinitionKey, String userId) {
        return this.findTodoTasks(processDefinitionKey, userId, 1, Integer.MAX_VALUE).getResult();
    }

    @Override
    public Page<TodoTask> findTodoTasks(String processDefinitionKey, String userId, int pageIndex, int pageSize) {
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("userId is null");
        }
        if (StringUtils.isEmpty(processDefinitionKey)) {
            throw new RuntimeException("processDefinitionKey is null");
        }
        List<TodoTask> datas = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).taskCandidateOrAssigned(userId);
        List<Task> tasks = taskQuery.orderByTaskCreateTime().asc().listPage(((pageIndex - 1) * pageSize), pageSize);
        for (Task task : tasks) {
            TodoTask todoTask = new TodoTask();
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            todoTask.setBusinessKey(processInstance.getBusinessKey());
            todoTask.setProcessDefinitionId(processInstance.getProcessDefinitionId());
            todoTask.setProcessInstanceId(processInstanceId);
            todoTask.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
            todoTask.setProcessDefinitionName(processInstance.getProcessDefinitionName());
            todoTask.setCreateTime(task.getCreateTime());
            todoTask.setTenantId(task.getTenantId());
            todoTask.setApplyUserId((String) taskService.getVariable(task.getId(), APPLY_USERID));
            todoTask.setTaskId(task.getId());
            todoTask.setTaskName(task.getName());
            todoTask.setTaskAssignee(task.getAssignee());
            todoTask.setTaskVariables(taskService.getVariables(task.getId()));
            todoTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
            datas.add(todoTask);
        }
        Page<TodoTask> page = new Page<>();
        page.setPageSize(pageSize);
        page.setPageNum(pageIndex);
        page.setTotal(taskQuery.count());
        page.addAll(datas);
        return page;
    }

    @Override
    public List<Comment> findHistoryCommentsByInstanceId(String processInstanceId) {
        List<Comment> commentList = taskService.getProcessInstanceComments(processInstanceId);
        if (CollectionUtils.isNotEmpty(commentList)) {
            Collections.reverse(commentList);
        }
        return commentList;
    }

    @Override
    public List<Comment> findHistoryCommentsByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return null;
        }
        return this.findHistoryCommentsByInstanceId(task.getProcessInstanceId());
    }

    @Override
    @Transactional
    public void completeTask(String taskId, String comment, String userId, Map<String, Object> variables) {
        Map<String, Object> map = new HashMap<>();
        map.put(ASSIGNEE, null);
        map.put(CANDIDATE_USERS, null);
        map.put(CANDIDATE_GROUPS, null);
        if (variables != null) {
            map.putAll(variables);
        }
        try {
            if (StringUtils.isNotEmpty(comment)) {
                Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
                String processInstancesId = task.getProcessInstanceId();
                Authentication.setAuthenticatedUserId(userId);
                taskService.addComment(taskId, processInstancesId, comment);
            }
        } finally {
            Authentication.setAuthenticatedUserId(null);
        }
        taskService.complete(taskId, map);
    }

    @Override
    public String findProcessInstanceIdByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task.getProcessInstanceId();
    }

    @Override
    @Transactional
    public void suspendProcessInstanceByInstanceId(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance.isSuspended()) {
            return;
        }
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    @Override
    @Transactional
    public void activateProcessInstanceByInstanceId(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (!processInstance.isSuspended()) {
            return;
        }
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    @Transactional
    public void deleteFinishedProcessInstance(String processInstanceId) {
        historyService.deleteHistoricProcessInstance(processInstanceId);
    }

    @Override
    @Transactional
    public void deleteRuntimeProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    @Override
    @Transactional
    public void claimTask(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

    @Override
    public CustomExtensionElements findTaskExtendProperties(String processInstanceId) {
        Task taskEntity = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(taskEntity.getProcessDefinitionId());
        List<ActivityImpl> activityList = definitionEntity.getActivities();
        ActivityImpl currActivity = findActivityImpl(activityList, taskEntity.getTaskDefinitionKey());
        if (currActivity != null) {
            return (CustomExtensionElements) currActivity.getProperty(ExtensionUserTaskParseHandler.EXTEND_PROPERTY_KEY);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void rejectTask(String processInstanceId, String taskDefinitionKey, String rejectMessage, String userId) {
        // 获得当前任务的对应实列
        Task taskEntity = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        // 保存驳回意见
        taskEntity.setDescription(rejectMessage);
        taskService.saveTask(taskEntity);
        try {
            if (StringUtils.isNotEmpty(rejectMessage)) {
                Authentication.setAuthenticatedUserId(userId);
                taskService.addComment(taskEntity.getId(), processInstanceId, rejectMessage);
            }
        } finally {
            Authentication.setAuthenticatedUserId(null);
        }
        // 获得当前流程的定义模型
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(taskEntity.getProcessDefinitionId());
        // 获得当前流程定义模型的所有任务节点
        List<ActivityImpl> activityList = definitionEntity.getActivities();
        // 当前活动节点
        ActivityImpl currActivity = findActivityImpl(activityList, taskEntity.getTaskDefinitionKey());
        if (currActivity == null) {
            throw new RuntimeException("工作流找不到当前活动节点。");
        }
        // 驳回目标节点
        ActivityImpl destActivity;
        if (StringUtils.isEmpty(taskDefinitionKey)) {
            CustomExtensionElements customExtensionElements
                    = (CustomExtensionElements) currActivity.getProperty(ExtensionUserTaskParseHandler.EXTEND_PROPERTY_KEY);
            destActivity = findActivityImpl(activityList, customExtensionElements.getOperations().getTurnback().getBackTo());
        } else {
            destActivity = findActivityImpl(activityList, taskDefinitionKey);
        }
        if (destActivity == null) {
            throw new RuntimeException("工作流找不到目标跳转节点。");
        }
        Map<String, Object> variables = new HashMap<>();
        // 退回的时候没有调用complete方法，以下全局变量没有清空会导致脏数据，此处退回时也进行清空操作；
        variables.put(ASSIGNEE, null);
        variables.put(CANDIDATE_USERS, null);
        variables.put(CANDIDATE_GROUPS, null);
        managementService.executeCommand(new CommonJumpTaskCmd(processInstanceId, currActivity, destActivity, variables, rejectMessage));
    }

    @Override
    public InputStream getHistoryGraph(String processInstanceId) {
        // Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(processInstanceId);
        // return processEngine.getProcessEngineConfiguration().getManagementService().executeCommand(cmd);
        // 获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        // 高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<>();
        // 高亮线路id集合
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
        List<HistoricActivityInstance> highLightedActivitiList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitiList);
        for (HistoricActivityInstance tempActivity : highLightedActivitiList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        // 单独返回流程图，不高亮显示
        // InputStream imageStream = diagramGenerator.generatePngDiagram(bpmnModel);
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows, "宋体", "宋体", null, 1.0);
    }

    @Override
    public List<User> findGroupUsersByGroupName(String groupName) {
        List<User> userList = new ArrayList<>();
        if (StringUtils.isEmpty(groupName)) {
            return userList;
        }
        List<Group> groupList = identityService.createGroupQuery().groupName(groupName).list();
        if (CollectionUtils.isNotEmpty(groupList)) {
            for (Group group : groupList) {
                List<User> list = identityService.createUserQuery().memberOfGroup(group.getId()).list();
                if (CollectionUtils.isNotEmpty(list)) {
                    userList.addAll(list);
                }
            }
        }
        return userList;
    }

    @Override
    public User findUserByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return identityService.createUserQuery().userId(userId).singleResult();
    }

    @Override
    public List<HistoricActivityInstance> findHistoricActivitiByInstanceId(String processInstanceId) {
        return this.findHistoricActivitiByInstanceId(processInstanceId, 1, Integer.MAX_VALUE);
    }

    @Override
    public List<HistoricActivityInstance> findHistoricActivitiByInstanceId(String processInstanceId, int pageIndex, int pageSize) {
        return historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .listPage((pageIndex - 1) * pageSize, pageSize);
    }

    @Override
    public List<HistoricTaskInstance> findHistoricTaskByInstanceId(String processInstanceId) {
        return this.findHistoricTaskByInstanceId(processInstanceId, 1, Integer.MAX_VALUE);
    }

    @Override
    public long findHistoricTaskByInstanceIdCount(String processInstanceId) {
        return historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .count();
    }

    @Override
    public List<HistoricTaskInstance> findHistoricTaskByInstanceId(String processInstanceId, int pageIndex, int pageSize) {
        return historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .asc()
                .listPage((pageIndex - 1) * pageSize, pageSize);
    }

    @Override
    public String findProcessInstanceIdByBusinessKey(String businessKey, String processDefinitionKey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (processInstance != null) {
            return processInstance.getProcessInstanceId();
        }
        return null;
    }

    @Override
    public Task findTaskByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    @Transactional
    public void updateHiTaskReason(String taskId, String deleteReason) {
        managementService.executeCommand(new UpdateHiTaskReasonCommand(taskId, deleteReason));
    }

    @Override
    @Transactional
    public void assigneeTask(String taskId, String owner, String assignee) {
        taskService.setOwner(taskId, owner);
        taskService.setAssignee(taskId, assignee);
    }

    @Override
    @Transactional
    public void delegateTask(String taskId, String userId) {
        taskService.delegateTask(taskId, userId);
    }

    @Override
    @Transactional
    public void resolveTask(String taskId, Map<String, Object> variables) {
        taskService.resolveTask(taskId, variables);
    }

    @Override
    public List<Task> findDelegateTasks(String owner) {
        return this.findDelegateTasks(owner, 1, Integer.MAX_VALUE);
    }

    @Override
    public List<Task> findDelegateTasks(String owner, int pageIndex, int pageSize) {
        return taskService.createTaskQuery()
                .taskOwner(owner)
                .orderByTaskCreateTime()
                .desc()
                .listPage((pageIndex - 1) * pageSize, pageSize);
    }

    @Override
    public List<HistoricTaskInstance> findHistoricDelegateTasks(String owner) {
        return this.findHistoricDelegateTasks(owner, 1, Integer.MAX_VALUE);
    }

    @Override
    public List<HistoricTaskInstance> findHistoricDelegateTasks(String owner, int pageIndex, int pageSize) {
        return historyService.createHistoricTaskInstanceQuery()
                .taskOwner(owner)
                .orderByTaskCreateTime()
                .desc()
                .listPage((pageIndex - 1) * pageSize, pageSize);
    }

    @Override
    public List<ResourceProcessDefinition> findAllResourceAuthorities() {
        List<ResourceProcessDefinition> resourceProcessDefinitionList = new ArrayList<>();
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        if (CollectionUtils.isNotEmpty(processDefinitionList)) {
            for (ProcessDefinition processDefinition : processDefinitionList) {
                List<ResourceTaskDefinition> taskDefinitionList = new ArrayList<>();
                ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(processDefinition.getId());
                List<ActivityImpl> activityList = definitionEntity.getActivities();
                if (CollectionUtils.isNotEmpty(activityList)) {
                    for (ActivityImpl activity : activityList) {
                        if ("userTask".equals(activity.getProperty("type"))) {
                            ResourceTaskDefinition resourceTaskDefinition = new ResourceTaskDefinition();
                            resourceTaskDefinition.setTaskDefinitionId(activity.getId());
                            CustomExtensionElements customExtensionElements = (CustomExtensionElements) activity
                                    .getProperty(ExtensionUserTaskParseHandler.EXTEND_PROPERTY_KEY);
                            if (customExtensionElements != null && customExtensionElements.getResourceAuthorityList() != null) {
                                resourceTaskDefinition.setResources(customExtensionElements.getResourceAuthorityList().getResourceAuthority());
                            } else {
                                resourceTaskDefinition.setResources(new ArrayList<>());
                            }
                            taskDefinitionList.add(resourceTaskDefinition);
                        }
                    }
                }
                ResourceProcessDefinition resourceProcessDefinition = new ResourceProcessDefinition();
                resourceProcessDefinition.setProcessDefinitionId(processDefinition.getId());
                resourceProcessDefinition.setTaskDefinitions(taskDefinitionList);
                resourceProcessDefinitionList.add(resourceProcessDefinition);
            }
        }
        return resourceProcessDefinitionList;
    }

    @Override
    public List<ResourceAuthority> resourceAuthorities(String taskId) {
        Task taskEntity = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(taskEntity.getProcessDefinitionId());
        List<ActivityImpl> activityList = definitionEntity.getActivities();
        ActivityImpl currActivity = findActivityImpl(activityList, taskEntity.getTaskDefinitionKey());
        if (currActivity != null) {
            CustomExtensionElements customExtensionElements = (CustomExtensionElements) currActivity
                    .getProperty(ExtensionUserTaskParseHandler.EXTEND_PROPERTY_KEY);
            if (customExtensionElements != null && customExtensionElements.getResourceAuthorityList() != null) {
                return customExtensionElements.getResourceAuthorityList().getResourceAuthority();
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    private ActivityImpl findActivityImpl(List<ActivityImpl> activityList, String taskDefinitionKey) {
        if (CollectionUtils.isNotEmpty(activityList)) {
            for (ActivityImpl activityImpl : activityList) {
                if (taskDefinitionKey.equals(activityImpl.getId())) {
                    return activityImpl;
                }
            }
        }
        return null;
    }

    /**
     * 获取需要高亮的线
     */
    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity
            , List<HistoricActivityInstance> historicActivityInstances) {
        // 用以保存高亮的线flowId
        List<String> highFlows = new ArrayList<>();
        // 对历史流程节点进行遍历
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 得到节点定义的详细信息
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());
            // 用以保存后需开始时间相同的节点
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();
            ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                // 后续第一个节点
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);
                // 后续第二个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);
                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }
}