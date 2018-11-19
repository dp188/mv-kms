CREATE TABLE `t_kms_room` (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `key` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0 关闭 1 直播中',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `video_start_time` timestamp NOT NULL,
  `is_duplexing` varchar(10) DEFAULT NULL COMMENT 'false半双工，true双工',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `live_path` varchar(2000) DEFAULT NULL COMMENT '文件存储路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;