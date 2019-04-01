DROP PROCEDURE IF EXISTS `sensitive_word_table_create`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_table_create`()
BEGIN
    create table if not exists sensitive_word (
		id int primary key auto_increment not null, 
        content varchar(256),
        nature varchar(32)
	) default charset=utf8mb4;
END$$

DELIMITER ;
