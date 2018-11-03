package com.finance.activiti.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.finance.activiti.entity.CustomExtensionElements;

import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ExtensionUserTaskParseHandler extends UserTaskParseHandler {

    public static final String EXTEND_PROPERTY_KEY = "customProperties";
    private static final Logger logger = LoggerFactory.getLogger(ExtensionUserTaskParseHandler.class);
    private Class<? extends CustomExtensionElements> extensionElementsClass = CustomExtensionElements.class;

    @Override
    protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
        super.executeParse(bpmnParse, userTask);
        ActivityImpl activity = bpmnParse.getCurrentScope().findActivity(userTask.getId());
        this.parseCustomUserTaskOperation(userTask, activity);
    }

    /**
     * 解析自定义的用户任务扩展，放置到ActivityImpl的Property中
     */
    private void parseCustomUserTaskOperation(UserTask userTask, ActivityImpl activity) {
        JSONObject jsonObject = new JSONObject();
        JSONObject attributes = getAttributes(userTask.getAttributes());
        if (attributes != null) {
            jsonObject.putAll(attributes);
        }
        Map<String, List<ExtensionElement>> extensionElements = userTask.getExtensionElements();
        if (extensionElements != null && !extensionElements.isEmpty()) {
            for (Map.Entry<String, List<ExtensionElement>> entry : extensionElements.entrySet()) {
                jsonObject.put(entry.getKey(), this.processEachExtensionKey(entry.getValue(), null));
            }
        }
        if (jsonObject.size() > 0) {
            logger.debug("activiti " + activity.getId() + " find extension elements:" + jsonObject.toJSONString());
            CustomExtensionElements customExtensionElements = jsonObject.toJavaObject(extensionElementsClass);
            activity.setProperty(EXTEND_PROPERTY_KEY, customExtensionElements);
        }
    }

    private Object processEachExtensionKey(List<ExtensionElement> extensionElements, String parentAttributeName) {
        if (CollectionUtils.isEmpty(extensionElements)) {
            return null;
        }
        if (isListType(parentAttributeName)) {
            parentAttributeName = parentAttributeName + "[]";
        }
        // 1.resourceAuthorityList
        // 2.resourceAuthorityList.resourceAuthority
        // 3.resourceAuthorityList.resourceAuthority[].readable
        String name = parentAttributeName == null
                ? extensionElements.get(0).getName() : parentAttributeName + "." + extensionElements.get(0).getName();
        if (!isListType(name)) {
            return this.processElement(extensionElements.get(0), name);
        } else {
            JSONArray jsonArray = new JSONArray();
            for (ExtensionElement element : extensionElements) {
                jsonArray.add(this.processElement(element, name));
            }
            return jsonArray;
        }
    }

    private JSONObject processElement(ExtensionElement element, String parentAttributeName) {
        JSONObject jsonObject = new JSONObject();
        JSONObject attributes = getAttributes(element.getAttributes());
        if (attributes != null) {
            jsonObject.putAll(attributes);
        }
        Map<String, List<ExtensionElement>> childElements = element.getChildElements();
        for (Map.Entry<String, List<ExtensionElement>> childElementEntry : childElements.entrySet()) {
            List<ExtensionElement> childExtensionElementList = childElementEntry.getValue();
            jsonObject.put(childElementEntry.getKey(), processEachExtensionKey(childExtensionElementList, parentAttributeName));
        }
        return jsonObject;
    }

    private JSONObject getAttributes(Map<String, List<ExtensionAttribute>> attributesMap) {
        if (attributesMap == null || attributesMap.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        for (List<ExtensionAttribute> attributes : attributesMap.values()) {
            for (ExtensionAttribute attribute : attributes) {
                jsonObject.put(attribute.getName(), attribute.getValue());
            }
        }
        return jsonObject;
    }

    private boolean isListType(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        MetaClass metaConfig = MetaClass.forClass(extensionElementsClass, new DefaultReflectorFactory());
        if (metaConfig.hasGetter(name)) {
            Class<?> getterType = metaConfig.getGetterType(name);
            return List.class.equals(getterType);
        }
        return false;
    }

    public void setExtensionElementsClass(Class<? extends CustomExtensionElements> extensionElementsClass) {
        this.extensionElementsClass = extensionElementsClass;
    }
}

