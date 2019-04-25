DROP PROCEDURE IF EXISTS `how_net_word_insert`;

DELIMITER $$
CREATE PROCEDURE `how_net_word_insert`(
    in in_W_C text,
	in in_G_C text,
	in in_S_C text,
	in in_W_E text,
	in in_G_E text,
	in in_S_E text,
	in in_DEF text
)
BEGIN
    insert into how_net_word (W_C, G_C, S_C, W_E, G_E, S_E, DEF) value (in_W_C, in_G_C, in_S_C, in_W_E, in_G_E, in_S_E, in_DEF);
END$$

DELIMITER ;
