package com.finance.activiti.service.impl;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流跟踪相关Service
 */
@Component
public class ActivitiTraceProcessService {

    private static Map<String, String> ACTIVITY_TYPE = new HashMap<>();

    static {
        ACTIVITY_TYPE.put("userTask", "用户任务");
        ACTIVITY_TYPE.put("serviceTask", "系统任务");
        ACTIVITY_TYPE.put("startEvent", "开始节点");
        ACTIVITY_TYPE.put("endEvent", "结束节点");
        ACTIVITY_TYPE.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
        ACTIVITY_TYPE.put("inclusiveGateway", "并行处理任务");
        ACTIVITY_TYPE.put("callActivity", "调用活动");
        ACTIVITY_TYPE.put("subProcess", "子流程");
    }

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 根据英文获取中文类型
     */
    public static String getZhActivityType(String type) {
        return ACTIVITY_TYPE.get(type) == null ? type : ACTIVITY_TYPE.get(type);
    }

    public InputStream readResource(String executionId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        List<String> highLightedFlows = getHighLightedFlows(processDefinition, processInstance.getId());
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows);
    }

    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinition, String processInstanceId) {
        List<String> highLightedFlows = new ArrayList<>();
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc().list();
        List<String> historicActivityInstanceList = new ArrayList<>();
        for (HistoricActivityInstance hai : historicActivityInstances) {
            historicActivityInstanceList.add(hai.getActivityId());
        }
        // add current activities to list
        List<String> highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
        historicActivityInstanceList.addAll(highLightedActivities);
        // activities and their sequence-flows
        for (ActivityImpl activity : processDefinition.getActivities()) {
            int index = historicActivityInstanceList.indexOf(activity.getId());
            if (index >= 0 && index + 1 < historicActivityInstanceList.size()) {
                List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
                for (PvmTransition pvmTransition : pvmTransitionList) {
                    String destinationFlowId = pvmTransition.getDestination().getId();
                    if (destinationFlowId.equals(historicActivityInstanceList.get(index + 1))) {
                        highLightedFlows.add(pvmTransition.getId());
                    }
                }
            }
        }
        return highLightedFlows;
    }

    public Map<String, Object> historyDatas(String executionId) {
        Map<String, Object> historyDatas = new HashMap<>();
        // 查询Execution对象
        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        // 查询所有的历史活动记录
        String processInstanceId = execution.getProcessInstanceId();
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        // 查询历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        // 查询流程有关的变量
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
        List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).formProperties().list();
        // 查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
        // 查询运行时流程实例
        ProcessInstance parentProcessInstance = runtimeService.createProcessInstanceQuery()
                .subProcessInstanceId(execution.getProcessInstanceId()).singleResult();
        historyDatas.put("parentProcessInstance", parentProcessInstance);
        historyDatas.put("historicProcessInstance", historicProcessInstance);
        historyDatas.put("variableInstances", variableInstances);
        historyDatas.put("activities", activityInstances);
        historyDatas.put("formProperties", formProperties);
        historyDatas.put("processDefinition", processDefinition);
        historyDatas.put("executionId", executionId);
        return historyDatas;
    }

    /**
     * 读取跟踪数据
     *
     * @return 封装了各种节点信息
     */
    public List<Map<String, Object>> traceProcess(String executionId) {
        // 获取跟踪图信息
        Execution executionEntity = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(executionId).singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();
        List<Map<String, Object>> activityInfos = new ArrayList<>();
        for (ActivityImpl activity : activitiList) {
            ActivityBehavior activityBehavior = activity.getActivityBehavior();
            boolean currentActiviti = false;
            // 当前节点
            String activityId = activity.getId();
            if (activeActivityIds.contains(activityId)) {
                currentActiviti = true;
            }
            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, executionEntity.getId(), currentActiviti);
            activityInfos.add(activityImageInfo);
            // 处理子流程
            if (activityBehavior instanceof SubProcessActivityBehavior) {
                List<ActivityImpl> innerActivityList = activity.getActivities();
                for (ActivityImpl innerActivity : innerActivityList) {
                    String innerActivityId = innerActivity.getId();
                    currentActiviti = activeActivityIds.contains(innerActivityId);
                    activityImageInfo = packageSingleActivitiInfo(innerActivity, executionEntity.getId(), currentActiviti);
                    activityInfos.add(activityImageInfo);
                }
            }
        }
        return activityInfos;
    }

    /**
     * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
     */
    private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, String executionId,
                                                          boolean currentActiviti) {
        Map<String, Object> activityInfo = new HashMap<>();
        activityInfo.put("currentActiviti", currentActiviti);
        // 设置图形的XY坐标以及宽度、高度
        setSizeAndPositonInfo(activity, activityInfo);
        Map<String, Object> properties = activity.getProperties();
        Map<String, Object> vars = new HashMap<>();
        vars.put("任务类型", getZhActivityType(properties.get("type").toString()));
        vars.put("任务名称", properties.get("name"));
        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        if (activityBehavior instanceof UserTaskActivityBehavior) {
            Task currentTask;
            // 当前节点的task
            if (currentActiviti) {
                currentTask = getCurrentTaskInfo(executionId, activity.getId());
                setCurrentTaskAssignee(vars, currentTask);
            }
        }
        activityInfo.put("vars", vars);
        return activityInfo;
    }

    /**
     * 设置当前处理人信息
     */
    private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
        if (currentTask == null) {
            return;
        }
        String assignee = currentTask.getAssignee();
        if (assignee != null) {
            User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
            String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName() + "/" + assigneeUser.getId();
            vars.put("当前处理人", userInfo);
            vars.put("创建时间", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTask.getCreateTime()));
        } else {
            vars.put("任务状态", "未签收");
        }
    }

    /**
     * 获取当前节点信息
     */
    private Task getCurrentTaskInfo(String executionId, String activitiId) {
        return taskService.createTaskQuery()
                .processInstanceId(executionId)
                .taskDefinitionKey(activitiId)
                .singleResult();
    }

    /**
     * 设置宽度、高度、坐标属性
     */
    private void setSizeAndPositonInfo(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("width", activity.getWidth());
        activityInfo.put("height", activity.getHeight());
        activityInfo.put("x", activity.getX());
        activityInfo.put("y", activity.getY());
    }

}
