DROP PROCEDURE IF EXISTS `how_net_word_search_complete`;

DELIMITER $$
CREATE PROCEDURE `how_net_word_search_complete`(
	in in_id int,
	in in_W_C text,
	in in_G_C text,
	in in_S_C text,
	in in_W_E text,
	in in_G_E text,
	in in_S_E text,
	in in_DEF text,
    in in_offset int,
    in in_count int
)
BEGIN
    select h.* from how_net_word h 
    where 
    (in_id is null or in_id = h.id)
    and (in_G_C is null or in_G_C = h.G_C)
    and (in_S_C is null or in_S_C = h.S_C)
    and (in_G_E is null or in_G_E = h.G_E)
    and (in_S_E is null or in_S_E = h.S_E)
    and 
    (
    (in_W_E is null or h.W_E = in_W_E)
    or (in_W_C is null or h.W_C = in_W_C)
    or (in_DEF is null or find_in_set(in_DEF,h.DEF))
    )
    limit in_offset, in_count;
END$$

DELIMITER ;
