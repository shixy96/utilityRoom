DROP PROCEDURE IF EXISTS `sensitive_word_insert`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_insert`(
	in in_content varchar(256),
    in in_nature varchar(32)
)
BEGIN
    insert into sensitive_word (content, nature) value (in_content, in_nature);
END$$

DELIMITER ;
