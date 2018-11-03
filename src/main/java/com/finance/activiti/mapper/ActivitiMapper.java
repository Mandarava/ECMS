package com.finance.activiti.mapper;

import org.apache.ibatis.annotations.Delete;

public interface ActivitiMapper {

    @Delete(
            "delete from act_hi_varinst where PROC_INST_ID_= #{processInstanceId}" +
                    " and (TASK_ID_ = '' or TASK_ID_ is null) and NAME_ != 'applyUserId' "
    )
    int deleteVariables(String processInstanceId);

}
