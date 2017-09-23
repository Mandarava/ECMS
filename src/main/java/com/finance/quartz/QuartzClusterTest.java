package com.finance.quartz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by zt 2017/9/23 16:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-mybatis.xml"})
@WebAppConfiguration
public class QuartzClusterTest {

    @Test
    public void test() throws InterruptedException {
        Thread.sleep(1000000);
    }

}
