-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.26 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  10.2.0.5704
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 mango_blog.t_admin 结构
CREATE TABLE IF NOT EXISTS `t_admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `pass_word` varchar(255) NOT NULL COMMENT '密码',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `email` varchar(60) DEFAULT NULL COMMENT '邮箱',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机',
  `valid_code` varchar(50) DEFAULT NULL COMMENT '邮箱验证码',
  `summary` varchar(200) DEFAULT NULL COMMENT '自我简介最多150字',
  `login_count` int unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `qq_number` varchar(255) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `occupation` varchar(255) DEFAULT NULL COMMENT '职业',
  `github` varchar(255) DEFAULT NULL COMMENT 'github地址',
  `gitee` varchar(255) DEFAULT NULL COMMENT 'gitee地址',
  `role_id` int DEFAULT NULL COMMENT '拥有的角色id',
  `person_resume` text COMMENT '履历',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='管理员表';

-- 正在导出表  mango_blog.t_admin 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_admin` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_admin` ENABLE KEYS */;

-- 导出  表 mango_blog.t_blog 结构
CREATE TABLE IF NOT EXISTS `t_blog` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `title` varchar(200) DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '博客简介',
  `content` longtext COMMENT '博客内容',
  `tag_id` int DEFAULT NULL COMMENT '标签id',
  `click_count` int DEFAULT '0' COMMENT '博客点击数',
  `collect_count` int DEFAULT '0' COMMENT '博客收藏数',
  `file_id` varchar(255) DEFAULT NULL COMMENT '标题图片id',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `admin_id` int DEFAULT NULL COMMENT '管理员id',
  `is_original` varchar(1) DEFAULT '1' COMMENT '是否原创（0:不是 1：是）',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `articles_part` varchar(255) DEFAULT NULL COMMENT '文章出处',
  `blog_sort_id` int DEFAULT NULL COMMENT '博客分类ID',
  `level` tinyint(1) DEFAULT '0' COMMENT '推荐等级(0:正常)',
  `is_publish` varchar(1) DEFAULT '1' COMMENT '是否发布：0：否，1：是',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  `open_comment` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型【0 博客， 1：推广】',
  `outside_link` varchar(1024) DEFAULT NULL COMMENT '外链【如果是推广，那么将跳转到外链】',
  `is_deleted` tinyint unsigned NOT NULL COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客表';

-- 正在导出表  mango_blog.t_blog 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_blog` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_blog` ENABLE KEYS */;

-- 导出  表 mango_blog.t_blog_sort 结构
CREATE TABLE IF NOT EXISTS `t_blog_sort` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `sort_name` varchar(255) DEFAULT NULL COMMENT '分类内容',
  `content` varchar(255) DEFAULT NULL COMMENT '分类简介',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `click_count` int DEFAULT '0' COMMENT '点击数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='博客分类表';

-- 正在导出表  mango_blog.t_blog_sort 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_blog_sort` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_blog_sort` ENABLE KEYS */;

-- 导出  表 mango_blog.t_category_menu 结构
CREATE TABLE IF NOT EXISTS `t_category_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `menu_level` tinyint(1) DEFAULT NULL COMMENT '菜单级别',
  `summary` varchar(200) DEFAULT NULL COMMENT '简介',
  `parent_id` int DEFAULT NULL COMMENT '父id',
  `url` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `is_show` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示 1:是 0:否',
  `menu_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '菜单类型 0: 菜单   1: 按钮',
  `is_jump_external_url` tinyint(1) DEFAULT '0' COMMENT '是否跳转外部链接 0：否，1：是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='菜单表';

-- 正在导出表  mango_blog.t_category_menu 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_category_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_category_menu` ENABLE KEYS */;

-- 导出  表 mango_blog.t_comment 结构
CREATE TABLE IF NOT EXISTS `t_comment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `to_id` int DEFAULT NULL COMMENT '回复某条评论的id',
  `to_user_id` int DEFAULT NULL COMMENT '回复某个人的id',
  `content` varchar(2048) DEFAULT NULL COMMENT '评论内容',
  `blog_id` int DEFAULT NULL COMMENT '博客id',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `source` varchar(255) NOT NULL COMMENT '评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等',
  `TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '评论类型 1:点赞 0:评论',
  `first_comment_id` int DEFAULT NULL COMMENT '一级评论ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='评论表';

-- 正在导出表  mango_blog.t_comment 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_comment` ENABLE KEYS */;

-- 导出  表 mango_blog.t_exception_log 结构
CREATE TABLE IF NOT EXISTS `t_exception_log` (
  `id` int NOT NULL COMMENT '唯一id',
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
/*!40000 ALTER TABLE `t_exception_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_exception_log` ENABLE KEYS */;

-- 导出  表 mango_blog.t_link 结构
CREATE TABLE IF NOT EXISTS `t_link` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `title` varchar(255) DEFAULT NULL COMMENT '友情链接标题',
  `summary` varchar(255) DEFAULT NULL COMMENT '友情链接介绍',
  `url` varchar(255) DEFAULT NULL COMMENT '友情链接URL',
  `click_count` int DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `link_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '友链状态： 0 申请中， 1：已上线，  2：已下架',
  `user_id` int DEFAULT NULL COMMENT '申请用户ID',
  `admin_id` int DEFAULT NULL COMMENT '操作管理员ID',
  `email` varchar(255) DEFAULT NULL COMMENT '站长邮箱',
  `file_id` int DEFAULT NULL COMMENT '网站图标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='友情链接表';

-- 正在导出表  mango_blog.t_link 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_link` ENABLE KEYS */;

-- 导出  表 mango_blog.t_picture 结构
CREATE TABLE IF NOT EXISTS `t_picture` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `file_id` varchar(255) DEFAULT NULL COMMENT '图片id',
  `pic_name` varchar(255) DEFAULT NULL COMMENT '图片名',
  `picture_sort_id` int DEFAULT NULL COMMENT '分类id',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='图片表';

-- 正在导出表  mango_blog.t_picture 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_picture` ENABLE KEYS */;

-- 导出  表 mango_blog.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `summary` varchar(255) DEFAULT NULL COMMENT '角色介绍',
  `category_menu_ids` text COMMENT '角色管辖的菜单的ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色表';

-- 正在导出表  mango_blog.t_role 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;

-- 导出  表 mango_blog.t_tag 结构
CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `content` varchar(1000) DEFAULT NULL COMMENT '标签内容',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int DEFAULT '0' COMMENT '标签简介',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='标签表';

-- 正在导出表  mango_blog.t_tag 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_tag` ENABLE KEYS */;

-- 导出  表 mango_blog.t_todo 结构
CREATE TABLE IF NOT EXISTS `t_todo` (
  `id` int NOT NULL COMMENT '唯一id',
  `admin_id` int DEFAULT NULL COMMENT '管理员id',
  `text` varchar(255) DEFAULT NULL COMMENT '内容',
  `done` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '表示事项是否完成（0：未完成 1：已完成）',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='待办事项表';

-- 正在导出表  mango_blog.t_todo 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `t_todo` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_todo` ENABLE KEYS */;

-- 导出  表 mango_blog.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
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
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

-- 导出  表 mango_blog.t_web_config 结构
CREATE TABLE IF NOT EXISTS `t_web_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
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
/*!40000 ALTER TABLE `t_web_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_web_config` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;