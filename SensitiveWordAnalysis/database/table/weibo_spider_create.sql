drop table if exists weibo_spider_comment;
create table weibo_spider_comment (
	`id` int primary key auto_increment not null, 
	`text` text default null,
    `segment_num` int default null,
    `sensitive_level` double default null,
	`created_time` timestamp not null default current_timestamp,
	`updated_time` timestamp not null default current_timestamp on update current_timestamp
) engine=InnoDB default charset=utf8mb4;
