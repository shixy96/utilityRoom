DROP PROCEDURE IF EXISTS `txt_collection_delete`;

DELIMITER $$
CREATE PROCEDURE `txt_collection_delete`(
	in in_content text
)
BEGIN
    delete from txt_collection where content = in_content;
END$$

DELIMITER ;
