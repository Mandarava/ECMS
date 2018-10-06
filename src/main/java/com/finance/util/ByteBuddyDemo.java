package com.finance.util;

import com.google.gson.Gson;

import com.alibaba.fastjson.annotation.JSONField;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zt 2018/6/24 14:17
 */
public class ByteBuddyDemo {

    private static Map<String, Class> typeMap = new HashMap<>();

    static {
        typeMap.put("int", int.class);
        typeMap.put("long", long.class);
        typeMap.put("byte", byte.class);
        typeMap.put("short", short.class);
        typeMap.put("double", double.class);
        typeMap.put("float", float.class);
        typeMap.put("char", char.class);
        typeMap.put("boolean", boolean.class);
        typeMap.put("Integer", Integer.class);
        typeMap.put("Long", Long.class);
        typeMap.put("Byte", Byte.class);
        typeMap.put("Short", Short.class);
        typeMap.put("Double", Double.class);
        typeMap.put("Float", Float.class);
        typeMap.put("Character", Character.class);
        typeMap.put("Boolean", Boolean.class);
        typeMap.put("String", String.class);
        typeMap.put("Date", Date.class);
        typeMap.put("List", List.class);
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        ByteBuddyDemo byteBuddyDemo = new ByteBuddyDemo();
        byteBuddyDemo.test();
    }

    private void test() throws IllegalAccessException, InstantiationException {
        Class<?> dynamicClass = new ByteBuddy()
                .subclass(Object.class)
                .name("com.entity.Patient$subPatient")
//                .defineField("name",String.class,Modifier.PRIVATE)
                .implement(Serializable.class)
                .defineProperty("name", String.class)
                .annotateField(AnnotationDescription.Builder.ofType(JSONField.class)
                        .define("name", "patientName").build())
                .defineProperty("patientId", String.class)
                .annotateField(AnnotationDescription.Builder.ofType(JSONField.class)
                        .define("name", "pid").build())
                .defineProperty("ids", TypeDescription.Generic.Builder.parameterizedType(List.class, String.class).build())
                .annotateField(AnnotationDescription.Builder.ofType(JSONField.class)
                        .define("name", "jzs").build())
                .make()
                .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                //                .saveIn(new File("C:\\Users\\Administrator\\Desktop"));
                .getLoaded();
        Object o = dynamicClass.newInstance();
        System.out.println(new Gson().toJson(o));
    }

}
