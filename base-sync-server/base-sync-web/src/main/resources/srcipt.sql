-- 同步计划 --
CREATE TABLE `pub_sync_plan` (
  `uuid` varchar(32) NOT NULL,
  `plan_code` varchar(32) NOT NULL,
  `src_db` varchar(16) NOT NULL,
  `dest_db` varchar(16) NOT NULL,
  `src_project` varchar(32) NOT NULL,
  `dest_project` varchar(32) NOT NULL,
  `plan_content` text NOT NULL,
  `status` varchar(16) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 同步结果 --
CREATE TABLE `pub_sync_result` (
  `uuid` varchar(32) NOT NULL,
  `plan_uuid` varchar(32) DEFAULT NULL,
  `plan_code` varchar(64) DEFAULT NULL,
  `plan_name` varchar(32) DEFAULT NULL,
  `msg_src` varchar(16) DEFAULT NULL,
  `mq_id` varchar(64) DEFAULT NULL,
  `msg_id` varchar(64) DEFAULT NULL,
  `msg_content` text,
  `src_project` varchar(255) DEFAULT NULL,
  `dest_project` varchar(255) DEFAULT NULL,
  `sync_content` text,
  `msg_create_time` datetime DEFAULT NULL,
  `sync_time` datetime DEFAULT NULL,
  `status` varchar(16) DEFAULT NULL,
  `error_msg` varchar(4096) DEFAULT NULL,
  `repeat_count` int(11) DEFAULT NULL,
  `remark` varchar(16) DEFAULT NULL,
  `sync_to_es` bit(1) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 同步回调 --
CREATE TABLE `pub_callback` (
  `callback_id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(32) NOT NULL,
  `sync_uuid` char(32) NOT NULL,
  `src_project` varchar(16) NOT NULL,
  `dest_project` varchar(16) NOT NULL,
  `db_name` varchar(32) NOT NULL,
  `msg_content` varchar(4096) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `send_time` datetime DEFAULT NULL,
  `ack_time` datetime DEFAULT NULL,
  `repeat_count` int(11) NOT NULL,
  `status` varchar(16) NOT NULL,
  `remark` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`callback_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;