DROP PROCEDURE IF EXISTS `guide_update`;

DELIMITER $$
CREATE PROCEDURE `guide_update`(
	in in_num int,
    in in_table_name varchar(45)
)
BEGIN
	update guide set table_name = in_table_name where num = in_num;
END$$

DELIMITER ;
