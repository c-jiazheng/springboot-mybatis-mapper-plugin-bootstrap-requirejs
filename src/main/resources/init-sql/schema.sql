SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_module`
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `res_name` varchar(32) DEFAULT NULL,
  `res_url` varchar(50) DEFAULT NULL,
  `res_level` int(2) DEFAULT NULL COMMENT '1.URL, 2.功能',
  `res_permission` varchar(255) DEFAULT NULL,
  `res_no` varchar(32) DEFAULT NULL,
  `parent_res_no` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module
-- ----------------------------
INSERT INTO `t_resource` VALUES ('1', '用户管理', '/pages/jsp/sys/user/userIndex.jsp', '1', 'sys:user:*', '001', null, '2016-06-01 23:41:39');
INSERT INTO `t_resource` VALUES ('2', '角色管理', '/role/index', '1', 'sys:role:*', '002', null, '2016-06-02 09:42:17');
INSERT INTO `t_resource` VALUES ('3', '资源管理', '/resource/index', '1', 'sys:resource:*', '003', null, '2016-06-03 21:42:17');
INSERT INTO `t_resource` VALUES ('4', '系统用户信息', '/pages/jsp/sys/user/userIndex.jsp', '2', 'sys:user:*', '001-001', '001', '2016-06-03 21:42:17');
INSERT INTO `t_resource` VALUES ('5', '添加', '/user/index', '3', 'sys:user:add', '001-001-001', '001-001', '2016-06-03 21:42:17');
INSERT INTO `t_resource` VALUES ('6', '修改', '/user/update', '3', 'sys:user:update', '001-001-002', '001-001', '2016-06-03 21:42:17');
INSERT INTO `t_resource` VALUES ('7', '删除', '/user/delete', '3', 'sys:user:delete', '001-001-003', '001-001', '2016-06-03 21:42:17');
INSERT INTO `t_resource` VALUES ('8', '用户角色信息', '/pages/jsp/sys/role/roleIndex.jsp', '2', 'sys:role:*', '002-001', '002', '2016-06-03 21:42:17');
INSERT INTO `t_resource` VALUES ('9', '添加', '/role/index', '3', 'sys:role:add', '002-001-001', '002-001', '2016-06-03 21:42:17');
INSERT INTO `t_resource` VALUES ('10', '修改', '/role/update', '3', 'sys:role:update', '002-001-002', '002-001', '2016-06-03 21:42:17');
INSERT INTO `t_resource` VALUES ('11', '删除', '/role/delete', '3', 'sys:role:delete', '002-001-003', '002-001', '2016-06-03 21:42:17');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(32) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `resource_id` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '管理员', '系统管理员', 'all', '2016-06-01 23:41:11');
INSERT INTO `t_role` VALUES ('2', '二级管理员', '下属管理员', '1,4,5,6,7', '2016-06-01 23:41:11');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `role_id` varchar(4) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', 'f6fdffe48c908deb0f4c3bd36c032e72', '1', 'Admin', '2016-06-01 23:35:17');
INSERT INTO `t_user` VALUES ('2', 'admin1', 'f6fdffe48c908deb0f4c3bd36c032e72', '1', 'Admin1', '2016-06-01 23:35:17');
INSERT INTO `t_user` VALUES ('3', 'admin2', 'f6fdffe48c908deb0f4c3bd36c032e72', '1', 'Admin2', '2016-06-01 23:35:17');


