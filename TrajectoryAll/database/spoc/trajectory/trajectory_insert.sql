DROP PROCEDURE IF EXISTS `trajectory_insert`;

DELIMITER $$
CREATE PROCEDURE `trajectory_insert`(
    in in_table_name int,
    in in_num int,
    in in_lat double,
    in in_lng double,
	in in_height double,
	in in_rel_time double,
    in in_time datetime
)
BEGIN
#insert into table_0 (num, lat, lng, height, rel_time, time) values (100000, 0, 0, 0, 0, '2007-12-20 18:31:34');
    set @t_sql = CONCAT(
    	'insert into table_', in_table_name,
        ' (num, lat , lng , height , rel_time, time) values ',
        '(', in_num, ', ', 
			 in_lat , ', ', 
             in_lng , ', ', 
             in_height , ', ', 
             in_rel_time, ', ', 
             in_time,
		  ')'
	);
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
