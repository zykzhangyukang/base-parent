/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : base_auth

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 11/09/2022 17:46:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept`  (
  `dept_id` int(11) NOT NULL COMMENT '部门id',
  `dept_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门编号',
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_dept
-- ----------------------------
INSERT INTO `tb_dept` VALUES (1, 'yfb', '研发部', '2022-03-12 11:51:12', '2022-03-12 11:51:13');
INSERT INTO `tb_dept` VALUES (2, 'csb', '测试部', '2022-03-12 11:51:49', '2022-03-12 11:51:51');
INSERT INTO `tb_dept` VALUES (3, 'khb1', '客户一部', '2022-03-12 16:29:08', '2022-03-12 16:29:10');
INSERT INTO `tb_dept` VALUES (4, 'khb2', '客户二部', '2022-03-12 16:29:27', '2022-03-12 16:29:29');
INSERT INTO `tb_dept` VALUES (5, 'yyb', '运营部', '2022-03-12 16:29:44', '2022-03-12 16:29:45');
INSERT INTO `tb_dept` VALUES (6, 'jyb', '交易部', '2022-03-12 16:30:04', '2022-03-12 16:30:06');
INSERT INTO `tb_dept` VALUES (7, 'yuwb', '运维部', '2022-03-20 15:42:39', '2022-03-20 15:42:40');

-- ----------------------------
-- Table structure for tb_func
-- ----------------------------
DROP TABLE IF EXISTS `tb_func`;
CREATE TABLE `tb_func`  (
  `func_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `func_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能名称',
  `func_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能key',
  `func_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能类型(目录/功能)',
  `func_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录图标',
  `func_sort` int(11) NULL DEFAULT NULL COMMENT '功能排序',
  `dir_hide` bit(1) NULL DEFAULT NULL COMMENT '是否隐藏',
  `parent_id` int(11) NOT NULL COMMENT '父级功能id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`func_id`) USING BTREE,
  UNIQUE INDEX `idx_func_key`(`func_key`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_func
-- ----------------------------
INSERT INTO `tb_func` VALUES (1, '粒创商城', 'root', 'dir', 'setting', 1, b'0', 0, '2022-03-19 15:56:27', '2022-03-19 15:56:29');
INSERT INTO `tb_func` VALUES (2, '权限系统', 'auth', 'dir', 'setting', 1, b'0', 1, '2022-03-19 15:57:28', '2022-03-19 15:57:31');
INSERT INTO `tb_func` VALUES (3, '用户管理', 'user-management', 'dir', 'user', 3, b'0', 2, '2022-03-19 16:10:26', '2022-03-19 16:10:28');
INSERT INTO `tb_func` VALUES (4, '角色管理', 'role-management', 'dir', 'lock', 4, b'0', 2, '2022-03-19 16:11:29', '2022-03-19 16:11:30');
INSERT INTO `tb_func` VALUES (5, '功能管理', 'func-management', 'dir', 'profile', 5, b'0', 2, '2022-03-19 16:11:41', '2022-03-19 16:11:42');
INSERT INTO `tb_func` VALUES (6, '资源管理', 'res-management', 'dir', 'apartment', 6, b'0', 2, '2022-03-19 16:11:54', '2022-03-19 16:11:55');
INSERT INTO `tb_func` VALUES (8, '角色删除', 'roleDelete', 'func', 'setting', 7, NULL, 4, '2022-03-19 18:23:22', '2022-03-19 18:23:25');
INSERT INTO `tb_func` VALUES (9, '角色更新', 'roleUpdate', 'func', 'setting', 8, NULL, 4, '2022-03-19 18:23:38', '2022-03-19 18:23:40');
INSERT INTO `tb_func` VALUES (11, '角色查询', 'rolePage', 'func', 'setting', 9, NULL, 4, '2022-03-19 18:24:02', '2022-03-19 18:24:03');
INSERT INTO `tb_func` VALUES (12, '功能查询', 'funcPage', 'func', 'setting', 10, NULL, 5, '2022-03-19 18:24:30', '2022-03-19 18:24:32');
INSERT INTO `tb_func` VALUES (13, '资源查询', 'rescPage', 'func', 'setting', 11, NULL, 6, '2022-03-19 18:24:51', '2022-03-19 18:24:52');
INSERT INTO `tb_func` VALUES (14, '功能新增', 'funcSave', 'func', 'setting', 12, NULL, 5, '2022-03-19 18:26:06', '2022-03-19 18:26:08');
INSERT INTO `tb_func` VALUES (26, '资源新增', 'rescSave', 'func', 'setting', 13, NULL, 6, '2022-03-20 21:43:55', '2022-03-20 21:43:55');
INSERT INTO `tb_func` VALUES (27, '资源删除', 'rescDelete', 'func', 'setting', 14, NULL, 6, '2022-03-20 21:44:03', '2022-03-20 21:44:03');
INSERT INTO `tb_func` VALUES (54, '功能删除', 'funcDelete', 'func', 'setting', 15, NULL, 5, '2022-03-26 20:57:44', '2022-03-26 20:57:44');
INSERT INTO `tb_func` VALUES (55, '功能更新', 'funcUpdate', 'func', 'setting', 16, NULL, 5, '2022-03-26 20:58:00', '2022-03-26 20:58:00');
INSERT INTO `tb_func` VALUES (56, '资源更新', 'rescUpdate', 'func', 'setting', 17, NULL, 6, '2022-03-26 21:00:16', '2022-03-26 21:00:16');
INSERT INTO `tb_func` VALUES (57, '用户查询', 'userPage', 'func', 'setting', 18, NULL, 3, '2022-03-26 21:04:37', '2022-03-26 21:04:37');
INSERT INTO `tb_func` VALUES (58, '用户启用', 'userEnable', 'func', 'setting', 19, NULL, 3, '2022-03-26 21:05:02', '2022-03-26 21:05:02');
INSERT INTO `tb_func` VALUES (59, '用户禁用', 'userDisable', 'func', 'setting', 20, NULL, 3, '2022-03-26 21:05:19', '2022-03-26 21:05:19');
INSERT INTO `tb_func` VALUES (60, '用户更新', 'userUpdate', 'func', 'setting', 21, NULL, 3, '2022-03-26 21:05:33', '2022-03-26 21:05:33');
INSERT INTO `tb_func` VALUES (61, '用户删除', 'userDelete', 'func', 'setting', 22, NULL, 3, '2022-03-26 21:05:55', '2022-03-26 21:05:55');
INSERT INTO `tb_func` VALUES (62, '分配角色', 'userAssign', 'func', 'setting', 23, NULL, 3, '2022-03-26 21:06:48', '2022-03-26 21:06:48');
INSERT INTO `tb_func` VALUES (63, '分配用户', 'roleAssign', 'func', 'setting', 24, NULL, 4, '2022-03-26 21:08:39', '2022-03-26 21:08:39');
INSERT INTO `tb_func` VALUES (71, '授权功能', 'roleAuth', 'func', 'setting', 25, NULL, 4, '2022-04-04 17:45:36', '2022-04-04 17:45:36');
INSERT INTO `tb_func` VALUES (72, '角色新增', 'roleSave', 'func', 'setting', 26, NULL, 4, '2022-04-16 11:13:19', '2022-04-16 11:13:19');
INSERT INTO `tb_func` VALUES (73, '解绑用户', 'funcUserUnbind', 'func', 'setting', 27, NULL, 5, '2022-04-16 21:55:19', '2022-04-16 21:55:19');
INSERT INTO `tb_func` VALUES (74, '解绑资源', 'funcRescUnbind', 'func', 'setting', 28, NULL, 5, '2022-04-16 22:02:49', '2022-04-16 22:02:49');
INSERT INTO `tb_func` VALUES (75, '用户新增', 'userSave', 'func', 'setting', 29, NULL, 3, '2022-05-02 11:40:57', '2022-05-02 11:40:57');
INSERT INTO `tb_func` VALUES (84, '修改密码', 'userUpdatePwd', 'func', 'setting', 30, NULL, 3, '2022-05-14 12:20:12', '2022-05-14 12:20:12');

-- ----------------------------
-- Table structure for tb_func_resc
-- ----------------------------
DROP TABLE IF EXISTS `tb_func_resc`;
CREATE TABLE `tb_func_resc`  (
  `func_resc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `func_id` int(11) NOT NULL COMMENT '功能id',
  `resc_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`func_resc_id`) USING BTREE,
  INDEX `idx_func_id`(`func_id`) USING BTREE,
  INDEX `idx_resc_id`(`resc_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 342 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_func_resc
-- ----------------------------
INSERT INTO `tb_func_resc` VALUES (286, 75, 27);
INSERT INTO `tb_func_resc` VALUES (287, 62, 9);
INSERT INTO `tb_func_resc` VALUES (288, 62, 10);
INSERT INTO `tb_func_resc` VALUES (289, 61, 3);
INSERT INTO `tb_func_resc` VALUES (290, 60, 4);
INSERT INTO `tb_func_resc` VALUES (291, 60, 2);
INSERT INTO `tb_func_resc` VALUES (292, 60, 11);
INSERT INTO `tb_func_resc` VALUES (293, 59, 14);
INSERT INTO `tb_func_resc` VALUES (297, 58, 12);
INSERT INTO `tb_func_resc` VALUES (301, 56, 23);
INSERT INTO `tb_func_resc` VALUES (302, 56, 22);
INSERT INTO `tb_func_resc` VALUES (303, 56, 41);
INSERT INTO `tb_func_resc` VALUES (304, 55, 30);
INSERT INTO `tb_func_resc` VALUES (305, 55, 29);
INSERT INTO `tb_func_resc` VALUES (306, 54, 31);
INSERT INTO `tb_func_resc` VALUES (307, 27, 24);
INSERT INTO `tb_func_resc` VALUES (308, 26, 21);
INSERT INTO `tb_func_resc` VALUES (309, 14, 32);
INSERT INTO `tb_func_resc` VALUES (312, 73, 39);
INSERT INTO `tb_func_resc` VALUES (313, 72, 19);
INSERT INTO `tb_func_resc` VALUES (316, 63, 35);
INSERT INTO `tb_func_resc` VALUES (317, 63, 36);
INSERT INTO `tb_func_resc` VALUES (318, 63, 13);
INSERT INTO `tb_func_resc` VALUES (319, 8, 15);
INSERT INTO `tb_func_resc` VALUES (320, 9, 16);
INSERT INTO `tb_func_resc` VALUES (321, 9, 17);
INSERT INTO `tb_func_resc` VALUES (322, 11, 18);
INSERT INTO `tb_func_resc` VALUES (323, 12, 33);
INSERT INTO `tb_func_resc` VALUES (324, 12, 34);
INSERT INTO `tb_func_resc` VALUES (327, 57, 1);
INSERT INTO `tb_func_resc` VALUES (328, 57, 11);
INSERT INTO `tb_func_resc` VALUES (329, 13, 20);
INSERT INTO `tb_func_resc` VALUES (330, 13, 41);
INSERT INTO `tb_func_resc` VALUES (331, 71, 38);
INSERT INTO `tb_func_resc` VALUES (332, 71, 37);
INSERT INTO `tb_func_resc` VALUES (333, 71, 43);
INSERT INTO `tb_func_resc` VALUES (335, 84, 42);
INSERT INTO `tb_func_resc` VALUES (336, 74, 40);

-- ----------------------------
-- Table structure for tb_resc
-- ----------------------------
DROP TABLE IF EXISTS `tb_resc`;
CREATE TABLE `tb_resc`  (
  `resc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `resc_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `resc_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源url',
  `resc_domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源所属系统',
  `method_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`resc_id`) USING BTREE,
  UNIQUE INDEX `idx_resc_url`(`resc_url`) USING BTREE,
  INDEX `idx_resc_name`(`resc_name`) USING BTREE,
  INDEX `idx_resc_domain`(`resc_domain`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_resc
-- ----------------------------
INSERT INTO `tb_resc` VALUES (1, '用户列表', '/auth/user/page', 'auth', 'get', '2022-03-19 09:31:31', '2022-04-09 14:03:50');
INSERT INTO `tb_resc` VALUES (2, '用户获取', '/auth/user/select', 'auth', 'get', '2022-03-19 09:34:24', '2022-05-10 22:41:35');
INSERT INTO `tb_resc` VALUES (3, '用户删除', '/auth/user/delete', 'auth', 'get', '2022-03-19 09:34:40', '2022-05-01 23:18:10');
INSERT INTO `tb_resc` VALUES (4, '用户更新', '/auth/user/update', 'auth', 'post', '2022-03-19 09:34:58', '2022-05-10 22:36:29');
INSERT INTO `tb_resc` VALUES (9, '用户分配角色初始化', '/auth/user/role/update/init', 'auth', 'get', '2022-03-19 10:29:15', '2022-08-30 23:07:46');
INSERT INTO `tb_resc` VALUES (10, '用户分配角色', '/auth/user/role/update', 'auth', 'post', '2022-03-19 10:29:41', '2022-09-04 14:33:38');
INSERT INTO `tb_resc` VALUES (11, '部门列表', '/auth/dept/list', 'auth', 'get', '2022-03-19 10:30:01', '2022-04-09 14:03:20');
INSERT INTO `tb_resc` VALUES (12, '用户启用', '/auth/user/update/enable', 'auth', 'get', '2022-03-19 10:30:16', '2022-09-03 15:45:31');
INSERT INTO `tb_resc` VALUES (13, '用户角色信息', '/auth/user/select/role/names', 'auth', 'get', '2022-03-19 10:30:34', '2022-04-09 14:03:07');
INSERT INTO `tb_resc` VALUES (14, '用户禁用', '/auth/user/update/disable', 'auth', 'get', '2022-03-19 10:30:48', '2022-05-14 11:52:30');
INSERT INTO `tb_resc` VALUES (15, '角色删除', '/auth/role/delete', 'auth', 'get', '2022-03-19 10:31:30', '2022-05-01 23:18:07');
INSERT INTO `tb_resc` VALUES (16, '角色更新', '/auth/role/update', 'auth', 'post', '2022-03-19 10:31:44', '2022-05-01 22:23:59');
INSERT INTO `tb_resc` VALUES (17, '角色获取', '/auth/role/select', 'auth', 'get', '2022-03-19 10:32:06', '2022-04-09 14:02:34');
INSERT INTO `tb_resc` VALUES (18, '角色列表', '/auth/role/page', 'auth', 'get', '2022-03-19 10:32:28', '2022-04-09 14:02:25');
INSERT INTO `tb_resc` VALUES (19, '角色新增', '/auth/role/save', 'auth', 'post', '2022-03-19 10:32:48', '2022-04-09 14:02:29');
INSERT INTO `tb_resc` VALUES (20, '资源列表', '/auth/resc/page', 'auth', 'get', '2022-03-19 10:33:08', '2022-09-04 14:44:14');
INSERT INTO `tb_resc` VALUES (21, '资源新增', '/auth/resc/save', 'auth', 'post', '2022-03-19 10:33:26', '2022-04-09 14:02:15');
INSERT INTO `tb_resc` VALUES (22, '资源获取', '/auth/resc/select', 'auth', 'get', '2022-03-19 10:33:42', '2022-04-09 14:02:06');
INSERT INTO `tb_resc` VALUES (23, '资源更新', '/auth/resc/update', 'auth', 'post', '2022-03-19 10:33:56', '2022-05-01 22:23:25');
INSERT INTO `tb_resc` VALUES (24, '资源删除', '/auth/resc/delete', 'auth', 'get', '2022-03-19 10:34:13', '2022-05-01 23:17:58');
INSERT INTO `tb_resc` VALUES (27, '用户新增', '/auth/user/save', 'auth', 'post', '2022-03-19 11:18:04', '2022-04-09 14:01:38');
INSERT INTO `tb_resc` VALUES (29, '功能更新', '/auth/func/update', 'auth', 'post', '2022-03-26 20:52:38', '2022-05-01 21:57:53');
INSERT INTO `tb_resc` VALUES (30, '功能获取', '/auth/func/select', 'auth', 'get', '2022-03-26 20:52:51', '2022-04-09 14:01:25');
INSERT INTO `tb_resc` VALUES (31, '功能删除', '/auth/func/delete', 'auth', 'get', '2022-03-26 20:53:09', '2022-05-01 23:17:54');
INSERT INTO `tb_resc` VALUES (32, '功能新增', '/auth/func/save', 'auth', 'post', '2022-03-26 20:53:24', '2022-04-09 14:01:12');
INSERT INTO `tb_resc` VALUES (33, '功能查询', '/auth/func/page', 'auth', 'get', '2022-03-26 20:53:40', '2022-09-11 16:05:51');
INSERT INTO `tb_resc` VALUES (34, '功能树获取', '/auth/func/list/tree', 'auth', 'get', '2022-03-26 20:53:58', '2022-04-09 14:01:03');
INSERT INTO `tb_resc` VALUES (35, '角色分配用户初始化', '/auth/role/user/update/init', 'auth', 'get', '2022-03-26 21:07:44', '2022-09-03 16:02:34');
INSERT INTO `tb_resc` VALUES (36, '角色分配用户', '/auth/role/user/update', 'auth', 'post', '2022-03-26 21:07:59', '2022-09-03 16:02:22');
INSERT INTO `tb_resc` VALUES (37, '角色授权功能初始化', '/auth/role/func/update/init', 'auth', 'get', '2022-04-04 17:46:28', '2022-09-03 16:02:00');
INSERT INTO `tb_resc` VALUES (38, '角色分配功能', '/auth/role/func/update', 'auth', 'post', '2022-04-04 17:46:57', '2022-08-30 23:00:27');
INSERT INTO `tb_resc` VALUES (39, '功能解绑用户', '/auth/func/delete/user/bind', 'auth', 'get', '2022-04-16 21:54:38', '2022-04-16 22:02:08');
INSERT INTO `tb_resc` VALUES (40, '功能解绑资源', '/auth/func/delete/resc/bind', 'auth', 'get', '2022-04-16 22:02:24', '2022-04-23 21:28:09');
INSERT INTO `tb_resc` VALUES (41, '资源搜索', '/auth/resc/search', 'auth', 'get', '2022-05-01 19:17:13', '2022-09-01 00:02:42');
INSERT INTO `tb_resc` VALUES (42, '用户修改密码', '/auth/user/update/password', 'auth', 'post', '2022-05-14 12:19:03', '2022-09-03 11:24:39');
INSERT INTO `tb_resc` VALUES (43, '角色授权功能预检查', '/auth/role/func/update/check', 'auth', 'post', '2022-05-21 15:34:56', '2022-09-11 17:40:11');

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `idx_role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (42, '管理员', '高权限', '2022-04-16 11:38:56', '2022-09-11 16:24:52');
INSERT INTO `tb_role` VALUES (43, '普通用户', '低权限', '2022-04-16 11:39:07', '2022-09-11 16:32:38');

-- ----------------------------
-- Table structure for tb_role_func
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_func`;
CREATE TABLE `tb_role_func`  (
  `role_func_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `func_id` int(11) NOT NULL COMMENT '功能id',
  PRIMARY KEY (`role_func_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_func_id`(`func_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6078 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_role_func
-- ----------------------------
INSERT INTO `tb_role_func` VALUES (6016, 42, 2);
INSERT INTO `tb_role_func` VALUES (6017, 42, 5);
INSERT INTO `tb_role_func` VALUES (6018, 42, 54);
INSERT INTO `tb_role_func` VALUES (6019, 42, 12);
INSERT INTO `tb_role_func` VALUES (6020, 42, 74);
INSERT INTO `tb_role_func` VALUES (6021, 42, 14);
INSERT INTO `tb_role_func` VALUES (6022, 42, 55);
INSERT INTO `tb_role_func` VALUES (6023, 42, 73);
INSERT INTO `tb_role_func` VALUES (6024, 42, 6);
INSERT INTO `tb_role_func` VALUES (6025, 42, 27);
INSERT INTO `tb_role_func` VALUES (6026, 42, 13);
INSERT INTO `tb_role_func` VALUES (6027, 42, 26);
INSERT INTO `tb_role_func` VALUES (6028, 42, 56);
INSERT INTO `tb_role_func` VALUES (6029, 42, 4);
INSERT INTO `tb_role_func` VALUES (6030, 42, 63);
INSERT INTO `tb_role_func` VALUES (6031, 42, 71);
INSERT INTO `tb_role_func` VALUES (6032, 42, 8);
INSERT INTO `tb_role_func` VALUES (6033, 42, 11);
INSERT INTO `tb_role_func` VALUES (6034, 42, 72);
INSERT INTO `tb_role_func` VALUES (6035, 42, 9);
INSERT INTO `tb_role_func` VALUES (6036, 42, 1);
INSERT INTO `tb_role_func` VALUES (6037, 42, 3);
INSERT INTO `tb_role_func` VALUES (6038, 42, 62);
INSERT INTO `tb_role_func` VALUES (6039, 42, 61);
INSERT INTO `tb_role_func` VALUES (6040, 42, 59);
INSERT INTO `tb_role_func` VALUES (6041, 42, 58);
INSERT INTO `tb_role_func` VALUES (6042, 42, 57);
INSERT INTO `tb_role_func` VALUES (6043, 42, 75);
INSERT INTO `tb_role_func` VALUES (6044, 42, 60);
INSERT INTO `tb_role_func` VALUES (6045, 42, 84);
INSERT INTO `tb_role_func` VALUES (6047, 43, 2);
INSERT INTO `tb_role_func` VALUES (6048, 43, 5);
INSERT INTO `tb_role_func` VALUES (6049, 43, 54);
INSERT INTO `tb_role_func` VALUES (6050, 43, 12);
INSERT INTO `tb_role_func` VALUES (6051, 43, 74);
INSERT INTO `tb_role_func` VALUES (6052, 43, 14);
INSERT INTO `tb_role_func` VALUES (6053, 43, 55);
INSERT INTO `tb_role_func` VALUES (6054, 43, 73);
INSERT INTO `tb_role_func` VALUES (6055, 43, 6);
INSERT INTO `tb_role_func` VALUES (6056, 43, 27);
INSERT INTO `tb_role_func` VALUES (6057, 43, 13);
INSERT INTO `tb_role_func` VALUES (6058, 43, 26);
INSERT INTO `tb_role_func` VALUES (6059, 43, 56);
INSERT INTO `tb_role_func` VALUES (6060, 43, 4);
INSERT INTO `tb_role_func` VALUES (6061, 43, 63);
INSERT INTO `tb_role_func` VALUES (6062, 43, 71);
INSERT INTO `tb_role_func` VALUES (6063, 43, 8);
INSERT INTO `tb_role_func` VALUES (6064, 43, 11);
INSERT INTO `tb_role_func` VALUES (6065, 43, 72);
INSERT INTO `tb_role_func` VALUES (6066, 43, 9);
INSERT INTO `tb_role_func` VALUES (6067, 43, 1);
INSERT INTO `tb_role_func` VALUES (6068, 43, 3);
INSERT INTO `tb_role_func` VALUES (6069, 43, 62);
INSERT INTO `tb_role_func` VALUES (6070, 43, 61);
INSERT INTO `tb_role_func` VALUES (6071, 43, 59);
INSERT INTO `tb_role_func` VALUES (6072, 43, 58);
INSERT INTO `tb_role_func` VALUES (6073, 43, 57);
INSERT INTO `tb_role_func` VALUES (6074, 43, 75);
INSERT INTO `tb_role_func` VALUES (6075, 43, 60);
INSERT INTO `tb_role_func` VALUES (6076, 43, 84);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实名称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `dept_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门编号',
  `user_status` int(255) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `ix_username`(`username`) USING BTREE,
  INDEX `ix_deptcode`(`dept_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (61, 'admin', '小章鱼', 'e10adc3949ba59abbe56e057f20f883e', 'yfb', 1, '2022-05-01 23:27:25', '2022-05-21 16:57:00');
INSERT INTO `tb_user` VALUES (63, 'justIn', '贾斯汀', 'e10adc3949ba59abbe56e057f20f883e', 'csb', 1, '2022-05-04 19:41:43', '2022-05-11 21:35:03');
INSERT INTO `tb_user` VALUES (65, 'perter', '皮特', 'e10adc3949ba59abbe56e057f20f883e', 'khb1', 1, '2022-09-03 11:25:37', '2022-09-03 11:25:37');
INSERT INTO `tb_user` VALUES (66, 'whitelist', '怀特', 'e10adc3949ba59abbe56e057f20f883e', 'jyb', 1, '2022-09-03 11:26:13', '2022-09-03 15:46:14');
INSERT INTO `tb_user` VALUES (67, 'user1', '普通用户', 'e10adc3949ba59abbe56e057f20f883e', 'khb1', 0, '2022-09-03 11:26:47', '2022-09-11 17:23:18');

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组件',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 862 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (822, 61, 42);
INSERT INTO `tb_user_role` VALUES (831, 63, 42);
INSERT INTO `tb_user_role` VALUES (833, 66, 42);
INSERT INTO `tb_user_role` VALUES (835, 65, 42);
INSERT INTO `tb_user_role` VALUES (855, 63, 43);
INSERT INTO `tb_user_role` VALUES (856, 66, 43);
INSERT INTO `tb_user_role` VALUES (857, 65, 43);
INSERT INTO `tb_user_role` VALUES (861, 67, 43);

SET FOREIGN_KEY_CHECKS = 1;
