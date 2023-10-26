-- 本地消息表 --
CREATE TABLE IF NOT EXISTS `pub_mq_message` (
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
                                  KEY `ix_src_project_dest_project_create_time` (`src_project`,`dest_project`,`create_time`),
                                  FULLTEXT KEY `ix_msg_content` (`msg_content`)
) ENGINE=InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET=utf8mb4;
-- end --

-- 同步回调 --
CREATE TABLE IF NOT EXISTS `pub_callback` (
                                `callback_id` int(11) NOT NULL AUTO_INCREMENT,
                                `uuid` char(32) NOT NULL,
                                `sync_uuid` char(32) NOT NULL,
                                `src_project` varchar(16) NOT NULL,
                                `dest_project` varchar(16) NOT NULL,
                                `db_name` varchar(32) NOT NULL,
                                `msg_content` varchar(4096) NOT NULL,
                                `create_time` datetime NOT NULL,
                                `send_time` datetime DEFAULT NULL,
                                `ack_time` datetime DEFAULT NULL,
                                `repeat_count` int(11) NOT NULL,
                                `status` varchar(16) NOT NULL,
                                `remark` varchar(128) DEFAULT NULL,
                                PRIMARY KEY (`callback_id`),
                                KEY `ix_create_time` (`create_time`),
                                KEY `ix_send_time` (`send_time`),
                                KEY `ix_status` (`status`),
                                KEY `ix_uq_uuid` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
-- end --


