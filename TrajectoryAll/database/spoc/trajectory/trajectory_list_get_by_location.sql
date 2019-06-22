DROP PROCEDURE IF EXISTS `trajectory_List_get_by_location`;

DELIMITER $$
CREATE PROCEDURE `trajectory_List_get_by_location`(
    IN in_table_num int(11),
    IN in_max_lat double,
    IN in_max_lng double,
    IN in_min_lat double,
    IN in_min_lng double
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
    deallocate prepare stmt;
END$$

DELIMITER ;
