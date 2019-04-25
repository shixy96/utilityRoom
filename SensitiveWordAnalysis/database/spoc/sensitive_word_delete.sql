DROP PROCEDURE IF EXISTS `sensitive_word_delete`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_delete`(
	in in_id int,
	in in_content varchar(256),
    in in_nature_level varchar(32)
)
BEGIN
    delete from sensitive_word 
    where 
    id = in_id
    and (in_nature_level is null or content=in_content)
    and (in_nature_level is null or in_nature_level=nature_level);
END$$

DELIMITER ;
