-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: trajectory_baidu
-- ------------------------------------------------------
-- Server version	5.5.62-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `guide`
--

DROP TABLE IF EXISTS `guide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guide` (
  `num` int(11) NOT NULL,
  `table_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`num`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guide`
--

LOCK TABLES `guide` WRITE;
/*!40000 ALTER TABLE `guide` DISABLE KEYS */;
INSERT INTO `guide` VALUES (1,'table_1'),(2,'table_2'),(3,'table_3'),(4,'table_4'),(5,'table_5'),(6,'table_6'),(7,'table_7'),(8,'table_8'),(9,'table_9'),(10,'table_10'),(11,'table_11'),(12,'table_12'),(13,'table_13'),(14,'table_14'),(15,'table_15'),(16,'table_16'),(17,'table_17'),(18,'table_18'),(19,'table_19'),(20,'table_20'),(21,'table_21'),(22,'table_22'),(23,'table_23'),(24,'table_24'),(25,'table_25'),(26,'table_26'),(27,'table_27'),(28,'table_28'),(29,'table_29'),(30,'table_30'),(31,'table_31'),(32,'table_32'),(33,'table_33'),(34,'table_34'),(35,'table_35'),(36,'table_36'),(37,'table_37'),(38,'table_38'),(39,'table_39'),(40,'table_40'),(41,'table_41'),(42,'table_42'),(43,'table_43'),(44,'table_44'),(45,'table_45'),(46,'table_46'),(47,'table_47'),(48,'table_48'),(49,'table_49'),(50,'table_50'),(51,'table_51'),(52,'table_52'),(53,'table_53'),(54,'table_54'),(55,'table_55'),(56,'table_56'),(57,'table_57'),(58,'table_58'),(59,'table_59'),(60,'table_60'),(61,'table_61'),(62,'table_62'),(63,'table_63'),(64,'table_64'),(65,'table_65'),(66,'table_66'),(67,'table_67'),(68,'table_68'),(69,'table_69'),(70,'table_70'),(71,'table_71'),(72,'table_72'),(73,'table_73'),(74,'table_74'),(75,'table_75'),(76,'table_76'),(77,'table_77'),(78,'table_78'),(79,'table_79'),(80,'table_80'),(81,'table_81'),(82,'table_82'),(83,'table_83'),(84,'table_84'),(85,'table_85'),(86,'table_86'),(87,'table_87'),(88,'table_88'),(89,'table_89'),(90,'table_90'),(91,'table_91'),(92,'table_92'),(93,'table_93'),(94,'table_94'),(95,'table_95'),(96,'table_96'),(97,'table_97'),(98,'table_98'),(99,'table_99'),(100,'table_100'),(101,'table_101'),(102,'table_102'),(103,'table_103'),(104,'table_104'),(105,'table_105'),(106,'table_106'),(107,'table_107'),(108,'table_108'),(109,'table_109'),(110,'table_110'),(111,'table_111'),(112,'table_112'),(113,'table_113'),(114,'table_114'),(115,'table_115'),(116,'table_116'),(117,'table_117'),(118,'table_118'),(119,'table_119'),(120,'table_120'),(121,'table_121'),(122,'table_122'),(123,'table_123'),(124,'table_124'),(125,'table_125'),(126,'table_126'),(127,'table_127'),(128,'table_128'),(129,'table_129'),(130,'table_130'),(131,'table_131'),(132,'table_132'),(133,'table_133'),(134,'table_134'),(135,'table_135'),(136,'table_136'),(137,'table_137'),(138,'table_138'),(139,'table_139'),(140,'table_140'),(141,'table_141'),(142,'table_142'),(143,'table_143'),(144,'table_144'),(145,'table_145'),(146,'table_146'),(147,'table_147'),(148,'table_148'),(149,'table_149'),(150,'table_150'),(151,'table_151'),(152,'table_152'),(153,'table_153'),(154,'table_154'),(155,'table_155'),(156,'table_156'),(157,'table_157'),(158,'table_158'),(159,'table_159'),(160,'table_160'),(161,'table_161'),(162,'table_162'),(163,'table_163'),(164,'table_164'),(165,'table_165'),(166,'table_166'),(167,'table_167'),(168,'table_168'),(169,'table_169'),(170,'table_170'),(171,'table_171'),(172,'table_172'),(173,'table_173'),(174,'table_174'),(175,'table_175'),(176,'table_176'),(177,'table_177'),(178,'table_178'),(179,'table_179'),(180,'table_180'),(181,'table_181');
/*!40000 ALTER TABLE `guide` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-19 22:41:58
