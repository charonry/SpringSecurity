/*
Navicat MySQL Data Transfer

Source Server         : springsecurity
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : testdb

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2021-04-14 20:44:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_pid` int(11) NOT NULL COMMENT '父菜单ID',
  `menu_pids` varchar(64) NOT NULL COMMENT '当前菜单所有父菜单',
  `is_leaf` tinyint(4) NOT NULL COMMENT '0:不是叶子节点，1:是叶子节点',
  `menu_name` varchar(16) NOT NULL COMMENT '菜单名称',
  `url` varchar(64) DEFAULT NULL COMMENT '跳转URL',
  `icon` varchar(45) DEFAULT NULL,
  `icon_color` varchar(16) DEFAULT NULL,
  `sort` tinyint(4) DEFAULT NULL COMMENT '排序',
  `level` tinyint(4) NOT NULL COMMENT '菜单层级',
  `status` tinyint(4) NOT NULL COMMENT '0:启用,1:禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '0', '0', '系统管理', null, null, null, '1', '1', '0');
INSERT INTO `sys_menu` VALUES ('2', '1', '1', '1', '用户管理', '/sys_user', null, null, '1', '2', '0');
INSERT INTO `sys_menu` VALUES ('3', '1', '1', '1', '日志管理', '/sys_log', null, null, '2', '2', '0');
INSERT INTO `sys_menu` VALUES ('4', '1', '1', '1', '业务一', '/biz1', null, null, '3', '2', '0');
INSERT INTO `sys_menu` VALUES ('5', '1', '1', '1', '业务二', '/biz2', null, null, '4', '2', '0');

-- ----------------------------
-- Table structure for `sys_org`
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_pid` int(11) NOT NULL COMMENT '上级组织编码',
  `org_pids` varchar(64) NOT NULL COMMENT '所有的父节点id',
  `is_leaf` tinyint(4) NOT NULL COMMENT '0:不是叶子节点，1:是叶子节点',
  `org_name` varchar(32) NOT NULL COMMENT '组织名',
  `address` varchar(64) DEFAULT NULL COMMENT '地址',
  `phone` varchar(13) DEFAULT NULL COMMENT '电话',
  `email` varchar(32) DEFAULT NULL COMMENT '邮件',
  `sort` tinyint(4) DEFAULT NULL COMMENT '排序',
  `level` tinyint(4) NOT NULL COMMENT '组织层级',
  `status` tinyint(4) NOT NULL COMMENT '0:启用,1:禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='系统组织结构表';

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES ('1', '0', '0', '0', '总部', null, null, null, '1', '1', '0');
INSERT INTO `sys_org` VALUES ('2', '1', '1', '0', '研发部', null, null, null, '1', '2', '0');
INSERT INTO `sys_org` VALUES ('3', '2', '1,2', '1', '研发一部', null, null, null, '1', '3', '0');
INSERT INTO `sys_org` VALUES ('4', '2', '1,2', '1', '研发二部', null, null, null, '2', '3', '0');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(32) NOT NULL DEFAULT '0' COMMENT '角色名称(汉字)',
  `role_desc` varchar(128) NOT NULL DEFAULT '0' COMMENT '角色描述',
  `role_code` varchar(32) NOT NULL DEFAULT '0' COMMENT '角色的英文code.如：ADMIN',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '角色顺序',
  `status` int(11) DEFAULT NULL COMMENT '0表示可用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '角色的创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', '管理员', 'admin', '1', '0', '2019-12-23 22:56:48');
INSERT INTO `sys_role` VALUES ('2', '普通用户', '普通用户', 'common', '2', '0', '2019-12-23 22:57:22');

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色id',
  `menu_id` int(11) NOT NULL DEFAULT '0' COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色权限关系表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('2', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('3', '2', '4');
INSERT INTO `sys_role_menu` VALUES ('4', '2', '5');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL DEFAULT '0' COMMENT '用户名',
  `password` varchar(64) NOT NULL DEFAULT '0' COMMENT '密码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `org_id` int(11) NOT NULL COMMENT '组织id',
  `enabled` int(11) DEFAULT NULL COMMENT '0无效用户，1是有效用户',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号',
  `email` varchar(32) DEFAULT NULL COMMENT 'email',
  `accountNonLocked` int(1) NOT NULL COMMENT '0锁定，1是为被锁',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'yanfa1', '$2a$10$xPNoI0sBxOY6Y5Nj1bF6iO6OePqJ8tAJUsD5x5wh6G1BPphhSLcae', '2019-12-24 01:10:14', '3', '1', null, null);
INSERT INTO `sys_user` VALUES ('2', 'admin', '$2a$10$xPNoI0sBxOY6Y5Nj1bF6iO6OePqJ8tAJUsD5x5wh6G1BPphhSLcae', '2019-12-24 01:10:18', '1', '1', null, null);

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色自增id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户自增id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '2', '1');
INSERT INTO `sys_user_role` VALUES ('2', '1', '2');
