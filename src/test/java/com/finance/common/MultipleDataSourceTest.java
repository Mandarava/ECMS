package com.finance.common;

import com.finance.dao.MySqlMapper;
import com.finance.dao.MySqlMapper2;
import com.finance.model.pojo.Fund;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by zt on 2016/10/5.
 */
public class MultipleDataSourceTest {

    public static void main(String[] args) {
        //初始化ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-mybatis.xml");

        MySqlMapper mySqlMapper = applicationContext.getBean(MySqlMapper.class);

        MySqlMapper2 mySqlMapper2 = applicationContext.getBean(MySqlMapper2.class);

        //设置数据源为MySql,使用了AOP测试时请将下面这行注释
        DynamicDataSourceContextHolder.setCustomerType("master");
        List<Fund> result = mySqlMapper.getList();
        result.parallelStream().forEach(x -> System.out.println(x));
        //设置数据源为mySqlMapper2,使用AOP测试时请将下面这行注释
        DynamicDataSourceContextHolder.setCustomerType("slave");
        mySqlMapper2.getList();
    }
}
