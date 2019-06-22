DROP PROCEDURE IF EXISTS `trajectory_edge_insert`;

DELIMITER $$
CREATE PROCEDURE `trajectory_edge_insert`(
    in in_table_name int,
    in in_mat_lat double,
    in in_max_lng double,
	in in_min_lat double,
	in in_min_lng double
)
BEGIN
    insert into edge (`table_name`, max_lat , max_lng , min_lat , min_lng) values (
		in_table_name,
		in_mat_lat, 
		in_max_lng , 
        in_min_lat ,
        in_min_lng
    );
END$$

DELIMITER ;
