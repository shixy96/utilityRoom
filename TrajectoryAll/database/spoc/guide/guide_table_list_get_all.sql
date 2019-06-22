DROP PROCEDURE IF EXISTS `guide_table_List_get_all`;

DELIMITER $$
CREATE PROCEDURE `guide_table_List_get_all`()
BEGIN
	select g.num from guide g order by g.num;
END$$

DELIMITER ;
