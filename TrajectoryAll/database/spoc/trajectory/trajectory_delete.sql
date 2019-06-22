DROP PROCEDURE IF EXISTS `trajectory_delete`;

DELIMITER $$
CREATE PROCEDURE `trajectory_delete`(
	in in_table_name int,
    in in_time datetime
)
BEGIN
	delete from guide where num = in_num;
    set @t_sql = CONCAT(
    	'delete from table_', in_table_name,
    	' where num = ', in_num,
        ' and time = ', in_time
	);
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
