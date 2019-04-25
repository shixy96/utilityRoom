DROP PROCEDURE IF EXISTS `how_net_word_content_search`;

DELIMITER $$
CREATE PROCEDURE `how_net_word_content_search`(
	in in_W_C text,
	in in_W_E text,
    in in_offset int,
    in in_count int
)
BEGIN
    select h.* from how_net_word h 
    where 
    (
    (in_W_E is null or h.W_E like concat('%',in_W_E,'%'))
    or (in_W_C is null or h.W_C like concat('%',in_W_C,'%'))
    )
    limit in_offset, in_count;
END$$

DELIMITER ;
