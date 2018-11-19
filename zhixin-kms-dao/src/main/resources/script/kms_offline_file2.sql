CREATE TABLE `t_kms_offline_file` (
  `id` bigint(20) NOT NULL,
  `key` varchar(100) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_time_show` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `filename` varchar(2000) DEFAULT NULL COMMENT '文件名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;