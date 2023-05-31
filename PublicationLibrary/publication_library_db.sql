-- MySQL dump 10.13  Distrib 8.0.32, for macos13 (arm64)
--
-- Host: localhost    Database: PublicationLibraryDB
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `Authors`
--

DROP TABLE IF EXISTS `Authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Authors` (
  `author_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`author_id`),
  UNIQUE KEY `author_id_UNIQUE` (`author_id`),
  UNIQUE KEY `full_name_UNIQUE` (`full_name`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Authors`
--

LOCK TABLES `Authors` WRITE;
/*!40000 ALTER TABLE `Authors` DISABLE KEYS */;
INSERT INTO `Authors` VALUES (22,'Andrew Cochran'),(15,'Keyur Khant'),(16,'Khushal Gondaliya'),(20,'Mike McAllister'),(21,'Parth Mehta'),(17,'Parth Ramoliya'),(18,'Roger McAllister'),(19,'Vaidik Nimavat');
/*!40000 ALTER TABLE `Authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PublicationAuthors`
--

DROP TABLE IF EXISTS `PublicationAuthors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PublicationAuthors` (
  `publication_id` int DEFAULT NULL,
  `author_id` int DEFAULT NULL,
  KEY `publication_id_idx` (`publication_id`),
  KEY `author_id_idx` (`author_id`),
  CONSTRAINT `author_id_refs` FOREIGN KEY (`author_id`) REFERENCES `Authors` (`author_id`),
  CONSTRAINT `publication_id_refs` FOREIGN KEY (`publication_id`) REFERENCES `Publications` (`publication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PublicationAuthors`
--

LOCK TABLES `PublicationAuthors` WRITE;
/*!40000 ALTER TABLE `PublicationAuthors` DISABLE KEYS */;
INSERT INTO `PublicationAuthors` VALUES (16,15),(16,16),(17,15),(17,16),(18,17),(18,16),(19,17),(19,18),(19,19),(19,15),(19,16),(19,20),(19,21),(20,18),(20,22),(20,20);
/*!40000 ALTER TABLE `PublicationAuthors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PublicationReferences`
--

DROP TABLE IF EXISTS `PublicationReferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PublicationReferences` (
  `publication_id` int DEFAULT NULL,
  `referenced_publication_id` int DEFAULT NULL,
  KEY `publication_id_idx` (`publication_id`,`referenced_publication_id`),
  KEY `referenced_publication_reference_id_idx` (`referenced_publication_id`),
  CONSTRAINT `publication_reference_id` FOREIGN KEY (`publication_id`) REFERENCES `Publications` (`publication_id`),
  CONSTRAINT `referenced_publication_reference_id` FOREIGN KEY (`referenced_publication_id`) REFERENCES `Publications` (`publication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PublicationReferences`
--

LOCK TABLES `PublicationReferences` WRITE;
/*!40000 ALTER TABLE `PublicationReferences` DISABLE KEYS */;
INSERT INTO `PublicationReferences` VALUES (18,17),(18,19),(18,20),(19,16),(19,17),(19,18),(20,16),(20,19);
/*!40000 ALTER TABLE `PublicationReferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Publications`
--

DROP TABLE IF EXISTS `Publications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Publications` (
  `publication_id` int NOT NULL AUTO_INCREMENT,
  `identifier` varchar(255) DEFAULT NULL,
  `paper_title` varchar(255) DEFAULT NULL,
  `publication_title` varchar(255) DEFAULT NULL,
  `venue_id` int DEFAULT NULL,
  `pages` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `issue` varchar(45) DEFAULT NULL,
  `month` varchar(45) DEFAULT NULL,
  `year` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`publication_id`),
  UNIQUE KEY `publication_id_UNIQUE` (`publication_id`),
  UNIQUE KEY `identifier_UNIQUE` (`identifier`),
  KEY `venue_id_idx` (`venue_id`),
  CONSTRAINT `venue_id` FOREIGN KEY (`venue_id`) REFERENCES `Venues` (`venue_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Publications`
--

LOCK TABLES `Publications` WRITE;
/*!40000 ALTER TABLE `Publications` DISABLE KEYS */;
INSERT INTO `Publications` VALUES (16,'KeyurKhushal2023','Multi-Objective Bayesian Optimization over High-Dimensional Search Spaces','ICLMC Journal representation',8,'124-138','4','23','January','2023','New York'),(17,'KeyurKhushal2021','Tabular Data: Deep Learning is Not All You Need','7th International Computer Science Conference',7,'190',NULL,NULL,'March','2021','Toronto'),(18,'KhushalParth2022','Privacy for Free: How does Dataset Condensation Help Privacy?','7th International Computer Science Conference',7,'11-22',NULL,NULL,'March','2021','Toronto'),(19,'Recommender2022','Affective Signals in a Social Media Recommender System','Shift Key conference of new ML ideas',9,'432',NULL,NULL,'December','2022','Halifax'),(20,'McAllister2023','ItemSage: Learning Product Embeddings for Shopping Recommendations','Shift Key conference of new ML ideas',9,'143-222','98','5','October','2023','Halifax');
/*!40000 ALTER TABLE `Publications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Publishers`
--

DROP TABLE IF EXISTS `Publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Publishers` (
  `publisher_id` int NOT NULL AUTO_INCREMENT,
  `contact_name` varchar(45) DEFAULT NULL,
  `contact_email` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`publisher_id`),
  UNIQUE KEY `publisher_id_UNIQUE` (`publisher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Publishers`
--

LOCK TABLES `Publishers` WRITE;
/*!40000 ALTER TABLE `Publishers` DISABLE KEYS */;
INSERT INTO `Publishers` VALUES (9,'Springer','info@springer.com','Toronto'),(10,'Oxford','info@oxford.com','New York'),(11,'Blackwall','info@blackwall.com','Halifax');
/*!40000 ALTER TABLE `Publishers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `research_area_reference`
--

DROP TABLE IF EXISTS `research_area_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `research_area_reference` (
  `parent_research_area_id` int DEFAULT NULL,
  `child_research_area_id` int DEFAULT NULL,
  KEY `research_area_id_idx` (`parent_research_area_id`,`child_research_area_id`),
  KEY `child_research_area_id_idx` (`child_research_area_id`),
  CONSTRAINT `child_research_area_id` FOREIGN KEY (`child_research_area_id`) REFERENCES `ResearchAreas` (`research_area_id`),
  CONSTRAINT `parent_research_area_id` FOREIGN KEY (`parent_research_area_id`) REFERENCES `ResearchAreas` (`research_area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `research_area_reference`
--

LOCK TABLES `research_area_reference` WRITE;
/*!40000 ALTER TABLE `research_area_reference` DISABLE KEYS */;
INSERT INTO `research_area_reference` VALUES (13,16),(13,17),(13,19),(13,21),(13,22),(13,23),(16,18),(16,20),(16,23),(17,23);
/*!40000 ALTER TABLE `research_area_reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ResearchAreas`
--

DROP TABLE IF EXISTS `ResearchAreas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ResearchAreas` (
  `research_area_id` int NOT NULL AUTO_INCREMENT,
  `research_area_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`research_area_id`),
  UNIQUE KEY `research_area_name_UNIQUE` (`research_area_name`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ResearchAreas`
--

LOCK TABLES `ResearchAreas` WRITE;
/*!40000 ALTER TABLE `ResearchAreas` DISABLE KEYS */;
INSERT INTO `ResearchAreas` VALUES (13,'Artificial Intelligence'),(14,'Computer Science'),(19,'Data Management'),(17,'Deep Learning'),(18,'Generative learning'),(20,'Image Processing'),(16,'Machine Learning'),(15,'Mathematics'),(21,'NLP'),(22,'Text Processing'),(23,'Text Processing with GPT');
/*!40000 ALTER TABLE `ResearchAreas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VenueResearchAreas`
--

DROP TABLE IF EXISTS `VenueResearchAreas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VenueResearchAreas` (
  `venue_id` int DEFAULT NULL,
  `research_area_id` int DEFAULT NULL,
  KEY `venue_id_idx` (`venue_id`),
  KEY `research_area_id_idx` (`research_area_id`),
  CONSTRAINT `research_area_id_reference` FOREIGN KEY (`research_area_id`) REFERENCES `ResearchAreas` (`research_area_id`),
  CONSTRAINT `venue_id_ref` FOREIGN KEY (`venue_id`) REFERENCES `Venues` (`venue_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VenueResearchAreas`
--

LOCK TABLES `VenueResearchAreas` WRITE;
/*!40000 ALTER TABLE `VenueResearchAreas` DISABLE KEYS */;
INSERT INTO `VenueResearchAreas` VALUES (7,16),(7,17),(8,13),(8,14),(9,18),(9,20);
/*!40000 ALTER TABLE `VenueResearchAreas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Venues`
--

DROP TABLE IF EXISTS `Venues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Venues` (
  `venue_id` int NOT NULL AUTO_INCREMENT,
  `venue_name` varchar(255) DEFAULT NULL,
  `publisher_id` int DEFAULT NULL,
  `editor` varchar(255) DEFAULT NULL,
  `editor_contact` varchar(45) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `event_year` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`venue_id`),
  UNIQUE KEY `venue_id_UNIQUE` (`venue_id`),
  KEY `publisher_id_idx` (`publisher_id`),
  CONSTRAINT `publisher_id` FOREIGN KEY (`publisher_id`) REFERENCES `Publishers` (`publisher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Venues`
--

LOCK TABLES `Venues` WRITE;
/*!40000 ALTER TABLE `Venues` DISABLE KEYS */;
INSERT INTO `Venues` VALUES (7,'7th International Computer Science Conference',11,'Mike McAllister','+1 9929999221','DalU, Halifax, NS','2023'),(8,'ICLMC Journal representation',10,'Andrew Cochran','+1 9129999221','New York','2022'),(9,'Shift Key conference of new ML ideas',9,'MS Dhoni','+1 9119999221','Toronto','2023');
/*!40000 ALTER TABLE `Venues` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-20 22:13:54
