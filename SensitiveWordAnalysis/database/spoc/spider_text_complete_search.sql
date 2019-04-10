DROP PROCEDURE IF EXISTS `spider_text_complete_search`;

DELIMITER $$
CREATE PROCEDURE `spider_text_complete_search`(
	in in_id int,
	in in_text text,
    in in_segment_num int,
    in in_sensitive_level double
)
BEGIN
    select w.* from weibo_spider_comment w 
    where 
		(in_id is null or w.id = in_id)
	and (in_text is null or w.`text` = in_text)
    and (in_segment_num is null or w.segment_num = in_segment_num)
    and (in_sensitive_level is null or w.sensitive_level = in_sensitive_level);
END$$

DELIMITER ;
