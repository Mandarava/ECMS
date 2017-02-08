
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_source
-- ----------------------------
DROP TABLE IF EXISTS `data_source`;
CREATE TABLE `data_source` (
  `ID` int(11) NOT NULL COMMENT '流水号',
  `NAME` varchar(255) DEFAULT NULL COMMENT '数据源名称',
  `STATE` varchar(255) DEFAULT NULL,
  `SOURCE_TYPE` varchar(255) DEFAULT NULL,
  `DRIVER_CLASS_NAME` varchar(255) DEFAULT NULL COMMENT '驱动名称',
  `URL` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `USERNAME` varchar(255) DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(255) DEFAULT NULL COMMENT '密码',
  `MAX_ACTIVE` int(255) DEFAULT NULL,
  `INITIAL_SIZE` int(255) DEFAULT NULL,
  `MAX_WAIT` int(255) DEFAULT NULL,
  `MIN_IDLE` int(255) DEFAULT NULL,
  `TIME_BETWEEN_EVICTION_RUN_MILLS` int(255) DEFAULT NULL,
  `MIN_EVICTABLE_IDLE_TIME_MILLS` int(255) DEFAULT NULL,
  `TEST_ON_BORROW` varchar(255) DEFAULT NULL,
  `TEST_ON_RETURN` varchar(255) DEFAULT NULL,
  `TEST_WHILE_IDLE` varchar(255) DEFAULT NULL,
  `VALIDATION_QUERY` varchar(255) DEFAULT NULL,
  `POOL_PREPARED_STATEMENTS` varchar(255) DEFAULT NULL,
  `MAX_OPEN_PREPARED_STATEMENTS` int(255) DEFAULT NULL,
  `LOG_ABANDONED` varchar(255) DEFAULT NULL,
  `REMOVE_ABANDONED` varchar(255) DEFAULT NULL,
  `REMOVE_ABANDONED_TIMEOUT` int(11) DEFAULT NULL,
  `FILTERS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of data_source
-- ----------------------------
INSERT INTO `data_source` VALUES ('1', 'masterDataSource', '1', '0', 'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost/mmch?useUnicode=true&amp;characterEncoding=UTF-8&allowMultiQueries=true', 'root', 'root', '30', '5', '60000', '5', '60000', '300000', 'false', 'false', 'true', 'SELECT \'x\'', 'false', '100', 'true', 'true', '1800', 'stat,log4j');
INSERT INTO `data_source` VALUES ('2', 'keen', '1', '0', 'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost/mmch2?useUnicode=true&amp;characterEncoding=UTF-8&allowMultiQueries=true', 'root', 'root', '30', '5', '60000', '5', '60000', '300000', 'false', 'false', 'true', 'SELECT \'x\'', 'false', '100', 'true', 'true', '1800', 'stat,log4j');
INSERT INTO `data_source` VALUES ('3', 'slaveDataSource', '0', '0', 'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost/mmch?useUnicode=true&amp;characterEncoding=UTF-8&allowMultiQueries=true', 'root', 'root', '30', '5', '60000', '5', '60000', '300000', 'false', 'false', 'true', 'SELECT \'x\'', 'false', '100', 'true', 'true', '1800', 'stat,log4j');
