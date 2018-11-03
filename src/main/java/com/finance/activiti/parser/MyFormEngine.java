package com.finance.activiti.parser;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.scripting.ScriptingEngines;

import java.io.UnsupportedEncodingException;

/**
 * 自定义表单引擎
 */
public class MyFormEngine implements FormEngine {

    private static final String NOT_FOUND_FORM_CONTENT_FLAG = "____NOT_FOUND_FORM___";

    public MyFormEngine() {
        System.out.print("TestFormEngine init...............");
    }

    public String getName() {
        return "juel";
    }

    public Object renderStartForm(StartFormData startForm) {
        if (startForm.getFormKey() == null) {
            return null;
        } else {
            String formTemplateString = this.getFormTemplateString(startForm, startForm.getFormKey());
            if (NOT_FOUND_FORM_CONTENT_FLAG.equals(formTemplateString)) {
                return startForm.getFormKey();
            }
            ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
            return scriptingEngines.evaluate(formTemplateString, "juel", null);
        }
    }

    public Object renderTaskForm(TaskFormData taskForm) {
        if (taskForm.getFormKey() == null) {
            return null;
        } else {
            String formTemplateString = this.getFormTemplateString(taskForm, taskForm.getFormKey());
            if (NOT_FOUND_FORM_CONTENT_FLAG.equals(formTemplateString)) {
                return taskForm.getFormKey();
            }
            ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
            TaskEntity task = (TaskEntity) taskForm.getTask();
            return scriptingEngines.evaluate(formTemplateString, "juel", task.getExecution());
        }
    }

    protected String getFormTemplateString(FormData formInstance, String formKey) {
        String deploymentId = formInstance.getDeploymentId();
        ResourceEntity resourceStream = Context.getCommandContext().getResourceEntityManager().findResourceByDeploymentIdAndResourceName(deploymentId, formKey);
        if (resourceStream == null) {
            // throw new ActivitiObjectNotFoundException("Form with formKey \'" + formKey + "\' does not exist", String.class);
            return NOT_FOUND_FORM_CONTENT_FLAG;
        } else {
            byte[] resourceBytes = resourceStream.getBytes();
            String encoding = "UTF-8";
            String formTemplateString;
            try {
                formTemplateString = new String(resourceBytes, encoding);
                return formTemplateString;
            } catch (UnsupportedEncodingException exception) {
                throw new ActivitiException("Unsupported encoding of :" + encoding, exception);
            }
        }
    }
}
