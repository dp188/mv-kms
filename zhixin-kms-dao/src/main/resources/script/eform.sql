/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50623
Source Host           : 127.0.0.1:3306
Source Database       : eform

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2015-12-14 00:38:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_eform_data
-- ----------------------------
DROP TABLE IF EXISTS `t_eform_data`;
CREATE TABLE `t_eform_data` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `instance_id` bigint(20) DEFAULT NULL COMMENT 'eform实例id',
  `definition_id` bigint(20) DEFAULT NULL COMMENT '定义id',
  `data` longtext COLLATE utf8_bin COMMENT '数据体',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '删除状态(0删除 1正常)',
  PRIMARY KEY (`id`),
  KEY `fk_reference_4` (`definition_id`),
  KEY `fk_reference_5` (`instance_id`),
  CONSTRAINT `fk_reference_4` FOREIGN KEY (`definition_id`) REFERENCES `t_eform_definition` (`id`),
  CONSTRAINT `fk_reference_5` FOREIGN KEY (`instance_id`) REFERENCES `t_eform_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='EForm数据';

-- ----------------------------
-- Records of t_eform_data
-- ----------------------------
INSERT INTO `t_eform_data` VALUES ('3709280717172244480', '3709267160921669632', '3709230263700684800', 0x7B226964223A22222C22617661696C61626C65566172223A2234323334333234222C226E616D65223A22323334323334222C22656E616D65223A22323334222C2267656E657261746554797065223A2231222C2274656D706C61746555726C223A2233343233222C2276616C6964617465436C617373223A2232343233222C226E6F74696679436C617373223A2234333234222C226D61784E6F7469667954696D6573223A2234323334222C22617574686F72697469654F626A65637473223A22616E79222C2264657363223A2232343233227D, '2015-12-13 23:55:57', null, '2015-12-13 23:55:57', null, '1');

-- ----------------------------
-- Table structure for t_eform_definition
-- ----------------------------
DROP TABLE IF EXISTS `t_eform_definition`;
CREATE TABLE `t_eform_definition` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'eform定义名称',
  `ename` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'eform英文名称，对应mongoDB的table名称',
  `generate_type` tinyint(4) DEFAULT NULL COMMENT '生成类型（0 根据配置生成 1 根据模板生成 2 使用外部地址)',
  `available_var` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '模板可用变量（模板生成实例时，可以使用的变量），\r\n            比如询价单的原始数据结构json',
  `template_url` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '根据模板生成时，模板文件路径\r\n            如果是外部链接的话，此处是外部链接路径',
  `notify_class` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '接到数据后，通知处理类',
  `max_notify_times` int(11) DEFAULT NULL COMMENT '最大通知次数',
  `validate_class` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '接到数据后，数据校验处理类',
  `authoritie_objects` char(10) COLLATE utf8_bin DEFAULT NULL COMMENT '模板授权对象（any表示任何人都可以填，loginUser表示登录用户，其他角色名称）',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '删除状态(0删除 1正常)',
  `desc` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '定义描述',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='EForm定义';

-- ----------------------------
-- Records of t_eform_definition
-- ----------------------------
INSERT INTO `t_eform_definition` VALUES ('3709230192712089600', '中午能', '123', '1', '1321', '312', '1232', '1231', '132', 'loginUser', '0', '132', '2015-12-13 20:35:11', null, '2015-12-13 20:37:17', null);
INSERT INTO `t_eform_definition` VALUES ('3709230213322899456', '中午能', '123', '1', '1321', '312', '1232', '1231', '132', 'loginUser', '0', '132', '2015-12-13 20:35:16', null, '2015-12-13 20:55:25', null);
INSERT INTO `t_eform_definition` VALUES ('3709230263700684800', '供应商询价单', 'inquiry', '1', '{\r\n    id,询价单主键ID\r\n    no,询价单编号\r\n    date,询价单日期\r\n    list : [ 询价商品list\r\n        {\r\n          id,主键id\r\n          no,编号\r\n          brand,品牌\r\n          model,规格\r\n          name,名称\r\n          unit,单位\r\n          quantity,数量\r\n          price,价格\r\n          dates,货期\r\n          totalMoney总金额\r\n        }\r\n    ]\r\n}', 'eform/template/index', 'com.bwcmall.service.eform.impl.InquiryNotify', '3', 'com.bwcmall.service.eform.impl.InquiryDataValidate', 'loginUser', '1', '供应商询价单,基于模板定义', '2015-12-13 20:35:28', null, '2015-12-13 23:00:34', null);
INSERT INTO `t_eform_definition` VALUES ('3709230299931082752', '中午能', '123', '1', '1321', '312', '1232', '1231', '132', 'loginUser', '0', '132', '2015-12-13 20:35:37', null, '2015-12-13 20:55:27', null);
INSERT INTO `t_eform_definition` VALUES ('3709230701573439488', '213', '243', '1', '2423', '234', '24', '23432', '423', 'loginUser', '0', '2442', '2015-12-13 20:37:13', null, '2015-12-13 20:55:30', null);
INSERT INTO `t_eform_definition` VALUES ('3709240189508517888', '供应商询价单', 'inquiry', '2', '', 'http://eform.bwcmall/inquiry.html', 'com.bwcmall.service.eform.impl.InquiryNotify', '3', 'com.bwcmall.service.eform.impl.InquiryDataValidate', 'any', '1', '供应商询价单(基于外部地址)', '2015-12-13 21:14:55', null, '2015-12-13 21:14:55', null);

-- ----------------------------
-- Table structure for t_eform_ele_defintion
-- ----------------------------
DROP TABLE IF EXISTS `t_eform_ele_defintion`;
CREATE TABLE `t_eform_ele_defintion` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `definition_id` bigint(20) DEFAULT NULL COMMENT '定义id',
  `ele_type` tinyint(4) DEFAULT NULL COMMENT '元素类型:text hidden select teateare,rich textarea,label,div，checkbox，radio\r\n            对于checkbox，radio会根据data_source生成一组元素',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '元素显示中文名称',
  `ename` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '元素字段名称',
  `row_index` int(11) DEFAULT NULL COMMENT '元素在第几行',
  `ele_width` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '元素宽度',
  `ele_height` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '元素高度',
  `data_source_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '元素数据来源类型（0 确定值 1 后端数据字典）',
  `data_source` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '元素数据来源(type=0,填写json(key,value)数组，type=1，填写字典编码)',
  `ele_class` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '元素class',
  `ele_style` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '元素样式',
  `validate_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '数据校验类型(0 正则 1 非空 2 数字 3 金额 4 手机号码 5 身份证 6 js事件)',
  `validate_rule` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '校验规则(如果是字符正则，则填写长度范围，如果是js函数校验，则填写js函数，程序自动触发）',
  `data_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '元素数据类型(int,string,date)',
  `data_format` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '元素数据格式（如date指定yyyy-mm-dd)',
  `sn` int(11) DEFAULT NULL COMMENT '元素排序',
  `desc` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '元素描述（前端作为tips展示）',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '删除状态(0删除 1正常)',
  PRIMARY KEY (`id`),
  KEY `fk_reference_3` (`definition_id`),
  CONSTRAINT `fk_reference_3` FOREIGN KEY (`definition_id`) REFERENCES `t_eform_definition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='EForm元素定义';

-- ----------------------------
-- Records of t_eform_ele_defintion
-- ----------------------------

-- ----------------------------
-- Table structure for t_eform_instance
-- ----------------------------
DROP TABLE IF EXISTS `t_eform_instance`;
CREATE TABLE `t_eform_instance` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `definition_id` bigint(20) DEFAULT NULL COMMENT '定义ID',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'eform定义名称',
  `ename` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'eform英文名称，对应mongoDB的table名称',
  `authoritie_objects` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '模板授权对象（any表示任何人都可以填，loginUser表示登录用户，其他角色名称）',
  `share_url` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '分享页面地址',
  `notify_class` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '接到数据后，通知处理类',
  `validate_class` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '接到数据后，数据校验处理类',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '删除状态(0删除 1正常)',
  PRIMARY KEY (`id`),
  KEY `fk_reference_1` (`definition_id`),
  CONSTRAINT `fk_reference_1` FOREIGN KEY (`definition_id`) REFERENCES `t_eform_definition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='EForm实例';

-- ----------------------------
-- Records of t_eform_instance
-- ----------------------------
INSERT INTO `t_eform_instance` VALUES ('1', '3709230263700684800', '供应商询价单', 'inquiry', 'any', 'http://eform.bwcmall/inquiry.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 21:33:54', '1', '2015-12-13 23:01:42', '1', '0');
INSERT INTO `t_eform_instance` VALUES ('3709266202707755008', '3709230263700684800', 'Eform[供应商询价单]实例', 'inquiry', 'loginUser', 'http://127.0.0.1:8080/eform//eform/3709230263700684800/3709266202707755008.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 22:58:17', null, '2015-12-13 23:01:42', null, '0');
INSERT INTO `t_eform_instance` VALUES ('3709266612591919104', '3709230263700684800', 'Eform[供应商询价单]实例', 'inquiry', 'loginUser', 'http://127.0.0.1:8080/eform//eform/3709230263700684800/3709266612591919104.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 22:59:54', null, '2015-12-13 23:01:42', null, '0');
INSERT INTO `t_eform_instance` VALUES ('3709266799502688256', '3709230263700684800', 'Eform[供应商询价单]实例', 'inquiry', 'loginUser', 'http://127.0.0.1:8080/eform//eform/3709230263700684800/3709266799502688256.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 23:00:39', null, '2015-12-13 23:01:42', null, '0');
INSERT INTO `t_eform_instance` VALUES ('3709266885666275328', '3709230263700684800', 'Eform[供应商询价单]实例', 'inquiry', 'loginUser', 'http://127.0.0.1:8080/eform/eform/3709230263700684800/3709266885666275328.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 23:01:00', null, '2015-12-13 23:01:42', null, '0');
INSERT INTO `t_eform_instance` VALUES ('3709267080391032832', '3709240189508517888', 'Eform[供应商询价单]实例', 'inquiry', 'any', 'http://eform.bwcmall/inquiry.html?1=1&definitionId=3709240189508517888&instanceId=3709267080391032832', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 23:01:46', null, '2015-12-13 23:01:46', null, '1');
INSERT INTO `t_eform_instance` VALUES ('3709267101966532608', '3709230263700684800', 'Eform[供应商询价单]实例', 'inquiry', 'loginUser', 'http://127.0.0.1:8080/eform/eform/3709230263700684800/3709267101966532608.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 23:01:51', null, '2015-12-13 23:53:20', null, '0');
INSERT INTO `t_eform_instance` VALUES ('3709267116759842816', '3709230263700684800', 'Eform[供应商询价单]实例', 'inquiry', 'loginUser', 'http://127.0.0.1:8080/eform/eform/3709230263700684800/3709267116759842816.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 23:01:55', null, '2015-12-13 23:01:55', null, '1');
INSERT INTO `t_eform_instance` VALUES ('3709267160921669632', '3709230263700684800', 'Eform[供应商询价单]实例', 'inquiry', 'loginUser', 'http://127.0.0.1:8080/eform/eform/3709230263700684800/3709267160921669632.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 23:02:05', null, '2015-12-13 23:02:05', null, '1');
INSERT INTO `t_eform_instance` VALUES ('3709280012222988288', '3709230263700684800', 'Eform[供应商询价单]实例', 'inquiry', 'loginUser', 'http://127.0.0.1:8080/eform/eform/3709230263700684800/3709280012222988288.html', 'com.bwcmall.service.eform.impl.InquiryNotify', 'com.bwcmall.service.eform.impl.InquiryDataValidate', '2015-12-13 23:53:09', null, '2015-12-13 23:53:09', null, '1');

-- ----------------------------
-- Table structure for t_eform_notify
-- ----------------------------
DROP TABLE IF EXISTS `t_eform_notify`;
CREATE TABLE `t_eform_notify` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `instance_id` bigint(20) DEFAULT NULL COMMENT 'eform实例id',
  `notify_class` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '接到数据后，通知处理类',
  `notify_result` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '通知结果',
  `notify_status` tinyint(4) DEFAULT '0' COMMENT '通知状态(0未通知 1 通知中 2 通知成功 3 通知失败',
  `max_notify_times` int(11) DEFAULT NULL COMMENT '最大通知次数',
  `current_notify_time` int(11) DEFAULT '0' COMMENT '当前是第几次通知',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '删除状态(0删除 1正常)',
  `data_id` bigint(20) DEFAULT NULL COMMENT '数据主键ID',
  PRIMARY KEY (`id`),
  KEY `fk_reference_2` (`instance_id`),
  CONSTRAINT `fk_reference_2` FOREIGN KEY (`instance_id`) REFERENCES `t_eform_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='EForm通知队列';

-- ----------------------------
-- Records of t_eform_notify
-- ----------------------------
INSERT INTO `t_eform_notify` VALUES ('3709280717226770432', '3709267160921669632', 'com.bwcmall.service.eform.impl.InquiryNotify', null, '0', '3', '0', '2015-12-13 23:55:57', null, '2015-12-13 23:55:57', null, '1', '3709280717172244480');
