DROP PROCEDURE IF EXISTS `trajectory_get_first`;

DELIMITER $$
CREATE PROCEDURE `trajectory_get_first`(
	IN in_table_num int(11)
)
BEGIN
    set @t_sql = CONCAT(
		'select * from table_',in_table_num,
        ' where time = (select min(time) from table_',in_table_num,')'
	);
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
