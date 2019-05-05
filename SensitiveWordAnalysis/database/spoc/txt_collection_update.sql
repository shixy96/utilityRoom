DROP PROCEDURE IF EXISTS `txt_collection_update`;

DELIMITER $$
CREATE PROCEDURE `txt_collection_update`(
	in in_id int,
	in in_vectors char(45)
)
BEGIN
    update txt_collection set 
		`vectors` = in_vectors        
	where id = in_id;
END$$

DELIMITER ;
