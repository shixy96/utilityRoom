DROP PROCEDURE IF EXISTS `sensitive_database_create`;

DELIMITER $$
CREATE PROCEDURE `sensitive_database_create`()
BEGIN
    create database if not exists `sensitive_word_collection`;
END$$

DELIMITER ;
