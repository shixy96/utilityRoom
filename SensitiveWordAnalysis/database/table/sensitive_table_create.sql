DROP PROCEDURE IF EXISTS `sensitive_table_create`;

DELIMITER $$
CREATE PROCEDURE `sensitive_table_create`()
BEGIN
    create table if not exists sensitive_word (
		id int primary key auto_increment not null, 
        content varchar(256)
	) default charset=utf8mb4;
END$$

DELIMITER ;
