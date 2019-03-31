DROP PROCEDURE IF EXISTS `sensitive_database_create`;

DELIMITER $$
CREATE PROCEDURE `sensitive_database_create`()
BEGIN
    create database if not exists `sensitive_word_collection`
    DEFAULT CHARACTER SET utf8mb4;
END$$

DELIMITER ;
