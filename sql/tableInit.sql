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

-- mango_blog.t_admin definition

CREATE TABLE `t_admin` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '个人头像',
  `email` varchar(60) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机',
  `login_count` int unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `nickname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '昵称',
  `qq_number` varchar(255) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `enabled` tinyint unsigned DEFAULT '0' COMMENT '账号状态1:启用0:禁用',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='管理员表';

INSERT INTO t_admin (id,username,password,gender,avatar,email,phone,login_count,last_login_time,last_login_ip,create_time,update_time,nickname,qq_number,we_chat,enabled,is_deleted) VALUES
	 ('1295268474480156673','admin','$2a$10$ZugpqGGvXjSfRv3xh6h4Q.6vCt4ApJH/INFiYBbMMrQWfcaQ8.Iru',1,'http://qiniu.mangoblog.vip/avatar/2023/01/28/f66de97d-35ff-4f3d-bb00-7ae10bea3b5b-avatar.png','liguangcheng1997@163.com','18761616251',93,'2023-02-23 10:59:23','42.120.73.61','2021-09-26 16:56:46','2023-02-23 10:59:23','李斯特','872174823','18761616251',1,0),
	 ('1595268474480156674','visitor','$2a$10$ZugpqGGvXjSfRv3xh6h4Q.6vCt4ApJH/INFiYBbMMrQWfcaQ8.Iru',1,'https://guang1997.oss-cn-shanghai.aliyuncs.com/avatar/2020/07/22/index.jpg','872174823@qq.com','18888888888',7,'2023-02-15 14:56:18','127.0.0.1','2021-12-02 22:01:22','2023-02-15 14:56:18','游客','111111','111111',1,0);

-- mango_blog.t_blog definition

CREATE TABLE `t_blog` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '博客简介',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '博客内容',
  `click_count` int DEFAULT '0' COMMENT '博客点击数',
  `like_count` int DEFAULT '0' COMMENT '博客点赞数',
  `file_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '标题图片id',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `admin_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '管理员id',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `blog_sort_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '博客分类ID',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客表';

-- mango_blog.t_blog_tag definition

CREATE TABLE `t_blog_tag` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `blog_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '博客ID',
  `tag_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '标签ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `IDX_BLOGID_TAGID_UNIQUE` (`blog_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- mango_blog.t_comment definition

CREATE TABLE `t_comment` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `parent_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '父id',
  `user_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户id',
  `browser_finger` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '浏览器指纹',
  `ip` varchar(64) DEFAULT NULL COMMENT '评论来源ip',
  `unique_key` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '唯一标识，如果用户id不同，ip相同，则判断此字段',
  `avatar` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户头像',
  `nickname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户昵称',
  `answer_nickname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '被评论用户昵称',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论内容',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `source` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论来源',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '评论类型 1:点赞 0:评论',
  `blog_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '博客id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态: 0-待审核, 1-审核通过, 2-审核不通过',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='评论表';


-- mango_blog.t_dict definition

CREATE TABLE `t_dict` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `dict_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '字典名称',
  `summary` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '描述',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='字典表';

INSERT INTO mango_blog.t_dict (id,dict_name,summary,is_deleted,create_time,update_time,sort) VALUES
	 ('1506155694696247297','sys_yes_no','系统是否',0,'2022-03-22 14:27:55','2022-03-22 14:27:55',0),
	 ('1506174838367870978','sys_menu_type','菜单类型',0,'2022-03-22 15:43:59','2022-03-22 15:44:07',0),
	 ('1506640128227151874','sys_status','状态',0,'2022-03-23 22:32:53','2022-03-23 22:32:53',0),
	 ('1506648391412879362','sys_sort_type','分类级别',1,'2022-03-23 23:05:43','2022-03-23 23:05:43',0),
	 ('1507909943558524929','sys_gender','性别',0,'2022-03-27 10:38:41','2022-03-27 10:38:41',0),
	 ('1507913784651722753','sys_link_status','友链状态',0,'2022-03-27 10:53:57','2022-03-27 10:53:57',0),
	 ('1514209682880995330','sys_comment_type','评论类型',0,'2022-04-13 19:51:36','2022-04-13 19:51:36',0),
	 ('1514209728137535490','sys_comment_source','评论来源',0,'2022-04-13 19:51:47','2022-04-13 19:51:47',0),
	 ('1516045589821116417','sys_blog_level','博客推荐级别',0,'2022-04-18 21:26:50','2022-04-18 21:26:50',0);


-- mango_blog.t_dict_detail definition

CREATE TABLE `t_dict_detail` (
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

INSERT INTO mango_blog.t_dict_detail (id,dict_id,dict_label,dict_value,css_class,list_class,summary,is_deleted,create_time,update_time,sort) VALUES
	 ('1506162082013065218','1506155694696247297','是','1',NULL,'success','系统是否 是',0,'2022-03-22 14:53:18','2022-03-22 16:17:49',2),
	 ('1506171019126923265','1506155694696247297','否','0',NULL,'warning','系统是否 否',0,'2022-03-22 15:28:49','2022-03-22 15:35:13',1),
	 ('1506196718315483137','1506174838367870978','目录','0',NULL,'default','菜单类型 目录',0,'2022-03-22 17:10:56','2022-03-22 17:19:19',3),
	 ('1506198945411215362','1506174838367870978','菜单','1',NULL,'success','菜单类型 菜单',0,'2022-03-22 17:19:47','2022-03-22 17:19:47',2),
	 ('1506199049916493825','1506174838367870978','按钮','2','','warning','菜单类型 按钮',0,'2022-03-22 17:20:12','2022-03-22 17:20:23',1),
	 ('1506640400328429570','1506640128227151874','禁用','0',NULL,'danger','系统状态 禁用',0,'2022-03-23 22:33:58','2022-04-09 09:33:51',1),
	 ('1506640493521670145','1506640128227151874','启用','1',NULL,'success','系统状态 启用',0,'2022-03-23 22:34:20','2022-04-09 09:33:47',2),
	 ('1506648554332229633','1506648391412879362','一级分类','1',NULL,'success','分类级别 一级',1,'2022-03-23 23:06:22','2022-03-23 23:07:29',3),
	 ('1506648617326481409','1506648391412879362','二级分类','2',NULL,'primary','分类级别 二级',1,'2022-03-23 23:06:37','2022-03-23 23:07:10',2),
	 ('1506648738428620801','1506648391412879362','三级分类','3',NULL,'info','分类级别 三级',1,'2022-03-23 23:07:06','2022-03-23 23:07:06',1);
INSERT INTO mango_blog.t_dict_detail (id,dict_id,dict_label,dict_value,css_class,list_class,summary,is_deleted,create_time,update_time,sort) VALUES
	 ('1507910063435927554','1507909943558524929','男','1',NULL,'success','性别 男',0,'2022-03-27 10:39:09','2022-03-27 10:39:31',2),
	 ('1507910141563228162','1507909943558524929','女','2',NULL,'danger','性别 女',0,'2022-03-27 10:39:28','2022-03-27 10:39:35',1),
	 ('1507913903639932929','1507913784651722753','申请中','0',NULL,'default','友链状态 申请中',0,'2022-03-27 10:54:25','2022-03-27 10:54:58',1),
	 ('1507914027598393346','1507913784651722753','已上线','1',NULL,'success','友链状态 已上线',0,'2022-03-27 10:54:55','2022-03-27 10:54:55',2),
	 ('1507914160700436482','1507913784651722753','已下架','2',NULL,'danger','友链状态 已下架',1,'2022-03-27 10:55:26','2022-03-27 10:55:26',3),
	 ('1514210064461996034','1514209682880995330','点赞','1',NULL,'success','评论类型 点赞',0,'2022-04-13 19:53:07','2022-04-13 19:53:07',999),
	 ('1514210160368951298','1514209682880995330','评论','0',NULL,'warning','评论类型 评论',0,'2022-04-13 19:53:30','2022-04-13 19:53:34',999),
	 ('1514210349318152193','1514209728137535490','博客详情页面点赞','BLOG_INFO_LIKES',NULL,'success','评论来源 博客详情页面点赞',0,'2022-04-13 19:54:15','2022-06-15 16:23:46',999),
	 ('1514210482151759873','1514209728137535490','博客详情页面评论','BLOG_INFO_MESSAGE',NULL,'warning','评论来源 博客详情页面评论',0,'2022-04-13 19:54:46','2022-06-15 16:24:13',999),
	 ('1514210661902852098','1514209728137535490','博客详情页面评论点赞','BLOG_INFO_COMMENT_LIKES',NULL,'primary','评论来源 博客详情页面评论点赞',0,'2022-04-13 19:55:29','2022-06-15 16:24:35',999);
INSERT INTO mango_blog.t_dict_detail (id,dict_id,dict_label,dict_value,css_class,list_class,summary,is_deleted,create_time,update_time,sort) VALUES
	 ('1516045760239882241','1516045589821116417','正常','0',NULL,'default','博客推荐级别 正常',0,'2022-04-18 21:27:31','2022-04-18 21:27:31',999),
	 ('1516045852984332289','1516045589821116417','一级推荐','1',NULL,'success','博客推荐级别 一级推荐',0,'2022-04-18 21:27:53','2022-04-18 21:27:58',999),
	 ('1516045939999363073','1516045589821116417','二级推荐','2',NULL,'warning','博客推荐级别 二级推荐',0,'2022-04-18 21:28:14','2022-04-18 21:29:20',999),
	 ('1516045995942989826','1516045589821116417','三级推荐','3',NULL,'danger','博客推荐级别 三级推荐',0,'2022-04-18 21:28:27','2022-04-18 21:29:23',999),
	 ('1536988114884931585','1514209728137535490','留言板页面点赞','MESSAGE_BOARD_LIKES',NULL,'info','评论来源 留言板页面点赞',0,'2022-06-15 16:24:57','2022-06-15 16:24:57',999),
	 ('1536988229020332033','1514209728137535490','留言板页面评论','MESSAGE_BOARD_MESSAGE',NULL,'default','评论来源 留言板页面评论',0,'2022-06-15 16:25:24','2022-06-15 16:25:24',999);

-- mango_blog.t_email_config definition

CREATE TABLE `t_email_config` (
  `id` varchar(19) NOT NULL COMMENT 'ID',
  `from_user` varchar(255) DEFAULT NULL COMMENT '发件人',
  `host` varchar(255) DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '密码',
  `port` int DEFAULT NULL COMMENT '端口',
  `user` varchar(255) DEFAULT NULL COMMENT '发件者用户名',
  `subject` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮件主题',
  `source` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'ADMIN' COMMENT '邮件来源：ADMIN-后台管理系统，WEB-门户网站',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='邮箱配置';

INSERT INTO mango_blog.t_email_config (id,from_user,host,password,port,`user`,subject,source,create_time,update_time) VALUES
	 ('1595268474480156679','mango_blog@163.com','smtp.163.com','WVNMVElMUk9OVlJDSlNOTw==',25,'mango_blog','MANGO_BLOG后台管理系统','ADMIN','2022-05-28 21:49:13','2022-05-28 21:49:14'),
	 ('1595268474480156680','mango_blog@163.com','smtp.163.com','WVNMVElMUk9OVlJDSlNOTw==',25,'mango_blog','MANGO_BLOG门户网站','WEB','2022-05-28 21:49:31','2022-05-28 21:49:32');


-- mango_blog.t_exception_log definition

CREATE TABLE `t_exception_log` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `exception_details` mediumtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '异常对象json格式',
  `exception_message` mediumtext COMMENT '异常信息',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `ip` varchar(20) DEFAULT NULL COMMENT 'ip地址',
  `ip_source` varchar(100) DEFAULT NULL COMMENT 'ip来源',
  `method` varchar(255) DEFAULT NULL COMMENT '请求方法',
  `params` longtext COMMENT '请求参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='异常信息表';


-- mango_blog.t_link definition

CREATE TABLE `t_link` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `title` varchar(255) DEFAULT NULL COMMENT '友情链接标题',
  `summary` varchar(255) DEFAULT NULL COMMENT '友情链接介绍',
  `url` varchar(255) DEFAULT NULL COMMENT '友情链接URL',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `link_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '友链状态： 0 申请中， 1：已上线',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='友情链接表';


-- mango_blog.t_menu definition

CREATE TABLE `t_menu` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '菜单标题',
  `pid` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '父id',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除 1:是 0:否',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `hidden` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏 1:是 0:否',
  `component` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '组件名称',
  `path` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'url地址',
  `redirect` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '重定向地址',
  `permission` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '权限标识',
  `menu_type` tinyint(1) DEFAULT NULL COMMENT '菜单类型 0:目录 1:菜单 2:按钮',
  `sub_count` tinyint(1) NOT NULL DEFAULT '0' COMMENT '子菜单数量，默认0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='菜单表';

INSERT INTO mango_blog.t_menu (id,name,title,pid,icon,sort,is_deleted,create_time,update_time,hidden,component,`path`,redirect,permission,menu_type,sub_count) VALUES
	 ('1195268474480156673','权限管理','权限管理','0','authority',998,0,'2021-10-07 21:49:36','2022-03-27 23:04:19',0,'Layout','/authority',NULL,'',0,5),
	 ('1195268474480156674','管理员管理','管理员管理','1195268474480156673','peoples',999,0,'2021-10-07 22:38:13','2022-05-15 22:18:03',0,'authority/admin','admin',NULL,'admin:list',1,4),
	 ('1195268474480156675','菜单管理','菜单管理','1195268474480156673','authority',998,0,'2021-10-07 22:50:12','2022-05-15 22:18:10',0,'authority/menu','menu',NULL,'menu:list',1,4),
	 ('1195268474480156676','添加菜单','新增菜单','1195268474480156675',NULL,999,0,'2021-12-04 22:16:29','2022-05-15 22:18:56',0,NULL,NULL,NULL,'menu:add',2,0),
	 ('1195268474480156677','博客管理','博客管理','0','blog',999,0,'2022-03-07 14:11:22','2022-03-27 23:04:05',0,'Layout','/blog',NULL,'',0,4),
	 ('1195268474480156678','博客管理','博客管理','1195268474480156677','blog',999,0,'2022-03-07 14:20:01','2022-05-15 22:17:33',0,'blog/blog','blog',NULL,'blog:list',1,4),
	 ('1195268474480156679','分类管理','分类管理','1195268474480156677','sort',997,0,'2022-03-07 14:55:30','2022-05-15 22:17:45',0,'blog/sort','sort',NULL,'sort:list',1,4),
	 ('1501368407743528962','系统管理','系统管理','0','system',997,0,'2022-03-09 09:24:57','2022-03-27 23:04:25',0,'Layout','/system',NULL,NULL,0,5),
	 ('1501369279194071042','字典管理','字典管理','1501368407743528962','dict',999,0,'2022-03-09 09:28:25','2023-01-30 15:43:55',0,'system/dict','dict',NULL,'dict:list',1,4),
	 ('1501369997368082434','删除菜单','删除菜单','1195268474480156675',NULL,997,0,'2022-03-09 09:31:16','2022-05-15 22:17:01',0,NULL,NULL,NULL,'menu:del',2,0);
INSERT INTO mango_blog.t_menu (id,name,title,pid,icon,sort,is_deleted,create_time,update_time,hidden,component,`path`,redirect,permission,menu_type,sub_count) VALUES
	 ('1501483509264281601','修改菜单','编辑菜单','1195268474480156675',NULL,998,0,'2022-03-09 17:02:19','2022-05-15 22:19:03',0,NULL,NULL,NULL,'menu:edit',2,0),
	 ('1501490155294052354','角色管理','角色管理','1195268474480156673','adminRole',997,0,'2022-03-09 17:28:44','2022-05-15 22:17:57',0,'authority/role','role',NULL,'role:list',1,5),
	 ('1503383805083586562','添加管理员','新增管理员','1195268474480156674',NULL,999,0,'2022-03-14 22:53:25','2022-05-15 22:13:16',0,NULL,NULL,NULL,'admin:add',2,0),
	 ('1504633021835083777','标签管理','标签管理','1195268474480156677','tag',998,0,'2022-03-18 09:37:22','2022-05-15 22:17:39',0,'blog/tag','tag',NULL,'tag:list',1,4),
	 ('1506144477000859650','友情链接','友情链接','1501368407743528962','link',999,0,'2022-03-22 13:43:21','2022-03-22 13:43:21',0,'system/link','link',NULL,'link:list',1,4),
	 ('1508097474904268802','系统监控','系统监控','0','monitor',996,0,'2022-03-27 23:08:23','2022-03-27 23:08:23',0,'Layout','/monitor',NULL,NULL,0,3),
	 ('1508097940568481794','服务监控','服务监控','1508097474904268802','dashboard',999,0,'2022-03-27 23:10:30','2022-03-27 23:10:30',0,'monitor/server','server',NULL,'server:list',1,0),
	 ('1508812944326385665','druid监控','druid监控','1508097474904268802','druid',999,0,'2022-03-29 22:26:53','2022-03-29 22:26:53',0,'monitor/druid','druid',NULL,'druid:list',1,0),
	 ('1513015571486470146','评论管理','评论管理','1501368407743528962','message',999,0,'2022-04-10 12:46:37','2022-04-16 08:19:58',0,'system/comment','comment',NULL,'comment:list',1,2),
	 ('1525841882105839617','新增标签','新增标签','1504633021835083777',NULL,999,0,'2022-05-15 22:13:48','2022-05-15 22:13:48',0,NULL,NULL,NULL,'tag:add',2,0);
INSERT INTO mango_blog.t_menu (id,name,title,pid,icon,sort,is_deleted,create_time,update_time,hidden,component,`path`,redirect,permission,menu_type,sub_count) VALUES
	 ('1525841954847653889','编辑标签','编辑标签','1504633021835083777',NULL,998,0,'2022-05-15 22:14:05','2022-05-15 22:14:05',0,NULL,NULL,NULL,'tag:edit',2,0),
	 ('1525842038595321857','删除标签','删除标签','1504633021835083777',NULL,997,0,'2022-05-15 22:14:25','2022-05-15 22:14:25',0,NULL,NULL,NULL,'tag:del',2,0),
	 ('1525842246624411649','新增分类','新增分类','1195268474480156679',NULL,999,0,'2022-05-15 22:15:15','2022-05-15 22:15:15',0,NULL,NULL,NULL,'sort:add',2,0),
	 ('1525842314769268737','编辑分类','编辑分类','1195268474480156679',NULL,998,0,'2022-05-15 22:15:31','2022-05-15 22:15:31',0,NULL,NULL,NULL,'sort:edit',2,0),
	 ('1525842371665002498','删除分类','删除分类','1195268474480156679',NULL,997,0,'2022-05-15 22:15:45','2022-05-15 22:15:45',0,NULL,NULL,NULL,'sort:del',2,0),
	 ('1525842436408279041','新增博客','新增博客','1195268474480156678',NULL,999,0,'2022-05-15 22:16:00','2022-05-15 22:16:00',0,NULL,NULL,NULL,'blog:add',2,0),
	 ('1525842506499293186','编辑博客','编辑博客','1195268474480156678',NULL,998,0,'2022-05-15 22:16:17','2022-05-15 22:16:17',0,NULL,NULL,NULL,'blog:edit',2,0),
	 ('1525842561776025601','删除博客','删除博客','1195268474480156678',NULL,997,0,'2022-05-15 22:16:30','2022-05-15 22:16:30',0,NULL,NULL,NULL,'blog:del',2,0),
	 ('1525843063003742209','编辑管理员','编辑管理员','1195268474480156674',NULL,998,0,'2022-05-15 22:18:30','2022-05-15 22:18:30',0,NULL,NULL,NULL,'admin:edit',2,0),
	 ('1525843118246920193','删除管理员','删除管理员','1195268474480156674',NULL,997,0,'2022-05-15 22:18:43','2022-05-15 22:18:43',0,NULL,NULL,NULL,'admin:del',2,0);
INSERT INTO mango_blog.t_menu (id,name,title,pid,icon,sort,is_deleted,create_time,update_time,hidden,component,`path`,redirect,permission,menu_type,sub_count) VALUES
	 ('1525843281111744514','新增角色','新增角色','1501490155294052354',NULL,999,0,'2022-05-15 22:19:22','2022-05-15 22:19:22',0,NULL,NULL,NULL,'role:add',2,0),
	 ('1525843351437639681','编辑角色','编辑角色','1501490155294052354',NULL,998,0,'2022-05-15 22:19:38','2022-05-15 22:19:38',0,NULL,NULL,NULL,'role:edit',2,0),
	 ('1525843407997829121','删除角色','删除角色','1501490155294052354',NULL,997,0,'2022-05-15 22:19:52','2022-05-15 22:19:52',0,NULL,NULL,NULL,'role:del',2,0),
	 ('1525843528609234945','删除评论','删除评论','1513015571486470146',NULL,999,0,'2022-05-15 22:20:21','2022-05-15 22:20:21',0,NULL,NULL,NULL,'comment:del',2,0),
	 ('1525843884231688194','新增字典','新增字典','1501369279194071042',NULL,999,0,'2022-05-15 22:21:45','2022-05-15 22:21:45',0,NULL,NULL,NULL,'dict:add',2,0),
	 ('1525843954922487810','编辑字典','编辑字典','1501369279194071042',NULL,998,0,'2022-05-15 22:22:02','2022-05-15 22:22:02',0,NULL,NULL,NULL,'dict:edit',2,0),
	 ('1525844040964440065','删除字典','删除字典','1501369279194071042',NULL,997,0,'2022-05-15 22:22:23','2022-05-15 22:22:23',0,NULL,NULL,NULL,'dict:del',2,0),
	 ('1525844298687643650','新增友链','新增友链','1506144477000859650',NULL,999,0,'2022-05-15 22:23:24','2022-05-15 22:23:24',0,NULL,NULL,NULL,'link:add',2,0),
	 ('1525844355809869826','编辑友链','编辑友链','1506144477000859650',NULL,998,0,'2022-05-15 22:23:38','2022-05-15 22:23:38',0,NULL,NULL,NULL,'link:edit',2,0),
	 ('1525844427620548610','删除友链','删除友链','1506144477000859650',NULL,997,0,'2022-05-15 22:23:55','2022-05-15 22:23:55',0,NULL,NULL,NULL,'link:del',2,0);
INSERT INTO mango_blog.t_menu (id,name,title,pid,icon,sort,is_deleted,create_time,update_time,hidden,component,`path`,redirect,permission,menu_type,sub_count) VALUES
	 ('1525844539302281217','绑定菜单','绑定菜单','1501490155294052354',NULL,996,1,'2022-05-15 22:24:22','2022-05-15 22:24:22',0,NULL,NULL,NULL,'role:menu',2,0),
	 ('1525844698102824962','删除记录','删除记录','1515127581015547905',NULL,999,1,'2022-05-15 22:25:00','2022-05-15 22:25:00',0,NULL,NULL,NULL,'webVisit:del',2,0),
	 ('1605497765780004865','异常日志','异常日志','1501368407743528962','bug',999,1,'2022-12-21 17:37:51','2022-12-21 17:38:20',0,'exceptionLog','exceptionLog',NULL,'exceptionLog:list',1,0),
	 ('1605498324268359681','日志管理','日志管理','0','log',995,0,'2022-12-21 17:40:04','2022-12-21 17:58:38',0,'Layout','/log',NULL,NULL,0,3),
	 ('1605499105654611969','门户访问日志','门户访问日志','1605498324268359681','web',999,0,'2022-12-21 17:43:10','2022-12-21 17:43:10',0,'log/webVisit','webVisit',NULL,'webVisit:list',1,2),
	 ('1605499462208200705','删除日志','删除日志','1605499105654611969',NULL,999,0,'2022-12-21 17:44:35','2022-12-21 17:44:35',0,NULL,NULL,NULL,'webVisit:del',2,0),
	 ('1605500627012558849','异常日志','异常日志','1605498324268359681','bug',999,0,'2022-12-21 17:49:13','2022-12-21 17:49:13',0,'log/exceptionLog','exceptionLog',NULL,'exceptionLog:list',1,2),
	 ('1605504047601680386','删除按钮','删除按钮','1605500627012558849',NULL,999,0,'2022-12-21 18:02:48','2022-12-21 18:02:48',0,NULL,NULL,NULL,'exceptionLog:del',2,0),
	 ('1619964533705555970','网站配置','网站配置','1501368407743528962','web',999,0,'2023-01-30 15:43:37','2023-01-30 15:43:37',0,'system/webConfig','webConfig',NULL,NULL,1,3),
	 ('1619965101333299201','保存配置','保存配置','1619964533705555970',NULL,999,0,'2023-01-30 15:45:52','2023-01-30 15:45:52',0,NULL,NULL,NULL,'webConfig:add',2,0);
INSERT INTO mango_blog.t_menu (id,name,title,pid,icon,sort,is_deleted,create_time,update_time,hidden,component,`path`,redirect,permission,menu_type,sub_count) VALUES
	 ('1620668972804349953','删除配置','删除配置','1619964533705555970',NULL,999,0,'2023-02-01 14:22:48','2023-02-01 14:22:48',0,NULL,NULL,NULL,'webConfig:del',2,0),
	 ('1625745228759588865','用户管理','用户管理','1195268474480156673','user',999,0,'2023-02-15 14:34:02','2023-02-15 14:34:02',0,'authority/user','user',NULL,'user:list',1,2),
	 ('1625745652300406786','编辑用户','编辑用户','1625745228759588865',NULL,999,0,'2023-02-15 14:35:43','2023-02-15 14:35:43',0,NULL,NULL,NULL,'user:edit',2,0);

-- mango_blog.t_role definition

CREATE TABLE `t_role` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `summary` varchar(255) DEFAULT NULL COMMENT '角色介绍',
  `level` int unsigned DEFAULT NULL COMMENT '级别，数值越小，级别越大',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色表';

INSERT INTO mango_blog.t_role (id,role_name,create_time,update_time,summary,`level`) VALUES
	 ('1395268474480156673','admin','2021-10-02 22:52:24','2022-04-06 22:58:14','管理员',1),
	 ('1395268474480156674','visitor','2021-12-02 21:58:50','2022-05-15 17:38:20','游客',2);


-- mango_blog.t_role_admin definition

CREATE TABLE `t_role_admin` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `role_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色ID',
  `admin_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ROLE_ADMIN_UK` (`role_id`,`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色管理员中间表';

INSERT INTO mango_blog.t_role_admin (id,role_id,admin_id,create_time,update_time) VALUES
	 ('1512987650810048514','1395268474480156673','1295268474480156673','2022-04-10 10:55:41','2022-04-10 10:55:41'),
	 ('1525754355122515970','1395268474480156674','1595268474480156674','2022-05-15 16:26:00','2022-05-15 16:26:00');

-- mango_blog.t_role_menu definition

CREATE TABLE `t_role_menu` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `role_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色ID',
  `menu_id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '菜单ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ROLE_MENU_UK` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO mango_blog.t_role_menu (id,role_id,menu_id,create_time,update_time) VALUES
	 ('1507906766557794306','1502546893753643010','1501368407743528962','2022-03-27 10:26:03','2022-03-27 10:26:03'),
	 ('1507906766822035458','1502546893753643010','1501369279194071042','2022-03-27 10:26:03','2022-03-27 10:26:03'),
	 ('1507906767212105729','1502546893753643010','1506144477000859650','2022-03-27 10:26:04','2022-03-27 10:26:04'),
	 ('1625750761860259841','1395268474480156674','1195268474480156673','2023-02-15 14:56:01','2023-02-15 14:56:01'),
	 ('1625750762065780738','1395268474480156674','1195268474480156674','2023-02-15 14:56:01','2023-02-15 14:56:01'),
	 ('1625750762279690242','1395268474480156674','1195268474480156675','2023-02-15 14:56:01','2023-02-15 14:56:01'),
	 ('1625750762493599746','1395268474480156674','1195268474480156677','2023-02-15 14:56:01','2023-02-15 14:56:01'),
	 ('1625750762703314945','1395268474480156674','1195268474480156678','2023-02-15 14:56:01','2023-02-15 14:56:01'),
	 ('1625750762913030146','1395268474480156674','1195268474480156679','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750763173076994','1395268474480156674','1501368407743528962','2023-02-15 14:56:02','2023-02-15 14:56:02');
INSERT INTO mango_blog.t_role_menu (id,role_id,menu_id,create_time,update_time) VALUES
	 ('1625750763382792193','1395268474480156674','1501369279194071042','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750763592507393','1395268474480156674','1501490155294052354','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750763806416898','1395268474480156674','1504633021835083777','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750764011937793','1395268474480156674','1506144477000859650','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750764221652994','1395268474480156674','1508097474904268802','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750764431368193','1395268474480156674','1508097940568481794','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750764678832130','1395268474480156674','1508812944326385665','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750764896935938','1395268474480156674','1513015571486470146','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750765110845441','1395268474480156674','1605498324268359681','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750765316366337','1395268474480156674','1605499105654611969','2023-02-15 14:56:02','2023-02-15 14:56:02');
INSERT INTO mango_blog.t_role_menu (id,role_id,menu_id,create_time,update_time) VALUES
	 ('1625750765526081537','1395268474480156674','1605500627012558849','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750765752573953','1395268474480156674','1619964533705555970','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625750765979066370','1395268474480156674','1625745228759588865','2023-02-15 14:56:02','2023-02-15 14:56:02'),
	 ('1625752393171886081','1395268474480156673','1195268474480156673','2023-02-15 15:02:30','2023-02-15 15:02:30'),
	 ('1625752393406767106','1395268474480156673','1195268474480156674','2023-02-15 15:02:30','2023-02-15 15:02:30'),
	 ('1625752393645842434','1395268474480156673','1195268474480156675','2023-02-15 15:02:30','2023-02-15 15:02:30'),
	 ('1625752394816053249','1395268474480156673','1195268474480156676','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752395076100097','1395268474480156673','1195268474480156677','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752395331952642','1395268474480156673','1195268474480156678','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752395654914050','1395268474480156673','1195268474480156679','2023-02-15 15:02:31','2023-02-15 15:02:31');
INSERT INTO mango_blog.t_role_menu (id,role_id,menu_id,create_time,update_time) VALUES
	 ('1625752395889795074','1395268474480156673','1501368407743528962','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752396980314113','1395268474480156673','1501369279194071042','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752397223583746','1395268474480156673','1501369997368082434','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752397538156545','1395268474480156673','1501483509264281601','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752397777231873','1395268474480156673','1501490155294052354','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752398016307202','1395268474480156673','1503383805083586562','2023-02-15 15:02:31','2023-02-15 15:02:31'),
	 ('1625752399073271810','1395268474480156673','1504633021835083777','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752399433981954','1395268474480156673','1506144477000859650','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752399664668673','1395268474480156673','1508097474904268802','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752399949881345','1395268474480156673','1508097940568481794','2023-02-15 15:02:32','2023-02-15 15:02:32');
INSERT INTO mango_blog.t_role_menu (id,role_id,menu_id,create_time,update_time) VALUES
	 ('1625752400197345282','1395268474480156673','1508812944326385665','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752401283670017','1395268474480156673','1513015571486470146','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752401514356738','1395268474480156673','1525841882105839617','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752401753432066','1395268474480156673','1525841954847653889','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752401988313090','1395268474480156673','1525842038595321857','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752402248359938','1395268474480156673','1525842246624411649','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752402487435266','1395268474480156673','1525842314769268737','2023-02-15 15:02:32','2023-02-15 15:02:32'),
	 ('1625752403590537217','1395268474480156673','1525842371665002498','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752403825418241','1395268474480156673','1525842436408279041','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752404131602433','1395268474480156673','1525842506499293186','2023-02-15 15:02:33','2023-02-15 15:02:33');
INSERT INTO mango_blog.t_role_menu (id,role_id,menu_id,create_time,update_time) VALUES
	 ('1625752404366483458','1395268474480156673','1525842561776025601','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752404613947393','1395268474480156673','1525843063003742209','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752405117263873','1395268474480156673','1525843118246920193','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752405360533506','1395268474480156673','1525843281111744514','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752405666717697','1395268474480156673','1525843351437639681','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752406027427841','1395268474480156673','1525843407997829121','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752406262308866','1395268474480156673','1525843528609234945','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752406497189890','1395268474480156673','1525843884231688194','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752406736265217','1395268474480156673','1525843954922487810','2023-02-15 15:02:33','2023-02-15 15:02:33'),
	 ('1625752407868727297','1395268474480156673','1525844040964440065','2023-02-15 15:02:34','2023-02-15 15:02:34');
INSERT INTO mango_blog.t_role_menu (id,role_id,menu_id,create_time,update_time) VALUES
	 ('1625752408208465922','1395268474480156673','1525844298687643650','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752408443346945','1395268474480156673','1525844355809869826','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752408686616578','1395268474480156673','1525844427620548610','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752408942469122','1395268474480156673','1605498324268359681','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752409517088770','1395268474480156673','1605499105654611969','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752409760358402','1395268474480156673','1605499462208200705','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752409991045121','1395268474480156673','1605500627012558849','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752410225926145','1395268474480156673','1605504047601680386','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752410460807169','1395268474480156673','1619964533705555970','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752410737631234','1395268474480156673','1619965101333299201','2023-02-15 15:02:34','2023-02-15 15:02:34');
INSERT INTO mango_blog.t_role_menu (id,role_id,menu_id,create_time,update_time) VALUES
	 ('1625752410976706561','1395268474480156673','1625745228759588865','2023-02-15 15:02:34','2023-02-15 15:02:34'),
	 ('1625752411207393282','1395268474480156673','1625745652300406786','2023-02-15 15:02:35','2023-02-15 15:02:35'),
	 ('1625752412260163585','1395268474480156673','1620668972804349953','2023-02-15 15:02:35','2023-02-15 15:02:35');

-- mango_blog.t_sort definition

CREATE TABLE `t_sort` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `sort_name` varchar(255) DEFAULT NULL COMMENT '分类内容',
  `summary` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `click_count` int DEFAULT '0' COMMENT '点击数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客分类表';


-- mango_blog.t_tag definition

CREATE TABLE `t_tag` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `summary` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '标签描述',
  `click_count` int NOT NULL DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `tag_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '标签名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='标签表';


-- mango_blog.t_user definition

CREATE TABLE `t_user` (
  `id` varchar(19) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '唯一id',
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `gender` tinyint unsigned DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '个人头像',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机',
  `login_count` int unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态：0-禁用; 1-激活',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `nickname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '昵称',
  `qq_number` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '微信号',
  `comment_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '评论状态 1:正常 0:禁言',
  `user_tag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户标签：0：普通用户，1：管理员，2：博主 等',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户表';


-- mango_blog.t_web_config definition

CREATE TABLE `t_web_config` (
  `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '网站名称',
  `summary` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `author` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '作者',
  `record_num` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备案号',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `github` varchar(255) DEFAULT NULL COMMENT 'github地址',
  `gitee` varchar(255) DEFAULT NULL COMMENT 'gitee地址',
  `rolling_sentences` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '首页滚动句子',
  `friend_link_desc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '友链简介',
  `friend_link_url` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '友链地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='web配置表';

INSERT INTO mango_blog.t_web_config (id,name,summary,author,record_num,create_time,update_time,github,gitee,rolling_sentences,friend_link_desc,friend_link_url) VALUES
	 ('1619987156988645378','Lisite''s Blog','Hi，欢迎来到 Lisite''s Blog，这是一个使用 vue 开发的，记录学习与生活的个人博客，你可以在<a href="http://web.mangoblog.vip/blog/1625704224711094273">这里</a>了解到关于本站的更多技术细节。','Lisite','鲁ICP备 2022023673号','2023-01-30 17:13:31','2023-02-20 17:50:50','https://github.com/guang1997','https://gitee.com/lisite1997','小鱼干是换不来幸福的，因为它就是幸福本身##有些事本来很遥远，你争取，它就会离你愈来愈近','这是一个记录技术的博客网站','http://web.mangoblog.vip');

-- mango_blog.t_web_visit definition

CREATE TABLE `t_web_visit` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `ip` varchar(255) DEFAULT NULL COMMENT '访问ip地址',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `os` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器',
  `ip_source` varchar(255) DEFAULT NULL COMMENT 'ip来源',
  `behavior` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户行为',
  `content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '行为内容',
  `request_time` timestamp NULL DEFAULT NULL COMMENT '请求时间',
  `is_menu` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否是菜单1:是0:否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='Web访问记录表';

