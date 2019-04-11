DROP PROCEDURE IF EXISTS `spider_text_delete_repeat`;

DELIMITER $$
CREATE PROCEDURE `spider_text_delete_repeat`()
BEGIN
    delete w1 from weibo_spider_comment as w1
    inner join weibo_spider_comment w2 on w1.`text` = w2.`text`;
END$$

DELIMITER ;
