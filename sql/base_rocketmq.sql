CREATE TABLE `pub_mq_msg` (
  `uuid` varchar(32) NOT NULL,
  `mid` varchar(64) DEFAULT NULL,
  `tag` varchar(32) DEFAULT NULL,
  `msg` varchar(2048) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `send_status` varchar(16) NOT NULL,
  `retry` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `ix_tag` (`tag`),
  KEY `ix_status_retry_ctime` (`send_status`,`retry`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;