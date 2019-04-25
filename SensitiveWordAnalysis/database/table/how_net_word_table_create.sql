DROP PROCEDURE IF EXISTS `how_net_word_table_create`;

DELIMITER $$
CREATE PROCEDURE `how_net_word_table_create`()
BEGIN
    create table if not exists how_net_word (
		id int primary key auto_increment not null, 
        W_C text,
        G_C text,
        S_C text,
        W_E text,
        G_E text,
        S_E text,
        DEF text
	) default charset=utf8mb4;
END$$

DELIMITER ;
