CREATE TABLE `pub_queue` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `business_data` varchar(1024) DEFAULT NULL,
  `queue_name` varchar(255) NOT NULL,
  `is_consumed` bit(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `ix_queue_name` (`queue_name`,`is_consumed`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 查看事件错误日志
SHOW VARIABLES LIKE 'log_error';

-- 检查事件调度器的状态：
SHOW VARIABLES LIKE 'event_scheduler';

-- 如果它是禁用的（OFF），则将其设置为启用状态（ON）
SET GLOBAL event_scheduler = IF(@@event_scheduler = 'OFF', 'ON', @@event_scheduler);

-- 删除冗余数据 （保留近7天的数据）
DELIMITER //

CREATE EVENT delete_old_consumed_data
    ON SCHEDULE EVERY 1 DAY
    DO
    BEGIN
        DELETE FROM pub_queue WHERE is_consumed = 1 AND create_time <= DATE_SUB(NOW(), INTERVAL 7 DAY);
    END;

//
DELIMITER ;