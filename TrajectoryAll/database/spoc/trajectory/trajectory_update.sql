DROP PROCEDURE IF EXISTS `trajectory_update`;

DELIMITER $$
CREATE PROCEDURE `trajectory_update`(
    in in_table_name int,
    in in_num int,
    in in_lat double,
    in in_lng double,
	in in_height double,
	in in_rel_time double,
    in in_time datetime
)
BEGIN
    set @t_sql = CONCAT(
    	'update table_', in_table_name, ' set ',
        ' lat = ', in_lat ,
        ' lng = ', in_lng ,
        ' height = ', in_height ,
        ' rel_time = ', in_rel_time ,
        ' time = ', in_time ,
        ' where num = ', in_num
	);
    prepare stmt from @t_sql;
    execute stmt;
END$$

DELIMITER ;
