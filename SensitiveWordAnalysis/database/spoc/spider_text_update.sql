DROP PROCEDURE IF EXISTS `spider_text_update`;

DELIMITER $$
CREATE PROCEDURE `spider_text_update`(
	in in_id int,
	in in_text text,
    in in_is_sensitive int,
    in in_segment_num int,
    in in_sensitive_level double
)
BEGIN
    update weibo_spider_comment set 
		`text` = in_text, 
        is_sensitive = in_is_sensitive,
        segment_num = in_segment_num,
        sensitive_level = in_sensitive_level
	where id = in_id;
END$$

DELIMITER ;
