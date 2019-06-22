DROP PROCEDURE IF EXISTS `trajectory_num_get_by_time`;

DELIMITER $$
CREATE PROCEDURE `trajectory_num_get_by_time`(
	IN in_table_num int(11),
    IN in_time int(11)
)
BEGIN
	DECLARE table_name VARCHAR(100);
    set @t_sql = CONCAT(
		'select t.num from table_',in_table_num,'_timedis AS t ', 
        'where t.time = ', in_time
	);
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
