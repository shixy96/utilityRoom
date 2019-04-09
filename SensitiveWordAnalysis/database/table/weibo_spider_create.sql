DROP PROCEDURE IF EXISTS `weibo_spider_table_create`;

DELIMITER $$
CREATE PROCEDURE `weibo_spider_table_create`()
BEGIN
    create table if not exists weibo_spider (
		id int primary key auto_increment not null, 
		comment_id int,
		user_name varchar(256),
		create_at date,
		`text` text,
		likenum int,
		source text,
        content varchar(256)
	) default charset=utf8mb4;
END$$

DELIMITER ;
