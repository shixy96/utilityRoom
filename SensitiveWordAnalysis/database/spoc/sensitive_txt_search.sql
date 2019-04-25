DROP PROCEDURE IF EXISTS `sensitive_txt_search`;

DELIMITER $$
CREATE PROCEDURE `sensitive_txt_search`(
	in in_content text,
    in in_offset int,
    in in_count int
)
BEGIN
    select s.content from sensitive_txt s where 
    in_content is null or s.content = in_content
    limit in_offset, in_count;
END$$

DELIMITER ;
