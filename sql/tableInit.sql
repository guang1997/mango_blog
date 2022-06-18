-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.27 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  10.2.0.5704
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 mango_blog 的数据库结构
DROP DATABASE IF EXISTS `mango_blog`;
CREATE DATABASE IF NOT EXISTS `mango_blog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mango_blog`;

-- 导出  表 mango_blog.t_admin 结构
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE IF NOT EXISTS `t_admin` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '个人头像',
  `email` varchar(60) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机',
  `login_count` int unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `qq_number` varchar(255) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `enabled` tinyint unsigned DEFAULT '0' COMMENT '账号状态1:启用0:禁用',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='管理员表';

-- 正在导出表  mango_blog.t_admin 的数据：~2 rows (大约)
DELETE FROM `t_admin`;
/*!40000 ALTER TABLE `t_admin` DISABLE KEYS */;
INSERT INTO `t_admin` (`id`, `username`, `password`, `gender`, `avatar`, `email`, `phone`, `login_count`, `last_login_time`, `last_login_ip`, `create_time`, `update_time`, `nickname`, `qq_number`, `we_chat`, `enabled`, `is_deleted`) VALUES
	('1295268474480156673', 'admin', '$2a$10$0uPAzrAyQkWhfWoA9DlaDudQoveg/cMt35Vwof9tjRRWhx1Jkolqm', 1, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', 'guang1997qqq@163.com', '18761616251', 46, '2022-06-18 15:35:42', '192.168.14.49', '2021-09-26 16:56:46', '2022-06-18 15:36:14', '李斯特123', '872174823', '18761616251', 1, 0),
	('1595268474480156674', 'visitor', '$2a$10$Ak12ZqHxdWV4ooYfoWdNMuPGeN3NZzvgzsrtOj5WViECxU4FGrnBy', 1, 'https://guang1997.oss-cn-shanghai.aliyuncs.com/avatar/2020/07/22/index.jpg', '872174823@qq.com', '18888888888', 5, '2022-05-15 22:34:09', '192.168.1.7', '2021-12-02 22:01:22', '2022-05-15 22:34:09', '游客', '111111', '111111', 1, 0);
/*!40000 ALTER TABLE `t_admin` ENABLE KEYS */;

-- 导出  表 mango_blog.t_blog 结构
DROP TABLE IF EXISTS `t_blog`;
CREATE TABLE IF NOT EXISTS `t_blog` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客简介',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '博客内容',
  `click_count` int DEFAULT '0' COMMENT '博客点击数',
  `like_count` int DEFAULT '0' COMMENT '博客点赞数',
  `file_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标题图片id',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `admin_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '管理员id',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `blog_sort_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客分类ID',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客表';

-- 正在导出表  mango_blog.t_blog 的数据：~7 rows (大约)
DELETE FROM `t_blog`;
/*!40000 ALTER TABLE `t_blog` DISABLE KEYS */;
INSERT INTO `t_blog` (`id`, `title`, `summary`, `content`, `click_count`, `like_count`, `file_id`, `create_time`, `update_time`, `admin_id`, `author`, `blog_sort_id`, `is_deleted`) VALUES
	('1538044591879995393', '测试', '测试', '<p><img alt="" src="http://rdd9c3a39.hd-bkt.clouddn.com/blog/2022/06/18/01f4a9f7-e321-4594-816b-5633a179f398-微信图片_20211117092854.jpg" style="height:960px; width:960px" /></p>\n', 0, 0, 'http://rdd9c3a39.hd-bkt.clouddn.com/blog/2022/06/18/609e0194-28ad-4635-a73b-e126f7945ae3-微信图片_20210924114014.jpg', '2022-06-18 14:23:01', '2022-06-18 14:23:01', '1295268474480156673', 'admin', '1505006369916694530', 1),
	('1538092841412538370', '测试', '测试', '<p>11111</p>\n', 0, 0, 'http://rdd9c3a39.hd-bkt.clouddn.com/blog/2022/06/18/81d6dc53-02ca-48e1-a510-1f87219f8775-微信图片_20211117092854.jpg', '2022-06-18 17:34:44', '2022-06-18 17:34:44', '1295268474480156673', 'admin', '1505006369916694530', 0);
/*!40000 ALTER TABLE `t_blog` ENABLE KEYS */;

-- 导出  表 mango_blog.t_blog_tag 结构
DROP TABLE IF EXISTS `t_blog_tag`;
CREATE TABLE IF NOT EXISTS `t_blog_tag` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `blog_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '博客ID',
  `tag_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `IDX_BLOGID_TAGID_UNIQUE` (`blog_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  mango_blog.t_blog_tag 的数据：~11 rows (大约)
DELETE FROM `t_blog_tag`;
/*!40000 ALTER TABLE `t_blog_tag` DISABLE KEYS */;
INSERT INTO `t_blog_tag` (`id`, `blog_id`, `tag_id`, `create_time`, `update_time`) VALUES
	('1538092841542561794', '1538092841412538370', '1506663317590327298', '2022-06-18 17:34:44', '2022-06-18 17:34:44');
/*!40000 ALTER TABLE `t_blog_tag` ENABLE KEYS */;

-- 导出  表 mango_blog.t_comment 结构
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE IF NOT EXISTS `t_comment` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `parent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '父id',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户id',
  `browser_finger` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '浏览器指纹',
  `ip` varchar(64) DEFAULT NULL COMMENT '评论来源ip',
  `unique_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '唯一标识，如果用户id不同，ip相同，则判断此字段',
  `avatar` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户昵称',
  `answer_nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '被评论用户昵称',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论内容',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论来源',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '评论类型 1:点赞 0:评论',
  `blog_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态: 0-待审核, 1-审核通过, 2-审核不通过',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='评论表';

-- 正在导出表  mango_blog.t_comment 的数据：~30 rows (大约)
DELETE FROM `t_comment`;
/*!40000 ALTER TABLE `t_comment` DISABLE KEYS */;
INSERT INTO `t_comment` (`id`, `parent_id`, `user_id`, `browser_finger`, `ip`, `unique_key`, `avatar`, `nickname`, `answer_nickname`, `content`, `create_time`, `update_time`, `source`, `type`, `blog_id`, `status`, `like_count`) VALUES
	('1532722099151716353', '0', '1531998551454093313', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '测试123', NULL, '测试评论', '2022-06-03 21:53:20', '2022-06-03 22:39:06', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 7),
	('1532722106827292673', '1532728552252080130', '1531998551454093313', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '测试123', NULL, 'false', '2022-06-03 21:53:22', '2022-06-03 22:26:31', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1532728552252080130', '1532722099151716353', '1531998551454093313', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '测试123', '测试123', '测试二级评论', '2022-06-03 22:18:58', '2022-06-03 22:31:42', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 1),
	('1532729104239255554', '1532722099151716353', '1531998551454093313', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '测试123', NULL, 'true', '2022-06-03 22:21:10', '2022-06-03 22:26:34', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1532729327762104321', '0', '1531998551454093313', 'b1bd7743106c5abba2c10a5b5c53e2e8', '192.168.1.7', 'ae3ab06af9307c3534e739d256b82fdc', 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '测试123', NULL, 'true', '2022-06-03 22:22:03', '2022-06-03 22:22:37', 'BLOG_INFO_LIKES', 1, '1528008410976800770', 1, 20),
	('1532731079097958401', '1532722099151716353', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'true', '2022-06-03 22:29:01', '2022-06-03 22:39:06', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1532731755911823361', '1532728552252080130', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'true', '2022-06-03 22:31:42', '2022-06-03 22:31:42', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1532737579593875458', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:54:51', '2022-06-03 22:55:21', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 1),
	('1532737584215998465', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:54:52', '2022-06-03 22:54:52', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 0),
	('1532737589676982274', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:54:53', '2022-06-03 22:54:53', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 0),
	('1532737595779694594', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:54:54', '2022-06-03 22:54:54', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 0),
	('1532737600628310018', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:54:56', '2022-06-03 22:54:56', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 0),
	('1532737606886211585', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:54:57', '2022-06-03 22:54:57', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 0),
	('1532737612372361217', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:54:58', '2022-06-03 22:54:58', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 0),
	('1532737616780574721', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:54:59', '2022-06-03 23:18:33', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 0),
	('1532737621163622401', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:55:00', '2022-06-03 23:18:34', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 1),
	('1532737625416646657', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:55:01', '2022-06-03 23:18:37', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 0),
	('1532737683918798850', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '1', '2022-06-03 22:55:15', '2022-06-08 21:21:52', 'BLOG_INFO_MESSAGE', 0, '1528008410976800770', 1, 2),
	('1532737707658559490', '1532737579593875458', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'true', '2022-06-03 22:55:21', '2022-06-03 22:55:21', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1532738197473574914', '1532737625416646657', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'false', '2022-06-03 22:57:18', '2022-06-03 23:18:37', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1532738200422170626', '1532737621163622401', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'false', '2022-06-03 22:57:19', '2022-06-03 23:18:33', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1532738215186120705', '1532737616780574721', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'false', '2022-06-03 22:57:22', '2022-06-03 23:18:33', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1534523725411893250', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '留言板评论', '2022-06-08 21:12:21', '2022-06-15 17:38:31', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 13),
	('1534524364216975362', '1534523725411893250', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', '李斯特123', '回复一级评论', '2022-06-08 21:14:53', '2022-06-08 21:14:53', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1534524403739901953', '1534523725411893250', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', '李斯特123', '回复二级评论', '2022-06-08 21:15:03', '2022-06-08 21:15:03', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1534524451877928962', '0', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '留言板评论2', '2022-06-08 21:15:14', '2022-06-08 21:51:33', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1534525521958100993', '1534523725411893250', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'true', '2022-06-08 21:19:29', '2022-06-15 17:38:31', 'MESSAGE_BOARD_LIKES', 1, NULL, 1, 0),
	('1534526121278005249', '1532737683918798850', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'true', '2022-06-08 21:21:52', '2022-06-08 21:21:52', 'BLOG_INFO_COMMENT_LIKES', 1, '1528008410976800770', 1, 0),
	('1534533559247032322', '1534524451877928962', '1530907221067931649', NULL, '192.168.1.7', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, 'false', '2022-06-08 21:51:25', '2022-06-08 21:51:32', 'MESSAGE_BOARD_LIKES', 1, NULL, 1, 0),
	('1535958455441227778', '0', NULL, 'b1bd7743106c5abba2c10a5b5c53e2e8', '192.168.1.7', 'ae3ab06af9307c3534e739d256b82fdc', '', '', NULL, 'true', '2022-06-12 20:13:27', '2022-06-12 20:13:27', 'BLOG_INFO_LIKES', 1, '1528008410976800770', 1, 21),
	('1538089363944382465', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '12121', '2022-06-18 17:20:55', '2022-06-18 17:20:55', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538089512510824449', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '121', '2022-06-18 17:21:31', '2022-06-18 17:21:31', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538095779597946881', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '111', '2022-06-18 17:46:25', '2022-06-18 17:46:25', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538096117558185987', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '111', '2022-06-18 17:47:46', '2022-06-18 17:47:46', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538096195769372674', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '11', '2022-06-18 17:48:04', '2022-06-18 17:48:04', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538096298122973185', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '11', '2022-06-18 17:48:29', '2022-06-18 17:48:29', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538096342280605698', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '111', '2022-06-18 17:48:39', '2022-06-18 17:48:39', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538096446240624643', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '121', '2022-06-18 17:49:04', '2022-06-18 17:49:04', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538096797647802369', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '121', '2022-06-18 17:50:28', '2022-06-18 17:50:28', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538097960434700290', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '121', '2022-06-18 17:55:05', '2022-06-18 17:55:05', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538098027300294657', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '2121', '2022-06-18 17:55:21', '2022-06-18 17:55:21', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538098080685395971', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '2121', '2022-06-18 17:55:34', '2022-06-18 17:55:34', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538098146498220034', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '2121', '2022-06-18 17:55:49', '2022-06-18 17:55:49', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538099691302338562', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '12121', '2022-06-18 18:01:58', '2022-06-18 18:01:58', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538099725993426946', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '212121', '2022-06-18 18:02:06', '2022-06-18 18:02:06', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538099753558392834', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特123', NULL, '2121', '2022-06-18 18:02:12', '2022-06-18 18:02:12', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538101103243476993', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特', NULL, '212121', '2022-06-18 18:07:34', '2022-06-18 18:07:34', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538101538331213825', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特', NULL, '2121', '2022-06-18 18:09:18', '2022-06-18 18:09:18', 'MESSAGE_BOARD_MESSAGE', 0, NULL, 1, 0),
	('1538101651225100290', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特', NULL, '2121', '2022-06-18 18:09:45', '2022-06-18 18:09:45', 'BLOG_INFO_MESSAGE', 0, '1538092841412538370', 1, 0),
	('1538101887108562946', '0', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特', NULL, '21212', '2022-06-18 18:10:41', '2022-06-18 18:10:41', 'BLOG_INFO_MESSAGE', 0, '1538092841412538370', 1, 0),
	('1538101908335935489', '1538101887108562946', '1530907221067931649', NULL, '192.168.14.49', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', '李斯特', '李斯特', '212121', '2022-06-18 18:10:46', '2022-06-18 18:10:46', 'BLOG_INFO_MESSAGE', 0, '1538092841412538370', 1, 0);
/*!40000 ALTER TABLE `t_comment` ENABLE KEYS */;

-- 导出  表 mango_blog.t_dict 结构
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE IF NOT EXISTS `t_dict` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `dict_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '字典名称',
  `summary` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='字典表';

-- 正在导出表  mango_blog.t_dict 的数据：~9 rows (大约)
DELETE FROM `t_dict`;
/*!40000 ALTER TABLE `t_dict` DISABLE KEYS */;
INSERT INTO `t_dict` (`id`, `dict_name`, `summary`, `is_deleted`, `create_time`, `update_time`, `sort`) VALUES
	('1506155694696247297', 'sys_yes_no', '系统是否', 0, '2022-03-22 14:27:55', '2022-03-22 14:27:55', 0),
	('1506174838367870978', 'sys_menu_type', '菜单类型', 0, '2022-03-22 15:43:59', '2022-03-22 15:44:07', 0),
	('1506640128227151874', 'sys_status', '状态', 0, '2022-03-23 22:32:53', '2022-03-23 22:32:53', 0),
	('1506648391412879362', 'sys_sort_type', '分类级别', 1, '2022-03-23 23:05:43', '2022-03-23 23:05:43', 0),
	('1507909943558524929', 'sys_gender', '性别', 0, '2022-03-27 10:38:41', '2022-03-27 10:38:41', 0),
	('1507913784651722753', 'sys_link_status', '友链状态', 0, '2022-03-27 10:53:57', '2022-03-27 10:53:57', 0),
	('1514209682880995330', 'sys_comment_type', '评论类型', 0, '2022-04-13 19:51:36', '2022-04-13 19:51:36', 0),
	('1514209728137535490', 'sys_comment_source', '评论来源', 0, '2022-04-13 19:51:47', '2022-04-13 19:51:47', 0),
	('1516045589821116417', 'sys_blog_level', '博客推荐级别', 0, '2022-04-18 21:26:50', '2022-04-18 21:26:50', 0);
/*!40000 ALTER TABLE `t_dict` ENABLE KEYS */;

-- 导出  表 mango_blog.t_dict_detail 结构
DROP TABLE IF EXISTS `t_dict_detail`;
CREATE TABLE IF NOT EXISTS `t_dict_detail` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `dict_id` varchar(255) DEFAULT NULL COMMENT '字典类型ID',
  `dict_label` varchar(255) DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典键值',
  `css_class` varchar(255) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(255) DEFAULT NULL COMMENT '表格回显样式',
  `summary` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='字典详细数据表';

-- 正在导出表  mango_blog.t_dict_detail 的数据：~26 rows (大约)
DELETE FROM `t_dict_detail`;
/*!40000 ALTER TABLE `t_dict_detail` DISABLE KEYS */;
INSERT INTO `t_dict_detail` (`id`, `dict_id`, `dict_label`, `dict_value`, `css_class`, `list_class`, `summary`, `is_deleted`, `create_time`, `update_time`, `sort`) VALUES
	('1506162082013065218', '1506155694696247297', '是', '1', NULL, 'success', '系统是否 是', 0, '2022-03-22 14:53:18', '2022-03-22 16:17:49', 2),
	('1506171019126923265', '1506155694696247297', '否', '0', NULL, 'warning', '系统是否 否', 0, '2022-03-22 15:28:49', '2022-03-22 15:35:13', 1),
	('1506196718315483137', '1506174838367870978', '目录', '0', NULL, 'default', '菜单类型 目录', 0, '2022-03-22 17:10:56', '2022-03-22 17:19:19', 3),
	('1506198945411215362', '1506174838367870978', '菜单', '1', NULL, 'success', '菜单类型 菜单', 0, '2022-03-22 17:19:47', '2022-03-22 17:19:47', 2),
	('1506199049916493825', '1506174838367870978', '按钮', '2', '', 'warning', '菜单类型 按钮', 0, '2022-03-22 17:20:12', '2022-03-22 17:20:23', 1),
	('1506640400328429570', '1506640128227151874', '禁用', '0', NULL, 'danger', '系统状态 禁用', 0, '2022-03-23 22:33:58', '2022-04-09 09:33:51', 1),
	('1506640493521670145', '1506640128227151874', '启用', '1', NULL, 'success', '系统状态 启用', 0, '2022-03-23 22:34:20', '2022-04-09 09:33:47', 2),
	('1506648554332229633', '1506648391412879362', '一级分类', '1', NULL, 'success', '分类级别 一级', 1, '2022-03-23 23:06:22', '2022-03-23 23:07:29', 3),
	('1506648617326481409', '1506648391412879362', '二级分类', '2', NULL, 'primary', '分类级别 二级', 1, '2022-03-23 23:06:37', '2022-03-23 23:07:10', 2),
	('1506648738428620801', '1506648391412879362', '三级分类', '3', NULL, 'info', '分类级别 三级', 1, '2022-03-23 23:07:06', '2022-03-23 23:07:06', 1),
	('1507910063435927554', '1507909943558524929', '男', '1', NULL, 'success', '性别 男', 0, '2022-03-27 10:39:09', '2022-03-27 10:39:31', 2),
	('1507910141563228162', '1507909943558524929', '女', '2', NULL, 'danger', '性别 女', 0, '2022-03-27 10:39:28', '2022-03-27 10:39:35', 1),
	('1507913903639932929', '1507913784651722753', '申请中', '0', NULL, 'default', '友链状态 申请中', 0, '2022-03-27 10:54:25', '2022-03-27 10:54:58', 1),
	('1507914027598393346', '1507913784651722753', '已上线', '1', NULL, 'success', '友链状态 已上线', 0, '2022-03-27 10:54:55', '2022-03-27 10:54:55', 2),
	('1507914160700436482', '1507913784651722753', '已下架', '2', NULL, 'danger', '友链状态 已下架', 1, '2022-03-27 10:55:26', '2022-03-27 10:55:26', 3),
	('1514210064461996034', '1514209682880995330', '点赞', '1', NULL, 'success', '评论类型 点赞', 0, '2022-04-13 19:53:07', '2022-04-13 19:53:07', 999),
	('1514210160368951298', '1514209682880995330', '评论', '0', NULL, 'warning', '评论类型 评论', 0, '2022-04-13 19:53:30', '2022-04-13 19:53:34', 999),
	('1514210349318152193', '1514209728137535490', '博客详情页面点赞', 'BLOG_INFO_LIKES', NULL, 'success', '评论来源 博客详情页面点赞', 0, '2022-04-13 19:54:15', '2022-06-15 16:23:46', 999),
	('1514210482151759873', '1514209728137535490', '博客详情页面评论', 'BLOG_INFO_MESSAGE', NULL, 'warning', '评论来源 博客详情页面评论', 0, '2022-04-13 19:54:46', '2022-06-15 16:24:13', 999),
	('1514210661902852098', '1514209728137535490', '博客详情页面评论点赞', 'BLOG_INFO_COMMENT_LIKES', NULL, 'primary', '评论来源 博客详情页面评论点赞', 0, '2022-04-13 19:55:29', '2022-06-15 16:24:35', 999),
	('1516045760239882241', '1516045589821116417', '正常', '0', NULL, 'default', '博客推荐级别 正常', 0, '2022-04-18 21:27:31', '2022-04-18 21:27:31', 999),
	('1516045852984332289', '1516045589821116417', '一级推荐', '1', NULL, 'success', '博客推荐级别 一级推荐', 0, '2022-04-18 21:27:53', '2022-04-18 21:27:58', 999),
	('1516045939999363073', '1516045589821116417', '二级推荐', '2', NULL, 'warning', '博客推荐级别 二级推荐', 0, '2022-04-18 21:28:14', '2022-04-18 21:29:20', 999),
	('1516045995942989826', '1516045589821116417', '三级推荐', '3', NULL, 'danger', '博客推荐级别 三级推荐', 0, '2022-04-18 21:28:27', '2022-04-18 21:29:23', 999),
	('1536988114884931585', '1514209728137535490', '留言板页面点赞', 'MESSAGE_BOARD_LIKES', NULL, 'info', '评论来源 留言板页面点赞', 0, '2022-06-15 16:24:57', '2022-06-15 16:24:57', 999),
	('1536988229020332033', '1514209728137535490', '留言板页面评论', 'MESSAGE_BOARD_MESSAGE', NULL, 'default', '评论来源 留言板页面评论', 0, '2022-06-15 16:25:24', '2022-06-15 16:25:24', 999);
/*!40000 ALTER TABLE `t_dict_detail` ENABLE KEYS */;

-- 导出  表 mango_blog.t_email_config 结构
DROP TABLE IF EXISTS `t_email_config`;
CREATE TABLE IF NOT EXISTS `t_email_config` (
  `id` varchar(19) NOT NULL COMMENT 'ID',
  `from_user` varchar(255) DEFAULT NULL COMMENT '发件人',
  `host` varchar(255) DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `port` int DEFAULT NULL COMMENT '端口',
  `user` varchar(255) DEFAULT NULL COMMENT '发件者用户名',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮件主题',
  `source` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'ADMIN' COMMENT '邮件来源：ADMIN-后台管理系统，WEB-门户网站',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='邮箱配置';

-- 正在导出表  mango_blog.t_email_config 的数据：~2 rows (大约)
DELETE FROM `t_email_config`;
/*!40000 ALTER TABLE `t_email_config` DISABLE KEYS */;
INSERT INTO `t_email_config` (`id`, `from_user`, `host`, `password`, `port`, `user`, `subject`, `source`, `create_time`, `update_time`) VALUES
	('1595268474480156679', 'mango_blog@163.com', 'smtp.163.com', 'WVNMVElMUk9OVlJDSlNOTw==', 25, 'mango_blog', 'MANGO_BLOG后台管理系统', 'ADMIN', '2022-05-28 21:49:13', '2022-05-28 21:49:14'),
	('1595268474480156680', 'mango_blog@163.com', 'smtp.163.com', 'WVNMVElMUk9OVlJDSlNOTw==', 25, 'mango_blog', 'MANGO_BLOG门户网站', 'WEB', '2022-05-28 21:49:31', '2022-05-28 21:49:32');
/*!40000 ALTER TABLE `t_email_config` ENABLE KEYS */;

-- 导出  表 mango_blog.t_exception_log 结构
DROP TABLE IF EXISTS `t_exception_log`;
CREATE TABLE IF NOT EXISTS `t_exception_log` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `exception_json` mediumtext COMMENT '异常对象json格式',
  `exception_message` mediumtext COMMENT '异常信息',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `ip` varchar(20) DEFAULT NULL COMMENT 'ip地址',
  `ip_source` varchar(100) DEFAULT NULL COMMENT 'ip来源',
  `method` varchar(255) DEFAULT NULL COMMENT '请求方法',
  `operation` varchar(100) DEFAULT NULL COMMENT '方法描述',
  `params` longtext COMMENT '请求参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='异常信息表';

-- 正在导出表  mango_blog.t_exception_log 的数据：~0 rows (大约)
DELETE FROM `t_exception_log`;
/*!40000 ALTER TABLE `t_exception_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_exception_log` ENABLE KEYS */;

-- 导出  表 mango_blog.t_link 结构
DROP TABLE IF EXISTS `t_link`;
CREATE TABLE IF NOT EXISTS `t_link` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `title` varchar(255) DEFAULT NULL COMMENT '友情链接标题',
  `summary` varchar(255) DEFAULT NULL COMMENT '友情链接介绍',
  `url` varchar(255) DEFAULT NULL COMMENT '友情链接URL',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `link_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '友链状态： 0 申请中， 1：已上线',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='友情链接表';

-- 正在导出表  mango_blog.t_link 的数据：~2 rows (大约)
DELETE FROM `t_link`;
/*!40000 ALTER TABLE `t_link` DISABLE KEYS */;
INSERT INTO `t_link` (`id`, `title`, `summary`, `url`, `email`, `create_time`, `update_time`, `link_status`) VALUES
	('1507920187256823810', '测试2', '123', 'https://www.yanshisan.cn/1', '872174823@qq.com', '2022-03-27 11:19:23', '2022-05-15 17:34:17', 1),
	('1525771593183432706', '测试2', '123', 'https://www.yanshisan.cn/12', '872174823@qq.com', '2022-05-15 17:34:30', '2022-06-15 16:13:15', 1);
/*!40000 ALTER TABLE `t_link` ENABLE KEYS */;

-- 导出  表 mango_blog.t_menu 结构
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE IF NOT EXISTS `t_menu` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单标题',
  `pid` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父id',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除 1:是 0:否',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `hidden` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏 1:是 0:否',
  `component` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '组件名称',
  `path` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'url地址',
  `redirect` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '重定向地址',
  `permission` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限标识',
  `menu_type` tinyint(1) DEFAULT NULL COMMENT '菜单类型 0:目录 1:菜单 2:按钮',
  `sub_count` tinyint(1) NOT NULL DEFAULT '0' COMMENT '子菜单数量，默认0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='菜单表';

-- 正在导出表  mango_blog.t_menu 的数据：~43 rows (大约)
DELETE FROM `t_menu`;
/*!40000 ALTER TABLE `t_menu` DISABLE KEYS */;
INSERT INTO `t_menu` (`id`, `name`, `title`, `pid`, `icon`, `sort`, `is_deleted`, `create_time`, `update_time`, `hidden`, `component`, `path`, `redirect`, `permission`, `menu_type`, `sub_count`) VALUES
	('1195268474480156673', '权限管理', '权限管理', '0', 'authority', 998, 0, '2021-10-07 21:49:36', '2022-03-27 23:04:19', 0, 'Layout', '/authority', NULL, '', 0, 5),
	('1195268474480156674', '管理员管理', '管理员管理', '1195268474480156673', 'peoples', 999, 0, '2021-10-07 22:38:13', '2022-05-15 22:18:03', 0, 'authority/admin', 'admin', NULL, 'admin:list', 1, 4),
	('1195268474480156675', '菜单管理', '菜单管理', '1195268474480156673', 'authority', 998, 0, '2021-10-07 22:50:12', '2022-05-15 22:18:10', 0, 'authority/menu', 'menu', NULL, 'menu:list', 1, 4),
	('1195268474480156676', '添加菜单', '新增菜单', '1195268474480156675', NULL, 999, 0, '2021-12-04 22:16:29', '2022-05-15 22:18:56', 0, NULL, NULL, NULL, 'menu:add', 2, 0),
	('1195268474480156677', '博客管理', '博客管理', '0', 'blog', 999, 0, '2022-03-07 14:11:22', '2022-03-27 23:04:05', 0, 'Layout', '/blog', NULL, '', 0, 4),
	('1195268474480156678', '博客管理', '博客管理', '1195268474480156677', 'blog', 999, 0, '2022-03-07 14:20:01', '2022-05-15 22:17:33', 0, 'blog/blog', 'blog', NULL, 'blog:list', 1, 4),
	('1195268474480156679', '分类管理', '分类管理', '1195268474480156677', 'sort', 997, 0, '2022-03-07 14:55:30', '2022-05-15 22:17:45', 0, 'blog/sort', 'sort', NULL, 'sort:list', 1, 4),
	('1501368407743528962', '系统管理', '系统管理', '0', 'system', 997, 0, '2022-03-09 09:24:57', '2022-03-27 23:04:25', 0, 'Layout', '/system', NULL, NULL, 0, 5),
	('1501369279194071042', '字典管理', '字典管理', '1501368407743528962', 'authority', 999, 0, '2022-03-09 09:28:25', '2022-03-22 13:40:06', 0, 'system/dict', 'dict', NULL, 'dict:list', 1, 4),
	('1501369997368082434', '删除菜单', '删除菜单', '1195268474480156675', NULL, 997, 0, '2022-03-09 09:31:16', '2022-05-15 22:17:01', 0, NULL, NULL, NULL, 'menu:del', 2, 0),
	('1501483509264281601', '修改菜单', '编辑菜单', '1195268474480156675', NULL, 998, 0, '2022-03-09 17:02:19', '2022-05-15 22:19:03', 0, NULL, NULL, NULL, 'menu:edit', 2, 0),
	('1501490155294052354', '角色管理', '角色管理', '1195268474480156673', 'adminRole', 997, 0, '2022-03-09 17:28:44', '2022-05-15 22:17:57', 0, 'authority/role', 'role', NULL, 'role:list', 1, 5),
	('1503383805083586562', '添加管理员', '新增管理员', '1195268474480156674', NULL, 999, 0, '2022-03-14 22:53:25', '2022-05-15 22:13:16', 0, NULL, NULL, NULL, 'admin:add', 2, 0),
	('1504633021835083777', '标签管理', '标签管理', '1195268474480156677', 'tag', 998, 0, '2022-03-18 09:37:22', '2022-05-15 22:17:39', 0, 'blog/tag', 'tag', NULL, 'tag:list', 1, 4),
	('1506144477000859650', '友情链接', '友情链接', '1501368407743528962', 'link', 999, 0, '2022-03-22 13:43:21', '2022-03-22 13:43:21', 0, 'system/link', 'link', NULL, 'link:list', 1, 4),
	('1508097474904268802', '系统监控', '系统监控', '0', 'monitor', 996, 0, '2022-03-27 23:08:23', '2022-03-27 23:08:23', 0, 'Layout', '/monitor', NULL, NULL, 0, 3),
	('1508097940568481794', '服务监控', '服务监控', '1508097474904268802', 'dashboard', 999, 0, '2022-03-27 23:10:30', '2022-03-27 23:10:30', 0, 'monitor/server', 'server', NULL, 'server:list', 1, 0),
	('1508812944326385665', 'druid监控', 'druid监控', '1508097474904268802', 'druid', 999, 0, '2022-03-29 22:26:53', '2022-03-29 22:26:53', 0, 'monitor/druid', 'druid', NULL, 'druid:list', 1, 0),
	('1513015571486470146', '评论管理', '评论管理', '1501368407743528962', 'message', 999, 0, '2022-04-10 12:46:37', '2022-04-16 08:19:58', 0, 'system/comment', 'comment', NULL, 'comment:list', 1, 2),
	('1515127581015547905', '门户访问记录管理', '门户访问记录管理', '1501368407743528962', 'web', 999, 0, '2022-04-16 08:39:00', '2022-04-16 17:29:20', 0, 'system/webVisit', 'webVisit', NULL, 'webVisit:list', 1, 2),
	('1525841882105839617', '新增标签', '新增标签', '1504633021835083777', NULL, 999, 0, '2022-05-15 22:13:48', '2022-05-15 22:13:48', 0, NULL, NULL, NULL, 'tag:add', 2, 0),
	('1525841954847653889', '编辑标签', '编辑标签', '1504633021835083777', NULL, 998, 0, '2022-05-15 22:14:05', '2022-05-15 22:14:05', 0, NULL, NULL, NULL, 'tag:edit', 2, 0),
	('1525842038595321857', '删除标签', '删除标签', '1504633021835083777', NULL, 997, 0, '2022-05-15 22:14:25', '2022-05-15 22:14:25', 0, NULL, NULL, NULL, 'tag:del', 2, 0),
	('1525842246624411649', '新增分类', '新增分类', '1195268474480156679', NULL, 999, 0, '2022-05-15 22:15:15', '2022-05-15 22:15:15', 0, NULL, NULL, NULL, 'sort:add', 2, 0),
	('1525842314769268737', '编辑分类', '编辑分类', '1195268474480156679', NULL, 998, 0, '2022-05-15 22:15:31', '2022-05-15 22:15:31', 0, NULL, NULL, NULL, 'sort:edit', 2, 0),
	('1525842371665002498', '删除分类', '删除分类', '1195268474480156679', NULL, 997, 0, '2022-05-15 22:15:45', '2022-05-15 22:15:45', 0, NULL, NULL, NULL, 'sort:del', 2, 0),
	('1525842436408279041', '新增博客', '新增博客', '1195268474480156678', NULL, 999, 0, '2022-05-15 22:16:00', '2022-05-15 22:16:00', 0, NULL, NULL, NULL, 'blog:add', 2, 0),
	('1525842506499293186', '编辑博客', '编辑博客', '1195268474480156678', NULL, 998, 0, '2022-05-15 22:16:17', '2022-05-15 22:16:17', 0, NULL, NULL, NULL, 'blog:edit', 2, 0),
	('1525842561776025601', '删除博客', '删除博客', '1195268474480156678', NULL, 997, 0, '2022-05-15 22:16:30', '2022-05-15 22:16:30', 0, NULL, NULL, NULL, 'blog:del', 2, 0),
	('1525843063003742209', '编辑管理员', '编辑管理员', '1195268474480156674', NULL, 998, 0, '2022-05-15 22:18:30', '2022-05-15 22:18:30', 0, NULL, NULL, NULL, 'admin:edit', 2, 0),
	('1525843118246920193', '删除管理员', '删除管理员', '1195268474480156674', NULL, 997, 0, '2022-05-15 22:18:43', '2022-05-15 22:18:43', 0, NULL, NULL, NULL, 'admin:del', 2, 0),
	('1525843281111744514', '新增角色', '新增角色', '1501490155294052354', NULL, 999, 0, '2022-05-15 22:19:22', '2022-05-15 22:19:22', 0, NULL, NULL, NULL, 'role:add', 2, 0),
	('1525843351437639681', '编辑角色', '编辑角色', '1501490155294052354', NULL, 998, 0, '2022-05-15 22:19:38', '2022-05-15 22:19:38', 0, NULL, NULL, NULL, 'role:edit', 2, 0),
	('1525843407997829121', '删除角色', '删除角色', '1501490155294052354', NULL, 997, 0, '2022-05-15 22:19:52', '2022-05-15 22:19:52', 0, NULL, NULL, NULL, 'role:del', 2, 0),
	('1525843528609234945', '删除评论', '删除评论', '1513015571486470146', NULL, 999, 0, '2022-05-15 22:20:21', '2022-05-15 22:20:21', 0, NULL, NULL, NULL, 'comment:del', 2, 0),
	('1525843884231688194', '新增字典', '新增字典', '1501369279194071042', NULL, 999, 0, '2022-05-15 22:21:45', '2022-05-15 22:21:45', 0, NULL, NULL, NULL, 'dict:add', 2, 0),
	('1525843954922487810', '编辑字典', '编辑字典', '1501369279194071042', NULL, 998, 0, '2022-05-15 22:22:02', '2022-05-15 22:22:02', 0, NULL, NULL, NULL, 'dict:edit', 2, 0),
	('1525844040964440065', '删除字典', '删除字典', '1501369279194071042', NULL, 997, 0, '2022-05-15 22:22:23', '2022-05-15 22:22:23', 0, NULL, NULL, NULL, 'dict:del', 2, 0),
	('1525844298687643650', '新增友链', '新增友链', '1506144477000859650', NULL, 999, 0, '2022-05-15 22:23:24', '2022-05-15 22:23:24', 0, NULL, NULL, NULL, 'link:add', 2, 0),
	('1525844355809869826', '编辑友链', '编辑友链', '1506144477000859650', NULL, 998, 0, '2022-05-15 22:23:38', '2022-05-15 22:23:38', 0, NULL, NULL, NULL, 'link:edit', 2, 0),
	('1525844427620548610', '删除友链', '删除友链', '1506144477000859650', NULL, 997, 0, '2022-05-15 22:23:55', '2022-05-15 22:23:55', 0, NULL, NULL, NULL, 'link:del', 2, 0),
	('1525844539302281217', '绑定菜单', '绑定菜单', '1501490155294052354', NULL, 996, 1, '2022-05-15 22:24:22', '2022-05-15 22:24:22', 0, NULL, NULL, NULL, 'role:menu', 2, 0),
	('1525844698102824962', '删除记录', '删除记录', '1515127581015547905', NULL, 999, 0, '2022-05-15 22:25:00', '2022-05-15 22:25:00', 0, NULL, NULL, NULL, 'webVisit:del', 2, 0),
	('1538071195570028546', '用户管理', '用户管理', '1195268474480156673', 'people', 999, 0, '2022-06-18 16:08:44', '2022-06-18 16:10:26', 0, 'authority/user', 'user', NULL, 'user:list', 1, 0);
/*!40000 ALTER TABLE `t_menu` ENABLE KEYS */;

-- 导出  表 mango_blog.t_picture 结构
DROP TABLE IF EXISTS `t_picture`;
CREATE TABLE IF NOT EXISTS `t_picture` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `file_id` varchar(255) DEFAULT NULL COMMENT '图片id',
  `pic_name` varchar(255) DEFAULT NULL COMMENT '图片名',
  `picture_sort_id` int DEFAULT NULL COMMENT '分类id',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='图片表';

-- 正在导出表  mango_blog.t_picture 的数据：~0 rows (大约)
DELETE FROM `t_picture`;
/*!40000 ALTER TABLE `t_picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_picture` ENABLE KEYS */;

-- 导出  表 mango_blog.t_role 结构
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `summary` varchar(255) DEFAULT NULL COMMENT '角色介绍',
  `level` int unsigned DEFAULT NULL COMMENT '级别，数值越小，级别越大',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色表';

-- 正在导出表  mango_blog.t_role 的数据：~2 rows (大约)
DELETE FROM `t_role`;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` (`id`, `role_name`, `create_time`, `update_time`, `summary`, `level`) VALUES
	('1395268474480156673', 'admin', '2021-10-02 22:52:24', '2022-04-06 22:58:14', '管理员', 1),
	('1395268474480156674', 'visitor', '2021-12-02 21:58:50', '2022-05-15 17:38:20', '游客', 2);
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;

-- 导出  表 mango_blog.t_role_admin 结构
DROP TABLE IF EXISTS `t_role_admin`;
CREATE TABLE IF NOT EXISTS `t_role_admin` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `role_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色ID',
  `admin_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ROLE_ADMIN_UK` (`role_id`,`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色管理员中间表';

-- 正在导出表  mango_blog.t_role_admin 的数据：~2 rows (大约)
DELETE FROM `t_role_admin`;
/*!40000 ALTER TABLE `t_role_admin` DISABLE KEYS */;
INSERT INTO `t_role_admin` (`id`, `role_id`, `admin_id`, `create_time`, `update_time`) VALUES
	('1512987650810048514', '1395268474480156673', '1295268474480156673', '2022-04-10 10:55:41', '2022-04-10 10:55:41'),
	('1525754355122515970', '1395268474480156674', '1595268474480156674', '2022-05-15 16:26:00', '2022-05-15 16:26:00');
/*!40000 ALTER TABLE `t_role_admin` ENABLE KEYS */;

-- 导出  表 mango_blog.t_role_menu 结构
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE IF NOT EXISTS `t_role_menu` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `role_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色ID',
  `menu_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ROLE_MENU_UK` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  mango_blog.t_role_menu 的数据：~61 rows (大约)
DELETE FROM `t_role_menu`;
/*!40000 ALTER TABLE `t_role_menu` DISABLE KEYS */;
INSERT INTO `t_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`) VALUES
	('1507906766557794306', '1502546893753643010', '1501368407743528962', '2022-03-27 10:26:03', '2022-03-27 10:26:03'),
	('1507906766822035458', '1502546893753643010', '1501369279194071042', '2022-03-27 10:26:03', '2022-03-27 10:26:03'),
	('1507906767212105729', '1502546893753643010', '1506144477000859650', '2022-03-27 10:26:04', '2022-03-27 10:26:04'),
	('1526015264132853761', '1395268474480156674', '1195268474480156677', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264141242369', '1395268474480156674', '1195268474480156678', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264149630977', '1395268474480156674', '1195268474480156679', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264158019585', '1395268474480156674', '1504633021835083777', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264166408194', '1395268474480156674', '1195268474480156673', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264174796802', '1395268474480156674', '1195268474480156674', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264178991106', '1395268474480156674', '1195268474480156675', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264187379714', '1395268474480156674', '1501490155294052354', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264195768321', '1395268474480156674', '1501368407743528962', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264204156929', '1395268474480156674', '1501369279194071042', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264204156930', '1395268474480156674', '1506144477000859650', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264212545537', '1395268474480156674', '1513015571486470146', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264220934145', '1395268474480156674', '1515127581015547905', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264229322754', '1395268474480156674', '1508097474904268802', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264237711361', '1395268474480156674', '1508097940568481794', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1526015264246099969', '1395268474480156674', '1508812944326385665', '2022-05-16 09:42:46', '2022-05-16 09:42:46'),
	('1538071224296816642', '1395268474480156673', '1195268474480156673', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224313593858', '1395268474480156673', '1195268474480156674', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224317788161', '1395268474480156673', '1195268474480156675', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224326176769', '1395268474480156673', '1195268474480156676', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224342953985', '1395268474480156673', '1195268474480156677', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224351342593', '1395268474480156673', '1195268474480156678', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224359731201', '1395268474480156673', '1195268474480156679', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224372314113', '1395268474480156673', '1501368407743528962', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224380702721', '1395268474480156673', '1501369279194071042', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224389091330', '1395268474480156673', '1501369997368082434', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224397479938', '1395268474480156673', '1501483509264281601', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224401674242', '1395268474480156673', '1501490155294052354', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224418451457', '1395268474480156673', '1503383805083586562', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224426840066', '1395268474480156673', '1504633021835083777', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224435228674', '1395268474480156673', '1506144477000859650', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224443617281', '1395268474480156673', '1508097474904268802', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224452005889', '1395268474480156673', '1508097940568481794', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224460394498', '1395268474480156673', '1508812944326385665', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224472977409', '1395268474480156673', '1513015571486470146', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224481366017', '1395268474480156673', '1515127581015547905', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224489754626', '1395268474480156673', '1525841882105839617', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224498143233', '1395268474480156673', '1525841954847653889', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224506531842', '1395268474480156673', '1525842038595321857', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224514920450', '1395268474480156673', '1525842246624411649', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224531697665', '1395268474480156673', '1525842314769268737', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224540086274', '1395268474480156673', '1525842371665002498', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224548474881', '1395268474480156673', '1525842436408279041', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224556863490', '1395268474480156673', '1525842506499293186', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224565252097', '1395268474480156673', '1525842561776025601', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224573640706', '1395268474480156673', '1525843063003742209', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224582029314', '1395268474480156673', '1525843118246920193', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224590417921', '1395268474480156673', '1525843281111744514', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224598806530', '1395268474480156673', '1525843351437639681', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224607195138', '1395268474480156673', '1525843407997829121', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224623972353', '1395268474480156673', '1525843528609234945', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224636555265', '1395268474480156673', '1525843884231688194', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224640749570', '1395268474480156673', '1525843954922487810', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224657526785', '1395268474480156673', '1525844040964440065', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224665915394', '1395268474480156673', '1525844298687643650', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224674304001', '1395268474480156673', '1525844355809869826', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224682692609', '1395268474480156673', '1525844427620548610', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224691081218', '1395268474480156673', '1525844698102824962', '2022-06-18 16:08:51', '2022-06-18 16:08:51'),
	('1538071224699469826', '1395268474480156673', '1538071195570028546', '2022-06-18 16:08:51', '2022-06-18 16:08:51');
/*!40000 ALTER TABLE `t_role_menu` ENABLE KEYS */;

-- 导出  表 mango_blog.t_sort 结构
DROP TABLE IF EXISTS `t_sort`;
CREATE TABLE IF NOT EXISTS `t_sort` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `sort_name` varchar(255) DEFAULT NULL COMMENT '分类内容',
  `summary` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `click_count` int DEFAULT '0' COMMENT '点击数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客分类表';

-- 正在导出表  mango_blog.t_sort 的数据：~4 rows (大约)
DELETE FROM `t_sort`;
/*!40000 ALTER TABLE `t_sort` DISABLE KEYS */;
INSERT INTO `t_sort` (`id`, `sort_name`, `summary`, `create_time`, `update_time`, `click_count`) VALUES
	('1505006369916694530', '测试', '测试', '2022-03-19 10:20:55', '2022-03-19 10:27:49', 0),
	('1506662963603652609', '测试', NULL, '2022-03-24 00:03:38', '2022-05-15 16:46:01', 0),
	('1506663003336294401', '测试3', NULL, '2022-03-24 00:03:47', '2022-03-24 00:03:47', 0),
	('1507905421998137346', 'test', '666', '2022-03-27 10:20:43', '2022-03-27 10:23:28', 0);
/*!40000 ALTER TABLE `t_sort` ENABLE KEYS */;

-- 导出  表 mango_blog.t_tag 结构
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `summary` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签描述',
  `click_count` int NOT NULL DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `tag_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='标签表';

-- 正在导出表  mango_blog.t_tag 的数据：~3 rows (大约)
DELETE FROM `t_tag`;
/*!40000 ALTER TABLE `t_tag` DISABLE KEYS */;
INSERT INTO `t_tag` (`id`, `summary`, `click_count`, `create_time`, `update_time`, `tag_name`) VALUES
	('1506663317590327298', NULL, 0, '2022-03-24 00:05:02', '2022-05-15 18:01:44', '测试1'),
	('1507906192298856450', NULL, 0, '2022-03-27 10:23:46', '2022-03-27 10:23:50', '123'),
	('1525798034717634562', '123', 0, '2022-05-15 19:19:34', '2022-05-15 19:19:34', '1231');
/*!40000 ALTER TABLE `t_tag` ENABLE KEYS */;

-- 导出  表 mango_blog.t_todo 结构
DROP TABLE IF EXISTS `t_todo`;
CREATE TABLE IF NOT EXISTS `t_todo` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `admin_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '管理员id',
  `text` varchar(255) DEFAULT NULL COMMENT '内容',
  `done` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '表示事项是否完成（0：未完成 1：已完成）',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='待办事项表';

-- 正在导出表  mango_blog.t_todo 的数据：~0 rows (大约)
DELETE FROM `t_todo`;
/*!40000 ALTER TABLE `t_todo` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_todo` ENABLE KEYS */;

-- 导出  表 mango_blog.t_user 结构
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `gender` tinyint unsigned DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '个人头像',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机',
  `login_count` int unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态：0-禁用; 1-激活',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `qq_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信号',
  `comment_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '评论状态 1:正常 0:禁言',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

-- 正在导出表  mango_blog.t_user 的数据：~2 rows (大约)
DELETE FROM `t_user`;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` (`id`, `username`, `password`, `gender`, `avatar`, `birthday`, `email`, `mobile`, `login_count`, `last_login_time`, `last_login_ip`, `status`, `create_time`, `update_time`, `nickname`, `qq_number`, `we_chat`, `comment_status`) VALUES
	('1530907221067931649', '李斯特', '$2a$10$mRyGRbLPkiAblV11ATLGDu5sIG5w9.t2uUlSMI2hWvTEKgfHxI9Le', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', NULL, '872174823@qq.com', NULL, 36, '2022-06-18 16:53:43', '192.168.14.49', 1, '2022-05-29 21:41:39', '2022-06-18 18:09:16', '李斯特', NULL, NULL, 1),
	('1531998551454093313', '测试123', '$2a$10$2NllPZWFJv5Gmk.57YhdpOT3Bs0OyIwV0Rl/.QWotW87WTC1v/2s.', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/blog/2022/05/21/369ec60f-846b-4eb2-ab6e-81044b5babdd-test.jpeg', NULL, 'guang1997qqq@163.com', NULL, 2, '2022-06-18 16:53:44', '127.0.0.1', 1, '2022-06-01 21:58:12', '2022-06-03 21:43:02', '测试123', NULL, NULL, 1);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

-- 导出  表 mango_blog.t_web_config 结构
DROP TABLE IF EXISTS `t_web_config`;
CREATE TABLE IF NOT EXISTS `t_web_config` (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `logo` varchar(255) NOT NULL COMMENT 'logo(文件ID)',
  `name` varchar(255) NOT NULL COMMENT '网站名称',
  `summary` varchar(255) NOT NULL COMMENT '介绍',
  `keyword` varchar(255) NOT NULL COMMENT '关键字',
  `author` varchar(255) NOT NULL COMMENT '作者',
  `record_num` varchar(255) NOT NULL COMMENT '备案号',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `ali_pay` varchar(32) DEFAULT NULL COMMENT '支付宝收款码FileId',
  `weixin_pay` varchar(32) DEFAULT NULL COMMENT '微信收款码FileId',
  `github` varchar(255) DEFAULT NULL COMMENT 'github地址',
  `gitee` varchar(255) DEFAULT NULL COMMENT 'gitee地址',
  `qq_number` varchar(20) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `show_list` varchar(255) DEFAULT NULL COMMENT '显示的列表（用于控制邮箱、QQ、Github、Gitee、微信是否显示在前端）',
  `login_type_list` varchar(255) DEFAULT NULL COMMENT '登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）',
  `open_comment` varchar(1) DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `open_admiration` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启赞赏(0:否， 1:是)',
  `link_apply_template` varchar(2021) DEFAULT NULL COMMENT '友链申请模板,添加友链申请模板格式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='web配置表';

-- 正在导出表  mango_blog.t_web_config 的数据：~0 rows (大约)
DELETE FROM `t_web_config`;
/*!40000 ALTER TABLE `t_web_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_web_config` ENABLE KEYS */;

-- 导出  表 mango_blog.t_web_visit 结构
DROP TABLE IF EXISTS `t_web_visit`;
CREATE TABLE IF NOT EXISTS `t_web_visit` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `ip` varchar(255) DEFAULT NULL COMMENT '访问ip地址',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `os` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器',
  `ip_source` varchar(255) DEFAULT NULL COMMENT 'ip来源',
  `behavior` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户行为',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '行为内容',
  `request_time` timestamp NULL DEFAULT NULL COMMENT '请求时间',
  `is_menu` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否是菜单1:是0:否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='Web访问记录表';

-- 正在导出表  mango_blog.t_web_visit 的数据：~6 rows (大约)
DELETE FROM `t_web_visit`;
/*!40000 ALTER TABLE `t_web_visit` DISABLE KEYS */;
INSERT INTO `t_web_visit` (`id`, `ip`, `create_time`, `update_time`, `os`, `browser`, `ip_source`, `behavior`, `content`, `request_time`, `is_menu`) VALUES
	('1537370910296047618', '192.168.14.49', '2022-06-16 17:46:03', '2022-06-16 17:46:03', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-16 17:46:03', 1),
	('1537370949428903938', '192.168.14.49', '2022-06-16 17:46:12', '2022-06-16 17:46:12', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-16 17:46:12', 1),
	('1537371088394584066', '192.168.14.49', '2022-06-16 17:46:45', '2022-06-16 17:46:45', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'ARCHIVE', '点击归档页面', '2022-06-16 17:46:45', 1),
	('1537371248839294978', '192.168.14.49', '2022-06-16 17:47:23', '2022-06-16 17:47:23', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'TAG', '点击博客标签页面', '2022-06-16 17:47:23', 1),
	('1537371249061593090', '192.168.14.49', '2022-06-16 17:47:23', '2022-06-16 17:47:23', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_TAG', '点击标签', '2022-06-18 17:47:23', 0),
	('1537371266212102146', '192.168.14.49', '2022-06-16 17:47:27', '2022-06-16 17:47:27', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-18 17:47:27', 1),
	('1538087720079204353', '192.168.14.49', '2022-06-18 17:14:23', '2022-06-18 17:14:23', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:14:23', 1),
	('1538088163995951105', '192.168.14.49', '2022-06-18 17:16:09', '2022-06-18 17:16:09', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'FRIENDSHIP_LINK', '点击友情链接页面', '2022-06-18 17:16:09', 1),
	('1538089349478227969', '192.168.14.49', '2022-06-18 17:20:52', '2022-06-18 17:20:52', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:20:52', 1),
	('1538089363906633729', '192.168.14.49', '2022-06-18 17:20:55', '2022-06-18 17:20:55', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:20:55', 0),
	('1538089364082794498', '192.168.14.49', '2022-06-18 17:20:55', '2022-06-18 17:20:55', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:20:55', 1),
	('1538089473075978242', '192.168.14.49', '2022-06-18 17:21:21', '2022-06-18 17:21:21', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:21:21', 1),
	('1538089512510824450', '192.168.14.49', '2022-06-18 17:21:31', '2022-06-18 17:21:31', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:21:31', 0),
	('1538089512645042177', '192.168.14.49', '2022-06-18 17:21:31', '2022-06-18 17:21:31', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:21:31', 1),
	('1538089723987632129', '192.168.14.49', '2022-06-18 17:22:21', '2022-06-18 17:22:21', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:22:21', 1),
	('1538091477730992130', '192.168.14.49', '2022-06-18 17:29:19', '2022-06-18 17:29:19', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:29:19', 1),
	('1538091607452426241', '192.168.14.49', '2022-06-18 17:29:50', '2022-06-18 17:29:50', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:29:50', 1),
	('1538092132193411074', '192.168.14.49', '2022-06-18 17:31:55', '2022-06-18 17:31:55', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'ARCHIVE', '点击归档页面', '2022-06-18 17:31:55', 1),
	('1538092877202464769', '192.168.14.49', '2022-06-18 17:34:53', '2022-06-18 17:34:53', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-18 17:34:53', 1),
	('1538093120539205634', '192.168.14.49', '2022-06-18 17:35:51', '2022-06-18 17:35:51', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-18 17:35:51', 1),
	('1538093172812816385', '192.168.14.49', '2022-06-18 17:36:03', '2022-06-18 17:36:03', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'FRIENDSHIP_LINK', '点击友情链接页面', '2022-06-18 17:36:03', 1),
	('1538095567164837889', '192.168.14.49', '2022-06-18 17:45:34', '2022-06-18 17:45:34', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-18 17:45:34', 1),
	('1538095585326174209', '192.168.14.49', '2022-06-18 17:45:39', '2022-06-18 17:45:39', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:45:39', 1),
	('1538095766113259522', '192.168.14.49', '2022-06-18 17:46:22', '2022-06-18 17:46:22', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:46:22', 0),
	('1538095779568586754', '192.168.14.49', '2022-06-18 17:46:25', '2022-06-18 17:46:25', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:46:25', 0),
	('1538095779723776001', '192.168.14.49', '2022-06-18 17:46:25', '2022-06-18 17:46:25', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:46:25', 1),
	('1538096049644015618', '192.168.14.49', '2022-06-18 17:47:29', '2022-06-18 17:47:29', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:47:29', 1),
	('1538096117558185986', '192.168.14.49', '2022-06-18 17:47:46', '2022-06-18 17:47:46', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:47:46', 0),
	('1538096117721763842', '192.168.14.49', '2022-06-18 17:47:46', '2022-06-18 17:47:46', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:47:46', 1),
	('1538096178946019330', '192.168.14.49', '2022-06-18 17:48:00', '2022-06-18 17:48:00', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:48:00', 1),
	('1538096195769372675', '192.168.14.49', '2022-06-18 17:48:04', '2022-06-18 17:48:04', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:48:04', 0),
	('1538096195928756225', '192.168.14.49', '2022-06-18 17:48:04', '2022-06-18 17:48:04', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:48:04', 1),
	('1538096225267912705', '192.168.14.49', '2022-06-18 17:48:11', '2022-06-18 17:48:11', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:48:11', 1),
	('1538096298122973186', '192.168.14.49', '2022-06-18 17:48:29', '2022-06-18 17:48:29', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:48:29', 0),
	('1538096298290745345', '192.168.14.49', '2022-06-18 17:48:29', '2022-06-18 17:48:29', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:48:29', 1),
	('1538096326715543553', '192.168.14.49', '2022-06-18 17:48:35', '2022-06-18 17:48:35', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:48:35', 1),
	('1538096342288994306', '192.168.14.49', '2022-06-18 17:48:39', '2022-06-18 17:48:39', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:48:39', 0),
	('1538096342431600642', '192.168.14.49', '2022-06-18 17:48:39', '2022-06-18 17:48:39', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:48:39', 1),
	('1538096439148056577', '192.168.14.49', '2022-06-18 17:49:02', '2022-06-18 17:49:02', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:49:02', 1),
	('1538096446240624642', '192.168.14.49', '2022-06-18 17:49:04', '2022-06-18 17:49:04', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:49:04', 0),
	('1538096446374842370', '192.168.14.49', '2022-06-18 17:49:04', '2022-06-18 17:49:04', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:49:04', 1),
	('1538096468000673794', '192.168.14.49', '2022-06-18 17:49:09', '2022-06-18 17:49:09', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:49:09', 1),
	('1538096615543705602', '192.168.14.49', '2022-06-18 17:49:44', '2022-06-18 17:49:44', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:49:44', 1),
	('1538096656832434178', '192.168.14.49', '2022-06-18 17:49:54', '2022-06-18 17:49:54', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:49:54', 1),
	('1538096797647802370', '192.168.14.49', '2022-06-18 17:50:28', '2022-06-18 17:50:28', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:50:28', 0),
	('1538096797769437185', '192.168.14.49', '2022-06-18 17:50:28', '2022-06-18 17:50:28', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:50:28', 1),
	('1538096912248770562', '192.168.14.49', '2022-06-18 17:50:55', '2022-06-18 17:50:55', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:50:55', 1),
	('1538096949284474881', '192.168.14.49', '2022-06-18 17:51:04', '2022-06-18 17:51:04', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:51:04', 1),
	('1538097005047746562', '192.168.14.49', '2022-06-18 17:51:17', '2022-06-18 17:51:17', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:51:17', 1),
	('1538097229292015617', '192.168.14.49', '2022-06-18 17:52:11', '2022-06-18 17:52:11', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:52:11', 1),
	('1538097334191558657', '192.168.14.49', '2022-06-18 17:52:36', '2022-06-18 17:52:36', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:52:36', 1),
	('1538097343490330625', '192.168.14.49', '2022-06-18 17:52:38', '2022-06-18 17:52:38', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:52:38', 1),
	('1538097414118215681', '192.168.14.49', '2022-06-18 17:52:55', '2022-06-18 17:52:55', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:52:55', 1),
	('1538097426218782722', '192.168.14.49', '2022-06-18 17:52:58', '2022-06-18 17:52:58', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:52:58', 1),
	('1538097441511215106', '192.168.14.49', '2022-06-18 17:53:01', '2022-06-18 17:53:01', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:53:01', 1),
	('1538097556682608642', '192.168.14.49', '2022-06-18 17:53:29', '2022-06-18 17:53:29', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:53:29', 1),
	('1538097838422396930', '192.168.14.49', '2022-06-18 17:54:36', '2022-06-18 17:54:36', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:54:36', 1),
	('1538097934316769281', '192.168.14.49', '2022-06-18 17:54:59', '2022-06-18 17:54:59', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:54:59', 1),
	('1538097934375489537', '192.168.14.49', '2022-06-18 17:54:59', '2022-06-18 17:54:59', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:54:59', 0),
	('1538097943569403906', '192.168.14.49', '2022-06-18 17:55:01', '2022-06-18 17:55:01', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:55:01', 1),
	('1538097943649095681', '192.168.14.49', '2022-06-18 17:55:01', '2022-06-18 17:55:01', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:55:01', 0),
	('1538097960434700289', '192.168.14.49', '2022-06-18 17:55:05', '2022-06-18 17:55:05', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:55:05', 0),
	('1538097960552140801', '192.168.14.49', '2022-06-18 17:55:05', '2022-06-18 17:55:05', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:55:05', 1),
	('1538098017229770753', '192.168.14.49', '2022-06-18 17:55:18', '2022-06-18 17:55:18', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:55:18', 1),
	('1538098027300294658', '192.168.14.49', '2022-06-18 17:55:21', '2022-06-18 17:55:21', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:55:21', 0),
	('1538098027455483906', '192.168.14.49', '2022-06-18 17:55:21', '2022-06-18 17:55:21', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:55:21', 1),
	('1538098072170958849', '192.168.14.49', '2022-06-18 17:55:32', '2022-06-18 17:55:32', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:55:32', 1),
	('1538098072254844930', '192.168.14.49', '2022-06-18 17:55:32', '2022-06-18 17:55:32', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:55:32', 0),
	('1538098080685395970', '192.168.14.49', '2022-06-18 17:55:34', '2022-06-18 17:55:34', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:55:34', 0),
	('1538098080844779522', '192.168.14.49', '2022-06-18 17:55:34', '2022-06-18 17:55:34', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:55:34', 1),
	('1538098134573813762', '192.168.14.49', '2022-06-18 17:55:46', '2022-06-18 17:55:46', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:55:46', 0),
	('1538098134607368193', '192.168.14.49', '2022-06-18 17:55:46', '2022-06-18 17:55:46', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:55:46', 1),
	('1538098146506608642', '192.168.14.49', '2022-06-18 17:55:49', '2022-06-18 17:55:49', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 17:55:49', 0),
	('1538098146603077634', '192.168.14.49', '2022-06-18 17:55:49', '2022-06-18 17:55:49', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:55:49', 1),
	('1538098271840800770', '192.168.14.49', '2022-06-18 17:56:19', '2022-06-18 17:56:19', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:56:17', 1),
	('1538098273417859073', '192.168.14.49', '2022-06-18 17:56:20', '2022-06-18 17:56:20', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:56:20', 0),
	('1538098411917971457', '192.168.14.49', '2022-06-18 17:56:53', '2022-06-18 17:56:53', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:56:51', 0),
	('1538098444570628097', '192.168.14.49', '2022-06-18 17:57:00', '2022-06-18 17:57:00', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:57:00', 0),
	('1538098444604182530', '192.168.14.49', '2022-06-18 17:57:00', '2022-06-18 17:57:00', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:57:00', 1),
	('1538098505794883586', '192.168.14.49', '2022-06-18 17:57:15', '2022-06-18 17:57:15', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:57:15', 1),
	('1538098639228276737', '192.168.14.49', '2022-06-18 17:57:47', '2022-06-18 17:57:47', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:57:47', 1),
	('1538098659079917569', '192.168.14.49', '2022-06-18 17:57:51', '2022-06-18 17:57:51', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:57:51', 1),
	('1538098812717273089', '192.168.14.49', '2022-06-18 17:58:28', '2022-06-18 17:58:28', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:58:28', 0),
	('1538098883844280322', '192.168.14.49', '2022-06-18 17:58:45', '2022-06-18 17:58:45', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:58:45', 1),
	('1538099069014413314', '192.168.14.49', '2022-06-18 17:59:29', '2022-06-18 17:59:29', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 17:59:29', 1),
	('1538099076727738370', '192.168.14.49', '2022-06-18 17:59:31', '2022-06-18 17:59:31', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 17:59:31', 0),
	('1538099638793846786', '192.168.14.49', '2022-06-18 18:01:45', '2022-06-18 18:01:45', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:01:45', 1),
	('1538099643512438786', '192.168.14.49', '2022-06-18 18:01:46', '2022-06-18 18:01:46', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:01:46', 0),
	('1538099691272978434', '192.168.14.49', '2022-06-18 18:01:58', '2022-06-18 18:01:58', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 18:01:58', 0),
	('1538099691461722114', '192.168.14.49', '2022-06-18 18:01:58', '2022-06-18 18:01:58', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:01:58', 1),
	('1538099725993426945', '192.168.14.49', '2022-06-18 18:02:06', '2022-06-18 18:02:06', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 18:02:06', 0),
	('1538099726131838977', '192.168.14.49', '2022-06-18 18:02:06', '2022-06-18 18:02:06', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:02:06', 1),
	('1538099744502890498', '192.168.14.49', '2022-06-18 18:02:10', '2022-06-18 18:02:10', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:02:10', 1),
	('1538099744561610753', '192.168.14.49', '2022-06-18 18:02:10', '2022-06-18 18:02:10', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:02:10', 0),
	('1538099753570975745', '192.168.14.49', '2022-06-18 18:02:12', '2022-06-18 18:02:12', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 18:02:12', 0),
	('1538099753680027649', '192.168.14.49', '2022-06-18 18:02:12', '2022-06-18 18:02:12', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:02:12', 1),
	('1538099785669984258', '192.168.14.49', '2022-06-18 18:02:20', '2022-06-18 18:02:20', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:02:20', 1),
	('1538099867324694529', '192.168.14.49', '2022-06-18 18:02:40', '2022-06-18 18:02:40', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:02:40', 1),
	('1538099867400192002', '192.168.14.49', '2022-06-18 18:02:40', '2022-06-18 18:02:40', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:02:40', 0),
	('1538100892882354177', '192.168.14.49', '2022-06-18 18:06:44', '2022-06-18 18:06:44', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:06:44', 1),
	('1538100892945268738', '192.168.14.49', '2022-06-18 18:06:44', '2022-06-18 18:06:44', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:06:44', 0),
	('1538100973475905537', '192.168.14.49', '2022-06-18 18:07:03', '2022-06-18 18:07:03', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:07:03', 1),
	('1538101087238012930', '192.168.14.49', '2022-06-18 18:07:30', '2022-06-18 18:07:30', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:07:30', 0),
	('1538101103243476994', '192.168.14.49', '2022-06-18 18:07:34', '2022-06-18 18:07:34', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 18:07:34', 0),
	('1538101103394471938', '192.168.14.49', '2022-06-18 18:07:34', '2022-06-18 18:07:34', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:07:34', 1),
	('1538101116342288386', '192.168.14.49', '2022-06-18 18:07:37', '2022-06-18 18:07:37', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:07:37', 1),
	('1538101441497317378', '192.168.14.49', '2022-06-18 18:08:55', '2022-06-18 18:08:55', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:08:55', 1),
	('1538101528143249409', '192.168.14.49', '2022-06-18 18:09:16', '2022-06-18 18:09:16', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:09:16', 0),
	('1538101538331213826', '192.168.14.49', '2022-06-18 18:09:18', '2022-06-18 18:09:18', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 18:09:18', 0),
	('1538101538423488513', '192.168.14.49', '2022-06-18 18:09:18', '2022-06-18 18:09:18', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:09:18', 1),
	('1538101581985529857', '192.168.14.49', '2022-06-18 18:09:28', '2022-06-18 18:09:28', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'MESSAGE_BOARD', '点击留言板页面', '2022-06-18 18:09:28', 1),
	('1538101582044250114', '192.168.14.49', '2022-06-18 18:09:28', '2022-06-18 18:09:28', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:09:28', 0),
	('1538101637446811649', '192.168.14.49', '2022-06-18 18:09:42', '2022-06-18 18:09:42', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:09:42', 0),
	('1538101637488754689', '192.168.14.49', '2022-06-18 18:09:42', '2022-06-18 18:09:42', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-18 18:09:42', 1),
	('1538101651216711681', '192.168.14.49', '2022-06-18 18:09:45', '2022-06-18 18:09:45', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 18:09:45', 0),
	('1538101825502625794', '192.168.14.49', '2022-06-18 18:10:26', '2022-06-18 18:10:26', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-18 18:10:26', 1),
	('1538101873137336322', '192.168.14.49', '2022-06-18 18:10:38', '2022-06-18 18:10:38', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'LOGIN', '登录', '2022-06-18 18:10:38', 0),
	('1538101873179279361', '192.168.14.49', '2022-06-18 18:10:38', '2022-06-18 18:10:38', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'BLOG_DETAIL', '点击博客页面', '2022-06-18 18:10:38', 1),
	('1538101887108562947', '192.168.14.49', '2022-06-18 18:10:41', '2022-06-18 18:10:41', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 18:10:41', 0),
	('1538101908327546881', '192.168.14.49', '2022-06-18 18:10:46', '2022-06-18 18:10:46', 'Windows', 'Chrome-100.0.4896.127', '内网IP|内网IP', 'PUBLISH_COMMENT', '发表评论', '2022-06-18 18:10:46', 0);
/*!40000 ALTER TABLE `t_web_visit` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
