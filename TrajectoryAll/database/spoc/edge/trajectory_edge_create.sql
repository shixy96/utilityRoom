DROP PROCEDURE IF EXISTS `trajectory_edge_create`;

DELIMITER $$
CREATE PROCEDURE `trajectory_edge_create`()
BEGIN
    create table edge (table_name varchar(50) primary key, max_lat double,max_lng double,min_lat double,min_lng double);
END$$

DELIMITER ;
