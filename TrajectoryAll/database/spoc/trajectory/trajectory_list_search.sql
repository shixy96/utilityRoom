DROP PROCEDURE IF EXISTS `trajectory_List_search`;

DELIMITER $$
CREATE PROCEDURE `trajectory_List_search`(
	IN in_table_num int(11),
    IN in_max_lat double,
    IN in_max_lng double,
    IN in_max_time datetime,
    IN in_min_lat double,
    IN in_min_lng double,
    IN in_min_time datetime
)
BEGIN
    set @t_sql = CONCAT(
    	'select * from table_', in_table_num, ' as t ',
    	'where t.lat <= ', in_max_lat ,
        ' and t.lng <= ', in_max_lng,
        ' and t.time <= ',  date(in_max_time), 
		' and t.lat >= ', in_min_lat, 
        ' and t.lng >= ',  in_min_lng, 
        ' and t.time >= ',  date(in_min_time), 
        ' order by time'
	);
    prepare stmt from @t_sql;
    execute stmt;
    deallocate prepare stmt;
END$$

DELIMITER ;
