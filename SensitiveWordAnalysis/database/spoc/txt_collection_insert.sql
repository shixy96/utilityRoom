DROP PROCEDURE IF EXISTS `txt_collection_insert`;

DELIMITER $$
CREATE PROCEDURE `txt_collection_insert`(
	in in_content text,
    in in_is_sensitive int,
    in in_vectors char(45)
)
BEGIN
    insert into txt_collection (content, is_sensitive, vectors) value (in_content, in_is_sensitive, in_vectors);
END$$

DELIMITER ;
