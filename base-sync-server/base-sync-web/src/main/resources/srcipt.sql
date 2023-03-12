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
  PRIMARY KEY (`uuid`),
  KEY `ix_msg_id` (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 同步回调 --
CREATE TABLE `pub_callback` (
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
) ENGINE=InnoDB AUTO_INCREMENT=90531 DEFAULT CHARSET=utf8mb4;

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


use demo;
-- end --
select count(1) as demo_user_count from demo_user;
-- end --

use ecp_market;
-- end --
select count(1) as  market_user_count from market_user;
-- end --

use ecp_order;
-- end --
select count(1) as order_user_count from order_user;
-- end --

use ecp_pim;
-- end --
select count(1) as pim_user_count from pim_user;
-- end --

use erp_mms;
-- end --
select count(1) as mms_user_count from mms_user;
-- end --

use erp_pms;
-- end --
select count(1) as pms_user_count from pms_user;
-- end --

use erp_wms;
-- end --
select count(1) as wms_user_count from wms_user;
-- end --
