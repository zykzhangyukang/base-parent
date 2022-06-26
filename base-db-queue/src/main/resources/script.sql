CREATE TABLE `pub_queue` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `business_data` varchar(1024) DEFAULT NULL,
  `queue_name` varchar(255) NOT NULL,
  `is_consumed` bit(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `ix_queue_name` (`queue_name`,`is_consumed`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;