DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job` (
  `JOB_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `JOB_NAME` varchar(200) NOT NULL COMMENT '任务名称',
  `JOB_GROUP` varchar(200) NOT NULL COMMENT '任务分组',
  `CRON_EXPRESSION` varchar(120) NOT NULL COMMENT 'cron表达式',
  `DESCRIPTION` varchar(200) DEFAULT NULL COMMENT '任务描述',
  `TARGET_OBJECT` varchar(200) NOT NULL COMMENT '执行类',
  `TARGET_METHOD` varchar(200) NOT NULL COMMENT '执行方法',
  `IS_RECOVERY` tinyint(1) NOT NULL COMMENT '恢复',
  `IS_DURABLE` tinyint(1) NOT NULL COMMENT '持久',
  `IS_CONCURRENT` tinyint(1) NOT NULL,
  `IS_CLUSTER` tinyint(1) NOT NULL COMMENT '集群',
  `MISFIRE_INSTRUCTION` smallint(2) DEFAULT NULL,
  `ARGUMENTS` varchar(250) DEFAULT NULL,
  `JOB_LISTENER_NAMES` varchar(250) DEFAULT NULL,
  `CREATE_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `IS_DELETED` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`JOB_ID`),
  UNIQUE KEY `UK_GROUP_NAME` (`JOB_GROUP`，`JOB_NAME`) USING BTREE
) ENGINE=InnoDB;

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES ('1', 'Cluster_Hello_World_DB', 'DEFAULT', '* 0/30 * * * ?', 'cluster job test', 'helloQuartzService', 'helloWorld', '0', '1', '0', '1', '0', '', '', '2017-10-01 11:11:24', '2017-10-01 22:39:20', '0');
