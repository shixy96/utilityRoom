DROP PROCEDURE IF EXISTS `sensitive_txt_search`;

DELIMITER $$
CREATE PROCEDURE `sensitive_txt_search`(
	in in_content varchar(256)
)
BEGIN
    select s.content from sensitive_txt s where s.content = in_content;
END$$

DELIMITER ;
