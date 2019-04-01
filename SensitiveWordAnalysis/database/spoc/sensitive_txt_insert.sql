DROP PROCEDURE IF EXISTS `sensitive_txt_insert`;

DELIMITER $$
CREATE PROCEDURE `sensitive_txt_insert`(
	in in_content varchar(256)
)
BEGIN
    insert into sensitive_txt (content) value (in_content);
END$$

DELIMITER ;
