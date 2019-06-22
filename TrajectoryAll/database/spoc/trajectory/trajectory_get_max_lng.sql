DROP PROCEDURE IF EXISTS `trajectory_get_max_lng`;

DELIMITER $$
CREATE PROCEDURE `trajectory_get_max_lng`(
	IN in_table_num int(11)
)
BEGIN
    set @t_sql = CONCAT(
		'select max(lng) from table_',in_table_num,';'
	);
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
