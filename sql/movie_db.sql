/*
 Navicat Premium Dump SQL

 Source Server         : ywx
 Source Server Type    : MySQL
 Source Server Version : 90400 (9.4.0)
 Source Host           : localhost:3306
 Source Schema         : movie_db

 Target Server Type    : MySQL
 Target Server Version : 90400 (9.4.0)
 File Encoding         : 65001

 Date: 01/12/2025 15:22:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for movie_category
-- ----------------------------
DROP TABLE IF EXISTS `movie_category`;
CREATE TABLE `movie_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父分类ID',
  `sort` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_name` (`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电影分类表';

-- ----------------------------
-- Records of movie_category
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for movie_category_relation
-- ----------------------------
DROP TABLE IF EXISTS `movie_category_relation`;
CREATE TABLE `movie_category_relation` (
  `movie_id` bigint NOT NULL COMMENT '电影ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  PRIMARY KEY (`movie_id`,`category_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电影分类关联表';

-- ----------------------------
-- Records of movie_category_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for movie_info
-- ----------------------------
DROP TABLE IF EXISTS `movie_info`;
CREATE TABLE `movie_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '电影ID',
  `title` varchar(255) NOT NULL COMMENT '电影名称',
  `original_title` varchar(255) DEFAULT NULL COMMENT '原始名称',
  `poster` varchar(255) DEFAULT NULL COMMENT '海报URL',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面URL',
  `director` varchar(255) DEFAULT NULL COMMENT '导演',
  `actors` text COMMENT '演员列表',
  `description` text COMMENT '剧情简介',
  `release_date` date DEFAULT NULL COMMENT '上映日期',
  `duration` int DEFAULT NULL COMMENT '片长(分钟)',
  `rating` decimal(3,2) DEFAULT NULL COMMENT '评分',
  `views_count` bigint DEFAULT '0' COMMENT '观看次数',
  `source_url` varchar(255) DEFAULT NULL COMMENT '资源链接',
  `status` tinyint DEFAULT '1' COMMENT '状态(0下架,1上架)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_title` (`title`),
  KEY `idx_rating` (`rating`),
  KEY `idx_release_date` (`release_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电影信息表';

-- ----------------------------
-- Records of movie_info
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
