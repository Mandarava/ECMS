package com.finance.activemq;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * Created by zt on 2017/1/23.
 */
@Component("consumerExceptionListener")
public class ConsumerExceptionListener implements ExceptionListener {

    private static final Logger logger = Logger.getLogger(ConsumerExceptionListener.class);

    @Override
    public void onException(JMSException exception) {
        logger.debug(exception);
        // TODO sth.
    }
}
