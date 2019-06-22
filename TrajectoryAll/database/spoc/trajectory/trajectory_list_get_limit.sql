DROP PROCEDURE IF EXISTS `trajectory_List_get_limit`;

DELIMITER $$
CREATE PROCEDURE `trajectory_List_get_limit`(
    IN in_table_num int(11),
    IN in_offset int,
    IN in_limit int
)
BEGIN
    set @t_sql = CONCAT(
    	'select * from table_', in_table_num,
		' order by time limit ',in_offset,',', in_limit
    );
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
