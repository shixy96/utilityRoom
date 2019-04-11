DROP PROCEDURE IF EXISTS `spider_text_nonrepeat_delete`;

DELIMITER $$
CREATE PROCEDURE `spider_text_nonrepeat_delete`(
	in in_id int
)
BEGIN
    delete from weibo_spider_nonrepeat_comment where id = in_id;
END$$

DELIMITER ;
