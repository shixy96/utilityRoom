DROP PROCEDURE IF EXISTS `guide_table_num_get_all`;

DELIMITER $$
CREATE PROCEDURE `guide_table_num_get_all`(
    OUT out_num INT(11)
)
BEGIN
	select count(*) into out_num from guide g;
END$$

DELIMITER ;
