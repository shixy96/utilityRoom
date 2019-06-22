DROP PROCEDURE IF EXISTS `trajectory_time_get_max`;

DELIMITER $$
CREATE PROCEDURE `trajectory_time_get_max`(
	IN in_table_num int(11)
)
BEGIN
    set @t_sql = CONCAT('select max(time) from table_',in_table_num);
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
