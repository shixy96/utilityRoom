DROP PROCEDURE IF EXISTS `spider_text_insert`;

DELIMITER $$
CREATE PROCEDURE `spider_text_insert`(
	in in_text text,
    in in_is_sensitive int,
    in in_segment_num int,
    in in_sensitive_level double
)
BEGIN
    insert into weibo_spider_comment (`text`, is_sensitive, segment_num, sensitive_level) values (in_text, in_is_sensitive, in_segment_num, in_sensitive_level);
END$$

DELIMITER ;
