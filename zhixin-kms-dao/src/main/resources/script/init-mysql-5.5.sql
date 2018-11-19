
CREATE TABLE `t_fmp_task` (
`id`  bigint(20) NOT NULL COMMENT '主键' ,
`task_name`  varchar(255) NULL COMMENT '任务名称（会议室名称）' ,
`file_path`  varchar(255) NULL COMMENT '视频文件以及字幕文件所处的路径' ,
`task_start_date`  timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务开始异常' ,
`task_status`  tinyint(4) NULL COMMENT '任务的状态：未开始，执行中，执行完成，执行失败' ,
`created_time`  timestamp  NULL COMMENT '任务创建时间' ,
`count`  int NULL DEFAULT 0 COMMENT '当前是第几次' ,
`updated_time`  timestamp NULL   COMMENT '修改时间' ,
`status`  tinyint(4) NULL COMMENT '状态：0删除 1 有效' ,
PRIMARY KEY (`id`)
)
;
CREATE TRIGGER `trigger_t_fmp_task_update` BEFORE update ON `t_fmp_task`
 FOR EACH ROW SET NEW.`task_start_date` = CURRENT_TIMESTAMP(),NEW.`updated_time` = CURRENT_TIMESTAMP();
 
CREATE TRIGGER `trigger_t_fmp_task_insert` BEFORE insert ON `t_fmp_task`
 FOR EACH ROW SET NEW.`updated_time` = CURRENT_TIMESTAMP(),NEW.`created_time` = CURRENT_TIMESTAMP();
 
CREATE TABLE `t_kms_room` (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `key` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0 关闭 1 直播中',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp  NULL,
  `video_start_time` timestamp NULL,
  `is_duplexing` varchar(10) DEFAULT NULL COMMENT 'false半双工，true双工',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `live_path` varchar(2000) DEFAULT NULL COMMENT '文件存储路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 CREATE TRIGGER `trigger_t_kms_room_update` BEFORE update ON `t_kms_room`
 FOR EACH ROW SET NEW.`updated_time` = CURRENT_TIMESTAMP();
 
CREATE TRIGGER `trigger_t_kms_room_insert` BEFORE insert ON `t_kms_room`
 FOR EACH ROW SET NEW.`updated_time` = CURRENT_TIMESTAMP(),NEW.`video_start_time` = CURRENT_TIMESTAMP();
 
CREATE TABLE `t_kms_many2many_room` (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `call_type` bigint(20) DEFAULT 1,
  `key` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0 关闭 1 直播中',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NULL,
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `live_path` varchar(2000) DEFAULT NULL COMMENT '文件存储路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 CREATE TRIGGER `trigger_t_kms_many2many_room_update` BEFORE update ON `t_kms_many2many_room`
 FOR EACH ROW SET NEW.`updated_time` = CURRENT_TIMESTAMP();
 
CREATE TRIGGER `trigger_t_kms_many2many_room_insert` BEFORE insert ON `t_kms_many2many_room`
 FOR EACH ROW SET NEW.`updated_time` = CURRENT_TIMESTAMP();
 
CREATE TABLE `t_kms_caption` (
  `id` bigint(20) NOT NULL,
  `key` varchar(100) DEFAULT NULL,
  `cap_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cap_time_show` timestamp  NULL,
  `caption` varchar(2000) DEFAULT NULL COMMENT '字幕',
  `filename` varchar(2000) DEFAULT NULL COMMENT '字幕所属文件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 CREATE TRIGGER `trigger_t_kms_caption_update` BEFORE update ON `t_kms_caption`
 FOR EACH ROW SET NEW.`cap_time_show` = CURRENT_TIMESTAMP();
 
CREATE TRIGGER `trigger_t_kms_caption_insert` BEFORE insert ON `t_kms_caption`
 FOR EACH ROW SET NEW.`cap_time_show` = CURRENT_TIMESTAMP();