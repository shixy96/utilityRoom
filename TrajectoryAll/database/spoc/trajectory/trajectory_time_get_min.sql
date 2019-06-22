DROP PROCEDURE IF EXISTS `trajectory_time_get_min`;

DELIMITER $$
CREATE PROCEDURE `trajectory_time_get_min`(
	IN in_table_num int(11)
)
BEGIN
    set @t_sql = CONCAT('select min(time) from table_',in_table_num);
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
