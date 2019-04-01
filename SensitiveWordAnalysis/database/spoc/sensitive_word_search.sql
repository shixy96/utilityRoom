DROP PROCEDURE IF EXISTS `sensitive_word_search`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_search`(
	in in_content varchar(256),
    in in_nature varchar(32)
)
BEGIN
    select s.content from sensitive_word s 
    where s.content = in_content
    and (in_nature=null or in_nature=nature);
END$$

DELIMITER ;
