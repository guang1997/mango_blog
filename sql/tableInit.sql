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
CREATE DATABASE IF NOT EXISTS `mango_blog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mango_blog`;

-- 导出  表 mango_blog.t_admin 结构
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE IF NOT EXISTS `t_admin` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `pass_word` varchar(255) NOT NULL COMMENT '密码',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `email` varchar(60) DEFAULT NULL COMMENT '邮箱',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机',
  `summary` varchar(200) DEFAULT NULL COMMENT '自我简介最多150字',
  `token` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户token信息',
  `login_count` int unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `qq_number` varchar(255) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `occupation` varchar(255) DEFAULT NULL COMMENT '职业',
  `github` varchar(255) DEFAULT NULL COMMENT 'github地址',
  `gitee` varchar(255) DEFAULT NULL COMMENT 'gitee地址',
  `person_resume` text COMMENT '履历',
  `is_deleted` tinyint DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='管理员表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_blog 结构
DROP TABLE IF EXISTS `t_blog`;
CREATE TABLE IF NOT EXISTS `t_blog` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `title` varchar(200) DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '博客简介',
  `content` longtext COMMENT '博客内容',
  `tag_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签id',
  `click_count` int DEFAULT '0' COMMENT '博客点击数',
  `collect_count` int DEFAULT '0' COMMENT '博客收藏数',
  `file_id` varchar(255) DEFAULT NULL COMMENT '标题图片id',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `admin_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '管理员id',
  `is_original` varchar(1) DEFAULT '1' COMMENT '是否原创（0:不是 1：是）',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `articles_part` varchar(255) DEFAULT NULL COMMENT '文章出处',
  `blog_sort_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客分类ID',
  `level` tinyint(1) DEFAULT '0' COMMENT '推荐等级(0:正常)',
  `is_publish` varchar(1) DEFAULT '1' COMMENT '是否发布：0：否，1：是',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  `open_comment` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型【0 博客， 1：推广】',
  `outside_link` varchar(1024) DEFAULT NULL COMMENT '外链【如果是推广，那么将跳转到外链】',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_blog_sort 结构
DROP TABLE IF EXISTS `t_blog_sort`;
CREATE TABLE IF NOT EXISTS `t_blog_sort` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `sort_name` varchar(255) DEFAULT NULL COMMENT '分类内容',
  `content` varchar(255) DEFAULT NULL COMMENT '分类简介',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `click_count` int DEFAULT '0' COMMENT '点击数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客分类表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_comment 结构
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE IF NOT EXISTS `t_comment` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `user_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户id',
  `to_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '回复某条评论的id',
  `to_user_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '回复某个人的id',
  `content` varchar(2048) DEFAULT NULL COMMENT '评论内容',
  `blog_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客id',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `source` varchar(255) NOT NULL COMMENT '评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等',
  `TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '评论类型 1:点赞 0:评论',
  `first_comment_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '一级评论ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='评论表';

-- 数据导出被取消选择。

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

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_link 结构
DROP TABLE IF EXISTS `t_link`;
CREATE TABLE IF NOT EXISTS `t_link` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `title` varchar(255) DEFAULT NULL COMMENT '友情链接标题',
  `summary` varchar(255) DEFAULT NULL COMMENT '友情链接介绍',
  `url` varchar(255) DEFAULT NULL COMMENT '友情链接URL',
  `click_count` int DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `link_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '友链状态： 0 申请中， 1：已上线，  2：已下架',
  `user_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请用户ID',
  `admin_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作管理员ID',
  `email` varchar(255) DEFAULT NULL COMMENT '站长邮箱',
  `file_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '网站图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='友情链接表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_menu 结构
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE IF NOT EXISTS `t_menu` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单标题',
  `menu_level` tinyint(1) DEFAULT NULL COMMENT '菜单级别',
  `summary` varchar(200) DEFAULT NULL COMMENT '简介',
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='菜单表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_permission 结构
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE IF NOT EXISTS `t_permission` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `api_name` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'API名称',
  `api_url` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'API请求地址',
  `api_method` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'API请求方式',
  `pid` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父ID',
  `api_sort` int NOT NULL COMMENT '排序',
  `description` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='API权限表';

-- 数据导出被取消选择。

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

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_role 结构
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色id',
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `summary` varchar(255) DEFAULT NULL COMMENT '角色介绍',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_role_admin 结构
DROP TABLE IF EXISTS `t_role_admin`;
CREATE TABLE IF NOT EXISTS `t_role_admin` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `role_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色ID',
  `admin_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色管理员中间表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_role_menu 结构
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE IF NOT EXISTS `t_role_menu` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `role_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色ID',
  `menu_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_role_permission 结构
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE IF NOT EXISTS `t_role_permission` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `role_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色ID',
  `permission_id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='API权限表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_sys_dict_data 结构
DROP TABLE IF EXISTS `t_sys_dict_data`;
CREATE TABLE IF NOT EXISTS `t_sys_dict_data` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `dict_type_id` varchar(255) DEFAULT NULL COMMENT '字典类型ID',
  `dict_label` varchar(255) DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典键值',
  `css_class` varchar(255) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(255) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认（1是 0否）,默认为0',
  `create_admin_id` varchar(32) DEFAULT NULL COMMENT '创建人id',
  `update_admin_id` varchar(32) DEFAULT NULL COMMENT '最后更新人id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='字典数据表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_sys_dict_type 结构
DROP TABLE IF EXISTS `t_sys_dict_type`;
CREATE TABLE IF NOT EXISTS `t_sys_dict_type` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `dict_name` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `dict_type` varchar(255) DEFAULT NULL COMMENT '字典类型',
  `create_admin_uid` varchar(32) NOT NULL COMMENT '创建人id',
  `update_admin_uid` varchar(32) NOT NULL COMMENT '最后更新人id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='字典类型表';

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_tag 结构
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一id',
  `content` varchar(1000) DEFAULT NULL COMMENT '标签内容',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int DEFAULT '0' COMMENT '标签简介',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='标签表';

-- 数据导出被取消选择。

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

-- 数据导出被取消选择。

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

-- 数据导出被取消选择。

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

-- 数据导出被取消选择。

-- 导出  表 mango_blog.t_web_visit 结构
DROP TABLE IF EXISTS `t_web_visit`;
CREATE TABLE IF NOT EXISTS `t_web_visit` (
  `id` varchar(19) NOT NULL COMMENT '主键',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `ip` varchar(255) DEFAULT NULL COMMENT '访问ip地址',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `os` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器',
  `ip_source` varchar(255) DEFAULT NULL COMMENT 'ip来源',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='Web访问记录表';

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
