DROP PROCEDURE IF EXISTS `txt_collection_get_all_num`;

DELIMITER $$
CREATE PROCEDURE `txt_collection_get_all_num`()
BEGIN
    select count(*) from txt_collection;
END$$

DELIMITER ;
