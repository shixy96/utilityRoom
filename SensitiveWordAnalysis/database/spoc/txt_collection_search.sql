DROP PROCEDURE IF EXISTS `txt_collection_search`;

DELIMITER $$
CREATE PROCEDURE `txt_collection_search`(
	in in_content text,
    in in_is_sensitive int,
    in in_offset int,
    in in_count int
)
BEGIN
    select t.* from txt_collection t 
    where 
    in_content is null or t.content like in_content
    and in_is_sensitive is null or t.is_sensitive = in_is_sensitive
    limit in_offset, in_count;
END$$

DELIMITER ;
