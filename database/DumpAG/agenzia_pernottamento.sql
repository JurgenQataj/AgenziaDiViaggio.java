-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: agenzia
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pernottamento`
--

DROP TABLE IF EXISTS `pernottamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pernottamento` (
  `data_inizio` date NOT NULL,
  `viaggio` varchar(30) NOT NULL,
  `data_fine` date NOT NULL,
  `localita` varchar(60) NOT NULL,
  `albergo` int DEFAULT NULL,
  PRIMARY KEY (`data_inizio`,`viaggio`),
  KEY `localita` (`localita`),
  KEY `viaggio_pernottamento` (`viaggio`,`data_inizio`),
  KEY `Pernottamento_Albergo_codice_fk` (`albergo`),
  CONSTRAINT `Pernottamento_Albergo_codice_fk` FOREIGN KEY (`albergo`) REFERENCES `albergo` (`codice`),
  CONSTRAINT `Pernottamento_ibfk_1` FOREIGN KEY (`viaggio`) REFERENCES `viaggio` (`titolo`),
  CONSTRAINT `Pernottamento_ibfk_2` FOREIGN KEY (`localita`) REFERENCES `localita` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pernottamento`
--

LOCK TABLES `pernottamento` WRITE;
/*!40000 ALTER TABLE `pernottamento` DISABLE KEYS */;
INSERT INTO `pernottamento` VALUES ('2024-12-17','extralux','2024-12-21','Roma',1);
/*!40000 ALTER TABLE `pernottamento` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `check_overnight_dates` BEFORE INSERT ON `pernottamento` FOR EACH ROW begin

    declare var_startDate date;
    select data_partenza from Viaggio where titolo = NEW.viaggio into var_startDate;

    if NEW.data_inizio < NEW.data_fine then
        signal sqlstate '45001' set message_text = 'La data di fine pernottamento non può precedere la data di inizio.';
    end if;

    if not exists(select data_inizio from Pernottamento P where P.viaggio = NEW.viaggio) then
        if not (NEW.data_inizio = var_startDate) then
            signal sqlstate '45001' set message_text =
                    'La data di inizio del primo pernottamento deve coincidere con la data di partenza del viaggio.';
        end if;
    end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `check_hotel` BEFORE UPDATE ON `pernottamento` FOR EACH ROW begin

    declare var_bookings int unsigned;
    declare var_capacity int unsigned;

    select SUM(numero_persone) from Prenotazione P where P.viaggio = OLD.viaggio into var_bookings;

    if (NEW.albergo is not null) then
        select capienza from Albergo where codice = NEW.albergo into var_capacity;
        if (var_bookings > var_capacity) then
            signal sqlstate '45001' set message_text = 'Il numero di prenotati per il viaggio supera la capienza dell''albergo.';
        end if;
    end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-22 18:32:30
