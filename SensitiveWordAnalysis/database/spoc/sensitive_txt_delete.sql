DROP PROCEDURE IF EXISTS `sensitive_txt_delete`;

DELIMITER $$
CREATE PROCEDURE `sensitive_txt_delete`(
	in in_content text
)
BEGIN
    delete from sensitive_txt where content = in_content;
END$$

DELIMITER ;
