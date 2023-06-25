
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pub_serial_number
-- ----------------------------
DROP TABLE IF EXISTS `pub_serial_number`;
CREATE TABLE `pub_serial_number`  (
  `serial_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `serial_prefix` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_ymd` bit(1) NOT NULL,
  `digit_with` int(11) NOT NULL,
  `next_seq` int(11) NOT NULL,
  `buffer_step` int(11) NOT NULL,
  `update_time` datetime NOT NULL,
  `c_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `u_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`serial_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pub_serial_number
-- ----------------------------
INSERT INTO `pub_serial_number` VALUES ('order', 'SO', b'1', 4, 2000, 50, '2022-06-21 21:21:29', '2022-06-21 20:18:16', '2022-06-21 21:21:29');

SET FOREIGN_KEY_CHECKS = 1;




create definer = `root`@`%` PROCEDURE `usp_get_serial_number`(
  IN in_serial_type   VARCHAR(32),
  IN in_serial_count  INT,
  OUT out_next_seq     INT,
  OUT out_update_time  DATETIME
)
begin

SET autocommit =0;
START TRANSACTION;

set out_update_time = NOW();
UPDATE
    pub_serial_number
SET
next_seq =
(
  case when is_ymd = 1 and datediff(out_update_time,update_time)!=0 then 1+in_serial_count else next_seq+in_serial_count
  end
),update_time = out_update_time
where serial_type = in_serial_type;

select (next_seq - in_serial_count) into out_next_seq from pub_serial_number where serial_type = in_serial_type;
commit;
end