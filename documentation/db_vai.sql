-- MySQL dump 10.13  Distrib 8.0.21, for macos10.15 (x86_64)
--
-- Host: localhost    Database: db_vai
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `db_vai`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db_vai` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `db_vai`;

--
-- Table structure for table `Annotazione`
--

DROP TABLE IF EXISTS `Annotazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Annotazione` (
  `IdImmagine` int NOT NULL,
  `LavoratoreName` varchar(45) NOT NULL,
  `DataCreazione` date NOT NULL,
  `Validita` tinyint(1) NOT NULL,
  `Fiducia` varchar(6) NOT NULL,
  `Note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`IdImmagine`,`LavoratoreName`),
  KEY `LavoratoreName` (`LavoratoreName`),
  CONSTRAINT `annotazione_ibfk_1` FOREIGN KEY (`IdImmagine`) REFERENCES `Immagine` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `annotazione_ibfk_2` FOREIGN KEY (`LavoratoreName`) REFERENCES `Utente` (`Name`) ON UPDATE CASCADE,
  CONSTRAINT `annotazione_chk_1` CHECK ((`Fiducia` in (_utf8mb4'alta',_utf8mb4'media',_utf8mb4'bassa')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Annotazione`
--

LOCK TABLES `Annotazione` WRITE;
/*!40000 ALTER TABLE `Annotazione` DISABLE KEYS */;
/*!40000 ALTER TABLE `Annotazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Campagna`
--

DROP TABLE IF EXISTS `Campagna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Campagna` (
  `Name` varchar(45) NOT NULL,
  `Committente` varchar(255) NOT NULL,
  `Stato` varchar(7) NOT NULL,
  `ManagerName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Name`),
  KEY `ManagerName` (`ManagerName`),
  CONSTRAINT `campagna_ibfk_1` FOREIGN KEY (`ManagerName`) REFERENCES `Utente` (`Name`),
  CONSTRAINT `campagna_chk_1` CHECK ((`Stato` in (_utf8mb4'creato',_utf8mb4'avviato',_utf8mb4'chiuso')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Campagna`
--

LOCK TABLES `Campagna` WRITE;
/*!40000 ALTER TABLE `Campagna` DISABLE KEYS */;
/*!40000 ALTER TABLE `Campagna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Immagine`
--

DROP TABLE IF EXISTS `Immagine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Immagine` (
  `Id` int NOT NULL,
  `Foto` longblob NOT NULL,
  `Latitudine` double NOT NULL,
  `Longitudine` double NOT NULL,
  `Comune` varchar(45) NOT NULL,
  `Regione` varchar(45) NOT NULL,
  `Provenienza` varchar(255) NOT NULL,
  `DataDiRecupero` date NOT NULL,
  `Risoluzione` varchar(6) NOT NULL,
  `CampagnaName` varchar(45) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Latitudine` (`Latitudine`,`Longitudine`),
  KEY `CampagnaName` (`CampagnaName`),
  CONSTRAINT `immagine_ibfk_1` FOREIGN KEY (`CampagnaName`) REFERENCES `Campagna` (`Name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `immagine_chk_1` CHECK ((`Risoluzione` in (_utf8mb4'alta',_utf8mb4'media',_utf8mb4'bassa')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Immagine`
--

LOCK TABLES `Immagine` WRITE;
/*!40000 ALTER TABLE `Immagine` DISABLE KEYS */;
/*!40000 ALTER TABLE `Immagine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Iscrizione`
--

DROP TABLE IF EXISTS `Iscrizione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Iscrizione` (
  `LavoratoreName` varchar(45) NOT NULL,
  `CampagnaName` varchar(45) NOT NULL,
  PRIMARY KEY (`LavoratoreName`,`CampagnaName`),
  KEY `CampagnaName` (`CampagnaName`),
  CONSTRAINT `iscrizione_ibfk_1` FOREIGN KEY (`LavoratoreName`) REFERENCES `Utente` (`Name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `iscrizione_ibfk_2` FOREIGN KEY (`CampagnaName`) REFERENCES `Campagna` (`Name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Iscrizione`
--

LOCK TABLES `Iscrizione` WRITE;
/*!40000 ALTER TABLE `Iscrizione` DISABLE KEYS */;
/*!40000 ALTER TABLE `Iscrizione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Utente`
--

DROP TABLE IF EXISTS `Utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Utente` (
  `Name` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Ruolo` varchar(12) NOT NULL,
  `LavoratoreLevel` varchar(6) DEFAULT NULL,
  `LavoratoreFoto` longblob,
  PRIMARY KEY (`Name`),
  CONSTRAINT `mailcheck` CHECK (regexp_like(`Email`,_utf8mb4'^(\\S+)\\@(\\S+).(\\S+)$')),
  CONSTRAINT `RuoloChecker` CHECK ((((`Ruolo` = _utf8mb4'Manager') and (`LavoratoreLevel` is null) and (`LavoratoreFoto` is null)) or ((`Ruolo` = _utf8mb4'Lavoratore') and (`LavoratoreLevel` is not null) and (`LavoratoreFoto` is not null)))),
  CONSTRAINT `utente_chk_1` CHECK ((`Ruolo` in (_utf8mb4'Manager',_utf8mb4'Lavoratore'))),
  CONSTRAINT `utente_chk_2` CHECK ((`LavoratoreLevel` in (_utf8mb4'alto',_utf8mb4'medio',_utf8mb4'basso')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Utente`
--

LOCK TABLES `Utente` WRITE;
/*!40000 ALTER TABLE `Utente` DISABLE KEYS */;
INSERT INTO `Utente` VALUES ('abc','123','123@qq.com','Manager',NULL,NULL),('manager','123','1234@qq.com','Manager',NULL,NULL),('word3','123','w3@qq,com','Lavoratore','medio',_binary 'fa'),('work','123','work@qq.com','Lavoratore','alto',_binary 'qwe'),('work2','1234','work2@qq.com','Lavoratore','alto',_binary 'dd');
/*!40000 ALTER TABLE `Utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-01 18:03:40
