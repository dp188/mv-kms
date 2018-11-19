CREATE TABLE `t_fmp_task` (
`id`  bigint(20) NOT NULL COMMENT '主键' ,
`task_name`  varchar(255) NULL COMMENT '任务名称（会议室名称）' ,
`file_path`  varchar(255) NULL COMMENT '视频文件以及字幕文件所处的路径' ,
`task_start_date`  timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务开始异常' ,
`task_status`  tinyint(4) NULL COMMENT '任务的状态：未开始，执行中，执行完成，执行失败' ,
`created_time`  timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '任务创建时间' ,
`count`  int NULL DEFAULT 0 COMMENT '当前是第几次' ,
`updated_time`  timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间' ,
`status`  tinyint(4) NULL COMMENT '状态：0删除 1 有效' ,
PRIMARY KEY (`id`)
)
;

