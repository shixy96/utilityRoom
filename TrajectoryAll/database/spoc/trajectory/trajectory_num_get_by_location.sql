DROP PROCEDURE IF EXISTS `trajectory_num_get_by_location`;

DELIMITER $$
CREATE PROCEDURE `trajectory_num_get_by_location`(
    IN in_table_num int(11),
    IN in_max_lat double,
    IN in_max_lng double,
    IN in_min_lat double,
    IN in_min_lng double,
    OUT out_num int(11)
)
BEGIN
    set @t_sql = CONCAT(
    	'select * from table_', in_table_num, ' as t ',
    	'where t.lat <= ', in_max_lat ,
        ' and t.lng <= ', in_max_lng,
		' and t.lat >= ', in_min_lat, 
       	' and t.lng >= ',  in_min_lng, 
		' order by time'
    );
    prepare stmt from @t_sql;
    execute stmt;
    set out_num = found_rows();
    deallocate prepare stmt;
END$$

DELIMITER ;
