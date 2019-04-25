DROP PROCEDURE IF EXISTS `sensitive_word_search`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_search`(
	in in_content varchar(256),
    in in_nature_level varchar(32),
    in in_offset int,
    in in_count int
)
BEGIN
    select s.* from sensitive_word s 
    where 
    (in_content is null or in_content = s.content)
    and (in_nature_level is null or in_nature_level = s.nature_level)
    limit in_offset, in_count;
END$$

DELIMITER ;
