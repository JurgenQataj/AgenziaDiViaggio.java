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
-- Table structure for table `albergo`
--

DROP TABLE IF EXISTS `albergo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `albergo` (
  `codice` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(60) NOT NULL,
  `referente` varchar(60) NOT NULL,
  `capienza` int unsigned NOT NULL,
  `via` varchar(50) NOT NULL,
  `civico` varchar(6) NOT NULL,
  `cp` varchar(7) NOT NULL,
  `email` varchar(50) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `fax` varchar(50) DEFAULT NULL,
  `localita` varchar(60) NOT NULL,
  PRIMARY KEY (`codice`),
  UNIQUE KEY `indirizzo_univoco` (`via`,`civico`,`cp`,`localita`),
  KEY `localita_albergo` (`localita`,`nome`),
  CONSTRAINT `Albergo_ibfk_1` FOREIGN KEY (`localita`) REFERENCES `localita` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `albergo`
--

LOCK TABLES `albergo` WRITE;
/*!40000 ALTER TABLE `albergo` DISABLE KEYS */;
INSERT INTO `albergo` VALUES (1,'Hotel Majestic','Matt Bozinsky',100,'Nomentana','73','12345','majestichotel@gmail.com','3801912090',NULL,'Roma'),(6,'NeapolitanHotel','Mario Esposito',48,'via spaccanapoli 34','34','00134','NeapolitanHotel@gmail.com','3205794888','-','Napoli');
/*!40000 ALTER TABLE `albergo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assegnato_a`
--

DROP TABLE IF EXISTS `assegnato_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assegnato_a` (
  `autobus` varchar(7) NOT NULL,
  `viaggio` varchar(30) NOT NULL,
  KEY `autobus` (`autobus`),
  KEY `viaggio` (`viaggio`),
  CONSTRAINT `Assegnato_a_ibfk_1` FOREIGN KEY (`autobus`) REFERENCES `autobus` (`targa`),
  CONSTRAINT `Assegnato_a_ibfk_2` FOREIGN KEY (`viaggio`) REFERENCES `viaggio` (`titolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assegnato_a`
--

LOCK TABLES `assegnato_a` WRITE;
/*!40000 ALTER TABLE `assegnato_a` DISABLE KEYS */;
/*!40000 ALTER TABLE `assegnato_a` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `check_assignment` BEFORE INSERT ON `assegnato_a` FOR EACH ROW begin
    declare var_startDateCur date;
    declare var_endDateCur date;

    select data_partenza from Viaggio where titolo = new.viaggio into var_startDateCur;
    select data_rientro from Viaggio where titolo = new.viaggio into var_endDateCur;

    if exists(select data_partenza, data_rientro
               from Viaggio V
                        join Assegnato_a Aa on V.titolo = Aa.viaggio
               where Aa.autobus = NEW.autobus
                 and ((data_partenza <= var_startDateCur and data_rientro >= var_startDateCur)
                  or (data_partenza <= var_endDateCur and data_rientro >= var_endDateCur)))
        then
        signal sqlstate '45001' set message_text = 'Questo autobus non è disponibile per le date indicate.';
    end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `autobus`
--

DROP TABLE IF EXISTS `autobus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `autobus` (
  `targa` varchar(7) NOT NULL,
  `capienza` int unsigned NOT NULL,
  PRIMARY KEY (`targa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autobus`
--

LOCK TABLES `autobus` WRITE;
/*!40000 ALTER TABLE `autobus` DISABLE KEYS */;
/*!40000 ALTER TABLE `autobus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `localita`
--

DROP TABLE IF EXISTS `localita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `localita` (
  `nome` varchar(60) NOT NULL,
  `paese` varchar(20) NOT NULL,
  PRIMARY KEY (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `localita`
--

LOCK TABLES `localita` WRITE;
/*!40000 ALTER TABLE `localita` DISABLE KEYS */;
INSERT INTO `localita` VALUES ('Firenze','Italia'),('Genova','Italia'),('Milano','Italia'),('Napoli','Italia'),('Roma','Italia');
/*!40000 ALTER TABLE `localita` ENABLE KEYS */;
UNLOCK TABLES;

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
INSERT INTO `pernottamento` VALUES ('2024-10-19','maxilux','2024-10-22','Roma',1),('2025-05-19','dkekwj','2025-05-20','Roma',NULL),('2025-05-20','jurinooo+','2025-05-24','Napoli',NULL),('2025-06-23','napolitour','2025-06-26','Napoli',6);
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `check_overnight_dates` BEFORE INSERT ON `pernottamento` FOR EACH ROW BEGIN
    --
    -- QUESTA È LA LOGICA CORRETTA:
    -- Controlliamo se la data di FINE è precedente a quella di INIZIO.
    --
    IF NEW.data_fine < NEW.data_inizio THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La data di fine pernottamento non può precedere la data di inizio.';
    END IF;
END */;;
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

--
-- Table structure for table `prenotazione`
--

DROP TABLE IF EXISTS `prenotazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prenotazione` (
  `codice` int NOT NULL AUTO_INCREMENT,
  `numero_persone` int unsigned NOT NULL,
  `cliente` varchar(50) NOT NULL,
  `viaggio` varchar(30) NOT NULL,
  PRIMARY KEY (`codice`),
  KEY `cliente` (`cliente`),
  KEY `viaggio_prenotazione` (`viaggio`,`codice`),
  CONSTRAINT `Prenotazione_ibfk_1` FOREIGN KEY (`cliente`) REFERENCES `utente` (`email`),
  CONSTRAINT `Prenotazione_ibfk_2` FOREIGN KEY (`viaggio`) REFERENCES `viaggio` (`titolo`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazione`
--

LOCK TABLES `prenotazione` WRITE;
/*!40000 ALTER TABLE `prenotazione` DISABLE KEYS */;
INSERT INTO `prenotazione` VALUES (1,4,'jurgenqataj@gmail.com','napolitour'),(2,6,'mariorossi@gmail.com','napolitour'),(3,5,'mariorossi@gmail.com','napolitour'),(5,6,'jurgenqataj@gmail.com','napolitour');
/*!40000 ALTER TABLE `prenotazione` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `check_cancellation` BEFORE DELETE ON `prenotazione` FOR EACH ROW BEGIN
    DECLARE var_startDate DATE;

    -- La query 
    SELECT V.data_partenza
    INTO var_startDate
    FROM Viaggio V
    JOIN Prenotazione P ON V.titolo = P.viaggio
    WHERE P.codice = OLD.codice;

    -- La  regola di business
    IF (CURDATE() < var_startDate AND DATEDIFF(var_startDate, CURDATE()) < 20) THEN
        -- Abbiamo cambiato il messaggio per la nostra prova
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'V2 - Non è possibile cancellare una prenotazione a meno di 20 giorni dalla data di partenza.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `email` varchar(50) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `cognome` varchar(30) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `password` varchar(255) NOT NULL,
  `ruolo` enum('cliente','segreteria') NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `telefono` (`telefono`),
  KEY `login` (`email`,`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('jurgenqataj@gmail.com','Jurgen','Qataj','3801912095','password','segreteria'),('mariorossi@gmail.com','Mario','Rossi','3303948382','mariorossi','cliente');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `viaggio`
--

DROP TABLE IF EXISTS `viaggio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `viaggio` (
  `titolo` varchar(30) NOT NULL,
  `data_partenza` date NOT NULL,
  `data_rientro` date NOT NULL,
  `costo` double(6,2) NOT NULL,
  PRIMARY KEY (`titolo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `viaggio`
--

LOCK TABLES `viaggio` WRITE;
/*!40000 ALTER TABLE `viaggio` DISABLE KEYS */;
INSERT INTO `viaggio` VALUES ('dkekwj','2025-05-19','2025-05-20',56.00),('ieq','2025-05-19','2025-05-20',45.00),('jurinooo+','2025-05-20','2025-05-24',90.00),('loeja','2025-05-19','2025-05-20',46.00),('lope','2025-05-19','2025-05-20',45.00),('losa','2025-05-19','2025-05-20',23.00),('losssa','2025-05-19','2025-05-20',46.00),('lox','2025-05-19','2025-05-20',34.00),('lux','2025-05-19','2025-05-22',45.00),('luxxx','2025-05-19','2025-05-24',34.00),('maxilux','2024-10-19','2024-10-22',90.00),('napolitour','2025-06-23','2025-06-26',99.00),('oekej','2025-05-19','2025-05-20',65.00);
/*!40000 ALTER TABLE `viaggio` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `check_dates` BEFORE INSERT ON `viaggio` FOR EACH ROW begin
    if NEW.data_rientro < NEW.data_partenza then
        signal sqlstate '45001' set message_text = 'La data del rientro non può precedere la data di partenza';
    end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping events for database 'agenzia'
--

--
-- Dumping routines for database 'agenzia'
--
/*!50003 DROP PROCEDURE IF EXISTS `assign_bus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `assign_bus`(
    IN _viaggio VARCHAR(30), 
    IN _autobus VARCHAR(7)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;
    
    INSERT INTO Assegnato_a (autobus, viaggio) 
    VALUES (_autobus, _viaggio);
    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `assign_hotel` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `assign_hotel`(
    IN _viaggio VARCHAR(30), 
    IN _data_inizio DATE, 
    IN _albergo INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
    START TRANSACTION;
    
    UPDATE Pernottamento 
    SET albergo = _albergo 
    WHERE viaggio = _viaggio 
    AND data_inizio = _data_inizio;
    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `book` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `book`(
    IN _viaggio VARCHAR(30), 
    IN _cliente VARCHAR(50), 
    IN _persone INT UNSIGNED,
    OUT codice INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    START TRANSACTION;

    INSERT INTO Prenotazione(numero_persone, cliente, viaggio) 
    VALUES (_persone, _cliente, _viaggio);

    COMMIT;

    SET codice = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `cancel_booking` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `cancel_booking`(IN _codice INT)
BEGIN
    DELETE FROM Prenotazione WHERE codice = _codice;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `create_trip` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `create_trip`(
    IN _titolo VARCHAR(30),
    IN _data_partenza DATE,
    IN _data_rientro DATE,
    IN _costo DOUBLE(4, 2)
)
BEGIN
    INSERT INTO Viaggio (titolo, data_partenza, data_rientro, costo)
    VALUES (_titolo, _data_partenza, _data_rientro, _costo);
    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_overnight_stay` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_overnight_stay`(
    IN trip_title_param VARCHAR(100), -- Assicurati che il tipo e la lunghezza siano corretti
    IN start_date_param DATE,
    IN end_date_param DATE,
    IN place_name_param VARCHAR(60) -- Assicurati che il tipo e la lunghezza siano corretti
)
BEGIN
    -- Questa è la nuova versione: non contiene NESSUN controllo 'IF' sulle date.
    -- Esegue direttamente e solo l'inserimento dei dati.
    INSERT INTO pernottamento (viaggio, data_inizio, data_fine, localita)
    VALUES (trip_title_param, start_date_param, end_date_param, place_name_param);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `list_hotels` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `list_hotels`()
BEGIN
    SELECT * 
    FROM Albergo
    JOIN Localita L ON Albergo.localita = L.nome;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `list_places` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `list_places`()
BEGIN
    SELECT * 
    FROM Localita;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `list_trips` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `list_trips`(
    IN _data_partenza DATE
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE; -- Avoid dirty reads

    START TRANSACTION;

    SELECT * 
    FROM Viaggio
    WHERE data_partenza >= _data_partenza;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `login` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `login`(
    IN _email VARCHAR(50),
    IN _password VARCHAR(255),
    OUT _role INT
)
BEGIN
    DECLARE var_userRole ENUM('cliente', 'segreteria');
    
    SELECT ruolo 
    FROM Utente 
    WHERE email = _email 
    AND password = _password 
    INTO var_userRole;
    
    IF var_userRole = 'cliente' THEN
        SET _role = 0;
    ELSEIF var_userRole = 'segreteria' THEN
        SET _role = 1;
    ELSE
        SET _role = -1;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `new_hotel` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `new_hotel`(
    IN _nome VARCHAR(60),
    IN _referente VARCHAR(60),
    IN _capienza INT UNSIGNED,
    IN _via VARCHAR(50),
    IN _civico VARCHAR(6),
    IN _cp VARCHAR(7),
    IN _email VARCHAR(50),
    IN _telefono VARCHAR(15),
    IN _fax VARCHAR(50),
    IN _localita VARCHAR(60)
)
BEGIN
    INSERT INTO Albergo (
        nome, referente, capienza, via, civico, cp, email, telefono, fax, localita
    )
    VALUES (
        _nome, _referente, _capienza, _via, _civico, _cp, _email, _telefono, _fax, _localita
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `new_location` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `new_location`(
    IN _nome VARCHAR(60),
    IN _paese VARCHAR(20)
)
BEGIN
    INSERT INTO Localita (nome, paese)
    VALUES (_nome, _paese);
    
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `registration` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `registration`(
    IN _email VARCHAR(50),
    IN _nome VARCHAR(30),
    IN _cognome VARCHAR(30),
    IN _password VARCHAR(255),
    IN _telefono VARCHAR(15),
    IN _ruolo ENUM('cliente', 'segreteria')
)
BEGIN
    IF (_ruolo IS NULL) THEN
        SET _ruolo = 'cliente';
    END IF;

    INSERT INTO Utente (email, nome, cognome, telefono, password, ruolo)
    VALUES (_email, _nome, _cognome, _telefono, _password, _ruolo);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `report` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `report`(
    IN _viaggio VARCHAR(100)
)
BEGIN
    DECLARE var_dataRientro DATE;

    -- Recupera la data di rientro, usando TRIM() per sicurezza
    -- Questo rimuove eventuali spazi all'inizio o alla fine di _viaggio
    SELECT V.data_rientro
    INTO var_dataRientro
    FROM Viaggio V
    WHERE V.titolo = TRIM(_viaggio); -- <-- MODIFICA CHIAVE: Aggiunto TRIM()

    -- Se la query precedente non trova nulla, lanciamo un errore chiaro
    IF var_dataRientro IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Viaggio non trovato.';
    END IF;

    -- Verifica se la data di rientro è futura
    IF (CURDATE() < var_dataRientro) THEN
        SIGNAL SQLSTATE '45001'
        SET MESSAGE_TEXT = 'Il report non può essere generato prima della fine del viaggio';
    END IF;

    -- Recupera i dati del report
    SELECT
        V.titolo,
        SUM(P.numero_persone) AS partecipanti,
        (V.costo * SUM(P.numero_persone)) AS guadagno
    FROM Viaggio V
    JOIN Prenotazione P ON V.titolo = P.viaggio
    WHERE V.titolo = TRIM(_viaggio) -- Usiamo TRIM() anche qui per coerenza
    GROUP BY V.titolo;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `trip_details` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `trip_details`(
    IN _viaggio VARCHAR(100) 
)
BEGIN
    SELECT P.data_inizio, 
           P.data_fine, 
           A.nome AS albergo,  -- Questo campo sarà NULL se non c'è un albergo assegnato
           L.nome AS localita, 
           L.paese
    FROM Pernottamento P
    LEFT JOIN Albergo A ON A.codice = P.albergo 
    JOIN Localita L ON P.localita = L.nome
    WHERE P.viaggio = _viaggio;
END ;;
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

-- Dump completed on 2025-07-12 18:51:47
