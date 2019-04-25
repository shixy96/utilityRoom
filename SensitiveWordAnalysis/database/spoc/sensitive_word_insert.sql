DROP PROCEDURE IF EXISTS `sensitive_word_insert`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_insert`(
	in in_content varchar(256),
    in in_nature_level varchar(32)
)
BEGIN
    insert into sensitive_word (content, nature_level) value (in_content, in_nature_level);
END$$

DELIMITER ;
