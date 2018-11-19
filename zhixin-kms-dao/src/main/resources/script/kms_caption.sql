CREATE TABLE `t_kms_caption` (
  `id` bigint(20) NOT NULL,
  `key` varchar(100) DEFAULT NULL,
  `cap_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cap_time_show` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `caption` varchar(2000) DEFAULT NULL COMMENT '字幕',
  `filename` varchar(2000) DEFAULT NULL COMMENT '字幕所属文件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;