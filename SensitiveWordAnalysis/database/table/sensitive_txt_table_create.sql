DROP PROCEDURE IF EXISTS `sensitive_txt_table_create`;

DELIMITER $$
CREATE PROCEDURE `sensitive_txt_table_create`()
BEGIN
    create table if not exists sensitive_txt (
		id int primary key auto_increment not null, 
        content varchar(256)
	) default charset=utf8mb4;
END$$

DELIMITER ;
