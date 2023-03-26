-- 本地消息表 --
CREATE TABLE `pub_mq_message` (
  `mq_message_id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(32) NOT NULL,
  `mid` varchar(64) DEFAULT NULL,
  `msg_content` varchar(8192) DEFAULT NULL,
  `src_project` varchar(16) NOT NULL,
  `dest_project` varchar(16) NOT NULL,
  `create_time` datetime NOT NULL,
  `send_time` datetime DEFAULT NULL,
  `ack_time` datetime DEFAULT NULL,
  `send_status` varchar(16) NOT NULL,
  `deal_count` int(11) NOT NULL,
  `deal_status` varchar(16) NOT NULL,
  PRIMARY KEY (`mq_message_id`),
  KEY `ix_uq_uuid` (`uuid`),
  KEY `ix_create_time` (`create_time`),
  KEY `ix_deal_status` (`deal_status`),
  KEY `ix_src_project_dest_project_create_time` (`src_project`,`dest_project`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=349249 DEFAULT CHARSET=utf8mb4;