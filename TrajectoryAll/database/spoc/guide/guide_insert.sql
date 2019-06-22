DROP PROCEDURE IF EXISTS `guide_insert`;

DELIMITER $$
CREATE PROCEDURE `guide_insert`(
	in in_num int,
    in in_table_name varchar(45)
)
BEGIN
	insert into guide (num, table_name) values (in_num, in_table_name);
END$$

DELIMITER ;
