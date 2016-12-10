package com.finance.util.myutil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * property读取工具
 *
 * @author s1507112
 */
public final class PropertyManager {

    /**
     * properties
     */
    private static Properties properties;
    /**
     * 声明一个PropertyManager类的引用
     */
    private static PropertyManager content = null;
    /**
     * 输入流
     */
    private InputStreamReader input;

    /**
     * 构造方法
     */
    private PropertyManager(String fileName) {
        properties = new Properties();
        try {
            input = new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName),
                    "UTF-8");
            properties.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实例化引用
     *
     * @return 该类的实例化对象
     */
    public static PropertyManager getInstance(String fileName) {
        if (content == null) {
            content = new PropertyManager(fileName);
        }
        return content;
    }

    /**
     * 得到key的值
     *
     * @param key 取得其值的键
     * @return key的值，如果不存在返回null
     */
    public String getContent(String key) {

        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        } else
            return null;
    }
}
