DROP PROCEDURE IF EXISTS `table_add_index`;

DELIMITER $$
CREATE PROCEDURE `table_add_index`()
BEGIN
    alter table sensitive_word add index (nature_level);
    alter table sensitive_word add unique (content);
    alter table txt_collection add index (is_sensitive);
    #alter table txt_collection add index (content);
END$$

DELIMITER ;
