package com.finance.quartz.quartz22;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

/**
 * Created by zt 2017/9/23 15:35
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class StatefulQuartzJobBean extends CustomQuartzJobBean {

}
