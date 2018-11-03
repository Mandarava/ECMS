package com.finance.activiti.listener;

import com.finance.activiti.mapper.ActivitiMapper;

import org.activiti.engine.ManagementService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.cmd.CustomSqlExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkflowProcessEndListener {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowProcessEndListener.class);

    @Autowired
    private ManagementService managementService;

    public void onEvent(ActivitiEvent event) {
        // do nothing.
    }

    /**
     * 清理无用的全局变量
     */
    private void deleteVariables(String processInstanceId) {
        CustomSqlExecution<ActivitiMapper, Integer> customSqlExecution =
                new AbstractCustomSqlExecution<ActivitiMapper, Integer>(ActivitiMapper.class) {
                    @Override
                    public Integer execute(ActivitiMapper activitiMapper) {
                        return activitiMapper.deleteVariables(processInstanceId);
                    }
                };
        Integer count = managementService.executeCustomSql(customSqlExecution);
        logger.debug("清理了 {} 条历史变量数据", count);
    }
}
    

