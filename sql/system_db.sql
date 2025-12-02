/*
 Navicat Premium Dump SQL

 Source Server         : ywx
 Source Server Type    : MySQL
 Source Server Version : 90400 (9.4.0)
 Source Host           : localhost:3306
 Source Schema         : system_db

 Target Server Type    : MySQL
 Target Server Version : 90400 (9.4.0)
 File Encoding         : 65001

 Date: 01/12/2025 15:25:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `content` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `is_top` tinyint DEFAULT '0' COMMENT '是否置顶(0否,1是)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公告表';

-- ----------------------------
-- Records of announcement
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `ip` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录IP',
  `location` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浏览器',
  `os` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作系统',
  `status` tinyint DEFAULT '0' COMMENT '登录状态(0成功,1失败)',
  `error_msg` text COLLATE utf8mb4_general_ci COMMENT '错误信息',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录日志表';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作类型',
  `method` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方法',
  `params` text COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `ip` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作IP',
  `location` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作地点',
  `browser` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浏览器',
  `os` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作系统',
  `status` tinyint DEFAULT '0' COMMENT '操作状态(0成功,1失败)',
  `error_msg` text COLLATE utf8mb4_general_ci COMMENT '错误信息',
  `operation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation_time` (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志表';

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
