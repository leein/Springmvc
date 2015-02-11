/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50537
Source Host           : localhost:3306
Source Database       : cms

Target Server Type    : MYSQL
Target Server Version : 50537
File Encoding         : 65001

Date: 2015-02-10 15:13:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  KEY `sys_22` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=432 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('5', '刘芬', '24');
INSERT INTO `user` VALUES ('7', '王市', '29');
INSERT INTO `user` VALUES ('43', '地位', '100');
INSERT INTO `user` VALUES ('431', '为I哦我去', '231');
