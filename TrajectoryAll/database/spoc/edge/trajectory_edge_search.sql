DROP PROCEDURE IF EXISTS `trajectory_edge_search`;

DELIMITER $$
CREATE PROCEDURE `trajectory_edge_search`(
    in in_table_name int
)
BEGIN
    select * from edge where table_name = in_table_name;
END$$

DELIMITER ;
