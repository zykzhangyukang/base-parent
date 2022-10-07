/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.28 : Database - base_auth
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`base_auth` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `base_auth`;

/*Table structure for table `auth_dept` */

DROP TABLE IF EXISTS `auth_dept`;

CREATE TABLE `auth_dept` (
  `dept_id` int(11) NOT NULL COMMENT '部门id',
  `dept_code` varchar(255) NOT NULL COMMENT '部门编号',
  `dept_name` varchar(255) NOT NULL COMMENT '部门名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_dept` */

insert  into `auth_dept`(`dept_id`,`dept_code`,`dept_name`,`create_time`,`update_time`) values (1,'yfb','研发部','2022-03-12 11:51:12','2022-03-12 11:51:13'),(2,'csb','测试部','2022-03-12 11:51:49','2022-03-12 11:51:51'),(3,'khb1','客户一部','2022-03-12 16:29:08','2022-03-12 16:29:10'),(4,'khb2','客户二部','2022-03-12 16:29:27','2022-03-12 16:29:29'),(5,'yyb','运营部','2022-03-12 16:29:44','2022-03-12 16:29:45'),(6,'jyb','交易部','2022-03-12 16:30:04','2022-03-12 16:30:06'),(7,'yuwb','运维部','2022-03-20 15:42:39','2022-03-20 15:42:40');

/*Table structure for table `auth_func` */

DROP TABLE IF EXISTS `auth_func`;

CREATE TABLE `auth_func` (
  `func_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `func_name` varchar(255) NOT NULL COMMENT '功能名称',
  `func_key` varchar(255) NOT NULL COMMENT '功能key',
  `func_type` varchar(255) NOT NULL COMMENT '功能类型(目录/功能)',
  `func_icon` varchar(255) DEFAULT NULL COMMENT '目录图标',
  `func_sort` int(11) DEFAULT NULL COMMENT '功能排序',
  `dir_hide` bit(1) DEFAULT NULL COMMENT '是否隐藏',
  `parent_id` int(11) NOT NULL COMMENT '父级功能id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`func_id`) USING BTREE,
  UNIQUE KEY `idx_func_key` (`func_key`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_func` */

insert  into `auth_func`(`func_id`,`func_name`,`func_key`,`func_type`,`func_icon`,`func_sort`,`dir_hide`,`parent_id`,`create_time`,`update_time`) values (1,'粒创商城','root','dir','setting',1,'\0',0,'2022-03-19 15:56:27','2022-03-19 15:56:29'),(2,'权限系统','auth','dir','setting',1,'\0',1,'2022-03-19 15:57:28','2022-03-19 15:57:31'),(3,'用户管理','user-management','dir','user',3,'\0',2,'2022-03-19 16:10:26','2022-03-19 16:10:28'),(4,'角色管理','role-management','dir','lock',4,'\0',2,'2022-03-19 16:11:29','2022-03-19 16:11:30'),(5,'功能管理','func-management','dir','profile',5,'\0',2,'2022-03-19 16:11:41','2022-03-19 16:11:42'),(6,'资源管理','res-management','dir','apartment',6,'\0',2,'2022-03-19 16:11:54','2022-03-19 16:11:55'),(8,'角色删除','roleDelete','func','setting',7,NULL,4,'2022-03-19 18:23:22','2022-03-19 18:23:25'),(9,'角色更新','roleUpdate','func','setting',8,NULL,4,'2022-03-19 18:23:38','2022-03-19 18:23:40'),(11,'角色查询','rolePage','func','setting',9,NULL,4,'2022-03-19 18:24:02','2022-03-19 18:24:03'),(12,'功能查询','funcPage','func','setting',10,NULL,5,'2022-03-19 18:24:30','2022-03-19 18:24:32'),(13,'资源查询','rescPage','func','setting',11,NULL,6,'2022-03-19 18:24:51','2022-03-19 18:24:52'),(14,'功能新增','funcSave','func','setting',12,NULL,5,'2022-03-19 18:26:06','2022-03-19 18:26:08'),(26,'资源新增','rescSave','func','setting',13,NULL,6,'2022-03-20 21:43:55','2022-03-20 21:43:55'),(27,'资源删除','rescDelete','func','setting',14,NULL,6,'2022-03-20 21:44:03','2022-03-20 21:44:03'),(54,'功能删除','funcDelete','func','setting',15,NULL,5,'2022-03-26 20:57:44','2022-03-26 20:57:44'),(55,'功能更新','funcUpdate','func','setting',16,NULL,5,'2022-03-26 20:58:00','2022-03-26 20:58:00'),(56,'资源更新','rescUpdate','func','setting',17,NULL,6,'2022-03-26 21:00:16','2022-03-26 21:00:16'),(57,'用户查询','userPage','func','setting',18,NULL,3,'2022-03-26 21:04:37','2022-03-26 21:04:37'),(58,'用户启用','userEnable','func','setting',19,NULL,3,'2022-03-26 21:05:02','2022-03-26 21:05:02'),(59,'用户禁用','userDisable','func','setting',20,NULL,3,'2022-03-26 21:05:19','2022-03-26 21:05:19'),(60,'用户更新','userUpdate','func','setting',21,NULL,3,'2022-03-26 21:05:33','2022-03-26 21:05:33'),(61,'用户删除','userDelete','func','setting',22,NULL,3,'2022-03-26 21:05:55','2022-03-26 21:05:55'),(62,'分配角色','userAssign','func','setting',23,NULL,3,'2022-03-26 21:06:48','2022-03-26 21:06:48'),(63,'分配用户','roleAssign','func','setting',24,NULL,4,'2022-03-26 21:08:39','2022-03-26 21:08:39'),(71,'授权功能','roleAuth','func','setting',25,NULL,4,'2022-04-04 17:45:36','2022-04-04 17:45:36'),(72,'角色新增','roleSave','func','setting',26,NULL,4,'2022-04-16 11:13:19','2022-04-16 11:13:19'),(73,'解绑用户','funcUserUnbind','func','setting',27,NULL,5,'2022-04-16 21:55:19','2022-04-16 21:55:19'),(74,'解绑资源','funcRescUnbind','func','setting',28,NULL,5,'2022-04-16 22:02:49','2022-04-16 22:02:49'),(75,'用户新增','userSave','func','setting',29,NULL,3,'2022-05-02 11:40:57','2022-05-02 11:40:57'),(84,'修改密码','userUpdatePwd','func','setting',30,NULL,3,'2022-05-14 12:20:12','2022-05-14 12:20:12'),(93,'工单系统','wos','dir','apartment',1,'\0',1,'2022-09-13 20:54:26','2022-09-13 20:54:26'),(94,'工单管理','order-management','dir','setting',1,'\0',93,'2022-09-13 20:54:58','2022-09-13 20:54:58');

/*Table structure for table `auth_func_resc` */

DROP TABLE IF EXISTS `auth_func_resc`;

CREATE TABLE `auth_func_resc` (
  `func_resc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `func_id` int(11) NOT NULL COMMENT '功能id',
  `resc_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`func_resc_id`) USING BTREE,
  KEY `idx_func_id` (`func_id`) USING BTREE,
  KEY `idx_resc_id` (`resc_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_func_resc` */

insert  into `auth_func_resc`(`func_resc_id`,`func_id`,`resc_id`) values (286,75,27),(287,62,9),(288,62,10),(289,61,3),(290,60,4),(291,60,2),(292,60,11),(293,59,14),(297,58,12),(301,56,23),(302,56,22),(303,56,41),(304,55,30),(305,55,29),(306,54,31),(307,27,24),(308,26,21),(309,14,32),(312,73,39),(313,72,19),(316,63,35),(317,63,36),(318,63,13),(319,8,15),(320,9,16),(321,9,17),(322,11,18),(323,12,33),(324,12,34),(327,57,1),(328,57,11),(329,13,20),(330,13,41),(331,71,38),(332,71,37),(333,71,43),(335,84,42),(336,74,40);

/*Table structure for table `auth_resc` */

DROP TABLE IF EXISTS `auth_resc`;

CREATE TABLE `auth_resc` (
  `resc_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `resc_name` varchar(255) NOT NULL COMMENT '资源名称',
  `resc_url` varchar(255) NOT NULL COMMENT '资源url',
  `resc_domain` varchar(255) NOT NULL COMMENT '资源所属系统',
  `method_type` varchar(255) NOT NULL COMMENT '请求方式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`resc_id`) USING BTREE,
  UNIQUE KEY `idx_resc_url` (`resc_url`) USING BTREE,
  KEY `idx_resc_name` (`resc_name`) USING BTREE,
  KEY `idx_resc_domain` (`resc_domain`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_resc` */

insert  into `auth_resc`(`resc_id`,`resc_name`,`resc_url`,`resc_domain`,`method_type`,`create_time`,`update_time`) values (1,'用户列表','/auth/user/page','auth','get','2022-03-19 09:31:31','2022-04-09 14:03:50'),(2,'用户获取','/auth/user/select','auth','get','2022-03-19 09:34:24','2022-05-10 22:41:35'),(3,'用户删除','/auth/user/delete','auth','get','2022-03-19 09:34:40','2022-05-01 23:18:10'),(4,'用户更新','/auth/user/update','auth','post','2022-03-19 09:34:58','2022-05-10 22:36:29'),(9,'用户分配角色初始化','/auth/user/role/update/init','auth','get','2022-03-19 10:29:15','2022-08-30 23:07:46'),(10,'用户分配角色','/auth/user/role/update','auth','post','2022-03-19 10:29:41','2022-09-04 14:33:38'),(11,'部门列表','/auth/dept/list','auth','get','2022-03-19 10:30:01','2022-04-09 14:03:20'),(12,'用户启用','/auth/user/update/enable','auth','get','2022-03-19 10:30:16','2022-09-03 15:45:31'),(13,'用户角色信息','/auth/user/select/role/names','auth','get','2022-03-19 10:30:34','2022-04-09 14:03:07'),(14,'用户禁用','/auth/user/update/disable','auth','get','2022-03-19 10:30:48','2022-05-14 11:52:30'),(15,'角色删除','/auth/role/delete','auth','get','2022-03-19 10:31:30','2022-05-01 23:18:07'),(16,'角色更新','/auth/role/update','auth','post','2022-03-19 10:31:44','2022-05-01 22:23:59'),(17,'角色获取','/auth/role/select','auth','get','2022-03-19 10:32:06','2022-04-09 14:02:34'),(18,'角色列表','/auth/role/page','auth','get','2022-03-19 10:32:28','2022-04-09 14:02:25'),(19,'角色新增','/auth/role/save','auth','post','2022-03-19 10:32:48','2022-04-09 14:02:29'),(20,'资源列表','/auth/resc/page','auth','get','2022-03-19 10:33:08','2022-09-04 14:44:14'),(21,'资源新增','/auth/resc/save','auth','post','2022-03-19 10:33:26','2022-04-09 14:02:15'),(22,'资源获取','/auth/resc/select','auth','get','2022-03-19 10:33:42','2022-04-09 14:02:06'),(23,'资源更新','/auth/resc/update','auth','post','2022-03-19 10:33:56','2022-05-01 22:23:25'),(24,'资源删除','/auth/resc/delete','auth','get','2022-03-19 10:34:13','2022-05-01 23:17:58'),(27,'用户新增','/auth/user/save','auth','post','2022-03-19 11:18:04','2022-04-09 14:01:38'),(29,'功能更新','/auth/func/update','auth','post','2022-03-26 20:52:38','2022-05-01 21:57:53'),(30,'功能获取','/auth/func/select','auth','get','2022-03-26 20:52:51','2022-04-09 14:01:25'),(31,'功能删除','/auth/func/delete','auth','get','2022-03-26 20:53:09','2022-05-01 23:17:54'),(32,'功能新增','/auth/func/save','auth','post','2022-03-26 20:53:24','2022-04-09 14:01:12'),(33,'功能查询','/auth/func/page','auth','get','2022-03-26 20:53:40','2022-09-11 16:05:51'),(34,'功能树获取','/auth/func/list/tree','auth','get','2022-03-26 20:53:58','2022-04-09 14:01:03'),(35,'角色分配用户初始化','/auth/role/user/update/init','auth','get','2022-03-26 21:07:44','2022-09-03 16:02:34'),(36,'角色分配用户','/auth/role/user/update','auth','post','2022-03-26 21:07:59','2022-09-03 16:02:22'),(37,'角色授权功能初始化','/auth/role/func/update/init','auth','get','2022-04-04 17:46:28','2022-09-03 16:02:00'),(38,'角色分配功能','/auth/role/func/update','auth','post','2022-04-04 17:46:57','2022-08-30 23:00:27'),(39,'功能解绑用户','/auth/func/delete/user/bind','auth','get','2022-04-16 21:54:38','2022-04-16 22:02:08'),(40,'功能解绑资源','/auth/func/delete/resc/bind','auth','get','2022-04-16 22:02:24','2022-04-23 21:28:09'),(41,'资源搜索','/auth/resc/search','auth','get','2022-05-01 19:17:13','2022-09-01 00:02:42'),(42,'用户修改密码','/auth/user/update/password','auth','post','2022-05-14 12:19:03','2022-09-03 11:24:39'),(43,'角色授权功能预检查','/auth/role/func/update/check','auth','post','2022-05-21 15:34:56','2022-10-07 21:40:45');

/*Table structure for table `auth_role` */

DROP TABLE IF EXISTS `auth_role`;

CREATE TABLE `auth_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(255) NOT NULL COMMENT '角色名称',
  `role_desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `idx_role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_role` */

insert  into `auth_role`(`role_id`,`role_name`,`role_desc`,`create_time`,`update_time`) values (42,'管理员','高权限','2022-04-16 11:38:56','2022-09-11 16:24:52'),(43,'普通用户','低权限','2022-04-16 11:39:07','2022-09-11 16:32:38'),(44,'查询角色','只有查询的功能','2022-09-12 10:56:43','2022-10-07 21:40:37'),(45,'查询和新增','有查询和新增的功能权限','2022-09-12 10:59:11','2022-10-07 21:40:34');

/*Table structure for table `auth_role_func` */

DROP TABLE IF EXISTS `auth_role_func`;

CREATE TABLE `auth_role_func` (
  `role_func_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `func_id` int(11) NOT NULL COMMENT '功能id',
  PRIMARY KEY (`role_func_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_func_id` (`func_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6348 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_role_func` */

insert  into `auth_role_func`(`role_func_id`,`role_id`,`func_id`) values (6127,43,1),(6145,44,1),(6278,42,2),(6279,42,5),(6280,42,54),(6281,42,12),(6282,42,74),(6283,42,14),(6284,42,55),(6285,42,73),(6286,42,94),(6287,42,6),(6288,42,27),(6289,42,13),(6290,42,26),(6291,42,56),(6292,42,4),(6293,42,63),(6294,42,71),(6295,42,8),(6296,42,11),(6297,42,72),(6298,42,9),(6299,42,1),(6300,42,3),(6301,42,62),(6302,42,61),(6303,42,59),(6304,42,58),(6305,42,57),(6306,42,75),(6307,42,60),(6308,42,84),(6309,42,93),(6341,45,2),(6342,45,1),(6343,45,3),(6344,45,57);

/*Table structure for table `auth_user` */

DROP TABLE IF EXISTS `auth_user`;

CREATE TABLE `auth_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) NOT NULL COMMENT '用户账号',
  `real_name` varchar(255) NOT NULL COMMENT '真实名称',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `dept_code` varchar(255) NOT NULL COMMENT '部门编号',
  `user_status` int(255) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `ix_username` (`username`) USING BTREE,
  KEY `ix_deptcode` (`dept_code`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_user` */

insert  into `auth_user`(`user_id`,`username`,`real_name`,`password`,`dept_code`,`user_status`,`create_time`,`update_time`) values (61,'admin','小章鱼','e10adc3949ba59abbe56e057f20f883e','yfb',1,'2022-05-01 23:27:25','2022-05-21 16:57:00'),(63,'justIn','贾斯汀','e10adc3949ba59abbe56e057f20f883e','csb',1,'2022-05-04 19:41:43','2022-05-11 21:35:03'),(65,'perter','皮特','e10adc3949ba59abbe56e057f20f883e','khb1',1,'2022-09-03 11:25:37','2022-09-03 11:25:37'),(66,'whitelist','怀特','e10adc3949ba59abbe56e057f20f883e','jyb',1,'2022-09-03 11:26:13','2022-09-03 15:46:14'),(67,'user1','普通用户','e10adc3949ba59abbe56e057f20f883e','khb1',0,'2022-09-03 11:26:47','2022-09-11 17:23:18'),(68,'query','查询','e10adc3949ba59abbe56e057f20f883e','yfb',1,'2022-09-12 10:56:59','2022-09-12 10:56:59'),(69,'querysave','查询和新增','e10adc3949ba59abbe56e057f20f883e','csb',1,'2022-09-12 11:00:11','2022-10-07 21:40:08');

/*Table structure for table `auth_user_role` */

DROP TABLE IF EXISTS `auth_user_role`;

CREATE TABLE `auth_user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组件',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=872 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_user_role` */

insert  into `auth_user_role`(`user_role_id`,`user_id`,`role_id`) values (822,61,42),(833,66,42),(835,65,42),(856,66,43),(857,65,43),(861,67,43),(862,63,43),(863,68,44),(871,69,45);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
