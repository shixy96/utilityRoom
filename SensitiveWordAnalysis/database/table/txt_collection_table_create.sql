DROP PROCEDURE IF EXISTS `txt_table_create`;

DELIMITER $$
CREATE PROCEDURE `txt_table_create`()
BEGIN
    create table if not exists txt_collection (
		id int primary key auto_increment not null, 
        content text,
        is_sensitive int
	) default charset=utf8mb4;
END$$

DELIMITER ;
