DROP PROCEDURE IF EXISTS `sensitive_word_delete`;

DELIMITER $$
CREATE PROCEDURE `sensitive_word_delete`(
	in in_content mediumtext
)
BEGIN
    delete from sensitive_word where content = in_content;
END$$

DELIMITER ;
