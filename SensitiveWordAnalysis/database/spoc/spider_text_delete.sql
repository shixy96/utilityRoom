DROP PROCEDURE IF EXISTS `spider_text_delete`;

DELIMITER $$
CREATE PROCEDURE `spider_text_delete`(
	in in_id int
)
BEGIN
    delete from weibo_spider_comment where id = in_id;
END$$

DELIMITER ;
