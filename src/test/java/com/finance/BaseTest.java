package com.finance;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {

    private String springXmlPath;

    private ClassPathXmlApplicationContext context;

    public BaseTest() {

    }

    public BaseTest(String springXmlPath) {
        this.springXmlPath = springXmlPath;
    }

    @Before
    public void setUp() throws Exception {
        if (StringUtils.isEmpty(springXmlPath)) {
            springXmlPath = "classpath:spring-*.xml";
        }
        try {
            context = new ClassPathXmlApplicationContext(springXmlPath.split("[,\\s]+]"));
            context.start();
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        context.destroy();
    }

    protected <T extends Object> T getBean(String beanId) {
        return (T) context.getBean(beanId);
    }

    protected <T extends Object> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
