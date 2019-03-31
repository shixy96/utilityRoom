DROP PROCEDURE IF EXISTS `sensitive_word_insert`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_insert`(
	in in_content mediumtext
)
BEGIN
    insert into sensitive_word (content) value (in_content);
END$$

DELIMITER ;
