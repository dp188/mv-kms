CREATE TABLE `t_kms_many2many_room` (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `call_type` bigint(20) DEFAULT 1,
  `key` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0 关闭 1 直播中',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `live_path` varchar(2000) DEFAULT NULL COMMENT '文件存储路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;