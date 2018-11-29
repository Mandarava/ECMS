package com.finance.activiti.service;

import com.finance.activiti.entity.CustomExtensionElements;
import com.finance.activiti.entity.ResourceAuthority;
import com.finance.activiti.entity.ResourceProcessDefinition;
import com.finance.activiti.entity.TodoTask;
import com.github.pagehelper.Page;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ActivitiProcessService {

    /**
     * 部署流程图
     *
     * @param processDefinitionKey 流程定义key
     */
    String deployDiagram(String processDefinitionKey);

    /**
     * 删除部署的流程图
     *
     * @param deploymentId 流程图部署ID
     * @param cascade      是否级联删除图下其他数据
     */
    void deleteDeployment(String deploymentId, boolean cascade);

    /**
     * 启动工作流
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务key
     * @param userId               流程发起人用户ID
     * @return 流程实例ID
     */
    String startProcessInstanceByKey(String processDefinitionKey, String businessKey, String userId);

    /**
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务key
     * @param userId               流程发起人用户ID
     * @param variables            流程变量
     * @return
     */
    String startProcessInstanceByKey(String processDefinitionKey, String businessKey
            , String userId, Map<String, Object> variables);

    /**
     * 查找待办任务
     *  @param processDefinitionKey 流程定义key
     * @param userId               用户ID
     */
    List<TodoTask> findTodoTasks(String processDefinitionKey, String userId);

    /**
     * 查找待办任务
     *  @param processDefinitionKey 流程定义key
     * @param userId               用户ID
     */
    Page<TodoTask> findTodoTasks(String processDefinitionKey, String userId, int pageIndex, int pageSize);

    /**
     * 查找历史批注信息
     *
     * @param processInstanceId 流程实例ID
     * @return 历史批注列表
     */
    List<Comment> findHistoryCommentsByInstanceId(String processInstanceId);

    /**
     * 查找历史批注信息
     *
     * @param taskId 任务ID
     * @return 历史批注列表
     */
    List<Comment> findHistoryCommentsByTaskId(String taskId);

    /**
     * 认领任务
     *
     * @param taskId 任务ID
     * @param userId 认领人ID
     */
    void claimTask(String taskId, String userId);

    /**
     * 完成任务
     *
     * @param taskId    任务ID
     * @param comment   批注
     * @param userId    当前操作用户ID
     * @param variables 流程变量
     */
    void completeTask(String taskId, String comment, String userId, Map<String, Object> variables);

    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void suspendProcessInstanceByInstanceId(String processInstanceId);

    /**
     * 根据taskId查询流程实例id
     *
     * @param taskId 任务ID
     * @return
     */
    String findProcessInstanceIdByTaskId(String taskId);

    /**
     * 激活流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void activateProcessInstanceByInstanceId(String processInstanceId);

    /**
     * 删除完成的流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void deleteFinishedProcessInstance(String processInstanceId);

    /**
     * 删除运行中的流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason      删除原因
     */
    void deleteRuntimeProcessInstance(String processInstanceId, String deleteReason);

    /**
     * 查看当前节点扩展属性
     *
     * @param processInstanceId 流程实例ID
     * @return 自定义扩展属性
     */
    CustomExtensionElements findTaskExtendProperties(String processInstanceId);

    /**
     * 驳回任务
     *
     * @param processInstanceId 流程实例ID
     * @param taskDefinitionKey 节点ID
     * @param rejectMessage     驳回原因
     * @param userId            驳回人用户ID
     * <p>
     *    <extensionElements>
     *        ...
     *       <activiti:operations>
     *           <activiti:turnback backTo="usertask3"></activiti:turnback>
     *       </activiti:operations>
     *       ...
     *    </extensionElements>
     * </p>
     */
    void rejectTask(String processInstanceId, String taskDefinitionKey, String rejectMessage, String userId);

    /**
     * 获取历史流程图
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    InputStream getHistoryGraph(String processInstanceId);

    /**
     * 根据角色名称查询所有用户
     *
     * @param groupName 角色名称
     * @return
     */
    List<User> findGroupUsersByGroupName(String groupName);

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return
     */
    User findUserByUserId(String userId);

    /**
     * 包含一个活动(流程上的节点)的执行信息(某一次流程的执行经历的多少步)
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    List<HistoricActivityInstance> findHistoricActivitiByInstanceId(String processInstanceId);

    /**
     * 包含一个活动(流程上的节点)的执行信息(某一次流程的执行经历的多少步)，分页
     *
     * @param processInstanceId 流程实例ID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<HistoricActivityInstance> findHistoricActivitiByInstanceId(String processInstanceId, int pageIndex, int pageSize);

    /**
     * 某一次流程的执行经历的任务总数
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    long findHistoricTaskByInstanceIdCount(String processInstanceId);

    /**
     * 某一次流程的执行经历的任务
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    List<HistoricTaskInstance> findHistoricTaskByInstanceId(String processInstanceId);

    /**
     * 某一次流程的执行经历的任务，分页
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    List<HistoricTaskInstance> findHistoricTaskByInstanceId(String processInstanceId, int pageIndex, int pageSize);

    /**
     * 根据businessKey查询流程实例ID
     *
     * @param businessKey
     * @param processDefinitionKey
     * @return
     */
    String findProcessInstanceIdByBusinessKey(String businessKey, String processDefinitionKey);

    /**
     * 根据流程实例ID查找TASK
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    Task findTaskByProcessInstanceId(String processInstanceId);

    /**
     * 更新act_hi_taskinst表DELETE_REASON_
     *
     * @param taskId       任务ID
     * @param deleteReason 原因
     */
    void updateHiTaskReason(String taskId, String deleteReason);

    /**
     * 转办任务，任务转他人处理
     *
     * @param taskId   任务id
     * @param owner    转办人用户id
     * @param assignee 被转办人用户id
     */
    void assigneeTask(String taskId, String owner, String assignee);

    /**
     * 委派、代办任务
     * 将任务节点分给其他人处理，等其他人处理好之后，委派任务会自动回到委派人的任务中
     *
     * @param taskId 任务ID
     * @param userId 被委派人ID
     */
    void delegateTask(String taskId, String userId);

    /**
     * 被委派人办理任务
     *
     * @param taskId    任务ID
     * @param variables 流程变量
     */
    void resolveTask(String taskId, Map<String, Object> variables);

    /**
     * 查询该用户委派给别人正在运行的任务
     *
     * @param owner 用户ID
     * @return
     */
    List<Task> findDelegateTasks(String owner);

    /**
     * 查询该用户委派给别人正在运行的任务
     *
     * @param owner 用户ID
     * @return
     */
    List<Task> findDelegateTasks(String owner, int pageIndex, int pageSize);

    /**
     * 查询该用户委派给别人已经完成的任务
     *
     * @param owner 用户ID
     * @return
     */
    List<HistoricTaskInstance> findHistoricDelegateTasks(String owner);

    /**
     * 查询该用户委派给别人已经完成的任务
     *
     * @param owner 用户ID
     * @return
     */
    List<HistoricTaskInstance> findHistoricDelegateTasks(String owner, int pageIndex, int pageSize);

    /**
     * 查询所有部署的流程图下的节点的资源权限
     * <p>
     *     bpmx20.xml sample:
     *     <activiti:resourceAuthorityList>
     *         <activiti:resourceAuthority id="reporterAddressId" status="1"/>
     *     </activiti:resourceAuthorityList>
     * </p>
     */
    List<ResourceProcessDefinition> findAllResourceAuthorities();

    /**
     * 查找当前任务节点的资源权限
     *
     * @param taskId
     * @return
     */
    List<ResourceAuthority> resourceAuthorities(String taskId);

    /**
     * 设置流程定义的候选启动人和候选启动组
     */
    void setStartables(String processDefinitionId, String[] userArray, String[] groupArray);

    /**
     * 读取流程定义的候选属性
     */
    Map<String, List<? extends Object>> getProcessCandidateUserAndGroups(String processDefinitionId);

}
