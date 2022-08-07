/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : project_user

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 17/07/2022 15:12:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_dept
-- ----------------------------
DROP TABLE IF EXISTS `tb_dept`;
CREATE TABLE `tb_dept`  (
  `dept_id` int(11) NOT NULL,
  `dept_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `func_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `func_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `func_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `func_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `func_sort` int(11) NULL DEFAULT NULL,
  `dir_hide` bit(1) NULL DEFAULT NULL,
  `parent_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`func_id`) USING BTREE,
  UNIQUE INDEX `idx_func_key`(`func_key`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_func
-- ----------------------------
INSERT INTO `tb_func` VALUES (1, '立创商城', 'root', 'dir', 'setting', 1, b'0', 0, '2022-03-19 15:56:27', '2022-03-19 15:56:29');
INSERT INTO `tb_func` VALUES (2, '系统管理', 'auth', 'dir', 'setting', 2, b'0', 1, '2022-03-19 15:57:28', '2022-03-19 15:57:31');
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
INSERT INTO `tb_func` VALUES (74, '解绑资源', 'funcResourceUnbind', 'func', 'setting', 28, NULL, 5, '2022-04-16 22:02:49', '2022-04-16 22:02:49');
INSERT INTO `tb_func` VALUES (75, '用户新增', 'userSave', 'func', 'setting', 29, NULL, 3, '2022-05-02 11:40:57', '2022-05-02 11:40:57');
INSERT INTO `tb_func` VALUES (84, '修改密码', 'userUpdatePwd', 'func', 'setting', 30, NULL, 3, '2022-05-14 12:20:12', '2022-05-14 12:20:12');

-- ----------------------------
-- Table structure for tb_func_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_func_resource`;
CREATE TABLE `tb_func_resource`  (
  `func_resource_id` int(11) NOT NULL AUTO_INCREMENT,
  `func_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  PRIMARY KEY (`func_resource_id`) USING BTREE,
  INDEX `idx_func_id`(`func_id`) USING BTREE,
  INDEX `idx_resource_id`(`resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 334 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_func_resource
-- ----------------------------
INSERT INTO `tb_func_resource` VALUES (285, 84, 42);
INSERT INTO `tb_func_resource` VALUES (286, 75, 27);
INSERT INTO `tb_func_resource` VALUES (287, 62, 9);
INSERT INTO `tb_func_resource` VALUES (288, 62, 10);
INSERT INTO `tb_func_resource` VALUES (289, 61, 3);
INSERT INTO `tb_func_resource` VALUES (290, 60, 4);
INSERT INTO `tb_func_resource` VALUES (291, 60, 2);
INSERT INTO `tb_func_resource` VALUES (292, 60, 11);
INSERT INTO `tb_func_resource` VALUES (293, 59, 14);
INSERT INTO `tb_func_resource` VALUES (297, 58, 12);
INSERT INTO `tb_func_resource` VALUES (300, 74, 40);
INSERT INTO `tb_func_resource` VALUES (301, 56, 23);
INSERT INTO `tb_func_resource` VALUES (302, 56, 22);
INSERT INTO `tb_func_resource` VALUES (303, 56, 41);
INSERT INTO `tb_func_resource` VALUES (304, 55, 30);
INSERT INTO `tb_func_resource` VALUES (305, 55, 29);
INSERT INTO `tb_func_resource` VALUES (306, 54, 31);
INSERT INTO `tb_func_resource` VALUES (307, 27, 24);
INSERT INTO `tb_func_resource` VALUES (308, 26, 21);
INSERT INTO `tb_func_resource` VALUES (309, 14, 32);
INSERT INTO `tb_func_resource` VALUES (312, 73, 39);
INSERT INTO `tb_func_resource` VALUES (313, 72, 19);
INSERT INTO `tb_func_resource` VALUES (316, 63, 35);
INSERT INTO `tb_func_resource` VALUES (317, 63, 36);
INSERT INTO `tb_func_resource` VALUES (318, 63, 13);
INSERT INTO `tb_func_resource` VALUES (319, 8, 15);
INSERT INTO `tb_func_resource` VALUES (320, 9, 16);
INSERT INTO `tb_func_resource` VALUES (321, 9, 17);
INSERT INTO `tb_func_resource` VALUES (322, 11, 18);
INSERT INTO `tb_func_resource` VALUES (323, 12, 33);
INSERT INTO `tb_func_resource` VALUES (324, 12, 34);
INSERT INTO `tb_func_resource` VALUES (327, 57, 1);
INSERT INTO `tb_func_resource` VALUES (328, 57, 11);
INSERT INTO `tb_func_resource` VALUES (329, 13, 20);
INSERT INTO `tb_func_resource` VALUES (330, 13, 41);
INSERT INTO `tb_func_resource` VALUES (331, 71, 38);
INSERT INTO `tb_func_resource` VALUES (332, 71, 37);
INSERT INTO `tb_func_resource` VALUES (333, 71, 43);

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource`  (
  `resource_id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource_domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `method_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`resource_id`) USING BTREE,
  UNIQUE INDEX `idx_resource_url`(`resource_url`) USING BTREE,
  INDEX `idx_resource_name`(`resource_name`) USING BTREE,
  INDEX `idx_resource_domain`(`resource_domain`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES (1, '用户列表', '/user/page', 'user', '2022-03-19 09:31:31', '2022-04-09 14:03:50', 'get');
INSERT INTO `tb_resource` VALUES (2, '用户获取', '/user/select', 'user', '2022-03-19 09:34:24', '2022-05-10 22:41:35', 'get');
INSERT INTO `tb_resource` VALUES (3, '用户删除', '/user/delete', 'user', '2022-03-19 09:34:40', '2022-05-01 23:18:10', 'get');
INSERT INTO `tb_resource` VALUES (4, '用户更新', '/user/update', 'user', '2022-03-19 09:34:58', '2022-05-10 22:36:29', 'post');
INSERT INTO `tb_resource` VALUES (9, '用户分配角色初始化', '/user/assign/init', 'user', '2022-03-19 10:29:15', '2022-05-14 11:50:20', 'get');
INSERT INTO `tb_resource` VALUES (10, '用户分配角色', '/user/assign', 'user', '2022-03-19 10:29:41', '2022-05-14 11:50:25', 'post');
INSERT INTO `tb_resource` VALUES (11, '部门列表', '/user/dept/list', 'user', '2022-03-19 10:30:01', '2022-04-09 14:03:20', 'get');
INSERT INTO `tb_resource` VALUES (12, '用户启用', '/user/update/enable', 'user', '2022-03-19 10:30:16', '2022-05-14 11:52:41', 'get');
INSERT INTO `tb_resource` VALUES (13, '用户角色信息', '/user/select/role/names', 'user', '2022-03-19 10:30:34', '2022-04-09 14:03:07', 'get');
INSERT INTO `tb_resource` VALUES (14, '用户禁用', '/user/update/disable', 'user', '2022-03-19 10:30:48', '2022-05-14 11:52:30', 'get');
INSERT INTO `tb_resource` VALUES (15, '角色删除', '/user/role/delete', 'user', '2022-03-19 10:31:30', '2022-05-01 23:18:07', 'get');
INSERT INTO `tb_resource` VALUES (16, '角色更新', '/user/role/update', 'user', '2022-03-19 10:31:44', '2022-05-01 22:23:59', 'post');
INSERT INTO `tb_resource` VALUES (17, '角色获取', '/user/role/select', 'user', '2022-03-19 10:32:06', '2022-04-09 14:02:34', 'get');
INSERT INTO `tb_resource` VALUES (18, '角色列表', '/user/role/page', 'user', '2022-03-19 10:32:28', '2022-04-09 14:02:25', 'get');
INSERT INTO `tb_resource` VALUES (19, '角色新增', '/user/role/save', 'user', '2022-03-19 10:32:48', '2022-04-09 14:02:29', 'post');
INSERT INTO `tb_resource` VALUES (20, '资源列表', '/user/resource/page', 'user', '2022-03-19 10:33:08', '2022-04-09 14:02:20', 'get');
INSERT INTO `tb_resource` VALUES (21, '资源新增', '/user/resource/save', 'user', '2022-03-19 10:33:26', '2022-04-09 14:02:15', 'post');
INSERT INTO `tb_resource` VALUES (22, '资源获取', '/user/resource/select', 'user', '2022-03-19 10:33:42', '2022-04-09 14:02:06', 'get');
INSERT INTO `tb_resource` VALUES (23, '资源更新', '/user/resource/update', 'user', '2022-03-19 10:33:56', '2022-05-01 22:23:25', 'post');
INSERT INTO `tb_resource` VALUES (24, '资源删除', '/user/resource/delete', 'user', '2022-03-19 10:34:13', '2022-05-01 23:17:58', 'get');
INSERT INTO `tb_resource` VALUES (27, '用户新增', '/user/save', 'user', '2022-03-19 11:18:04', '2022-04-09 14:01:38', 'post');
INSERT INTO `tb_resource` VALUES (29, '功能更新', '/user/func/update', 'user', '2022-03-26 20:52:38', '2022-05-01 21:57:53', 'post');
INSERT INTO `tb_resource` VALUES (30, '功能获取', '/user/func/select', 'user', '2022-03-26 20:52:51', '2022-04-09 14:01:25', 'get');
INSERT INTO `tb_resource` VALUES (31, '功能删除', '/user/func/delete', 'user', '2022-03-26 20:53:09', '2022-05-01 23:17:54', 'get');
INSERT INTO `tb_resource` VALUES (32, '功能新增', '/user/func/save', 'user', '2022-03-26 20:53:24', '2022-04-09 14:01:12', 'post');
INSERT INTO `tb_resource` VALUES (33, '功能查询', '/user/func/page', 'user', '2022-03-26 20:53:40', '2022-05-22 10:07:51', 'get');
INSERT INTO `tb_resource` VALUES (34, '功能树获取', '/user/func/list/tree', 'user', '2022-03-26 20:53:58', '2022-04-09 14:01:03', 'get');
INSERT INTO `tb_resource` VALUES (35, '角色分配用户初始化', '/user/role/assign/init', 'user', '2022-03-26 21:07:44', '2022-04-09 14:00:57', 'get');
INSERT INTO `tb_resource` VALUES (36, '角色分配用户', '/user/role/assign', 'user', '2022-03-26 21:07:59', '2022-04-09 14:00:48', 'post');
INSERT INTO `tb_resource` VALUES (37, '角色授权功能初始化', '/user/role/auth/func/init', 'user', '2022-04-04 17:46:28', '2022-04-09 14:00:39', 'get');
INSERT INTO `tb_resource` VALUES (38, '角色授权功能', '/user/role/auth/func', 'user', '2022-04-04 17:46:57', '2022-04-16 11:33:50', 'post');
INSERT INTO `tb_resource` VALUES (39, '功能解绑用户', '/user/func/delete/user/bind', 'user', '2022-04-16 21:54:38', '2022-04-16 22:02:08', 'get');
INSERT INTO `tb_resource` VALUES (40, '功能解绑资源', '/user/func/delete/resource/bind', 'user', '2022-04-16 22:02:24', '2022-04-23 21:28:09', 'get');
INSERT INTO `tb_resource` VALUES (41, '资源搜索', '/user/resource/search', 'user', '2022-05-01 19:17:13', '2022-05-11 21:34:46', 'get');
INSERT INTO `tb_resource` VALUES (42, '用户修改密码', '/user/update/password', 'user', '2022-05-14 12:19:03', '2022-05-14 12:19:03', 'post');
INSERT INTO `tb_resource` VALUES (43, '角色授权功能预检查', '/user/role/auth/func/check', 'user', '2022-05-21 15:34:56', '2022-05-21 16:56:53', 'post');

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `idx_role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (42, '管理员', '高权限', '2022-04-16 11:38:56', '2022-05-11 21:11:06');
INSERT INTO `tb_role` VALUES (43, '普通用户', '低权限', '2022-04-16 11:39:07', '2022-05-21 17:24:37');

-- ----------------------------
-- Table structure for tb_role_func
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_func`;
CREATE TABLE `tb_role_func`  (
  `role_func_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `func_id` int(11) NOT NULL,
  PRIMARY KEY (`role_func_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_func_id`(`func_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4921 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_func
-- ----------------------------
INSERT INTO `tb_role_func` VALUES (4788, 42, 2);
INSERT INTO `tb_role_func` VALUES (4790, 42, 5);
INSERT INTO `tb_role_func` VALUES (4791, 42, 54);
INSERT INTO `tb_role_func` VALUES (4792, 42, 12);
INSERT INTO `tb_role_func` VALUES (4793, 42, 74);
INSERT INTO `tb_role_func` VALUES (4794, 42, 14);
INSERT INTO `tb_role_func` VALUES (4795, 42, 55);
INSERT INTO `tb_role_func` VALUES (4796, 42, 73);
INSERT INTO `tb_role_func` VALUES (4798, 42, 6);
INSERT INTO `tb_role_func` VALUES (4799, 42, 27);
INSERT INTO `tb_role_func` VALUES (4800, 42, 13);
INSERT INTO `tb_role_func` VALUES (4801, 42, 26);
INSERT INTO `tb_role_func` VALUES (4802, 42, 56);
INSERT INTO `tb_role_func` VALUES (4803, 42, 4);
INSERT INTO `tb_role_func` VALUES (4804, 42, 63);
INSERT INTO `tb_role_func` VALUES (4805, 42, 71);
INSERT INTO `tb_role_func` VALUES (4806, 42, 8);
INSERT INTO `tb_role_func` VALUES (4807, 42, 11);
INSERT INTO `tb_role_func` VALUES (4808, 42, 72);
INSERT INTO `tb_role_func` VALUES (4809, 42, 9);
INSERT INTO `tb_role_func` VALUES (4810, 42, 1);
INSERT INTO `tb_role_func` VALUES (4811, 42, 3);
INSERT INTO `tb_role_func` VALUES (4812, 42, 62);
INSERT INTO `tb_role_func` VALUES (4813, 42, 61);
INSERT INTO `tb_role_func` VALUES (4814, 42, 59);
INSERT INTO `tb_role_func` VALUES (4815, 42, 58);
INSERT INTO `tb_role_func` VALUES (4816, 42, 57);
INSERT INTO `tb_role_func` VALUES (4817, 42, 75);
INSERT INTO `tb_role_func` VALUES (4818, 42, 60);
INSERT INTO `tb_role_func` VALUES (4819, 42, 84);
INSERT INTO `tb_role_func` VALUES (4896, 43, 2);
INSERT INTO `tb_role_func` VALUES (4897, 43, 5);
INSERT INTO `tb_role_func` VALUES (4898, 43, 54);
INSERT INTO `tb_role_func` VALUES (4899, 43, 12);
INSERT INTO `tb_role_func` VALUES (4900, 43, 74);
INSERT INTO `tb_role_func` VALUES (4901, 43, 14);
INSERT INTO `tb_role_func` VALUES (4902, 43, 55);
INSERT INTO `tb_role_func` VALUES (4903, 43, 73);
INSERT INTO `tb_role_func` VALUES (4904, 43, 4);
INSERT INTO `tb_role_func` VALUES (4905, 43, 63);
INSERT INTO `tb_role_func` VALUES (4906, 43, 71);
INSERT INTO `tb_role_func` VALUES (4907, 43, 8);
INSERT INTO `tb_role_func` VALUES (4908, 43, 11);
INSERT INTO `tb_role_func` VALUES (4909, 43, 72);
INSERT INTO `tb_role_func` VALUES (4910, 43, 9);
INSERT INTO `tb_role_func` VALUES (4911, 43, 1);
INSERT INTO `tb_role_func` VALUES (4912, 43, 3);
INSERT INTO `tb_role_func` VALUES (4913, 43, 62);
INSERT INTO `tb_role_func` VALUES (4914, 43, 61);
INSERT INTO `tb_role_func` VALUES (4915, 43, 59);
INSERT INTO `tb_role_func` VALUES (4916, 43, 58);
INSERT INTO `tb_role_func` VALUES (4917, 43, 57);
INSERT INTO `tb_role_func` VALUES (4918, 43, 75);
INSERT INTO `tb_role_func` VALUES (4919, 43, 60);
INSERT INTO `tb_role_func` VALUES (4920, 43, 84);

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
  UNIQUE INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (61, 'admin', '小章鱼', '$2a$10$bjyipeZnew3vNl/s.7Ck9eP7riBF/M33hSWjy/YxAKZuNKoUASvSu', 'yfb', 0, '2022-05-01 23:27:25', '2022-05-21 16:57:00');
INSERT INTO `tb_user` VALUES (63, 'justIn', '贾斯汀', '$2a$10$F9rIiDUBx1D1sadSRbDuDOBsZVdFXHv1qHgdeKgCpYebm3h9jIA2C', 'csb', 0, '2022-05-04 19:41:43', '2022-05-11 21:35:03');

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 823 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (822, 61, 42);

SET FOREIGN_KEY_CHECKS = 1;
