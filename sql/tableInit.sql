-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.26 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


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

-- 正在导出表  mango_blog.t_admin 的数据：~3 rows (大约)
DELETE FROM `t_admin`;
/*!40000 ALTER TABLE `t_admin` DISABLE KEYS */;
INSERT INTO `t_admin` (`id`, `username`, `password`, `gender`, `avatar`, `email`, `phone`, `login_count`, `last_login_time`, `last_login_ip`, `create_time`, `update_time`, `nickname`, `qq_number`, `we_chat`, `enabled`, `is_deleted`) VALUES
	('1295268474480156673', 'admin', '$2a$10$ZYi1kqMMQczWDw86u76uPu79CneIE1G0h4dw89a4tjZhrQCWa8QaW', 1, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', 'guang1997qqq@163.com', '18761616251', 22, '2022-04-26 20:18:46', '192.168.1.7', '2021-09-26 16:56:46', '2022-04-26 20:18:46', '李斯特', '872174823', '18761616251', 1, 0),
	('1595268474480156674', 'visitor', '$2a$10$Ak12ZqHxdWV4ooYfoWdNMuPGeN3NZzvgzsrtOj5WViECxU4FGrnBy', 1, 'https://guang1997.oss-cn-shanghai.aliyuncs.com/avatar/2020/07/22/index.jpg', '872174823@qq.com', '18888888888', 1, '2022-04-06 22:37:27', '192.168.1.7', '2021-12-02 22:01:22', '2022-04-18 21:46:08', '游客', '111111', '111111', 0, 0);
/*!40000 ALTER TABLE `t_admin` ENABLE KEYS */;

-- 导出  表 mango_blog.t_blog 结构
DROP TABLE IF EXISTS `t_blog`;
CREATE TABLE IF NOT EXISTS `t_blog` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `title` varchar(200) DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '博客简介',
  `content` longtext COMMENT '博客内容',
  `click_count` int DEFAULT '0' COMMENT '博客点击数',
  `like_count` int DEFAULT '0' COMMENT '博客点赞数',
  `file_id` varchar(255) DEFAULT NULL COMMENT '标题图片id',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `admin_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '管理员id',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `blog_sort_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客分类ID',
  `level` tinyint(1) DEFAULT '0' COMMENT '推荐等级(0:正常)',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `open_comment` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客表';

-- 正在导出表  mango_blog.t_blog 的数据：~3 rows (大约)
DELETE FROM `t_blog`;
/*!40000 ALTER TABLE `t_blog` DISABLE KEYS */;
INSERT INTO `t_blog` (`id`, `title`, `summary`, `content`, `click_count`, `like_count`, `file_id`, `create_time`, `update_time`, `admin_id`, `author`, `blog_sort_id`, `level`, `sort`, `open_comment`, `is_deleted`) VALUES
	('1', '1', '1', '1', 0, 0, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', '2022-04-17 09:42:18', '2022-04-17 09:42:19', '1', '1', '1505006369916694530', 1, 0, 0, 0),
	('2', '2', '2', '2', 0, 0, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', '2022-04-17 20:05:21', '2022-04-17 20:05:22', '2', '2', '1505006369916694530', 2, 0, 1, 0),
	('3', '3', '3', '3', 0, 0, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', '2022-04-17 20:05:39', '2022-04-17 20:05:40', '3', '3', '1506662963603652609', 3, 0, 1, 0),
	('4', '1', '1', '2', 0, 0, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', '2022-04-17 20:07:37', '2022-04-17 20:07:37', '1', '1', '1506662963603652609', 0, 0, 1, 0);
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

-- 正在导出表  mango_blog.t_blog_tag 的数据：~5 rows (大约)
DELETE FROM `t_blog_tag`;
/*!40000 ALTER TABLE `t_blog_tag` DISABLE KEYS */;
INSERT INTO `t_blog_tag` (`id`, `blog_id`, `tag_id`, `create_time`, `update_time`) VALUES
	('1', '1', '1506663317590327298', '2022-04-16 18:43:14', '2022-04-16 18:43:15'),
	('2', '1', '1507906192298856450', '2022-04-16 18:43:50', '2022-04-16 18:43:51'),
	('3', '2', '1506663317590327298', '2022-04-16 18:43:58', '2022-04-16 18:43:59'),
	('4', '3', '1506663317590327298', '2022-04-16 18:45:14', '2022-04-16 18:45:14'),
	('5', '4', '1506663317590327298', '2022-04-17 20:08:06', '2022-04-17 20:08:07');
/*!40000 ALTER TABLE `t_blog_tag` ENABLE KEYS */;

-- 导出  表 mango_blog.t_comment 结构
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE IF NOT EXISTS `t_comment` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `parent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '父id',
  `user_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户id',
  `avatar` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户昵称',
  `content` varchar(2048) DEFAULT NULL COMMENT '评论内容',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `source` varchar(255) NOT NULL COMMENT '评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '评论类型 1:点赞 0:评论',
  `blog_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态: 0-待审核, 1-审核通过, 2-审核不通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='评论表';

-- 正在导出表  mango_blog.t_comment 的数据：~4 rows (大约)
DELETE FROM `t_comment`;
/*!40000 ALTER TABLE `t_comment` DISABLE KEYS */;
INSERT INTO `t_comment` (`id`, `parent_id`, `user_id`, `avatar`, `nickname`, `content`, `create_time`, `update_time`, `source`, `type`, `blog_id`, `status`) VALUES
	('1295268474480156673', '0', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', '李斯特', '测试', '2022-04-13 20:05:53', '2022-04-13 20:05:54', 'ABOUT', 0, NULL, 1),
	('1295268474480156674', '1295268474480156673', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', '李斯特', '回复测试', '2022-04-13 20:07:09', '2022-04-13 20:07:10', 'BLOG_INFO', 0, NULL, 1),
	('1295268474480156675', '1295268474480156674', NULL, 'https://guang1997.oss-cn-shanghai.aliyuncs.com/avatar/2020/07/22/index.jpg', '游客', '回复李斯特', '2022-04-13 20:17:24', '2022-04-13 20:17:24', 'BLOG_INFO', 0, NULL, 1),
	('1295268474480156676', '1295268474480156675', NULL, 'https://lisite-blog.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/04/d3b5c4c0-6734-4532-84c3-79a199997298-avatar.png', '李斯特', '回复游客', '2022-04-13 20:18:42', '2022-04-13 20:18:42', 'MESSAGE_BOARD', 0, NULL, 1);
/*!40000 ALTER TABLE `t_comment` ENABLE KEYS */;

-- 导出  表 mango_blog.t_dict 结构
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE IF NOT EXISTS `t_dict` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `dict_name` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `summary` varchar(255) DEFAULT NULL COMMENT '描述',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='字典表';

-- 正在导出表  mango_blog.t_dict 的数据：~8 rows (大约)
DELETE FROM `t_dict`;
/*!40000 ALTER TABLE `t_dict` DISABLE KEYS */;
INSERT INTO `t_dict` (`id`, `dict_name`, `summary`, `is_deleted`, `create_time`, `update_time`, `sort`) VALUES
	('1506155694696247297', 'sys_yes_no', '系统是否', 0, '2022-03-22 14:27:55', '2022-03-22 14:27:55', 0),
	('1506174838367870978', 'sys_menu_type', '菜单类型', 0, '2022-03-22 15:43:59', '2022-03-22 15:44:07', 0),
	('1506640128227151874', 'sys_status', '状态', 0, '2022-03-23 22:32:53', '2022-03-23 22:32:53', 0),
	('1506648391412879362', 'sys_sort_type', '分类级别', 0, '2022-03-23 23:05:43', '2022-03-23 23:05:43', 0),
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

-- 正在导出表  mango_blog.t_dict_detail 的数据：~24 rows (大约)
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
	('1506648554332229633', '1506648391412879362', '一级分类', '1', NULL, 'success', '分类级别 一级', 0, '2022-03-23 23:06:22', '2022-03-23 23:07:29', 3),
	('1506648617326481409', '1506648391412879362', '二级分类', '2', NULL, 'primary', '分类级别 二级', 0, '2022-03-23 23:06:37', '2022-03-23 23:07:10', 2),
	('1506648738428620801', '1506648391412879362', '三级分类', '3', NULL, 'info', '分类级别 三级', 0, '2022-03-23 23:07:06', '2022-03-23 23:07:06', 1),
	('1507910063435927554', '1507909943558524929', '男', '1', NULL, 'success', '性别 男', 0, '2022-03-27 10:39:09', '2022-03-27 10:39:31', 2),
	('1507910141563228162', '1507909943558524929', '女', '2', NULL, 'danger', '性别 女', 0, '2022-03-27 10:39:28', '2022-03-27 10:39:35', 1),
	('1507913903639932929', '1507913784651722753', '申请中', '0', NULL, 'default', '友链状态 申请中', 0, '2022-03-27 10:54:25', '2022-03-27 10:54:58', 1),
	('1507914027598393346', '1507913784651722753', '已上线', '1', NULL, 'success', '友链状态 已上线', 0, '2022-03-27 10:54:55', '2022-03-27 10:54:55', 2),
	('1507914160700436482', '1507913784651722753', '已下架', '2', NULL, 'danger', '友链状态 已下架', 0, '2022-03-27 10:55:26', '2022-03-27 10:55:26', 3),
	('1514210064461996034', '1514209682880995330', '点赞', '1', NULL, 'success', '评论类型 点赞', 0, '2022-04-13 19:53:07', '2022-04-13 19:53:07', 999),
	('1514210160368951298', '1514209682880995330', '评论', '0', NULL, 'warning', '评论类型 评论', 0, '2022-04-13 19:53:30', '2022-04-13 19:53:34', 999),
	('1514210349318152193', '1514209728137535490', '留言板', 'MESSAGE_BOARD', NULL, 'success', '评论来源 留言板', 0, '2022-04-13 19:54:15', '2022-04-13 19:54:15', 999),
	('1514210482151759873', '1514209728137535490', '关于我', 'ABOUT', NULL, 'warning', '评论来源 关于我', 0, '2022-04-13 19:54:46', '2022-04-13 19:54:46', 999),
	('1514210661902852098', '1514209728137535490', '博客详情', 'BLOG_INFO', NULL, 'primary', '评论来源 博客详情', 0, '2022-04-13 19:55:29', '2022-04-13 19:55:29', 999),
	('1516045760239882241', '1516045589821116417', '正常', '0', NULL, 'default', '博客推荐级别 正常', 0, '2022-04-18 21:27:31', '2022-04-18 21:27:31', 999),
	('1516045852984332289', '1516045589821116417', '一级推荐', '1', NULL, 'success', '博客推荐级别 一级推荐', 0, '2022-04-18 21:27:53', '2022-04-18 21:27:58', 999),
	('1516045939999363073', '1516045589821116417', '二级推荐', '2', NULL, 'warning', '博客推荐级别 二级推荐', 0, '2022-04-18 21:28:14', '2022-04-18 21:29:20', 999),
	('1516045995942989826', '1516045589821116417', '三级推荐', '3', NULL, 'danger', '博客推荐级别 三级推荐', 0, '2022-04-18 21:28:27', '2022-04-18 21:29:23', 999);
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
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='邮箱配置';

-- 正在导出表  mango_blog.t_email_config 的数据：~0 rows (大约)
DELETE FROM `t_email_config`;
/*!40000 ALTER TABLE `t_email_config` DISABLE KEYS */;
INSERT INTO `t_email_config` (`id`, `from_user`, `host`, `password`, `port`, `user`, `subject`, `create_time`, `update_time`) VALUES
	('1595268474480156679', 'mango_blog@163.com', 'smtp.163.com', 'WVNMVElMUk9OVlJDSlNOTw==', 25, 'mango_blog', 'MANGO_BLOG后台管理系统', NULL, NULL);
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
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `link_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '友链状态： 0 申请中， 1：已上线，  2：已下架',
  `file_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '网站图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='友情链接表';

-- 正在导出表  mango_blog.t_link 的数据：~2 rows (大约)
DELETE FROM `t_link`;
/*!40000 ALTER TABLE `t_link` DISABLE KEYS */;
INSERT INTO `t_link` (`id`, `title`, `summary`, `url`, `create_time`, `update_time`, `link_status`, `file_id`) VALUES
	('1507920187256823810', '测试2', NULL, 'https://www.yanshisan.cn/1', '2022-03-27 11:19:23', '2022-03-27 11:19:23', 0, 'https://guang1997.oss-cn-shanghai.aliyuncs.com/avatar/2020/07/22/index.jpg');
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

-- 正在导出表  mango_blog.t_menu 的数据：~17 rows (大约)
DELETE FROM `t_menu`;
/*!40000 ALTER TABLE `t_menu` DISABLE KEYS */;
INSERT INTO `t_menu` (`id`, `name`, `title`, `pid`, `icon`, `sort`, `is_deleted`, `create_time`, `update_time`, `hidden`, `component`, `path`, `redirect`, `permission`, `menu_type`, `sub_count`) VALUES
	('1195268474480156673', '权限管理', '权限管理', '0', 'authority', 998, 0, '2021-10-07 21:49:36', '2022-03-27 23:04:19', 0, 'Layout', '/authority', NULL, '', 0, 4),
	('1195268474480156674', '管理员管理', '管理员管理', '1195268474480156673', 'peoples', 1, 0, '2021-10-07 22:38:13', '2022-04-10 12:45:12', 0, 'authority/admin', 'admin', NULL, 'admin:list', 1, 2),
	('1195268474480156675', '菜单管理', '菜单管理', '1195268474480156673', 'authority', 2, 0, '2021-10-07 22:50:12', '2022-03-09 17:27:53', 0, 'authority/menu', 'menu', NULL, 'menu:list', 1, 4),
	('1195268474480156676', '添加菜单', '添加菜单', '1195268474480156675', NULL, 1, 0, '2021-12-04 22:16:29', '2022-03-09 17:00:45', 0, NULL, NULL, NULL, 'menu:add', 2, 0),
	('1195268474480156677', '博客管理', '博客管理', '0', 'blog', 999, 0, '2022-03-07 14:11:22', '2022-03-27 23:04:05', 0, 'Layout', '/blog', NULL, '', 0, 4),
	('1195268474480156678', '博客管理', '博客管理', '1195268474480156677', 'blog', 1, 0, '2022-03-07 14:20:01', '2022-03-18 09:59:30', 0, 'blog/blog', 'blog', NULL, 'blog:list', 1, 0),
	('1195268474480156679', '分类管理', '分类管理', '1195268474480156677', 'sort', 2, 0, '2022-03-07 14:55:30', '2022-03-18 10:05:00', 0, 'blog/sort', 'sort', NULL, 'sort:list', 1, 0),
	('1501368407743528962', '系统管理', '系统管理', '0', 'system', 997, 0, '2022-03-09 09:24:57', '2022-03-27 23:04:25', 0, 'Layout', '/system', NULL, NULL, 0, 5),
	('1501369279194071042', '字典管理', '字典管理', '1501368407743528962', 'authority', 999, 0, '2022-03-09 09:28:25', '2022-03-22 13:40:06', 0, 'system/dict', 'dict', NULL, 'dict:list', 1, 0),
	('1501369997368082434', '删除菜单', '删除菜单', '1195268474480156675', NULL, 3, 0, '2022-03-09 09:31:16', '2022-03-09 17:26:48', 0, NULL, NULL, NULL, 'menu:del', 2, 0),
	('1501483509264281601', '修改菜单', '修改菜单', '1195268474480156675', NULL, 2, 0, '2022-03-09 17:02:19', '2022-03-09 17:02:19', 0, NULL, NULL, NULL, 'menu:edit', 2, 0),
	('1501490155294052354', '角色管理', '角色管理', '1195268474480156673', 'adminRole', 3, 0, '2022-03-09 17:28:44', '2022-04-10 12:43:48', 0, 'authority/role', 'role', NULL, 'role:list', 1, 0),
	('1503383805083586562', '添加管理员', '添加管理员', '1195268474480156674', NULL, 999, 0, '2022-03-14 22:53:25', '2022-03-14 22:53:25', 0, NULL, NULL, NULL, 'admin:add', 2, 0),
	('1504633021835083777', '标签管理', '标签管理', '1195268474480156677', 'tag', 3, 0, '2022-03-18 09:37:22', '2022-03-18 10:05:36', 0, 'blog/tag', 'tag', NULL, 'blog:list', 1, 0),
	('1506144477000859650', '友情链接', '友情链接', '1501368407743528962', 'link', 999, 0, '2022-03-22 13:43:21', '2022-03-22 13:43:21', 0, 'system/link', 'link', NULL, 'link:list', 1, 0),
	('1508097474904268802', '系统监控', '系统监控', '0', 'monitor', 996, 0, '2022-03-27 23:08:23', '2022-03-27 23:08:23', 0, 'Layout', '/monitor', NULL, NULL, 0, 3),
	('1508097940568481794', '服务监控', '服务监控', '1508097474904268802', 'dashboard', 999, 0, '2022-03-27 23:10:30', '2022-03-27 23:10:30', 0, 'monitor/server', 'server', NULL, 'server:list', 1, 0),
	('1508812944326385665', 'druid监控', 'druid监控', '1508097474904268802', 'druid', 999, 0, '2022-03-29 22:26:53', '2022-03-29 22:26:53', 0, 'monitor/druid', 'druid', NULL, 'druid:list', 1, 0),
	('1513015571486470146', '评论管理', '评论管理', '1501368407743528962', 'message', 999, 0, '2022-04-10 12:46:37', '2022-04-16 08:19:58', 0, 'system/comment', 'comment', NULL, 'comment:list', 1, 0),
	('1515127581015547905', '门户访问记录管理', '门户访问记录管理', '1501368407743528962', 'web', 999, 0, '2022-04-16 08:39:00', '2022-04-16 17:29:20', 0, 'system/webVisit', 'webVisit', NULL, 'webVisit:list', 1, 0);
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

-- 正在导出表  mango_blog.t_role 的数据：~3 rows (大约)
DELETE FROM `t_role`;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` (`id`, `role_name`, `create_time`, `update_time`, `summary`, `level`) VALUES
	('1395268474480156673', 'admin', '2021-10-02 22:52:24', '2022-04-06 22:58:14', '管理员', 1),
	('1395268474480156674', 'visitor', '2021-12-02 21:58:50', '2022-04-04 23:59:19', '游客', 2),
	('1502546893753643010', '12', '2022-03-12 15:27:50', '2022-03-12 15:27:50', '12', 3);
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
	('1516050445092786177', '1395268474480156674', '1595268474480156674', '2022-04-18 21:46:08', '2022-04-18 21:46:08');
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

-- 正在导出表  mango_blog.t_role_menu 的数据：~22 rows (大约)
DELETE FROM `t_role_menu`;
/*!40000 ALTER TABLE `t_role_menu` DISABLE KEYS */;
INSERT INTO `t_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`) VALUES
	('1503383316199706625', '1395268474480156674', '1195268474480156677', '2022-03-14 22:51:29', '2022-03-14 22:51:29'),
	('1503383317231505410', '1395268474480156674', '1195268474480156678', '2022-03-14 22:51:29', '2022-03-14 22:51:29'),
	('1503383318091337730', '1395268474480156674', '1195268474480156679', '2022-03-14 22:51:29', '2022-03-14 22:51:29'),
	('1507906766557794306', '1502546893753643010', '1501368407743528962', '2022-03-27 10:26:03', '2022-03-27 10:26:03'),
	('1507906766822035458', '1502546893753643010', '1501369279194071042', '2022-03-27 10:26:03', '2022-03-27 10:26:03'),
	('1507906767212105729', '1502546893753643010', '1506144477000859650', '2022-03-27 10:26:04', '2022-03-27 10:26:04'),
	('1515127626116898817', '1395268474480156673', '1195268474480156673', '2022-04-16 08:39:10', '2022-04-16 08:39:10'),
	('1515127626699907073', '1395268474480156673', '1195268474480156674', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627354218497', '1395268474480156673', '1195268474480156675', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627354218498', '1395268474480156673', '1195268474480156677', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627354218499', '1395268474480156673', '1195268474480156678', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627354218500', '1395268474480156673', '1195268474480156679', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627354218501', '1395268474480156673', '1501368407743528962', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627354218502', '1395268474480156673', '1501369279194071042', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627421327362', '1395268474480156673', '1501490155294052354', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627421327363', '1395268474480156673', '1504633021835083777', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627421327364', '1395268474480156673', '1506144477000859650', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627421327365', '1395268474480156673', '1508097474904268802', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627421327366', '1395268474480156673', '1508097940568481794', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627421327367', '1395268474480156673', '1508812944326385665', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627421327368', '1395268474480156673', '1513015571486470146', '2022-04-16 08:39:11', '2022-04-16 08:39:11'),
	('1515127627488436225', '1395268474480156673', '1515127581015547905', '2022-04-16 08:39:11', '2022-04-16 08:39:11');
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
  `sort_level` int DEFAULT '0' COMMENT '分类级别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客分类表';

-- 正在导出表  mango_blog.t_sort 的数据：~3 rows (大约)
DELETE FROM `t_sort`;
/*!40000 ALTER TABLE `t_sort` DISABLE KEYS */;
INSERT INTO `t_sort` (`id`, `sort_name`, `summary`, `create_time`, `update_time`, `click_count`, `sort_level`) VALUES
	('1505006369916694530', '测试', '测试', '2022-03-19 10:20:55', '2022-03-19 10:27:49', 0, 2),
	('1506662963603652609', '测试1', NULL, '2022-03-24 00:03:38', '2022-03-24 00:03:38', 0, 1),
	('1506663003336294401', '测试3', NULL, '2022-03-24 00:03:47', '2022-03-24 00:03:47', 0, 3),
	('1507905421998137346', 'test', '666', '2022-03-27 10:20:43', '2022-03-27 10:23:28', 0, 1);
/*!40000 ALTER TABLE `t_sort` ENABLE KEYS */;

-- 导出  表 mango_blog.t_tag 结构
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `summary` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签描述',
  `click_count` int NOT NULL DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `tag_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='标签表';

-- 正在导出表  mango_blog.t_tag 的数据：~0 rows (大约)
DELETE FROM `t_tag`;
/*!40000 ALTER TABLE `t_tag` DISABLE KEYS */;
INSERT INTO `t_tag` (`id`, `summary`, `click_count`, `create_time`, `update_time`, `sort`, `tag_name`) VALUES
	('1506663317590327298', NULL, 0, '2022-03-24 00:05:02', '2022-03-24 00:05:02', 999, '测试'),
	('1507906192298856450', NULL, 0, '2022-03-27 10:23:46', '2022-03-27 10:23:50', 999, '123');
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
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `pass_word` varchar(32) NOT NULL COMMENT '密码',
  `gender` tinyint unsigned DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机',
  `login_count` int unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `qq_number` varchar(20) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `comment_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '评论状态 1:正常 0:禁言',
  `user_tag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户标签：0：普通用户，1：管理员，2：博主 等',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

-- 正在导出表  mango_blog.t_user 的数据：~0 rows (大约)
DELETE FROM `t_user`;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
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
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
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

-- 正在导出表  mango_blog.t_web_visit 的数据：~11 rows (大约)
DELETE FROM `t_web_visit`;
/*!40000 ALTER TABLE `t_web_visit` DISABLE KEYS */;
INSERT INTO `t_web_visit` (`id`, `user_id`, `ip`, `create_time`, `update_time`, `os`, `browser`, `ip_source`, `behavior`, `content`, `request_time`, `is_menu`) VALUES
	('1', '1', '1', '2022-04-17 10:19:36', '2022-04-17 10:19:36', NULL, NULL, NULL, '点击博客', NULL, '2022-04-17 10:19:39', 1),
	('10', NULL, NULL, '2022-04-17 17:31:07', '2022-04-17 17:31:08', NULL, NULL, NULL, '点击作者', NULL, '2022-04-17 17:31:08', 1),
	('11', NULL, NULL, '2022-04-17 17:31:17', '2022-04-17 17:31:18', NULL, NULL, NULL, '点击博客标签页面', NULL, '2022-04-17 17:31:19', 1),
	('2', '2', '1', '2022-04-17 10:19:46', '2022-04-17 10:19:46', NULL, NULL, NULL, '点击博客分类页面', NULL, '2022-04-17 10:19:48', 1),
	('3', NULL, NULL, '2022-04-17 10:45:45', '2022-04-17 10:45:45', NULL, NULL, NULL, '点击博客', NULL, '2022-04-17 10:45:48', 1),
	('4', NULL, NULL, '2022-04-16 10:46:05', '2022-04-17 10:46:06', NULL, NULL, NULL, '点击博客', NULL, '2022-04-16 10:46:11', 1),
	('5', NULL, NULL, '2022-04-17 17:29:18', '2022-04-17 17:29:18', NULL, NULL, NULL, '点击首页', NULL, '2022-04-17 17:29:24', 1),
	('6', NULL, NULL, '2022-04-17 17:29:45', '2022-04-17 17:29:45', NULL, NULL, NULL, '点击友情链接', NULL, '2022-04-17 17:29:46', 1),
	('7', NULL, NULL, '2022-04-17 17:30:20', '2022-04-17 17:30:20', NULL, NULL, NULL, '点击归档', NULL, '2022-04-17 17:30:21', 1),
	('8', NULL, NULL, '2022-04-17 17:30:34', '2022-04-17 17:30:34', NULL, NULL, NULL, '点击归档', NULL, '2022-04-17 17:30:39', 1),
	('9', NULL, NULL, '2022-04-17 17:30:47', '2022-04-17 17:30:47', NULL, NULL, NULL, '点击归档', NULL, '2022-04-17 17:30:52', 1);
/*!40000 ALTER TABLE `t_web_visit` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
