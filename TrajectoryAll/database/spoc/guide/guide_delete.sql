DROP PROCEDURE IF EXISTS `guide_delete`;

DELIMITER $$
CREATE PROCEDURE `guide_delete`(
	in in_num int
)
BEGIN
	delete from guide where num = in_num;
END$$

DELIMITER ;
