DROP PROCEDURE IF EXISTS `sensitive_word_delete`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_delete`(
	in in_content varchar(256),
    in in_nature varchar(32)
)
BEGIN
    delete from sensitive_word 
    where content=in_content
    and (in_nature=null or in_nature=nature);
END$$

DELIMITER ;
