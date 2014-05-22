-- MySQL dump 10.13  Distrib 5.6.15, for Win64 (x86_64)
--
-- Host: localhost    Database: core
-- ------------------------------------------------------
-- Server version	5.6.15-log

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
-- Current Database: `core`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `core` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `core`;

--
-- Table structure for table `cas_user`
--

DROP TABLE IF EXISTS `cas_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cas_user` (
  `cas_user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) NOT NULL COMMENT 'used to login',
  `password` varchar(100) NOT NULL COMMENT 'MD5 hash',
  `failed_attempts` int(11) DEFAULT NULL COMMENT 'The number of failed login attempts for this user.',
  `last_login_date` datetime DEFAULT NULL,
  PRIMARY KEY (`cas_user_id`),
  UNIQUE KEY `casuser_user_name_uc` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=55214 DEFAULT CHARSET=utf8 COMMENT='table to hold CAS user credentials';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cas_user`
--

LOCK TABLES `cas_user` WRITE;
/*!40000 ALTER TABLE `cas_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `cas_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `contact_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contact_type_id` int(10) unsigned NOT NULL,
  `org_id` int(10) unsigned NOT NULL,
  `name` varchar(100) NOT NULL COMMENT 'Full name of the person (first, middle, last)',
  `title` varchar(50) DEFAULT NULL COMMENT 'Title (Mr, Mrs, etc)',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email address',
  `address_line_1` varchar(100) DEFAULT NULL COMMENT 'Address line #1',
  `address_line_2` varchar(100) DEFAULT NULL COMMENT 'Address line #2',
  `city` varchar(50) DEFAULT NULL COMMENT 'City',
  `state` varchar(20) DEFAULT NULL COMMENT 'state/province/region',
  `country` varchar(2) DEFAULT NULL COMMENT '2 digit country code',
  `zip` varchar(10) DEFAULT NULL COMMENT 'zip or postal code',
  `phone` varchar(20) DEFAULT NULL COMMENT 'Phone number ... 20 digits to accomodate international numbers',
  `phone_extension` varchar(10) DEFAULT NULL COMMENT 'Phone extension',
  `fax` varchar(20) DEFAULT NULL COMMENT 'Fax number ... 20 digits to accomodate international numbers',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`contact_id`),
  UNIQUE KEY `contact_contacttypeid_orgid_uc` (`contact_type_id`,`org_id`),
  KEY `contact_contacttype_fki` (`contact_type_id`),
  KEY `contact_org_fki` (`org_id`),
  CONSTRAINT `contact_contacttype_fk` FOREIGN KEY (`contact_type_id`) REFERENCES `contact_type` (`contact_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `contact_org_fk` FOREIGN KEY (`org_id`) REFERENCES `org` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=505 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER contact_before_insert   BEFORE INSERT  ON contact  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER contact_before_update   BEFORE UPDATE   ON contact  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.address_line_1 <=> NEW.address_line_1   
	  AND OLD.address_line_2 <=> NEW.address_line_2   
	  AND OLD.city <=> NEW.city   
	  AND OLD.contact_id <=> NEW.contact_id   
	  AND OLD.contact_type_id <=> NEW.contact_type_id   
	  AND OLD.country <=> NEW.country   
	  AND OLD.email <=> NEW.email   
	  AND OLD.fax <=> NEW.fax   
	  AND OLD.name <=> NEW.name   
	  AND OLD.org_id <=> NEW.org_id   
	  AND OLD.phone <=> NEW.phone   
	  AND OLD.phone_extension <=> NEW.phone_extension   
	  AND OLD.state <=> NEW.state   
	  AND OLD.title <=> NEW.title   
	  AND OLD.zip <=> NEW.zip   
    ) ) THEN
	  INSERT INTO contact_hist (change_type,change_date,change_user,
	     address_line_1
	     , address_line_2
	     , city
	     , contact_id
	     , contact_type_id
	     , country
	     , email
	     , fax
	     , name
	     , org_id
	     , phone
	     , phone_extension
	     , state
	     , title
	     , zip
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.address_line_1
	     , OLD.address_line_2
	     , OLD.city
	     , OLD.contact_id
	     , OLD.contact_type_id
	     , OLD.country
	     , OLD.email
	     , OLD.fax
	     , OLD.name
	     , OLD.org_id
	     , OLD.phone
	     , OLD.phone_extension
	     , OLD.state
	     , OLD.title
	     , OLD.zip
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER contact_before_delete   BEFORE DELETE   ON contact  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO contact_hist (change_type,change_date,change_user,
	     address_line_1
	     , address_line_2
	     , city
	     , contact_id
	     , contact_type_id
	     , country
	     , email
	     , fax
	     , name
	     , org_id
	     , phone
	     , phone_extension
	     , state
	     , title
	     , zip
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.address_line_1
	    , OLD.address_line_2
	    , OLD.city
	    , OLD.contact_id
	    , OLD.contact_type_id
	    , OLD.country
	    , OLD.email
	    , OLD.fax
	    , OLD.name
	    , OLD.org_id
	    , OLD.phone
	    , OLD.phone_extension
	    , OLD.state
	    , OLD.title
	    , OLD.zip
	);

    INSERT INTO contact_hist (change_type,change_date,change_user,
	     address_line_1
	     , address_line_2
	     , city
	     , contact_id
	     , contact_type_id
	     , country
	     , email
	     , fax
	     , name
	     , org_id
	     , phone
	     , phone_extension
	     , state
	     , title
	     , zip
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.address_line_1
	    , OLD.address_line_2
	    , OLD.city
	    , OLD.contact_id
	    , OLD.contact_type_id
	    , OLD.country
	    , OLD.email
	    , OLD.fax
	    , OLD.name
	    , OLD.org_id
	    , OLD.phone
	    , OLD.phone_extension
	    , OLD.state
	    , OLD.title
	    , OLD.zip
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `contact_hist`
--

DROP TABLE IF EXISTS `contact_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_hist` (
  `contact_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `contact_id` int(10) unsigned DEFAULT NULL,
  `contact_type_id` int(10) unsigned DEFAULT NULL,
  `org_id` int(10) unsigned DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address_line_1` varchar(100) DEFAULT NULL,
  `address_line_2` varchar(100) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `country` varchar(2) DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `phone_extension` varchar(10) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`contact_hist_id`)
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_hist`
--

LOCK TABLES `contact_hist` WRITE;
/*!40000 ALTER TABLE `contact_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_type`
--

DROP TABLE IF EXISTS `contact_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_type` (
  `contact_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `code` varchar(50) NOT NULL COMMENT 'Unique (within scope) code that can be used by import, exports, etc.',
  `name` varchar(100) NOT NULL COMMENT 'Unique (within scope) name that is displayed to the user.',
  `display_order` tinyint(4) DEFAULT NULL COMMENT 'Used to force the order (ascending) in which the contact types are displayed on the website.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`contact_type_id`),
  UNIQUE KEY `contacttype_code_scopeid_uc` (`code`,`scope_id`),
  UNIQUE KEY `contacttype_name_scopeid_uc` (`name`,`scope_id`),
  KEY `contacttype_scope_fki` (`scope_id`),
  CONSTRAINT `contacttype_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_type`
--

LOCK TABLES `contact_type` WRITE;
/*!40000 ALTER TABLE `contact_type` DISABLE KEYS */;
INSERT INTO `contact_type` VALUES (36,28,'primary','Primary',1,null,null),(37,28,'secondary','Secondary',2,null,null);
/*!40000 ALTER TABLE `contact_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER contact_type_before_insert   BEFORE INSERT  ON contact_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.contact_type_id,'I', vChangeDate, vChangeUser, 'contact_type','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.contact_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.contact_type_id,'I', vChangeDate, vChangeUser, 'contact_type','contact_type_id',null, NEW.contact_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.contact_type_id,'I', vChangeDate, vChangeUser, 'contact_type','display_order',null, NEW.display_order);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.contact_type_id,'I', vChangeDate, vChangeUser, 'contact_type','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.contact_type_id,'I', vChangeDate, vChangeUser, 'contact_type','scope_id',null, NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER contact_type_before_update   BEFORE UPDATE   ON contact_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'U', vChangeDate, vChangeUser, 'contact_type','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.contact_type_id <=> NEW.contact_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'U', vChangeDate, vChangeUser, 'contact_type','contact_type_id',OLD.contact_type_id, NEW.contact_type_id);
	END IF;
	IF(NOT OLD.display_order <=> NEW.display_order) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'U', vChangeDate, vChangeUser, 'contact_type','display_order',OLD.display_order, NEW.display_order);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'U', vChangeDate, vChangeUser, 'contact_type','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'U', vChangeDate, vChangeUser, 'contact_type','scope_id',OLD.scope_id, NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER contact_type_before_delete   BEFORE DELETE   ON contact_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'D', vChangeDate, vChangeUser, 'contact_type','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.contact_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'D', vChangeDate, vChangeUser, 'contact_type','contact_type_id',OLD.contact_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'D', vChangeDate, vChangeUser, 'contact_type','display_order',OLD.display_order, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'D', vChangeDate, vChangeUser, 'contact_type','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.contact_type_id,'D', vChangeDate, vChangeUser, 'contact_type','scope_id',OLD.scope_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `custom_text`
--

DROP TABLE IF EXISTS `custom_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_text` (
  `custom_text_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `code` varchar(100) NOT NULL COMMENT 'Unique (within scope) identifier that can be used code to lookup a specific row.  This code should match the keys to the message bundles.',
  `text` varchar(20000) NOT NULL COMMENT 'The text that is displayed to the user.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`custom_text_id`),
  UNIQUE KEY `customtext_code_scopeid_uc` (`code`,`scope_id`),
  KEY `customtext_scope_fki` (`scope_id`),
  CONSTRAINT `customtext_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=279 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_text`
--

LOCK TABLES `custom_text` WRITE;
/*!40000 ALTER TABLE `custom_text` DISABLE KEYS */;
INSERT INTO `custom_text` VALUES (2,1,'core.add','Add',null,null),(11,28,'core.header.logo','',null,null),(13,28,'tab.organizations.title','Organization Management',null,null),(14,28,'tab.users.title','User Accounts & Permissions',null,null),(15,28,'tab.fileBatch.title','Batch File Importing & Exporting',null,null),(32,28,'ready.org.task.network','School Readiness Survey Questions',null,null),(37,28,'tab.organizations.description','Manage organizations, hierarchy structure and contacts data',null,null),(43,28,'ready.tab.device.title','Device Inventory Management',null,null),(60,28,'core.fileTypeName.org_import','Organization Import',null,null),(61,28,'org.details.parentOrgLabel','Organization Parent Name',null,null),(62,28,'organization.list.orgTypeHeader','Organization Type',null,null),(63,28,'core.org','Organization',null,null),(64,28,'ready.tab.snapshots.description','Manage snapshots and progress reporting',null,null),(67,28,'task.user.new.selectOrgs','Select Organizations',null,null),(68,28,'core.fileTypeName.org_export','Organization Export',null,null),(70,28,'core.header.logo.src','readiness-logo.png',null,null),(130,28,'core.header.icon.src','readiness.ico',null,null),(131,28,'core.footer.content','<p><a href=\"http://www.k12.wa.us/smarter\" target=\"_blank\"><img class=\"addcontext-src-path\" onmouseover=\"this.src=contextPath+\'/static/images/program/smarter-logo.gif\'\" onmouseout=\"this.src=contextPath+\'/static/images/program/smarter-logo-grayscale.gif\'\" src=\"/static/images/program/smarter-logo-grayscale.gif\" alt=\"Smarter Balanced\" width=\"125\" height=\"40\" /></a> <a href=\"http://www.parcconline.org\" target=\"_blank\"><img id=\"parcc\" class=\"addcontext-src-path\" style=\"margin-left: 40px;\" onmouseover=\"this.src=contextPath+\'/static/images/program/parcc-logo.gif\'\" onmouseout=\"this.src=contextPath+\'/static/images/program/parcc-logo-grayscale.gif\'\" src=\"/static/images/program/parcc-logo-grayscale.gif\" alt=\"PARCC\" width=\"175\" height=\"40\" /></a> <a href=\"http://www.setda.org\" target=\"_blank\"><img id=\"setda\" class=\"addcontext-src-path\" style=\"margin-left: 40px;\" onmouseover=\"this.src=contextPath+\'/static/images/program/setda-logo.gif\'\" onmouseout=\"this.src=contextPath+\'/static/images/program/setda-logo-grayscale.gif\'\" src=\"/static/images/program/setda-logo-grayscale.gif\" alt=\"setda\" width=\"150\" height=\"40\" /></a> <a href=\"http://www.pearson.com\" target=\"_blank\"><img id=\"pearson\" class=\"addcontext-src-path\" style=\"margin-left: 40px;\" onmouseover=\"this.src=contextPath+\'/static/images/program/pearson-logo.gif\'\" onmouseout=\"this.src=contextPath+\'/static/images/program/pearson-logo-grayscale.gif\'\" src=\"/static/images/program/pearson-logo-grayscale.gif\" alt=\"pearson\" width=\"125\" height=\"40\" /></a></p>',null,null),(137,28,'ready.tab.minimumRequirements.title','Consortium Minimum/Recommended Requirements',null,null),(138,28,'ready.tab.minimumRequirements.description','Add and manage minimum requirements',null,null),(139,28,'ready.tab.reports.title','Results & Indicators',null,null),(140,28,'ready.tab.reports.deviceAssessment.sample.title','Device Indicators - Coming April 9, 2012',null,null),(142,28,'ready.tab.reports.sample.description','<p style=\"text-align: left;\"><span style=\"color: #000080;\"><strong><br /></strong></span></p><p style=\"text-align: left;\"><span style=\"color: #000000;\"><strong>This report will be available beginning April 9th, 2012. &nbsp;</strong></span></p><p style=\"text-align: left;\"><span style=\"color: #000000;\"><strong><br /></strong></span></p><p style=\"text-align: left;\"><span style=\"color: #000000;\"><strong>The screen depiction below represents the type of information that will be available from this report. &nbsp;The data will be assembled and aggregated at the School, District, State and Consortium levels.</strong></span></p><p style=\"text-align: left;\"><span style=\"color: #ff0000;\"><strong><br /></strong></span></p><p style=\"text-align: left;\"><span style=\"color: #ff0000;\"><strong><br /></strong></span></p>',null,null),(143,28,'ready.tab.reports.testAssessment.sample.title','Device to Test-Taker Indicators - Coming April 9, 2012',null,null),(144,28,'ready.tab.reports.networkAssessment.sample.title','Network Indicators - Coming April 9, 2012',null,null),(147,28,'ready.tab.reports.networkAssessment.description','Results show the percentage of bandwidth currently available to test the maximum number of simultaneous test-takers',null,null),(150,28,'ready.tab.reports.deviceAssessment.sample.description','Results show the percentage of devices that meet requirements',null,null),(151,28,'ready.tab.reports.deviceAssessment.description','Results show the percentage of devices that meet requirements',null,null),(152,28,'ready.tab.reports.description','Results & Indicators ',null,null),(153,28,'ready.tab.reports.testAssessment.sample.description','Results show the percentage of students who can be tested with available devices in the available time',null,null),(154,28,'ready.tab.reports.networkAssessment.sample.description','Results show the percentage of bandwidth currently available to test the maximum number of simultaneous test-takers',null,null),(155,28,'ready.tab.reports.testAssessment.description','Results show the percentage of students who can be tested with available devices in the available time',null,null),(156,28,'help_filebatch.list','<html><head><title>Batch File Importing &amp; Exporting</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Batch File Importing &amp; Exporting</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Batch File Importing &amp; Exporting</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Import, export, and resolve file and import alerts for multiple file types. </p>\r\n<h3>Find</h3>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can control which filters are used to locate files. Click<span class=\"hyperlink\">Manage Filters</span> to access a list of available filters. \r\n  You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can click <span class=\"hyperlink\">More</span> to access a list of available filters. You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<h3>Files</h3>\r\n<p class=\"Body-Text\">After a record is selected in the grid, it will be displayed in this section. If you want to start over or for some other reason clear the list, click the <span class=\"hyperlink\">Clear</span> hyperlink. </p>\r\n<h3>Tasks</h3>\r\n<p class=\"Body-Text\">The tasks that are available display in this section. After selecting a record in the grid, select the applicable check box. You can select multiple check boxes if you have multiple tasks to complete. After you have selected the applicable check boxes, click <span class=\"button\">Start Tasks</span> button. </p>\r\n<h3>Grid</h3>\r\n<p class=\"Body-Text\">Use the <span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page. Select \r\n  the check box next to the column you want to include in the grid. </p>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(157,28,'help_task.batch.org.info.org-info-batch-task','<html><head><title>Import or Export School Survey Files</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Import or Export School Survey Files</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Import or Export School Survey Files</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Import and export school survey files. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Batch File Importing & Exporting ==> Select the <span class=\"hyperlink\">Import or export school survey files</span> check box ==> Start Tasks </p>\r\n<h3>Importing or Exporting Users Files</h3>\r\n<p class=\"Body-Text\">The <span class=\"dropdown\">Action</span> drop-down determines if you are going to import or export organization files. </p>\r\n<p class=\"Body-Text\">A <span class=\"red\">*</span> after the field label means the field is required. </p>\r\n<ol>\r\n  <li> To import a school survey file, select <span class=\"dropdown\">Import</span> from the <span class=\"dropdown\">Action</span> from the drop-down list, select the method for importing the file, the file name and location and a description.<br>\r\n    To export a school survey file, select <span class=\"dropdown\">Export</span> from the <span class=\"dropdown\">Action</span> from the drop-down list and enter a description. </li>\r\n  <li>Click a button to finish your changes: </li>\r\n  <ul>\r\n    <li>Click <span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page. </li>\r\n    <li>Click <span class=\"button\">Reset</span> to revert all of your changes to their original value. </li>\r\n    <li>Click <span class=\"button\">Process File</span> to save all of your changes. </li>\r\n    <li>Click <span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\n      previous page. </li>\r\n    <li>Click <span class=\"button\">Exit</span> to return to the previous page. </li>\r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(158,28,'help_task.scope.limits.limits','<html><head><title>Consortium Minimum Requirements</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Consortium Minimum Requirements</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<hr>\r\n<h1>Consortium Minimum Requirements</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Configure the minimum device and network requirements. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Consortium Minimum Requirements </p>\r\n<h3>Setting Minimum Requirements</h3>\r\n<ol>\r\n  <li> <span class=\"Body-Text\">Using the drop-downs, set the minimum requirements for proc</span>essor and memory for devices. </li>\r\n  <li>Click a button to finish your changes: </li>\r\n  <ul>\r\n    <li>Click <span class=\"button\">Cancel</span> to revert all of your changes to their original value. </li>\r\n    <li>Click <span class=\"button\">Save</span> to save all of your changes. </li>\r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(159,28,'help_task.device.remove.remove','<html><head><title>Delete Devices</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<script type=\"text/javascript\" src=\"autocreatetoc.js\" language=\"JavaScript1.2\"></script>\r\n<title>Delete Devices</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Delete Devices</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Remove a device from the organization. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; Device Inventory Management ==&gt; Select a user in the grid ==&gt; Select the Delete Devices check box ==&gt; Start Tasks </p>\r\n<h3>Assigning an Organization to a User</h3>\r\n<ol>\r\n  <li>Select the check box next to the device. </li>\r\n  <li>Click a button to finish your changes: </li>\r\n</ol>\r\n<ul>\r\n  <li>Click <span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page. </li>\r\n  <li>Click <span class=\"button\">Reset</span> to revert all of your changes to their original value. </li>\r\n  <li>Click <span class=\"button\">Delete</span> to save all of your changes. </li>\r\n  <li>Click <span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\n    previous page. </li>\r\n  <li>Click <span class=\"button\">Exit</span> to return to the previous page. </li>\r\n</ul>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(160,28,'help_task.device.edit.edit','<html><head><title>Edit Devices</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Edit Devices</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Edit Devices</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">After a device has been created, you can change the previously defined details for any device within the <span class=\"software\">Tool</span>. </p>\r\n<p class=\"Body-Text\">You can toggle from grid mode to details mode on this window by clicking the icon between the <span class=\"button\">Next Task</span> and <span class=\"button\">Exit</span> buttons. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; Device Inventory Management ==&gt; Select a user in the grid ==&gt; Select the Edit Devices check box ==&gt; Start Tasks </p>\r\n<h3>Editing a User</h3>\r\n<p class=\"Body-Text\">An <span class=\"color_red\">*</span> before or after the field label means the field is required. </p>\r\n<ol>\r\n  <li>Enter the required information and select values from the drop-downs. </li>\r\n  <li>Click a button to finish your changes: </li>\r\n</ol>\r\n<ul>\r\n  <li>Click <span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page. </li>\r\n  <li>Click <span class=\"button\">Reset</span> to revert all of your changes to their original value. </li>\r\n  <li>Click <span class=\"button\">Save</span> to save all of your changes. </li>\r\n  <li>Click <span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\n    previous page. </li>\r\n  <li>Click <span class=\"button\">Exit</span> to return to the previous page. </li>\r\n</ul>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(161,28,'help_task.device.add','<html><head><title>New Devices</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>New Devices</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>New Devices</h1>\r\n  <hr>\r\n\r\n \r\n<h3>Overview</h3>    \r\n<p class=\"Body-Text\">Use this tab to enter new devices into the <span class=\"software\">Tool</span>. \r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Device Management ==> Select the \r\n<span class=\"checkbox\">New  Devices</span> check box.\r\n</p>\r\n<h3>Adding a New Device</h3>\r\n<p class=\"Body-Text\">You can create multiple devices without leaving the tab. As devices are added through this tab, a list of devices displays in the \r\n\r\n<span class=\"list\">Devices</span> section.\r\n</p>\r\n<p class=\"Body-Text\">A \r\n<span class=\"red\">*</span> before or after the field label means the field is required.\r\n</p>\r\n<ol>\r\n<li>In the \r\n<span class=\"section\">Organization</span> section, select an organization. \r\nClick the \r\n<span class=\"hyperlink\">Change</span> hyperlink to display a list of available organizations.\r\n</li>\r\n<li>Enter the device information:\r\n  <ul>\r\n<li>Enter the required information and select values from the drop-downs.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Auto populate fields with values from current system</span> to use the existing device to create a new device.\r\n</li></ul><li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Create Device</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(162,28,'help_device.list','<html><head><title>Device Management</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Device Management</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Device Management</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Establish and maintain devices. </p>\r\n<h3>Find</h3>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can control which filters are used to locate devices. Click <span class=\"hyperlink\">Manage Filters</span> to access a list of available filters. \r\n  You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can click <span class=\"hyperlink\">More</span> to access a list of available filters. You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<h3>Devices</h3>\r\n<p class=\"Body-Text\">After a record is selected in the grid, it will be displayed in this section. If you want to start over or clear the list, click the <span class=\"hyperlink\">Clear</span> hyperlink. </p>\r\n<h3>Tasks</h3>\r\n<p class=\"Body-Text\">The tasks that are available display in this section. After selecting a record in the grid, select the applicable check box. You can select multiple check boxes if you have multiple tasks to complete. After you have selected the applicable check boxes, click <span class=\"button\">Start Tasks</span> button. </p>\r\n<p class=\"Body-Text\">If you have multiple devices to add, it may be easier to import them into the <span class=\"software\">Tool</span>. Click the <span class=\"hyperlink\">Device Import/Export</span> hyperlink to access this functionality.</p>\r\n<h3>Grid</h3>\r\n<p class=\"Body-Text\">Use the <span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page. Select \r\n  the check box next to the column you want to include in the grid. </p>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(163,28,'help_task.batch.device.devices','<html><head><title>Import or Export Devices Files</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Import or Export Devices Files</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Import or Export Devices Files</h1>\r\n  <hr>\r\n\r\n \r\n<p class=\"Body-Text\">Import and export devices.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; Device Inventory Management ==&gt; Click the \r\n<span class=\"hyperlink\">Import or export devices files</span> hyperlink\r\n</p>\r\n<p class=\"Body-Text\">Setup ==> Batch File Importing & Exporting ==> Select the \r\n<span class=\"hyperlink\">Import or export devices files</span> check box ==> Start Tasks\r\n</p>\r\n<h3>Importing or Exporting Devices Files</h3>\r\n<p class=\"Body-Text\">The \r\n<span class=\"dropdown\">Action</span> drop-down determines if you are going to import or export device files. \r\n</p>\r\n<p class=\"Body-Text\">A \r\n<span class=\"red\">*</span> after the field label means the field is required.\r\n</p>\r\n<ol>\r\n<li> To import a device file, select \r\n<span class=\"dropdown\">Import</span> from the \r\n<span class=\"dropdown\">Action</span> from the drop-down list, select the method for importing the file, the file name and location and a description.<br>\r\n  To export a device file, select \r\n<span class=\"dropdown\">Export</span> from the \r\n<span class=\"dropdown\">Action</span> from the drop-down list and enter a description.\r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Process File</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(164,28,'help_task.batch.user.users','<html><head><title>Import or Export Users Files</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Import or Export Users Files</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Import or Export Users Files</h1>\r\n  <hr>\r\n\r\n \r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Import and export users.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Batch File Importing & Exporting ==> Select the \r\n<span class=\"hyperlink\">Import or export users files</span> check box ==> Start Tasks\r\n</p>\r\n<h3>Importing or Exporting Users Files</h3>\r\n<p class=\"Body-Text\">The \r\n<span class=\"dropdown\">Action</span> drop-down determines if you are going to import or export users files. \r\n</p>\r\n<p class=\"Body-Text\">A \r\n<span class=\"red\">*</span> after the field label means the field is required.\r\n</p>\r\n<ol>\r\n<li> To import a users file, select \r\n<span class=\"dropdown\">Import</span> from the \r\n<span class=\"dropdown\">Action</span> from the drop-down list, select the method for importing the file, the file name and location and a description.<br>\r\n  To export a users file, select \r\n<span class=\"dropdown\">Export</span> from the \r\n<span class=\"dropdown\">Action</span> from the drop-down list and enter a description.\r\n</li>\r\n  <li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Process File</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(165,28,'help_task.batch.details.details','<html><head><title>View Files Details</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>View Files Details</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>View Files Details</h1>\r\n  <hr>\r\n\r\n \r\n<p class=\"Body-Text\">You can view the details of the file, including filename, type, and other information. \r\n</p>\r\n<p class=\"Body-Text\">You can toggle from grid mode to details mode on this window by clicking the icon between the \r\n<span class=\"button\">Next Task</span> and \r\n<span class=\"button\">Exit</span> buttons.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Batch File Importing & Exporting ==> Select the \r\n<span class=\"checkbox\">View files details</span> check box ==> Start Tasks\r\n<h3>Adding a Device</h3>\r\n<ol>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>    \r\n<li>Click \r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(166,28,'help_task.batch.org.org-batch-task','<html><head><title>Import or Export Organization Files</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Import or Export Organization Files</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Import or Export Organization Files</h1>\r\n  <hr>\r\n\r\n \r\n<p class=\"Body-Text\">Import and export organization files.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Batch File Importing & Exporting ==> Select the \r\n<span class=\"hyperlink\">Import or export org files</span> check box ==> Start Tasks\r\n</p>\r\n<h3>Importing or Exporting Users Files</h3>\r\n<p class=\"Body-Text\">The \r\n<span class=\"dropdown\">Action</span> drop-down determines if you are going to import or export users files. \r\n</p>\r\n<p class=\"Body-Text\">A \r\n<span class=\"red\">*</span> after the field label means the field is required.\r\n</p>\r\n<ol>\r\n<li> To import a users file, select \r\n<span class=\"dropdown\">Import</span> from the \r\n<span class=\"dropdown\">Action</span> from the drop-down list,  the file name and location and a description.<br>\r\n  To export a users file, select \r\n<span class=\"dropdown\">Export</span> from the \r\n<span class=\"dropdown\">Action</span> from the drop-down list and enter a description.\r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Process File</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(167,28,'help_task.org.delete.delete','<html><head><title>Delete Organization</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Delete Organization</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Delete Organization</h1>\r\n  <hr>\r\n\r\n \r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Delete an organization.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Organization Management==> Select a user in the grid ==> Select the Delete Organizations check box ==> Start Tasks\r\n</p>\r\n<h3>Deleting an Organization</h3>\r\n\r\n<ol>\r\n<li>Select an organization.\r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n</ol>\r\n<ul>\r\n<li>Click \r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Delete</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>\r\n</ul>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(168,28,'help_task.org.edit.edit','<html><head><title>Edit Organizations</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Edit Organizations</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Edit Organizations</h1>\r\n  <hr>\r\n\r\n \r\n        <h3>Overview</h3>\r\n<p class=\"Body-Text\">After an organization has been created, you can change the previously defined details for any organization within the \r\n<span class=\"software\">Tool</span>. \r\n</p>\r\n<p class=\"Body-Text\">You can toggle from grid mode to details mode on this window by clicking the icon between the \r\n<span class=\"button\">Next Task</span> and \r\n<span class=\"button\">Exit</span> buttons.\r\n</p>\r\n<p class=\"Body-Text\">Use the \r\n<span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page if you are viewing the page in grid mode. Select \r\nthe check box next to the column you want to include in the grid.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; Device Inventory Management ==&gt; Select a user in the grid ==&gt; Select the Edit Devices check box ==&gt; Start Tasks\r\n</p>\r\n<h3>Editing an Organization</h3>\r\n<p class=\"Body-Text\">An <span class=\"color_red\">*</span> before or after the field label means the field is required. \r\n</p>\r\n<ol> \r\n<li>Enter the required information and select values from the drop-downs.\r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n</ol>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Save</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(169,28,'help_task.org.contact.edit','<html><head><title>Edit Contacts</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Edit Contacts</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Edit Contacts</h1>\r\n  <hr>\r\n\r\n \r\n    <h3>Overview</h3>\r\n<p class=\"Body-Text\">Enter information about data entry, network, testing, and  personnel readiness into the \r\n<span class=\"software\">Tool</span>. \r\n</p>\r\n<p class=\"Body-Text\">Use the \r\n<span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page if you are viewing the page in grid mode. Select \r\nthe check box next to the column you want to include in the grid.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Organization Management==> Select a user in the grid ==> Select the Edit Contacts check box ==> Start Tasks\r\n</p>\r\n<h3>Editing Contact Information</h3>\r\n<ol>\r\n<li>Complete the applicable fields.\r\n</li>\r\n  <li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Save</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(170,28,'help_task.org.create.add','<html><head><title>Organizations New</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Organizations New</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>New Organizations</h1>\r\n  <hr>\r\n\r\n \r\n    <h3>Overview</h3>\r\n<p class=\"Body-Text\">Use this tab to enter new organizations into the <span class=\"software\">Tool</span>.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Organization Management==> Select a user in the grid ==> Select the New Organizations check box ==> Start Tasks\r\n</p>\r\n<h3>Adding an Organization</h3>\r\n<p class=\"Body-Text\">A <span class=\"red\">*</span> after the field label means the field is required.\r\n</p>\r\n<ol>\r\n<li>Complete the information by choosing the applicable options from the drop-down lists.\r\n</li>\r\n  <li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Create</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(171,28,'help_organization.list','<html><head><title>Organization Management</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Organization Management</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Organization Management</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Establish and maintain organizations. By definition, an organization is a district or school. </p>\r\n<h3>Find</h3>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can control which filters are used to locate organizations. Click <span class=\"hyperlink\">Manage Filters</span> to access a list of available filters. \r\n  You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes.  After  you enter information in the entry field, you can select the <span class=\"checkbox\">Search All Organizations</span> check box. </p>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can click <span class=\"hyperlink\">More</span> to access a list of available filters. You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<h3>Organizations</h3>\r\n<p class=\"Body-Text\">After a record is selected in the grid, it will be displayed in this section. If you want to start over or clear the list, click the <span class=\"hyperlink\">Clear</span> hyperlink. </p>\r\n<h3>Tasks</h3>\r\n<p class=\"Body-Text\">The tasks that are available display in this section. After selecting a record in the grid, select the applicable check box. You can select multiple check boxes if you have multiple tasks to complete. After you have selected the applicable check boxes, click <span class=\"button\">Start Tasks</span> button. </p>\r\n<p class=\"Body-Text\">If you have multiple organizations to add, it may be easier to import them into the <span class=\"software\">Tool</span>. Click the <span class=\"hyperlink\">Device Import/Export</span> hyperlink to access this functionality.</p>\r\n<h3>Grid</h3>\r\n<p class=\"Body-Text\">Use the <span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page. Select \r\n  the check box next to the column you want to include in the grid. </p>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(172,28,'help_task.org.part.edit','<html><head><title>Participations</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Participations</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Participations</h1>\r\n  <hr>\r\n\r\n \r\n    <h3>Overview</h3>\r\n<p class=\"Body-Text\">Designate if an organization is participating.\r\n</p>\r\n<p class=\"Body-Text\">You can toggle from grid mode to details mode on this window by clicking the icon between the \r\n<span class=\"button\">Next Task</span> and \r\n<span class=\"button\">Exit</span> buttons.\r\n</p>\r\n<p class=\"Body-Text\">Use the \r\n<span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page if you are viewing the page in grid mode. Select \r\nthe check box next to the column you want to include in the grid.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; Organization Management ==&gt; Select an organization from the grid ==&gt; Select the \r\n<span class=\"checkbox\">Participations</span> check box ==&gt; Start Tasks\r\n</p>\r\n<h3>Procedure Name here</h3>\r\n<ol>\r\n<li>Select whether the organization is participating by selecting \r\n<span class=\"dropdownvalue\">Yes</span>.\r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Save</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>    \r\n<li>Click \r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n</ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(173,28,'help_task.org.network.network','<html><head><title>School Survey Questions</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>School Survey Questions</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>School Survey Questions</h1>\r\n  <hr>\r\n\r\n \r\n    <h3>Overview</h3>\r\n<p class=\"Body-Text\">Enter information about data entry, network, testing, and  personnel readiness into the \r\n<span class=\"software\">Tool</span>. \r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Organization Management==> Select a user in the grid ==> Select the School Survey Questions check box ==> Start Tasks\r\n</p>\r\n<h3>Entering School Survey Question Information</h3>\r\n\r\n<ol>\r\n<li>Enter your information and select values from the drop-downs.\r\n</li>\r\n  <li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Save</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(174,28,'help_default','<html><head><title>Welcome to the Technology Readiness Tool</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Welcome to the Technology Readiness Tool</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Welcome to the Technology Readiness Tool</h1>\r\n  <hr>\r\n\r\n \r\n <h3>Overview</h3>\r\n <p class=\"Body-Text\">The Technology Readiness Tool makes it easy for you to  perform your readiness-related duties, from generating reports about readiness  to submitting student data to viewing student results.<br>\r\n   The Technology Readiness Tool is an online tool to collect  information from various states. Currently, there are two major consortias:  SBAC and PARCC. Between these two groups, states need to assess their Readiness  for online testing for the 2014-2015 school year. To determine readiness,  devices that will be used for testing are entered through the Technology  Readiness Tool. The minimum requirements for a device is established through the Technology Readiness Tool and, as this information is entered, the level of  readiness for the state, district, and school is determined. The Technology Readiness  Tool allows states to set up, manage, and open up the tool to  the local districts and  schools, and provide support.</p>\r\n\r\n<p class=\"Body-Text\">States will set  up districts and schools  matching the statesâ  organizational structures, and assign user IDs to allow districts and  schools to access the Readiness Tool and reports. A State Readiness Coordinator (SRC)  will set up a district-level user, who, in turn, will set up a school-level  user.</p>\r\n <p class=\"Body-Text\">The Technology Readiness Tool   assesses current capacity and compares that to the technology that will   be needed to administer the new online assessments in four areas:</p>\r\n<ol> <li> Computers &amp; other devices</li>\r\n   <li>Ratio of devices to test-takers</li>\r\n   <li>Network and infrastructure</li>\r\n   <li>Personnel (staffing &amp; training)</li>\r\n   </ol>\r\n\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(175,28,'help_reports.completion.completionStatus','<html><head><title>Completion Status</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Completion Status</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Completion Status</h1>\r\n<hr>\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show each organization\'s current status. \r\n</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<p class=\"Body-Text\">Click <span class=\"button\">Print to .pdf</span> to  generate a PDF of the information on the page. You must have a PDF reader  installed, such as Adobe Reader. The Map and Table option buttons allow you to  toggle from viewing the information in a color-coded map or in a color-coded  table. </p>\r\n<p class=\"Body-Text\">  Click a state  to drill down to the district level. When you are looking at a list of  districts, click a district to drill down to the school level. Click <span class=\"button\">Export to .csv</span> to export the information  to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. </p>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(176,28,'help_reports.device.deviceAssessment','<html><head><title>Device Indicators</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Device Indicators</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Device Indicators</h1>\r\n<hr>\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show the percent of students who can be tested with available devices that meet minimum requirements during the scheduled sessions. You can identify which organizations will not be able to test all students expected to test with the available devices within the scheduled testing sessions.</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<p class=\"Body-Text\">Click <span class=\"button\">Print to .pdf</span> to  generate a PDF of the information on the page. You must have a PDF reader  installed, such as Adobe Reader. The Map and Table option buttons allow you to  toggle from viewing the information in a color-coded map or in a color-coded  table. </p>\r\n<p class=\"Body-Text\">  Click a state  to drill down to the district level. When you are looking at a list of  districts, click a district to drill down to the school level. Click <span class=\"button\">Export to .csv</span> to export the information  to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. </p>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(177,28,'help_reports.tester.testerAssessment','<html><head><title>Device to Test-Taker Indicators</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Device to Test-Taker Indicators</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Device to Test-Taker Indicators</h1>\r\n<hr>\r\n\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show the percent of devices that meet minimum requirements. You can identify which organizations need additional acceptable devices to meet minimum requirements.</p>    \r\n    \r\n    <p class=\"Body-Text\">Click <span class=\"button\">Export to .csv</span> to export the information to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. Click \r\n  <span class=\"button\">Print to .pdf</span> to generate a PDF of the information on the page. You must have a PDF reader installed, such as Adobe Reader. \r\n</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(178,28,'help_reports.network.networkAssessment','<html><head><title>Network Indicators</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Network Indicators</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Network Indicators</h1>\r\n<hr>\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show the percent of bandwidth that is currently available to test the maximum number of testers. You can identify which organizations need additional bandwidth to meet minimum requirements.</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<p class=\"Body-Text\">Click <span class=\"button\">Print to .pdf</span> to  generate a PDF of the information on the page. You must have a PDF reader  installed, such as Adobe Reader. The Map and Table option buttons allow you to  toggle from viewing the information in a color-coded map or in a color-coded  table. </p>\r\n<p class=\"Body-Text\">  Click a state  to drill down to the district level. When you are looking at a list of  districts, click a district to drill down to the school level. Click <span class=\"button\">Export to .csv</span> to export the information  to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. </p>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(179,28,'help_reports.staff.staffReportSample','<html><head><title>Staff &amp; Personnel Indicators</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Staff &amp; Personnel Indicators</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Staff &amp; Personnel Indicators</h1>\r\n<hr>\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show staff and personnel concerns/problems. \r\n</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<p class=\"Body-Text\">Click <span class=\"button\">Print to .pdf</span> to  generate a PDF of the information on the page. You must have a PDF reader  installed, such as Adobe Reader. The Map and Table option buttons allow you to  toggle from viewing the information in a color-coded map or in a color-coded  table. </p>\r\n<p class=\"Body-Text\">  Click a state  to drill down to the district level. When you are looking at a list of  districts, click a district to drill down to the school level. Click <span class=\"button\">Export to .csv</span> to export the information  to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. </p>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(180,28,'help_task.snapshot.remove.remove','<html><head><title>Delete Snapshot</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <script type=\"text/javascript\" src=\"autocreatetoc.js\" language=\"JavaScript1.2\"></script>\r\n    <title>Delete Snapshot</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Delete Snapshot</h1>\r\n  <hr>\r\n\r\n \r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Remove a Snapshot from the organization. \r\n</p>\r\n\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; Snapshot ==&gt; Select a user in the grid ==&gt; Select the Remove Snapshot check box ==&gt; Start Tasks\r\n</p>\r\n<h3>Removing a Snapshot</h3>\r\n<ol>\r\n<li>Select the check box next to the device.\r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n</ol>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Delete</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(181,28,'help_task.snapshot.edit.edit','<html><head><title>Edit Snapshot</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>Edit Snapshot</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Edit Snapshot</h1>\r\n  <hr>\r\n\r\n \r\n    <h3>Overview</h3>\r\n<p class=\"Body-Text\">After a snapshot has been created, you can change the previously defined details for any snapshot within the \r\n<span class=\"software\">Tool</span>.\r\n\r\n</p>\r\n<p class=\"Body-Text\">You can toggle from grid mode to details mode on this window by clicking the icon between the \r\n<span class=\"button\">Next Task</span> and <span class=\"button\">Exit</span> buttons.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; Snapshots ==&gt; Select a user in the grid ==&gt; Select the Edit Devices check box ==&gt; Start Tasks\r\n</p>\r\n<h3>Editing a Snapshot</h3>\r\n<p class=\"Body-Text\">An <span class=\"color_red\">*</span> before or after the field label means the field is required. \r\n</p>\r\n<ol> \r\n<li>Enter the required information and select values from the drop-downs.\r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n</ol>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Save</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(182,28,'help_task.snapshot.add','<html><head><title>New Devices</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n    <head>\r\n    <meta charset=\"utf-8\">\r\n    <meta name=\"description\" content=\"\">\r\n    <meta name=\"keywords\" content=\"\">\r\n    <title>New Devices</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n    </head>\r\n    <body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Add Snapshots</h1>\r\n  <hr>\r\n\r\n \r\n<h3>Overview</h3>    \r\n<p class=\"Body-Text\">Use this tab to enter a new snapshot into the <span class=\"software\">Tool</span>. \r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==> Snapshots ==> Select the \r\n<span class=\"checkbox\">Add  Snapshot</span> check box.\r\n</p>\r\n<h3>Adding a New Snapshot</h3>\r\n<p class=\"Body-Text\">A \r\n<span class=\"red\">*</span> before or after the field label means the field is required.\r\n</p>\r\n<ol>\r\n<li>Enter the name of the snapshot.\r\n  \r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n<ul>\r\n<li>Click \r\n\r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes on the previous page.\r\n</li> \r\n<li>Click \r\n\r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Create</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n\r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes on the \r\nprevious page.\r\n</li>    \r\n<li>Click \r\n\r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>        \r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(183,28,'help_snapshot.list','<html><head><title>Snapshots Overview</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Snapshots Overview</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Snapshots</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">The Snapshots tab allows each Consortia to take a  &ldquo;snapshot&rdquo; of the data at a certain point in time and archive it off into  another table. The data collection is ongoing through the entire contact. If you want to capture data at a specific point to show progress, you would add a  snapshot through this tab. The information is then viewable on the Progress  Report.Â  </p>\r\n<p class=\"Body-Text\">You can take a snapshot at any time. You only have access  to your snapshots so you can manage them accordingly. </p>\r\n<h3>Find</h3>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can control which filters are used to locate users. Click <span class=\"hyperlink\">Manage Filters</span> to access a list of available filters. You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can click <span class=\"hyperlink\">More</span> to access a list of available filters. You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<h3>Snapshots</h3>\r\n<p class=\"Body-Text\">After a record is selected in the grid, it will be displayed in this section. If you want to start over or clear the list, click the <span class=\"hyperlink\">Clear</span> hyperlink. </p>\r\n<h3>Tasks</h3>\r\n<p class=\"Body-Text\">The tasks that are available display in this section. After selecting a record in the grid, select the applicable check box. You can select multiple check boxes if you have multiple tasks to complete. After you have selected the applicable check boxes, click <span class=\"button\">Start Tasks</span> button. </p>\r\n<h3>Grid</h3>\r\n<p class=\"Body-Text\">Use the <span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page. Select \r\n  the check box next to the column you want to include in the grid. </p>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(184,28,'help_task.user.update.edit','<html><head><title>Edit Users</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Edit Users</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Edit Users</h1>\r\n<hr>\r\n\r\n\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">After a user has been created, you can change the previously defined details for any user within the \r\n<span class=\"software\">Tool</span>. \r\n</p>\r\n<p class=\"Body-Text\">You can toggle from grid mode to details mode on this window by clicking the icon between the \r\n<span class=\"button\">Next Task</span> and \r\n<span class=\"button\">Exit</span> buttons.\r\n</p>\r\n<p class=\"Body-Text\">Use the \r\n<span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page if you are viewing the page in grid mode. Select \r\nthe check box next to the column you want to include in the grid.\r\n</p>\r\n<!--<p class=\"Body-Text\">Use the \r\n<span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page if you are viewing the page in grid mode. Select \r\nthe check box next to the column you want to include in the grid.\r\n</p>-->\r\n<h3>Navigation</h3>\r\n\r\n<p class=\"Body-Text\">Setup ==&gt; User Accounts &amp; Permissions ==&gt; Select a user in the grid ==&gt; Select the \r\n<span class=\"checkbox\">Edit Users</span> check box ==&gt; Start Tasks\r\n</p>\r\n<h3>Editing a User</h3>\r\n<p class=\"Body-Text\">An \r\n<span class=\"color_red\">*</span> before or after the field label means the field is required. \r\n</p>\r\n<ol>\r\n<li>Complete the applicable fields. When you click into either the \r\n<span class=\"fieldname\">Active Begin Date</span> or \r\n<span class=\"fieldname\">Active End Date</span> field, a popup calendar displays on which you can select a date.\r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n</ol>\r\n<ul>\r\n<li>Click \r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Save</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>\r\n</ul>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(185,28,'help_task.user.enable.edit','<html><head><title>Disable/Enable Users</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Disable/Enable Users</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Disable/Enable Users</h1>\r\n<hr>\r\n\r\n\r\n<p class=\"Body-Text\">After a user has been created through the New User process, the next step is to enable the user to have access to the \r\n<span class=\"software\">Tool</span>.\r\n</p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; User Accounts &amp; Permissions ==&gt; Select a user in the grid ==&gt; Select the \r\n<span class=\"checkbox\">Password Reset</span> check box ==&gt; Start Tasks\r\n</p>\r\n\r\n<h3>Disabling/Enabling a User</h3>\r\n<p class=\"Body-Text\">The \r\n<span class=\"dropdown\">Status</span> drop-down determines if the user has access to the \r\n<span class=\"software\">Tool</span>.\r\n</p>\r\n<p class=\"Body-Text\">A \r\n<span class=\"red\">*</span> after the field label means the field is required.\r\n</p>\r\n<ol>\r\n<li> To disable a user, select \r\n<span class=\"dropdown\">Disabled</span> from the \r\n<span class=\"dropdown\">Status</span> from the drop-down list. <br>\r\n    To enable a user, select \r\n<span class=\"dropdown\">Enabled</span> from the \r\n<span class=\"dropdown\">Status</span> from the drop-down list. \r\n</li>\r\n<li> When you click in the \r\n<span class=\"fieldname\">Disable Date</span> field, \r\n    a popup calendar appears in order for you to specify the date on which the user is to be disabled in the \r\n<span class=\"software\">Tool</span>. \r\n</li>\r\n<li> Complete the \r\n<span class=\"fieldname\">Disable Reason</span> field if you set the \r\n<span class=\"dropdown\">Disabled</span> drop-down to \r\n<span class=\"dropdownvalue\">Disabled</span>. \r\n</li>\r\n<li>Click a button to finish your changes:\r\n</li>\r\n</ol>\r\n<ul>\r\n<li>Click \r\n<span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Reset</span> to revert all of your changes to their original value.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Save</span> to save all of your changes.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the \r\n<span class=\"tasks\">Section</span>.\r\n</li>\r\n<li>Click \r\n<span class=\"button\">Exit</span> to return to the previous page.\r\n</li>\r\n</ul>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(186,28,'help_task.user.create.add','<html><head><title>New Users</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>New Users</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>New Users</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Enter new users into the <span class=\"software\">Tool</span>. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; User Accounts &amp; Permissions ==&gt; Select the <span class=\"checkbox\">New Users</span> check box ==&gt; Start Tasks </p>\r\n<h3>Adding a New User</h3>\r\n<p class=\"Body-Text\">You can create multiple users without leaving the tab. As users are added through this tab, a list of users displays in the <span class=\"list\">Selected Users</span> section. </p>\r\n<p class=\"Body-Text\">A <span class=\"red\">*</span> after the field label means the field is required. </p>\r\n<ol>\r\n  <li>Complete the applicable fields. When you click into either the <span class=\"fieldname\">Active Begin Date</span> or <span class=\"fieldname\">Active End Date</span> field, a popup calendar displays on which you can select a date. </li>\r\n  <li>Click a button to finish your changes: </li>\r\n  <ul>\r\n    <li>Click <span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the <span class=\"tasks\">Section</span>. </li>\r\n    <li>Click <span class=\"button\">Reset</span> to revert all of your changes to their original value. </li>\r\n    <li>Click <span class=\"button\">Create</span> to save all of your changes. </li>\r\n    <li>Click <span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the <span class=\"tasks\">Section</span>. </li>\r\n    <li>Click <span class=\"button\">Exit</span> to return to the previous page. </li>\r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(187,28,'help_task.user.orgassign.edit','<html><head><title>Organization Assignment</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Organization Assignment</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Organization Assignment</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Assign an organization to a user. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; User Accounts &amp; Permissions ==&gt; Select a user in the grid ==&gt; Select the <span class=\"checkbox\">Organization Assignment</span> check box ==&gt; Start Tasks </p>\r\n<h3>Assigning an Organization to a User</h3>\r\n<ol>\r\n  <li>Click the <span class=\"hyperlink\">Add</span> hyperlink in the <span class=\"section\">Organizations</span> section. Locate the organization to which the user is to be assigned. A check box will display for you to select in order to assign the organization to the user. </li>\r\n  <li>Click a button to finish your changes: </li>\r\n</ol>\r\n<ul>\r\n  <li>Click <span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the <span class=\"tasks\">Section</span>. </li>\r\n  <li>Click <span class=\"button\">Reset</span> to revert all of your changes to their original value. </li>\r\n  <li>Click <span class=\"button\">Save</span> to save all of your changes. </li>\r\n  <li>Click <span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the <span class=\"tasks\">Section</span>. </li>\r\n  <li>Click <span class=\"button\">Exit</span> to return to the previous page. </li>\r\n</ul>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(188,28,'help_user.list','<html><head><title>Users Overview</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Users Overview</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Users</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Set up users. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; User Accounts &amp; Permissions </p>\r\n<h3>Find</h3>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can control which filters are used to locate users. Click <span class=\"hyperlink\">Manage Filters</span> to access a list of available filters. You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<p class=\"Body-Text\">Within the <span class=\"section\">Find</span> section, you can click <span class=\"hyperlink\">More</span> to access a list of available filters. You will not be able to clear any check box that is disabled and selected. After you make your changes, click <span class=\"button\">Apply</span> to apply your changes. </p>\r\n<h3>Users</h3>\r\n<p class=\"Body-Text\">After a record is selected in the grid, it will be displayed in this section. If you want to start over or clear the list, click the <span class=\"hyperlink\">Clear</span> hyperlink. </p>\r\n<h3>Tasks</h3>\r\n<p class=\"Body-Text\">The tasks that are available display in this section. After selecting a record in the grid, select the applicable check box. You can select multiple check boxes if you have multiple tasks to complete. After you have selected the applicable check boxes, click <span class=\"button\">Start Tasks</span> button. </p>\r\n<p class=\"Body-Text\">If you have multiple users to add, it may be easier to import them into the <span class=\"software\">Tool</span>. Click the <span class=\"hyperlink\">User Import/Export</span> hyperlink to access this functionality.</p>\r\n<h3>Grid</h3>\r\n<p class=\"Body-Text\">Use the <span class=\"hyperlink\">Manage Columns</span> hyperlink to control the visible columns on the page. Select \r\n  the check box next to the column you want to include in the grid. </p>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(189,28,'help_task.user.update.resetpassword','<html><head><title>Password Reset</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Password Reset</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Password Reset</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">If a user is locked out of the <span class=\"software\">Tool</span>, the password can be reset through this page. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; User Accounts &amp; Permissions ==&gt; Select a user in the grid ==&gt; Select the <span class=\"checkbox\">Password Reset</span> check box ==&gt; Start Tasks </p>\r\n<h3>Resetting a User Password</h3>\r\n<ol>\r\n  <li>Click a button to finish your changes: </li>\r\n  <ul>\r\n    <li>Click <span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the <span class=\"tasks\">Section</span>. </li>\r\n    <li>Click <span class=\"button\">Reset</span> to revert all of your changes to their original value. </li>\r\n    <li>Click <span class=\"button\">Save</span> to save all of your changes. </li>\r\n    <li>Click <span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the <span class=\"tasks\">Section</span>. </li>\r\n    <li>Click <span class=\"button\">Exit</span> to return to the previous page. </li>\r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n\r\n</body><html>',null,null),(190,28,'help_task.user.roleassign.edit','<html><head><title>Role Assignment</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Role Assignment</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange--> \r\n<!--\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Role Assignment</h1>\r\n<hr>\r\n<h3>Overview</h3>\r\n<p class=\"Body-Text\">Assign roles to a user. </p>\r\n<h3>Navigation</h3>\r\n<p class=\"Body-Text\">Setup ==&gt; User Accounts &amp; Permissions ==&gt; Select a user in the grid ==&gt; Select the <span class=\"checkbox\">Role Assignment</span> check box ==&gt; Start Tasks </p>\r\n<h3>Assigning a Role</h3>\r\n<p class=\"Body-Text\">Use the procedure to assign a role to a user. </p>\r\n<ol>\r\n  <li>Click the <span class=\"hyperlink\">Add</span> hyperlink to search for a  role in the <span class=\"section\">Role</span> section. </li>\r\n  <li>You can click the magnifying glass icon to view user details. </li>\r\n  <li>Select the <span class=\"checkbox\">Admin Role</span> to designate the user as an administrator. </li>\r\n  <li>Click a button to finish your changes: </li>\r\n  <ul>\r\n    <li>Click <span class=\"button\">Previous Task</span> to return to the previous task if you selected multiple check boxes in the <span class=\"tasks\">Section</span>. </li>\r\n    <li>Click <span class=\"button\">Reset</span> to revert all of your changes to their original value. </li>\r\n    <li>Click <span class=\"button\">Save</span> to save all of your changes. </li>\r\n    <li>Click <span class=\"button\">Next Task</span> to advance to the next task if you selected multiple check boxes in the <span class=\"tasks\">Section</span>. </li>\r\n    <li>Click <span class=\"button\">Exit</span> to return to the previous page. </li>\r\n  </ul>\r\n</ol>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n-->\r\n</body><html>',null,null),(191,28,'help_reports.completion.completionStatusSample','<html><head><title>Completion Status</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Completion Status</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Completion Status</h1>\r\n<hr>\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show each organization\'s current status. \r\n</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<p class=\"Body-Text\">Click <span class=\"button\">Print to .pdf</span> to  generate a PDF of the information on the page. You must have a PDF reader  installed, such as Adobe Reader. The Map and Table option buttons allow you to  toggle from viewing the information in a color-coded map or in a color-coded  table. </p>\r\n<p class=\"Body-Text\">  Click a state  to drill down to the district level. When you are looking at a list of  districts, click a district to drill down to the school level. Click <span class=\"button\">Export to .csv</span> to export the information  to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. </p>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(192,28,'help_reports.device.deviceAssessmentSample','<html><head><title>Device Indicators</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Device Indicators</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Device Indicators</h1>\r\n<hr>\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show the percent of students who can be tested with available devices that meet minimum requirements during the scheduled sessions. You can identify which organizations will not be able to test all students expected to test with the available devices within the scheduled testing sessions.</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<p class=\"Body-Text\">Click <span class=\"button\">Print to .pdf</span> to  generate a PDF of the information on the page. You must have a PDF reader  installed, such as Adobe Reader. The Map and Table option buttons allow you to  toggle from viewing the information in a color-coded map or in a color-coded  table. </p>\r\n<p class=\"Body-Text\">  Click a state  to drill down to the district level. When you are looking at a list of  districts, click a district to drill down to the school level. Click <span class=\"button\">Export to .csv</span> to export the information  to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. </p>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(193,28,'help_reports.tester.testerAssessmentSample','<html><head><title>Device to Test-Taker Indicators</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Device to Test-Taker Indicators</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Device to Test-Taker Indicators</h1>\r\n<hr>\r\n\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show the percent of devices that meet minimum requirements. You can identify which organizations need additional acceptable devices to meet minimum requirements.</p>    \r\n    \r\n    <p class=\"Body-Text\">Click <span class=\"button\">Export to .csv</span> to export the information to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. Click \r\n  <span class=\"button\">Print to .pdf</span> to generate a PDF of the information on the page. You must have a PDF reader installed, such as Adobe Reader. \r\n</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(194,28,'help_reports.network.networkAssessmentSample','<html><head><title>Network Indicators</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Network Indicators</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Network Indicators</h1>\r\n<hr>\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show the percent of bandwidth that is currently available to test the maximum number of testers. You can identify which organizations need additional bandwidth to meet minimum requirements.</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<p class=\"Body-Text\">Click <span class=\"button\">Print to .pdf</span> to  generate a PDF of the information on the page. You must have a PDF reader  installed, such as Adobe Reader. The Map and Table option buttons allow you to  toggle from viewing the information in a color-coded map or in a color-coded  table. </p>\r\n<p class=\"Body-Text\">  Click a state  to drill down to the district level. When you are looking at a list of  districts, click a district to drill down to the school level. Click <span class=\"button\">Export to .csv</span> to export the information  to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. </p>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(195,28,'help_reports.staff.staffReportSampleSample','<html><head><title>Staff &amp; Personnel Indicators</title></head><body><!DOCTYPE HTML PUBliC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<meta name=\"description\" content=\"\">\r\n<meta name=\"keywords\" content=\"\">\r\n<title>Staff &amp; Personnel Indicators</title>\r\n<link rel=\"stylesheet\" href=\"readiness.css\">\r\n\r\n</head>\r\n<body>\r\n<!--To change the color of the bar switch green below to either: blue, purple or orange-->\r\n<!--\r\n<div class=\"w100percent green\">\r\n  <div class=\"topBarLogo floatLeft w226\"><img src=\"logoPearson.png\" width=\"150\" height=\"44\" alt=\"Pearson logo\" /></a> </div>\r\n</div>\r\n-->\r\n<h1>Staff &amp; Personnel Indicators</h1>\r\n<hr>\r\n\r\n<p class=\"Body-Text\">The purpose of this report is to show staff and personnel concerns/problems. \r\n</p>\r\n<p class=\"Body-Text\">The Map and Table option buttons allow you to toggle from viewing the information in a color-coded map or in a color-coded table. Click a state to drill down to the district level. When you are looking at a list of districts, click a district to drill down to the school level.\r\n</p>\r\n<p class=\"Body-Text\">Click <span class=\"button\">Print to .pdf</span> to  generate a PDF of the information on the page. You must have a PDF reader  installed, such as Adobe Reader. The Map and Table option buttons allow you to  toggle from viewing the information in a color-coded map or in a color-coded  table. </p>\r\n<p class=\"Body-Text\">  Click a state  to drill down to the district level. When you are looking at a list of  districts, click a district to drill down to the school level. Click <span class=\"button\">Export to .csv</span> to export the information  to a CSV (Comma-Separated-Values) file that can then be opened in a spreadsheet program like Microsoft Excel. </p>\r\n\r\n<hr>\r\n\r\n<!--\r\n<p class=\"updatedon\">Copyright Â© 2012 Pearson Education, Inc. or its affiliate(s). All rights reserved.\r\n</p>\r\n--></body><html>',null,null),(196,28,'ready.tab.reports.staffAssessment.sample.title','Staff & Personnel Report - Coming April 9, 2012',null,null),(197,28,'ready.tab.reports.staffAssessment.sample.description','Results show concerns about staff and personnel readiness',null,null),(198,28,'ready.tab.reports.completionStatus.sample.title','Completion Status - Coming April 9, 2012',null,null),(199,28,'tab.users.description','Add and manage users',null,null),(200,28,'ready.tab.device.description','Add and manage devices',null,null),(201,28,'tab.fileBatch.description','Import and export files',null,null),(202,28,'ready.tab.reports.staffAssessment.description','Results show concerns about staff and personnel readiness',null,null),(203,28,'ready.tab.reports.completionStatus.description','Results show schools that have indicated data are submitted and ready for review and verification',null,null),(204,28,'ready.tab.reports.completionStatus.sample.description','Results show schools that have completed input of data',null,null),(213,28,'ready.tab.reports.staffAssessment.title','Staff & Personnel Report',null,null),(214,28,'ready.report.tbdtext','<p>The indicator TBD, for \"To Be Determined,\" is used in the Technology Readiness Tool reports when some or all of the minimum requirements to deliver the PARCC and Smarter Balanced assessments have not yet been set or finalized.</p><p>Parameters in the report that have been marked TBD will be populated at a later date with data reporting on the specific readiness metric, when the assessment delivery requirements associated with that particular metric have been finalized and published.</p>',null,null),(222,28,'ready.link.answercenter.url','https://support.pearsonaccess.com/kaidara-advisor/bookmark?token=8Rrax4/7REp/Nawu1LGQy7Zl6mal4NlEDpgqO6TstfOIxrcYMb7gMA==&project=Domain&state=dialog-undecided-value&branch=prd&quest=dialog',null,null),(225,28,'task.org.survey.notApplies','School Readiness Survey Questions do not apply to this organization.',null,null),(226,28,'task.org.network.pageTitle','School Readiness Survey Questions',null,null),(227,28,'tab.survey.title','School Readiness Survey Questions',null,null),(228,28,'ready.tab.surveyQuestions.title','School Readiness Survey Questions',null,null),(229,28,'ready.org.task.surveyQ','School Readiness Survey Questions',null,null),(268,28,'ready.report.tbdtext.title','<p>The indicator TBD, for \"To Be Determined,\" is used in the Technology Readiness Tool reports when some or all of the minimum requirements to deliver the PARCC and Smarter Balanced assessments have not yet been set or finalized.</p><p>Parameters in the report that have been marked TBD will be populated at a later date with data reporting on the specific readiness metric, when the assessment delivery requirements associated with that particular metric have been finalized and published.</p>',null,null),(273,28,'core.welcomeMessage','<div style=\"margin: 30px;\">\r\n<table style=\"width: 100%; vertical-align: top; border: none;\"><colgroup><col width=\"60%\" valign=\"top\" /><col width=\"40%\" valign=\"top\" /></colgroup>\r\n<tbody>\r\n<tr>\r\n<td>\r\n<p style=\"margin: 0 0 12px;\">The move to online assessments gives rise to a distinctive set of challenges for school technology. This online tool gives schools a convenient way to capture and report on indicators of their technology readiness for the new Race to the Top Assessments.</p>\r\n<p style=\"margin: 0 0 12px;\">The Smarter Balanced Assessment Consortium and the Partnership for Assessment of Readiness for College and Careers (PARCC), working with the State Educational Technology Directors Association (SETDA) and Pearson, have developed the Technology Readiness Tool to support states as they transition to next-generation assessments.</p>\r\n<p style=\"margin: 0 0 12px;\">This open source tool will help state education agencies work with schools and districts to ensure readiness when the online assessments are launched by the two consortia, beginning in the 2014/15 school year.</p>\r\n<p style=\"margin: 0 0 12px;\">The Technology Readiness Tool assesses current capacity and compares that to the technology that will be needed to administer the new online assessments in four areas:</p>\r\n<ol style=\"list-style-type: decimal; margin: 0 0 12px 25px;\">\r\n<li>Computers &amp; other devices</li>\r\n<li>Ratio of devices to test-takers</li>\r\n<li>Network and infrastructure</li>\r\n<li>Personnel (staffing &amp; training)</li>\r\n</ol><em>If you have any questions, issues or concerns, please click <a title=\"SRC Contact Form\" href=\"http://techreadiness.pearson.com/\" target=\"_blank\">here to contact your State Readiness Coordinator (SRC)</a> for your specific state.</em></td>\r\n<td style=\"padding-left: 10px;\">\r\n<div style=\"border: 3px solid green; border-radius: 20px; padding: 15px 7px; background-color: #e7f8da; margin-bottom: 12px;\">\r\n<h3>Support Information</h3>\r\n<hr style=\"color: green; border: 1px solid green;\" />\r\n<ul>\r\n<li>On-page Help (question mark icon located in upper right corner of screen)</li>\r\n<li><a title=\"District Admin Quick Start Guide\" href=\"http://techreadiness.org/DAQSG\" target=\"_blank\">District Admin Quick Start Guide</a> - Added 05/28/12</li>\r\n<li><a title=\"District / School Quick Start Guide\" href=\"http://techreadiness.org/DSQSG\" target=\"_blank\">District / School Quick Start Guide</a> - Added 05/28/12</li>\r\n<li><a title=\"User\'s Guide\" href=\"http://www.TechReadiness.org/UsersGuide\" target=\"_blank\">User\'s Guide</a> - Updated 02/13/13</li>\r\n<li><a title=\"Understanding &amp; Interpreting Reports Guide\" href=\"http://techreadiness.org/understandingreports\" target=\"_blank\">Understanding &amp; Interpreting Reports Guide</a> - Added 01/25/13</li>\r\n<li><a title=\"Key Local Considerations\" href=\"http://techreadiness.org/u/TRT_Key_Local_Considerations.pdf\" target=\"_blank\">Key Local Considerations</a> - Added 06/14/13</li>\r\n<li><a title=\"Guide to Finding Schools with Low Readiness\" href=\"http://techreadiness.org/u/Low%20Readiness%20District%20Spreadsheets%20Process%20Document.docx\" target=\"_blank\">Guide to finding schools with low readiness</a> - Added 11/20/13</li>\r\n<li><a title=\"Training Module\" href=\"http://www.TechReadiness.org/TrainingModule\" target=\"_blank\">Training Module</a> - Updated 02/14/13</li>\r\n<li><a title=\"FAQs / Answer Center\" href=\"https://support.pearsonaccess.com/kaidara-advisor/bookmark?token=8Rrax4/7REp/Nawu1LGQy7Zl6mal4NlEDpgqO6TstfOIxrcYMb7gMA==&amp;project=Domain&amp;state=dialog-undecided-value&amp;branch=prd&amp;quest=dialog\" target=\"_blank\">FAQs / Answer Center</a> - Updates - Ongoing</li>\r\n</ul>\r\n<h3 style=\"text-align: right;\">File Layouts and Templates</h3>\r\n<hr style=\"color: green; border: 1px solid green;\" />\r\n<ul style=\"text-align: right;\">\r\n<li>Organization file <a title=\"Organization File Layout\" href=\"http://www.TechReadiness.org/OrgFileLayout\" target=\"_blank\">Layout</a> / <a title=\"Organization File Template\" href=\"http://www.TechReadiness.org/OrgFileTemplate\" target=\"_blank\">Template</a> - Updated 09/19/12</li>\r\n<li>User file <a title=\"User File Layout\" href=\"http://www.TechReadiness.org/UserFileLayout\" target=\"_blank\">Layout</a> / <a title=\"User File Template\" href=\"http://www.TechReadiness.org/UserFileTemplate\" target=\"_blank\">Template</a> - Updated 04/04/13</li>\r\n<li>Device file <a title=\"Device File Layout\" href=\"http://www.TechReadiness.org/DeviceFileLayout\" target=\"_blank\">Layout</a> / <a title=\"Device File Template\" href=\"http://www.TechReadiness.org/DeviceFileTemplate\" target=\"_blank\">Template</a> - Updated 02/14/13</li>\r\n<li>School Survey file <a title=\"School Survey File Layout\" href=\"http://www.TechReadiness.org/SchoolSurveyFileLayout\" target=\"_blank\">Layout</a> / <a title=\"School Survey File Template\" href=\"http://www.TechReadiness.org/SchoolSurveyFileTemplate\" target=\"_blank\">Template</a> - Updated 02/14/13</li>\r\n</ul>\r\n</div>\r\n<div style=\"border: 3px solid green; border-radius: 20px; padding: 15px 7px; background-color: #e7f8da; margin-bottom: 12px;\">\r\n<h3>Notifications</h3>\r\n<hr style=\"color: green; border: 1px solid green;\" />\r\n<h4><span style=\"color: #ff0000;\">Posted 07/23/13</span></h4>\r\n<ul style=\"list-style-type: disc; margin: 0 0 12px 25px;\">\r\n<li>Version 3.0 is currently scheduled for August 9th. Enhancements include a new home page with dashboard features, atypical and error checks before marking data complete, edited values for operating system Ubuntu and bandwidth, and a new Overall Readiness Indicators report.</li>\r\n<li>The next anticipated extraction/snapshot of data will be taken on Friday, 12/13/13. Please continue to make updates as appropriate. If your data is up-to-date in the system, no action is needed by you.</li>\r\n</ul>\r\n</div>\r\n</td>\r\n</tr>\r\n</tbody>\r\n</table>\r\n</div>',null,null),(276,28,'ready.dashboard.notifications','<h1>Posted 12/13/13</h1>\r\n<ul>\r\n<li>The next anticipated extraction/snapshot of data will be taken on Friday, 06/13/14.&nbsp; Please continue to make updates as appropriate.&nbsp; If your data is up-to-date in the system, no action is needed by you.</li>\r\n</ul>',null,null),(277,28,'ready.dashboard.resources','<p><span style=\"font-weight: bold; font-size: 15px; color: gray;\">SUPPORT INFORMATION</span></p>\r\n<ul style=\"list-style-type: disc; margin: 0 0 12px 25px;\">\r\n<li><a id=\"what_is_link\" href=\"#\">What is the Technology Readiness Tool?</a></li>\r\n<li>On-page Help (question mark icon located in upper right corner of screen)</li>\r\n<li><a title=\"District Admin Quick Start Guide\" href=\"http://techreadiness.org/DAQSG\" target=\"_blank\">District Admin Quick Start Guide</a> <span style=\"color: gray;\">(Added 05/28/12)</span></li>\r\n<li><a title=\"District / School Quick Start Guide\" href=\"http://techreadiness.org/DSQSG\" target=\"_blank\">District / School Quick Start Guide</a> <span style=\"color: gray;\">(Added 05/28/12)</span></li>\r\n<li><a title=\"User\'s Guide\" href=\"http://www.TechReadiness.org/UsersGuide\" target=\"_blank\">User\'s Guide</a> <span style=\"color: gray;\">(Updated 08/12/13)</span></li>\r\n<li><a title=\"Understanding &amp; Interpreting Reports Guide\" href=\"http://techreadiness.org/understandingreports\" target=\"_blank\">Understanding &amp; Interpreting Reports Guide</a> <span style=\"color: gray;\">(Added </span><span style=\"color: gray;\"><span style=\"color: gray;\">08/12/13</span>)</span></li>\r\n<li><a title=\"Key Local Considerations\" href=\"http://techreadiness.org/u/TRT_Key_Local_Considerations.pdf\" target=\"_blank\">Key Local Considerations</a> <span style=\"color: gray;\">(Added 06/14/13)</span></li>\r\n<li><a title=\"Guide to Finding Schools with Low Readiness\" href=\"http://techreadiness.org/u/Low%20Readiness%20District%20Spreadsheets%20Process%20Document.docx\" target=\"_blank\">Guide to finding schools with low readiness</a> <span style=\"color: gray;\">(Added 11</span><span style=\"color: gray;\"><span style=\"color: gray;\">/20/13</span>)</span></li>\r\n<li><a title=\"Training Module\" href=\"http://www.TechReadiness.org/TrainingModule\" target=\"_blank\">Training Module</a> <span style=\"color: gray;\">(Updated </span><span style=\"color: gray;\"><span style=\"color: gray;\">08/12/13</span>)</span></li>\r\n<li><a title=\"FAQs / Answer Center\" href=\"https://support.pearsonaccess.com/kaidara-advisor/bookmark?token=8Rrax4/7REp/Nawu1LGQy7Zl6mal4NlEDpgqO6TstfOIxrcYMb7gMA==&amp;project=Domain&amp;state=dialog-undecided-value&amp;branch=prd&amp;quest=dialog\" target=\"_blank\">FAQs / Answer Center</a> <span style=\"color: gray;\">(Ongoing Updates)</span></li>\r\n<li><a title=\"SRC Contact Form\" href=\"http://techreadiness.pearson.com/\" target=\"_blank\">Contact your State Readiness Coordinator (SRC)</a></li>\r\n</ul>\r\n<p><span style=\"font-weight: bold; font-size: 15px; color: gray;\">LAYOUTS &amp; TEMPLATES</span></p>\r\n<ul style=\"list-style-type: disc; margin: 0 0 12px 25px;\">\r\n<li>Organization file <a title=\"Organization File Layout\" href=\"http://www.TechReadiness.org/OrgFileLayout\" target=\"_blank\">Layout</a> / <a title=\"Organization File Template\" href=\"http://www.TechReadiness.org/OrgFileTemplate\" target=\"_blank\">Template</a> - <span style=\"color: gray;\">Updated </span><span style=\"color: gray;\"><span style=\"color: gray;\">08/12/13</span></span></li>\r\n<li>User file <a title=\"User File Layout\" href=\"http://www.TechReadiness.org/UserFileLayout\" target=\"_blank\">Layout</a> / <a title=\"User File Template\" href=\"http://www.TechReadiness.org/UserFileTemplate\" target=\"_blank\">Template</a> - <span style=\"color: gray;\">Updated </span><span style=\"color: gray;\"><span style=\"color: gray;\">08/12/13</span></span></li>\r\n<li>Device file <a title=\"Device File Layout\" href=\"http://www.TechReadiness.org/DeviceFileLayout\" target=\"_blank\">Layout</a> / <a title=\"Device File Template\" href=\"http://www.TechReadiness.org/DeviceFileTemplate\" target=\"_blank\">Template</a> - <span style=\"color: gray;\">Updated </span><span style=\"color: gray;\"><span style=\"color: gray;\">08/12/13</span></span></li>\r\n<li>School Survey file <a title=\"School Survey File Layout\" href=\"http://www.TechReadiness.org/SchoolSurveyFileLayout\" target=\"_blank\">Layout</a> / <a title=\"School Survey File Template\" href=\"http://www.TechReadiness.org/SchoolSurveyFileTemplate\" target=\"_blank\">Template</a> - <span style=\"color: gray;\">Updated </span><span style=\"color: gray;\"><span style=\"color: gray;\">08/12/13</span></span></li>\r\n</ul>',null,null),(278,28,'ready.dashboard.whatis','<p style=\"margin: 0 0 12px;\">The move to online assessments gives rise to a distinctive set of challenges for school technology. This online tool gives schools a convenient way to capture and report on indicators of their technology readiness for the new Race to the Top Assessments.</p><p style=\"margin: 0 0 12px;\">The Smarter Balanced Assessment Consortium and the Partnership for Assessment of Readiness for College and Careers (PARCC), working with the State Educational Technology Directors Association (SETDA) and Pearson, have developed the Technology Readiness Tool to support states as they transition to next-generation assessments.</p><p style=\"margin: 0 0 12px;\">This open source tool will help state education agencies work with schools and districts to ensure readiness when the online assessments are launched by the two consortia, beginning in the 2014/15 school year.</p><p style=\"margin: 0 0 12px;\">The Technology Readiness Tool assesses current capacity and compares that to the technology that will be needed to administer the new online assessments in four areas:</p><ol style=\"list-style-type: decimal; margin: 0 0 12px 25px;\"><li>Computers &amp; other devices</li><li>Ratio of devices to test-takers</li><li>Network and infrastructure</li><li>Personnel (staffing &amp; training)</li></ol>',null,null);
/*!40000 ALTER TABLE `custom_text` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER custom_text_before_insert   BEFORE INSERT  ON custom_text  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.custom_text_id,'I', vChangeDate, vChangeUser, 'custom_text','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.custom_text_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.custom_text_id,'I', vChangeDate, vChangeUser, 'custom_text','custom_text_id',null, NEW.custom_text_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.custom_text_id,'I', vChangeDate, vChangeUser, 'custom_text','scope_id',null, NEW.scope_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.text ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.custom_text_id,'I', vChangeDate, vChangeUser, 'custom_text','text',null, NEW.text);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER custom_text_before_update   BEFORE UPDATE   ON custom_text  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.custom_text_id,'U', vChangeDate, vChangeUser, 'custom_text','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.custom_text_id <=> NEW.custom_text_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.custom_text_id,'U', vChangeDate, vChangeUser, 'custom_text','custom_text_id',OLD.custom_text_id, NEW.custom_text_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.custom_text_id,'U', vChangeDate, vChangeUser, 'custom_text','scope_id',OLD.scope_id, NEW.scope_id);
	END IF;
	IF(NOT OLD.text <=> NEW.text) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.custom_text_id,'U', vChangeDate, vChangeUser, 'custom_text','text',OLD.text, NEW.text);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER custom_text_before_delete   BEFORE DELETE   ON custom_text  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.custom_text_id,'D', vChangeDate, vChangeUser, 'custom_text','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.custom_text_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.custom_text_id,'D', vChangeDate, vChangeUser, 'custom_text','custom_text_id',OLD.custom_text_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.custom_text_id,'D', vChangeDate, vChangeUser, 'custom_text','scope_id',OLD.scope_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.text ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.custom_text_id,'D', vChangeDate, vChangeUser, 'custom_text','text',OLD.text, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device` (
  `device_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(10) unsigned NOT NULL,
  `name` varchar(200) NOT NULL COMMENT 'Name of the device',
  `location` varchar(200) DEFAULT NULL COMMENT 'Location (ex: lab1) of the device',
  `owner` varchar(200) DEFAULT NULL,
  `environment` varchar(200) DEFAULT NULL,
  `count` int(11) DEFAULT '1' COMMENT 'The number of devices with this identical configuration.',
  `operating_system` varchar(200) DEFAULT NULL COMMENT 'Operation system (possible values in option_list)',
  `processor` int(11) DEFAULT NULL COMMENT 'Type of processor (possible values in option_list)',
  `memory` int(11) DEFAULT NULL COMMENT 'Memory installed (possible values in option_list)',
  `browser` int(11) DEFAULT NULL COMMENT 'Primary browser used (possible values in option_list)',
  `screen_resolution` int(11) DEFAULT NULL COMMENT 'Size of the display (possible values in option_list)',
  `display_size` int(11) DEFAULT NULL,
  `storage` int(11) DEFAULT NULL COMMENT 'Storage (hard disk, etc) space (possible values in option_list)',
  `flash_version` int(11) DEFAULT NULL COMMENT 'Version of the flash player (possible values in option_list)',
  `java_version` int(11) DEFAULT NULL COMMENT 'Java version installed (possible values in option_list)',
  `device_type` varchar(50) DEFAULT NULL,
  `wireless` varchar(50) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`device_id`),
  KEY `device_org_fki` (`org_id`),
  KEY `device_changedate_i` (`change_date`),
  CONSTRAINT `device_org_fk` FOREIGN KEY (`org_id`) REFERENCES `org` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3770985 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER device_before_insert   BEFORE INSERT  ON device  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER device_before_update   BEFORE UPDATE   ON device  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.browser <=> NEW.browser   
	  AND OLD.count <=> NEW.count   
	  AND OLD.device_id <=> NEW.device_id   
	  AND OLD.device_type <=> NEW.device_type   
	  AND OLD.display_size <=> NEW.display_size   
	  AND OLD.environment <=> NEW.environment   
	  AND OLD.flash_version <=> NEW.flash_version   
	  AND OLD.java_version <=> NEW.java_version   
	  AND OLD.location <=> NEW.location   
	  AND OLD.memory <=> NEW.memory   
	  AND OLD.name <=> NEW.name   
	  AND OLD.operating_system <=> NEW.operating_system   
	  AND OLD.org_id <=> NEW.org_id   
	  AND OLD.owner <=> NEW.owner   
	  AND OLD.processor <=> NEW.processor   
	  AND OLD.screen_resolution <=> NEW.screen_resolution   
	  AND OLD.storage <=> NEW.storage   
	  AND OLD.wireless <=> NEW.wireless   
    ) ) THEN
	  INSERT INTO device_hist (change_type,change_date,change_user,
	     browser
	     , count
	     , device_id
	     , device_type
	     , display_size
	     , environment
	     , flash_version
	     , java_version
	     , location
	     , memory
	     , name
	     , operating_system
	     , org_id
	     , owner
	     , processor
	     , screen_resolution
	     , storage
	     , wireless
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.browser
	     , OLD.count
	     , OLD.device_id
	     , OLD.device_type
	     , OLD.display_size
	     , OLD.environment
	     , OLD.flash_version
	     , OLD.java_version
	     , OLD.location
	     , OLD.memory
	     , OLD.name
	     , OLD.operating_system
	     , OLD.org_id
	     , OLD.owner
	     , OLD.processor
	     , OLD.screen_resolution
	     , OLD.storage
	     , OLD.wireless
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER device_before_delete   BEFORE DELETE   ON device  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO device_hist (change_type,change_date,change_user,
	     browser
	     , count
	     , device_id
	     , device_type
	     , display_size
	     , environment
	     , flash_version
	     , java_version
	     , location
	     , memory
	     , name
	     , operating_system
	     , org_id
	     , owner
	     , processor
	     , screen_resolution
	     , storage
	     , wireless
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.browser
	    , OLD.count
	    , OLD.device_id
	    , OLD.device_type
	    , OLD.display_size
	    , OLD.environment
	    , OLD.flash_version
	    , OLD.java_version
	    , OLD.location
	    , OLD.memory
	    , OLD.name
	    , OLD.operating_system
	    , OLD.org_id
	    , OLD.owner
	    , OLD.processor
	    , OLD.screen_resolution
	    , OLD.storage
	    , OLD.wireless
	);

    INSERT INTO device_hist (change_type,change_date,change_user,
	     browser
	     , count
	     , device_id
	     , device_type
	     , display_size
	     , environment
	     , flash_version
	     , java_version
	     , location
	     , memory
	     , name
	     , operating_system
	     , org_id
	     , owner
	     , processor
	     , screen_resolution
	     , storage
	     , wireless
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.browser
	    , OLD.count
	    , OLD.device_id
	    , OLD.device_type
	    , OLD.display_size
	    , OLD.environment
	    , OLD.flash_version
	    , OLD.java_version
	    , OLD.location
	    , OLD.memory
	    , OLD.name
	    , OLD.operating_system
	    , OLD.org_id
	    , OLD.owner
	    , OLD.processor
	    , OLD.screen_resolution
	    , OLD.storage
	    , OLD.wireless
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `device_ext`
--

DROP TABLE IF EXISTS `device_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_ext` (
  `device_ext_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `device_id` int(10) unsigned NOT NULL,
  `entity_field_id` int(10) unsigned NOT NULL,
  `value` varchar(500) NOT NULL COMMENT 'Value for the "key" referenced by the entity_field_id.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`device_ext_id`),
  UNIQUE KEY `deviceext_deviceid_entityfieldid_uc` (`device_id`,`entity_field_id`),
  KEY `deviceext_value_entityfieldid_i` (`value`(10),`entity_field_id`),
  KEY `deviceext_entityfield_fki` (`entity_field_id`),
  KEY `deviceext_device_fki` (`device_id`),
  CONSTRAINT `deviceext_device_fk` FOREIGN KEY (`device_id`) REFERENCES `device` (`device_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `deviceext_entityfield_fk` FOREIGN KEY (`entity_field_id`) REFERENCES `entity_field` (`entity_field_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=585;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_ext`
--

LOCK TABLES `device_ext` WRITE;
/*!40000 ALTER TABLE `device_ext` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_ext` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER device_ext_before_insert   BEFORE INSERT  ON device_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER device_ext_before_update   BEFORE UPDATE   ON device_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.device_ext_id <=> NEW.device_ext_id   
	  AND OLD.device_id <=> NEW.device_id   
	  AND OLD.entity_field_id <=> NEW.entity_field_id   
	  AND OLD.value <=> NEW.value   
    ) ) THEN
	  INSERT INTO device_ext_hist (change_type,change_date,change_user,
	     device_ext_id
	     , device_id
	     , entity_field_id
	     , value
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.device_ext_id
	     , OLD.device_id
	     , OLD.entity_field_id
	     , OLD.value
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER device_ext_before_delete   BEFORE DELETE   ON device_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO device_ext_hist (change_type,change_date,change_user,
	     device_ext_id
	     , device_id
	     , entity_field_id
	     , value
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.device_ext_id
	    , OLD.device_id
	    , OLD.entity_field_id
	    , OLD.value
	);

    INSERT INTO device_ext_hist (change_type,change_date,change_user,
	     device_ext_id
	     , device_id
	     , entity_field_id
	     , value
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.device_ext_id
	    , OLD.device_id
	    , OLD.entity_field_id
	    , OLD.value
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `device_ext_hist`
--

DROP TABLE IF EXISTS `device_ext_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_ext_hist` (
  `device_ext_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `device_ext_id` int(10) unsigned DEFAULT NULL,
  `device_id` int(10) unsigned DEFAULT NULL,
  `entity_field_id` int(10) unsigned DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`device_ext_hist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=2340;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_ext_hist`
--

LOCK TABLES `device_ext_hist` WRITE;
/*!40000 ALTER TABLE `device_ext_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_ext_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_hist`
--

DROP TABLE IF EXISTS `device_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_hist` (
  `device_hist_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `device_id` int(10) unsigned DEFAULT NULL,
  `org_id` int(10) unsigned DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `owner` varchar(200) DEFAULT NULL,
  `environment` varchar(200) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `operating_system` varchar(200) DEFAULT NULL,
  `processor` int(11) DEFAULT NULL,
  `memory` int(11) DEFAULT NULL,
  `browser` int(11) DEFAULT NULL,
  `screen_resolution` int(11) DEFAULT NULL,
  `display_size` int(11) DEFAULT NULL,
  `storage` int(11) DEFAULT NULL,
  `flash_version` int(11) DEFAULT NULL,
  `java_version` int(11) DEFAULT NULL,
  `device_type` varchar(50) DEFAULT NULL,
  `wireless` varchar(50) DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`device_hist_id`),
  KEY `devicehist_changedate_changetype_i` (`change_date`,`change_type`)
) ENGINE=InnoDB AUTO_INCREMENT=449 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_hist`
--

LOCK TABLES `device_hist` WRITE;
/*!40000 ALTER TABLE `device_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entity`
--

DROP TABLE IF EXISTS `entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity` (
  `entity_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `entity_type_id` int(10) unsigned NOT NULL,
  `scope_id` int(10) unsigned NOT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`entity_id`),
  UNIQUE KEY `entity_entitytypeid_scopeid_uc` (`entity_type_id`,`scope_id`),
  KEY `entity_scope_fki` (`scope_id`),
  KEY `entity_entitytype_fki` (`entity_type_id`),
  CONSTRAINT `entity_entitytype_fk` FOREIGN KEY (`entity_type_id`) REFERENCES `entity_type` (`entity_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `entity_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8 COMMENT='Used to define the scope specific attributes for a specific ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity`
--

LOCK TABLES `entity` WRITE;
/*!40000 ALTER TABLE `entity` DISABLE KEYS */;
INSERT INTO `entity` VALUES (3,4,1,null,null),(14,8,1,null,null),(16,9,1,null,null),(17,6,1,null,null),(18,10,1,null,null),(92,4,28,null,null),(109,13,28,null,null),(110,6,28,null,null),(116,15,28,null,null),(118,10,28,null,null);
/*!40000 ALTER TABLE `entity` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_before_insert   BEFORE INSERT  ON entity  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.entity_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_id,'I', vChangeDate, vChangeUser, 'entity','entity_id',null, NEW.entity_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_id,'I', vChangeDate, vChangeUser, 'entity','entity_type_id',null, NEW.entity_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_id,'I', vChangeDate, vChangeUser, 'entity','scope_id',null, NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_before_update   BEFORE UPDATE   ON entity  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.entity_id <=> NEW.entity_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_id,'U', vChangeDate, vChangeUser, 'entity','entity_id',OLD.entity_id, NEW.entity_id);
	END IF;
	IF(NOT OLD.entity_type_id <=> NEW.entity_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_id,'U', vChangeDate, vChangeUser, 'entity','entity_type_id',OLD.entity_type_id, NEW.entity_type_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_id,'U', vChangeDate, vChangeUser, 'entity','scope_id',OLD.scope_id, NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_before_delete   BEFORE DELETE   ON entity  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.entity_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_id,'D', vChangeDate, vChangeUser, 'entity','entity_id',OLD.entity_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_id,'D', vChangeDate, vChangeUser, 'entity','entity_type_id',OLD.entity_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_id,'D', vChangeDate, vChangeUser, 'entity','scope_id',OLD.scope_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `entity_data_type`
--

DROP TABLE IF EXISTS `entity_data_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_data_type` (
  `entity_data_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(200) NOT NULL COMMENT 'Unique name which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`entity_data_type_id`),
  UNIQUE KEY `entitydatatype_code_uc` (`code`),
  UNIQUE KEY `entitydatatype_name_uc` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_data_type`
--

LOCK TABLES `entity_data_type` WRITE;
/*!40000 ALTER TABLE `entity_data_type` DISABLE KEYS */;
INSERT INTO `entity_data_type` VALUES (1,'STRING','String',null,null),(2,'NUMBER','Number',null,null),(3,'BOOLEAN','Boolean',null,null),(4,'DATE','Date',null,null),(5,'DATETIME','DateTime',null,null);
/*!40000 ALTER TABLE `entity_data_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_data_type_before_insert   BEFORE INSERT  ON entity_data_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_data_type_id,'I', vChangeDate, vChangeUser, 'entity_data_type','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_data_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_data_type_id,'I', vChangeDate, vChangeUser, 'entity_data_type','entity_data_type_id',null, NEW.entity_data_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_data_type_id,'I', vChangeDate, vChangeUser, 'entity_data_type','name',null, NEW.name);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_data_type_before_update   BEFORE UPDATE   ON entity_data_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_data_type_id,'U', vChangeDate, vChangeUser, 'entity_data_type','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.entity_data_type_id <=> NEW.entity_data_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_data_type_id,'U', vChangeDate, vChangeUser, 'entity_data_type','entity_data_type_id',OLD.entity_data_type_id, NEW.entity_data_type_id);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_data_type_id,'U', vChangeDate, vChangeUser, 'entity_data_type','name',OLD.name, NEW.name);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_data_type_before_delete   BEFORE DELETE   ON entity_data_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_data_type_id,'D', vChangeDate, vChangeUser, 'entity_data_type','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_data_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_data_type_id,'D', vChangeDate, vChangeUser, 'entity_data_type','entity_data_type_id',OLD.entity_data_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_data_type_id,'D', vChangeDate, vChangeUser, 'entity_data_type','name',OLD.name, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `entity_field`
--

DROP TABLE IF EXISTS `entity_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_field` (
  `entity_field_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `entity_id` int(10) unsigned NOT NULL,
  `entity_data_type_id` int(10) unsigned NOT NULL,
  `option_list_id` int(10) unsigned DEFAULT NULL,
  `code` varchar(100) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.  ',
  `name` varchar(200) NOT NULL COMMENT 'Unique name which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `description` varchar(1000) DEFAULT NULL COMMENT 'A longer description of the field which is displayed to the user as context sensitive help.',
  `environment_specific` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'ONLY USED FOR THE SCOPE ENTITY TYPE - indicates that this field is environment specific and should not be exported/imported from one environment to another.',
  `disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Similar to a logical delete: used to "remove" a field without physically deleting it.  This is needed because the extension key/value pair rows have a FK to this table.  A disabled field will not be displayed on any views and will not be validated.',
  `required` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'The field must be provided by the user of the web site and included in any batch loads.',
  `regex` varchar(2000) DEFAULT NULL COMMENT 'Regular expression that is used to validate the fields data.  For example "^[a-z]*$" would be used to enforce that only lower case alpha characters are provided.',
  `regex_display` varchar(2000) DEFAULT NULL COMMENT 'A text description of the regular expression which can be displayed to the user.  Example: Alpha-numeric, numeric, etc.',
  `min_length` int(11) DEFAULT NULL COMMENT 'Minimum number of characters allowed for this field.',
  `max_length` int(11) DEFAULT NULL COMMENT 'Maximum number of characters allowed for this field.  The extension records allow for 30,000 characters.',
  `display_order` int(11) DEFAULT NULL COMMENT 'Can be used to control the order the fields are displayed when no view is defined.  This is not normally used as a view is normally defined to control the fields displayed.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`entity_field_id`),
  UNIQUE KEY `entityfield_name_entityid_uc` (`name`,`entity_id`),
  UNIQUE KEY `entityfield_code_entityid_uc` (`code`,`entity_id`),
  KEY `entityfield_entity_fki` (`entity_id`),
  KEY `entityfield_optionlist_fki` (`option_list_id`),
  KEY `entityfield_entitydatatype_fki` (`entity_data_type_id`),
  CONSTRAINT `entityfield_entitydatatype_fk` FOREIGN KEY (`entity_data_type_id`) REFERENCES `entity_data_type` (`entity_data_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `entityfield_entity_fk` FOREIGN KEY (`entity_id`) REFERENCES `entity` (`entity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `entityfield_optionlist_fk` FOREIGN KEY (`option_list_id`) REFERENCES `option_list` (`option_list_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2696 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_field`
--

LOCK TABLES `entity_field` WRITE;
/*!40000 ALTER TABLE `entity_field` DISABLE KEYS */;
INSERT INTO `entity_field` VALUES (11,3,1,NULL,'code','Code','',0,0,1,NULL,NULL,NULL,50,1,NULL,NULL),(12,3,1,NULL,'name','Name','',0,0,1,NULL,NULL,NULL,100,2,NULL,NULL),(13,3,1,NULL,'localCode','Local Code','',0,0,0,NULL,NULL,NULL,50,3,NULL,NULL),(14,3,1,NULL,'addressLine1','Address Line 1','',0,0,0,NULL,NULL,NULL,100,4,NULL,NULL),(15,3,1,NULL,'addressLine2','Address Line 2','',0,0,0,NULL,NULL,NULL,100,5,NULL,NULL),(16,3,1,NULL,'city','City','',0,0,0,NULL,NULL,NULL,50,6,NULL,NULL),(17,3,1,7,'state','State','',0,0,0,NULL,NULL,NULL,2,7,NULL,NULL),(18,3,1,NULL,'zip','Zip Code','',0,0,0,'^[0-9]{5}(-[0-9]{4}){0,1}$','Predefined Regular Expression for: Zip+4 (#####-####) type',NULL,10,8,NULL,NULL),(19,3,1,NULL,'phone','Phone #','',0,0,0,'^[0-9]{3}-[0-9]{3}-[0-9]{4}$','Predefined Regular Expression for: Phone number (###-###-####) type',NULL,20,9,NULL,NULL),(20,3,2,NULL,'phoneExtension','Phone Extension','',0,0,0,NULL,NULL,NULL,10,10,NULL,NULL),(21,3,1,NULL,'fax','Fax #','',0,0,0,'^[0-9]{3}-[0-9]{3}-[0-9]{4}$','Predefined Regular Expression for: Phone number (###-###-####) type',NULL,20,11,NULL,NULL),(22,3,3,NULL,'inactive','Inactive','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(159,14,1,NULL,'username','Username','',0,0,1,NULL,NULL,1,100,NULL,NULL,NULL),(160,14,1,NULL,'firstName','First Name','',0,0,1,NULL,NULL,0,50,NULL,NULL,NULL),(161,14,1,NULL,'lastName','Last Name','',0,0,1,NULL,NULL,0,50,NULL,NULL,NULL),(162,14,1,NULL,'email','Email','',0,0,1,'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$','must be in the email address format.',1,100,NULL,NULL,NULL),(176,16,1,NULL,'category','Category','',0,0,0,NULL,NULL,NULL,100,NULL,NULL,NULL),(177,16,1,NULL,'name','Name','',0,0,1,NULL,NULL,NULL,100,NULL,NULL,NULL),(178,16,1,NULL,'shortName','Short Name','',0,0,0,NULL,NULL,NULL,50,NULL,NULL,NULL),(179,16,1,NULL,'code','Code','',0,0,1,NULL,NULL,NULL,50,NULL,NULL,NULL),(180,16,1,NULL,'description','Description','',0,0,0,NULL,NULL,NULL,1000,NULL,NULL,NULL),(182,17,1,NULL,'code','Code','',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(183,17,1,NULL,'name','Name','',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(184,17,1,NULL,'description','Description','',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(188,18,1,NULL,'name','Name','',0,0,1,'^[a-zA-Z0-9_]*$','must be in the alpha/numeric format.',NULL,100,1,NULL,NULL),(190,18,1,NULL,'email','Email','',0,0,1,'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$','must be in the email address format.',NULL,100,3,NULL,NULL),(191,18,1,NULL,'city','City','',0,0,0,'^[a-zA-Z0-9_]*$','must be in the alpha/numeric format.',NULL,50,12,NULL,NULL),(192,18,1,7,'state','State','',0,0,0,NULL,NULL,NULL,20,12,NULL,NULL),(193,17,1,507,'passwordComplexity','Password Complexity','PasswordComplexityValidator determines how complex a password is based on the following requirements: Standard Password rules: \r\n\r\nComplexity 0 = none of the rules below need to be met to be considered a valid password \r\nComplexity 1 = one of the rules below need to be met to be considered a valid password \r\nComplexity 2 = two of the rules below need to be met to be considered a valid password \r\nComplexity 3 = three of the rules below need to be met to be considered a valid password \r\nUppercase: 1 Lowercase: 1 Number: 1 Special Chars: 1 Min Length: 8 Max Length: 32 Special Chars: ~`!@#$%^&*()_-+=}{[]|\\:;<,>.?/',0,0,0,NULL,NULL,1,1,NULL,NULL,NULL),(1273,92,2,NULL,'studentCount','Estimated Test-Taker Count for 2014-2015','',0,0,0,NULL,NULL,NULL,4,NULL,NULL,NULL),(1275,92,1,NULL,'code','Code','Must be entered in the correct State – Local Code format (two-character state abbreviation and district/school code supplied by the state exactly as entered in the Local Code field below, separated by a hyphen).',0,0,1,NULL,NULL,NULL,50,NULL,NULL,NULL),(1276,92,1,NULL,'name','Name','',0,0,1,NULL,NULL,NULL,100,NULL,NULL,NULL),(2016,14,4,NULL,'activeBeginDate','Active Begin Date',' ',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2018,18,1,NULL,'title','Title','',0,0,0,'^[a-zA-Z0-9_]*$','must be in the alpha/numeric format.',NULL,50,2,NULL,NULL),(2019,18,1,NULL,'addressLine1','Address 1','',0,0,0,'^[a-zA-Z0-9_]*$','must be in the alpha/numeric format.',NULL,100,10,NULL,NULL),(2020,18,1,NULL,'addressLine2','Address 2','',0,0,0,'^[a-zA-Z0-9_]*$','must be in the alpha/numeric format.',NULL,100,11,NULL,NULL),(2021,18,1,NULL,'country','Country','',0,0,0,NULL,NULL,NULL,2,13,NULL,NULL),(2022,18,1,NULL,'zip','Zip','',0,0,0,'^[0-9]{5}(-[0-9]{4}){0,1}$','must be in the zip+4 (xxxxx-xxxx) format.',NULL,10,14,NULL,NULL),(2023,18,1,NULL,'phone','Phone','',0,0,0,'^[0-9]{3}-[0-9]{3}-[0-9]{4}$','must be in the phone number format (###-###-####).',NULL,20,20,NULL,NULL),(2024,18,2,NULL,'phoneExtension','Phone Extension','',0,0,0,NULL,NULL,NULL,10,21,NULL,NULL),(2025,18,1,NULL,'fax','Fax','',0,0,0,'^[0-9]{3}-[0-9]{3}-[0-9]{4}$','must be in the phone number format (###-###-####).',NULL,20,22,NULL,NULL),(2041,14,4,NULL,'activeEndDate','Active End Date',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2067,109,1,NULL,'name','Name','',0,0,1,NULL,NULL,NULL,100,0,NULL,NULL),(2068,109,2,550,'memory','Memory','',0,0,0,NULL,NULL,NULL,NULL,2,NULL,NULL),(2069,109,1,NULL,'location','Location','',0,0,0,NULL,NULL,NULL,NULL,1,NULL,NULL),(2073,109,1,584,'browser','Browser','',0,0,0,NULL,NULL,NULL,NULL,6,NULL,NULL),(2237,17,3,NULL,'hideBranding','Hide Branding','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2238,109,2,NULL,'count','Device Count','',0,0,1,'^([1-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9])$','must be a number 1 thru 9999.',NULL,4,NULL,NULL,NULL),(2242,109,1,548,'operatingSystem','Operating System','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2243,109,2,549,'processor','Processor','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2244,109,2,547,'screenResolution','Screen Resolution','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2253,110,1,603,'minimumScreenResolution','Minimum Screen Resolution','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2257,110,1,607,'minimumBrowser','Minimum Browser','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2258,109,1,594,'monitorDisplaySize','Monitor / Display Size','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2259,109,1,586,'wireless','Wireless','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2260,109,1,587,'deviceType','Device Type','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2261,92,2,NULL,'simultaneousTesters','Maximum Number of Simultaneous Test-Takers','',0,0,0,NULL,NULL,NULL,4,NULL,NULL,NULL),(2262,92,2,NULL,'wirelessAccessPoints','Number of Wireless Access Points','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2263,92,2,NULL,'networkUtilization','Estimated Internal Network Utilization','',0,0,0,'^0{0,2}[0-9]|0{0,1}[0-9]{2}|100$','must be between 0 - 100.\r\n',NULL,3,NULL,NULL,NULL),(2264,17,3,NULL,'orgPartDescendantCascadeDelete','Org Participation Cascade Deletes','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2265,110,1,590,'minimumSizeOfTest','Size of Test','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2266,110,1,591,'minimumMonitorDisplaySize','Minimum Monitor / Display Size','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2276,17,3,NULL,'orgPartDescendantCascadeAdd','Org Participation Cascade Adds','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2277,92,1,599,'surveyAdminCount','Number of Administrators','Test Administrators:  Test proctors or other staff assigned to set up and monitor test-taking sessions in the classroom, computer lab, library, or other designated test area.',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2278,92,1,599,'surveyTechstaffCount','Number of Technology Staff','Technology Support Staff: a technical coordinator or person responsible for ensuring the computer-based testing equipment (hardware, software and networking) is installed and functioning properly to support computerized assessment in each school. ',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2279,92,1,599,'surveyAdminUnderstanding','Administrator Technical Understanding','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2280,92,1,599,'surveyTechstaffUnderstanding','Technical Staff Technical Understanding','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2281,92,1,599,'surveyAdminTraining','Training of  Administrators','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2282,92,1,599,'surveyTechstaffTraining','Training of Technology Staff','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2283,92,2,NULL,'testingWindowLength','Length of Testing Window (School Days)','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2284,92,2,NULL,'sessionsPerDay','Number of Sessions per Day','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2285,92,1,598,'internetSpeed','Estimated Internet Bandwidth','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2286,92,1,546,'networkSpeed','Estimated Internal Network Bandwidth','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2414,110,1,606,'minMemory_windows_xp','Windows Xp minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2415,110,1,606,'minMemory_windows_vista','Windows Vista minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2416,110,1,606,'minMemory_windows_7','Windows 7 minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2417,110,1,606,'minMemory_windows_8','Windows 8 minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2418,110,1,606,'minMemory_windows_other','Windows Other minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2419,110,1,606,'minMemory_linux_mint','Linux Mint minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2421,110,1,606,'minMemory_linux_suse','Linux Suse minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2422,110,1,606,'minMemory_linux_other','Linux Other minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2425,110,1,606,'minMemory_mac_other','Mac other minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2426,110,1,606,'minMemory_ios_4_x','iOS 4.x minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2427,110,1,606,'minMemory_ios_5_x','iOS 5.x minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2428,110,1,606,'minMemory_ios_other','iOS other minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2429,110,1,606,'minMemory_android_3_x','Android 3.x minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2430,110,1,606,'minMemory_android_4_x','Android 4.x minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2431,110,1,606,'minMemory_android_other','Android other minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2432,110,1,606,'minMemory_other','Other minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2433,92,1,596,'dataEntryComplete','Data Entry Submitted and Eligible for Review','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2446,116,1,NULL,'name','Name','',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2447,116,1,NULL,'requestUser','Request User','',0,1,0,NULL,NULL,NULL,NULL,2,NULL,NULL),(2448,116,5,NULL,'requestDate','Request Date','',0,1,0,NULL,NULL,NULL,NULL,3,NULL,NULL),(2449,116,5,NULL,'executeDate','Execute Date','',0,1,0,NULL,NULL,NULL,NULL,4,NULL,NULL),(2453,92,2,NULL,'internetUtilization','Estimated Internet Bandwidth Utilization','',0,0,0,'^0{0,2}[0-9]|0{0,1}[0-9]{2}|100$','must be between 0 - 100.',NULL,3,NULL,NULL,NULL),(2454,92,1,NULL,'addressLine1','Address Line 1','',0,0,1,NULL,NULL,NULL,100,NULL,NULL,NULL),(2455,92,1,NULL,'addressLine2','Address Line 2','',0,0,0,NULL,NULL,NULL,100,NULL,NULL,NULL),(2456,92,1,NULL,'city','City','',0,0,1,NULL,NULL,NULL,50,NULL,'2014-04-01 11:11:57','aaron_core@localhost'),(2457,92,1,7,'state','State','',0,0,1,NULL,NULL,NULL,2,NULL,NULL,NULL),(2458,92,1,NULL,'zip','Zip Code','',0,0,1,'^[0-9]{5}(-[0-9]{4}){0,1}$','must be in correct zip+4 format (#####-####).',NULL,10,NULL,NULL,NULL),(2459,92,1,NULL,'phone','Phone #','',0,0,1,'^[0-9]{3}-[0-9]{3}-[0-9]{4}$','must be in the phone number format (###-###-####).',NULL,20,NULL,NULL,NULL),(2460,92,2,NULL,'phoneExtension','Phone Extension','',0,0,0,NULL,NULL,NULL,10,NULL,NULL,NULL),(2461,92,1,NULL,'fax','Fax #','',0,0,0,'^[0-9]{3}-[0-9]{3}-[0-9]{4}$','must be in the phone number format (###-###-####).',NULL,20,NULL,NULL,NULL),(2462,92,1,NULL,'localCode','Local Code','District/school code supplied by the state.',0,0,1,NULL,NULL,NULL,50,NULL,NULL,NULL),(2464,110,1,606,'recommendedMemory_windows_xp','Windows Xp recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2465,110,1,606,'recommendedMemory_windows_vista','Windows Vista recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2466,110,1,606,'recommendedMemory_windows_7','Windows 7 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2467,110,1,606,'recommendedMemory_windows_8','Windows 8 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2468,110,1,606,'recommendedMemory_windows_other','Windows Other recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2469,110,1,606,'recommendedMemory_linux_mint','Linux Mintrecommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2471,110,1,606,'recommendedMemory_linux_suse','Linux Suse recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2472,110,1,606,'recommendedMemory_linux_other','Linux Other recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2475,110,1,606,'recommendedMemory_mac_other','Mac other recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2476,110,1,606,'recommendedMemory_ios_4_x','iOS 4.x recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2477,110,1,606,'recommendedMemory_ios_5_x','iOS 5.x recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2478,110,1,606,'recommendedMemory_ios_other','iOS other recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2479,110,1,606,'recommendedMemory_android_3_x','Android 3.x recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2480,110,1,606,'recommendedMemory_android_4_x','Android 4.x recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2481,110,1,606,'recommendedMemory_android_other','Android other recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2482,110,1,606,'recommendedMemory_other','Other recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2503,110,1,603,'recommendedScreenResolution','Recommended Screen Resolution','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2507,110,1,591,'recommendedMonitorDisplaySize','Recommended Monitor / Display Size','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2518,14,4,NULL,'disableDate','Disable Date',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2519,14,1,NULL,'disableReason','Disable Reason',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2520,118,1,NULL,'name','Name','',0,0,1,NULL,NULL,NULL,100,1,NULL,NULL),(2521,118,1,NULL,'email','Email','',0,0,1,'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$','must be in the email address format.',NULL,100,3,NULL,NULL),(2522,118,1,NULL,'city','City','',0,0,0,NULL,NULL,NULL,50,12,NULL,NULL),(2523,118,1,7,'state','State','',0,0,0,NULL,NULL,NULL,20,12,NULL,NULL),(2524,118,1,NULL,'title','Title','',0,0,0,NULL,NULL,NULL,50,2,NULL,NULL),(2525,118,1,NULL,'addressLine1','Address 1','',0,0,0,NULL,NULL,NULL,100,10,NULL,NULL),(2526,118,1,NULL,'addressLine2','Address 2','',0,0,0,NULL,NULL,NULL,100,11,NULL,NULL),(2527,118,1,NULL,'country','Country','',0,0,0,NULL,NULL,NULL,2,13,NULL,NULL),(2528,118,1,NULL,'zip','Zip','',0,0,0,'^[0-9]{5}(-[0-9]{4}){0,1}$','must be in correct zip+4 format (#####-####).',NULL,10,14,NULL,NULL),(2529,118,1,NULL,'phone','Phone','',0,0,0,'^[0-9]{3}-[0-9]{3}-[0-9]{4}$','must be in the phone number format (###-###-####).',NULL,20,20,NULL,NULL),(2530,118,2,NULL,'phoneExtension','Phone Extension','',0,0,0,NULL,NULL,NULL,10,21,NULL,NULL),(2531,118,1,NULL,'fax','Fax','',0,0,0,'^[0-9]{3}-[0-9]{3}-[0-9]{4}$','must be in the phone number format (###-###-####).',NULL,20,22,NULL,NULL),(2532,92,3,NULL,'inactive','Inactive','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2533,110,1,606,'minMemory_google_chrome','Google Chrome minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2534,110,1,606,'recommendedMemory_google_chrome','Google Chrome recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2567,14,4,NULL,'deleteDate','Delete Date',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2568,110,1,NULL,'code','Code','',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2569,110,1,NULL,'name','Name','',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2570,110,1,NULL,'description','Description','',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2571,116,3,612,'visible','Progress Report','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2576,110,1,606,'minMemory_windows_xp2','minMemory_windows_xp2',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2577,110,1,606,'recommendedMemory_windows_xp2','recommendedMemory_windows_xp2',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2579,110,1,606,'minMemory_windows_rt','minMemory_windows_rt',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2580,110,1,606,'recommendedMemory_windows_rt','recommendedMemory_windows_rt',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2582,110,1,606,'minMemory_mac_108','minMemory_mac_108',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2583,110,1,606,'recommendedMemory_mac_108','recommendedMemory_mac_108',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2585,110,1,606,'minMemory_ios_6_x','minMemory_ios_6_x',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2586,110,1,606,'recommendedMemory_ios_6_x','recommendedMemory_ios_6_x',NULL,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2587,109,1,613,'environment','Assessment Environment','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2588,109,1,614,'owner','Device Owner','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2589,92,1,616,'schoolType','School Type','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2590,110,1,617,'includeGradeK','Include Grade K','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2591,110,1,617,'includeGrade1','Include Grade 1','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2592,110,1,617,'includeGrade2','Include Grade 2','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2593,110,1,617,'includeGrade3','Include Grade 3','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2594,110,1,617,'includeGrade4','Include Grade 4','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2595,110,1,617,'includeGrade5','Include Grade 5','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2596,110,1,617,'includeGrade6','Include Grade 6','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2597,110,1,617,'includeGrade7','Include Grade 7','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2598,110,1,617,'includeGrade8','Include Grade 8','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2599,110,1,617,'includeGrade9','Include Grade 9','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2600,110,1,617,'includeGrade10','Include Grade 10','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2601,110,1,617,'includeGrade11','Include Grade 11','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2602,110,1,617,'includeGrade12','Include Grade 12','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2603,92,2,NULL,'enrollmentCountK','Grade K Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2604,92,2,NULL,'enrollmentCount1','Grade 1 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2605,92,2,NULL,'enrollmentCount2','Grade 2 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2606,92,2,NULL,'enrollmentCount3','Grade 3 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2607,92,2,NULL,'enrollmentCount4','Grade 4 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2608,92,2,NULL,'enrollmentCount5','Grade 5 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2609,92,2,NULL,'enrollmentCount6','Grade 6 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2610,92,2,NULL,'enrollmentCount7','Grade 7 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2611,92,2,NULL,'enrollmentCount8','Grade 8 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2612,92,2,NULL,'enrollmentCount9','Grade 9 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2613,92,2,NULL,'enrollmentCount10','Grade 10 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2614,92,2,NULL,'enrollmentCount11','Grade 11 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2615,92,2,NULL,'enrollmentCount12','Grade 12 Enrollment Count','',0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL),(2616,110,1,NULL,'minimumTestingWindowLength','Minimum Testing Window Length (days)','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2617,110,1,NULL,'recommendedTestingWindowLength','Recommended Testing Window Length (days)','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2620,110,1,NULL,'minimumThroughputRequiredPerStudent','Minimum Throughput required per Student (Kbps)','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2621,110,1,NULL,'recommendedThroughputRequiredPerStudent','Recommended Throughput Required per Student (Kbps)','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2622,110,1,606,'recommendedMemory_linux_fedora_1_5','linux_fedora_1_5 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2623,110,1,606,'recommendedMemory_linux_ubuntu_9_11_04','linux_ubuntu_9_11.04 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2624,110,1,606,'recommendedMemory_mac_101_1043','mac_101_043 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2625,110,1,606,'recommendedMemory_mac_106','mac_106 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2626,110,1,606,'recommendedMemory_google_chrome19','google_chrome19 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2627,110,1,606,'recommendedMemory_windows_server_2003','windows_server_2003 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2628,110,1,606,'recommendedMemory_windows_server_2008','windows_server_2008 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2629,110,1,606,'recommendedMemory_windows_server_2012','windows_server_2012 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2630,110,1,606,'recommendedMemory_linux_ubuntu_4_8','linux_ubuntu_4_8 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2631,110,1,606,'recommendedMemory_linux_unbuntu_11_10','linux_unbuntu_11.10 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2632,110,1,606,'recommendedMemory_linux_fedora_6_15','linux_fedora_6_15 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2633,110,1,606,'recommendedMemory_linux_fedora_16','linux_fedora_16 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2634,110,1,606,'recommendedMemory_mac_1044','mac_1044 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2635,110,1,606,'recommendedMemory_mac_105','mac_105 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2636,110,1,606,'recommendedMemory_mac_107','mac_107 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2637,110,1,606,'minMemory_linux_fedora_1_5','linux_fedora_1_5 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2638,110,1,606,'minMemory_linux_ubuntu_9_11_04','linux_ubuntu_9_11.04 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2639,110,1,606,'minMemory_mac_101_1043','mac_101_1043 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2640,110,1,606,'minMemory_mac_106','mac_106 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2641,110,1,606,'minMemory_google_chrome19','google_chrome19 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2642,110,1,606,'minMemory_windows_server_2003','windows_server_2003 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2643,110,1,606,'minMemory_windows_server_2008','windows_server_2008 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2644,110,1,606,'minMemory_windows_server_2012','windows_server_2012 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2645,110,1,606,'minMemory_linux_ubuntu_4_8','linux_ubuntu_4_8 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2646,110,1,606,'minMemory_linux_unbuntu_11_10','linux_unbuntu_11.10 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2647,110,1,606,'minMemory_linux_fedora_6_15','linux_fedora_6_15 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2648,110,1,606,'minMemory_linux_fedora_16','linux_fedora_16 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2649,110,1,606,'minMemory_mac_1044','mac_1044 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2650,110,1,606,'minMemory_mac_105','mac_105 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2651,110,1,606,'minMemory_mac_107','mac_107 min memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2652,92,1,NULL,'dataEntryCompleteLastModificationDate','Data Entry Complete Last Modification Date','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2653,92,1,NULL,'dataEntryCompleteLastModificationUser','Data Entry Complete Last Modification User','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2672,92,1,NULL,'ncesOrgCode','NCES Org Code','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2673,92,2,NULL,'numberOfSimultaneousTesters','TEMP-KIRK Maximum Number of Simultaneous Test-Takers','',0,0,0,NULL,NULL,NULL,4,NULL,NULL,NULL),(2674,92,2,NULL,'numberOfWirelessAccessPoints','TEMP-KIRK Number of Wireless Access Points','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2675,92,2,NULL,'estimatedNetworkUtilization','vEstimated Internal Network Utilization','',0,0,0,'^0{0,2}[0-9]|0{0,1}[0-9]{2}|100$','must be between 0 - 100. ',NULL,3,NULL,NULL,NULL),(2676,92,1,599,'adequateAdministrators','TEMP-KIRK Number of Administrators','Test Administrators: Test proctors or other staff assigned to set up and monitor test-taking sessions in the classroom, computer lab, library, or other designated test area.',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2677,92,1,599,'numberTechnologyStaff','TEMP-KIRK Number of Technology Staff','Technology Support Staff: a technical coordinator or person responsible for ensuring the computer-based testing equipment (hardware, software and networking) is installed and functioning properly to support computerized assessment in each school. ',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2678,92,1,599,'administratorTechnicalUnderstanding','TEMP-KIRK Administrator Technical Understanding','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2679,92,1,599,'technicalStaffTechnicalUnderstanding','TEMP-KIRK Technical Staff Technical Understanding','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2680,92,1,599,'trainingAdministrators','TEMP-KIRK Training of Administrators','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2681,92,1,599,'trainingTechnologyStaff','TEMP-KIRK Training of Technology Staff','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2682,92,2,NULL,'lengthOfTestingWindow','TEMP-KIRK Length of Testing Window (School Days)','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2683,92,2,NULL,'numberOfSessionsPerDay','TEMP-KIRK Number of Sessions per Day','',0,0,0,NULL,NULL,NULL,3,NULL,NULL,NULL),(2684,92,1,598,'internetBandwidth','TEMP-KIRK Estimated Internet Bandwidth','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2685,92,1,546,'internalNetworkSpeed','TEMP-KIRK Estimated Internal Network Bandwidth','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2686,92,2,NULL,'estimatedInternetBandwidthUtilization','TEMP-KIRK Estimated Internet Bandwidth Utilization','',0,0,0,'^0{0,2}[0-9]|0{0,1}[0-9]{2}|100$','must be between 0 - 100.',NULL,3,NULL,NULL,NULL),(2687,110,1,606,'recommendedMemory_Windows_MultiPoint_server_2012',' Windows MultiPoint Server 2012 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2688,110,1,606,'minMemory_Windows_MultiPoint_server_2012','Windows MultiPoint Server 2012 minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2690,110,1,606,'minMemory_Windows_MultiPoint_server_2010_2011','Windows MultiPoint Server 2010-2011 minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2691,110,1,606,'recommendedMemory_Windows_MultiPoint_server_2010_2011',' Windows MultiPoint Server 2010-2011 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2692,110,1,606,'recommendedMemory_ios_7_x','iOS 7.x recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2693,110,1,606,'minMemory_ios_7_x','iOS 7.x minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2694,110,1,606,'minMemory_mac_109','Mac OS X 10.9 minimum memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2695,110,1,606,'recommendedMemory_mac_109','Mac OS X 10.9 recommended memory','',0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `entity_field` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_field_before_insert   BEFORE INSERT  ON entity_field  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','description',null, NEW.description);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.disabled ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','disabled',null, NEW.disabled);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','display_order',null, NEW.display_order);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_data_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','entity_data_type_id',null, NEW.entity_data_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','entity_field_id',null, NEW.entity_field_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','entity_id',null, NEW.entity_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.environment_specific ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','environment_specific',null, NEW.environment_specific);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.max_length ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','max_length',null, NEW.max_length);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.min_length ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','min_length',null, NEW.min_length);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.option_list_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','option_list_id',null, NEW.option_list_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.regex ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','regex',null, NEW.regex);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.regex_display ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','regex_display',null, NEW.regex_display);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.required ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_field_id,'I', vChangeDate, vChangeUser, 'entity_field','required',null, NEW.required);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_field_before_update   BEFORE UPDATE   ON entity_field  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.description <=> NEW.description) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','description',OLD.description, NEW.description);
	END IF;
	IF(NOT OLD.disabled <=> NEW.disabled) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','disabled',OLD.disabled, NEW.disabled);
	END IF;
	IF(NOT OLD.display_order <=> NEW.display_order) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','display_order',OLD.display_order, NEW.display_order);
	END IF;
	IF(NOT OLD.entity_data_type_id <=> NEW.entity_data_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','entity_data_type_id',OLD.entity_data_type_id, NEW.entity_data_type_id);
	END IF;
	IF(NOT OLD.entity_field_id <=> NEW.entity_field_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','entity_field_id',OLD.entity_field_id, NEW.entity_field_id);
	END IF;
	IF(NOT OLD.entity_id <=> NEW.entity_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','entity_id',OLD.entity_id, NEW.entity_id);
	END IF;
	IF(NOT OLD.environment_specific <=> NEW.environment_specific) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','environment_specific',OLD.environment_specific, NEW.environment_specific);
	END IF;
	IF(NOT OLD.max_length <=> NEW.max_length) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','max_length',OLD.max_length, NEW.max_length);
	END IF;
	IF(NOT OLD.min_length <=> NEW.min_length) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','min_length',OLD.min_length, NEW.min_length);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.option_list_id <=> NEW.option_list_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','option_list_id',OLD.option_list_id, NEW.option_list_id);
	END IF;
	IF(NOT OLD.regex <=> NEW.regex) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','regex',OLD.regex, NEW.regex);
	END IF;
	IF(NOT OLD.regex_display <=> NEW.regex_display) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','regex_display',OLD.regex_display, NEW.regex_display);
	END IF;
	IF(NOT OLD.required <=> NEW.required) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'U', vChangeDate, vChangeUser, 'entity_field','required',OLD.required, NEW.required);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_field_before_delete   BEFORE DELETE   ON entity_field  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','description',OLD.description, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.disabled ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','disabled',OLD.disabled, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','display_order',OLD.display_order, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_data_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','entity_data_type_id',OLD.entity_data_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','entity_field_id',OLD.entity_field_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','entity_id',OLD.entity_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.environment_specific ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','environment_specific',OLD.environment_specific, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.max_length ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','max_length',OLD.max_length, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.min_length ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','min_length',OLD.min_length, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.option_list_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','option_list_id',OLD.option_list_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.regex ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','regex',OLD.regex, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.regex_display ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','regex_display',OLD.regex_display, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.required ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_field_id,'D', vChangeDate, vChangeUser, 'entity_field','required',OLD.required, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `entity_rule`
--

DROP TABLE IF EXISTS `entity_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_rule` (
  `entity_rule_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `entity_id` int(10) unsigned NOT NULL,
  `entity_field_id` int(10) unsigned DEFAULT NULL COMMENT 'Used to associate the error message to a particular field.  If this field is null then it is a global error.',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Unique name (within an entity) which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `rule` varchar(10000) NOT NULL COMMENT 'Unique name which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Deactivates the rule so that it is no longer enforced.  Allows the user to disable a rule without deleting it.',
  `type` enum('validation','view') DEFAULT NULL COMMENT 'Type of rule (see enum for possible values)',
  `error_message` varchar(1000) DEFAULT NULL COMMENT 'Primary error message for this rule.  This error will be displayed to the user on the web site, and optionally displayed on batch loads if the "batch_error_message" is not provided.',
  `batch_error_message` varchar(1000) DEFAULT NULL COMMENT 'Alternate batch specific error message for this rule.  If this field is not provided, the mandatory "error_message" field will be used instead.',
  `description` varchar(1000) DEFAULT NULL COMMENT 'Detailed description the business rule being enforced.  This text will be displayed to the user as context sensitive help.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`entity_rule_id`),
  UNIQUE KEY `entityrule_entityid_name_uc` (`entity_id`,`name`),
  KEY `entityrule_entity_fki` (`entity_id`),
  KEY `entityrule_entityfield_fki` (`entity_field_id`),
  CONSTRAINT `entityrule_entityfield_fk` FOREIGN KEY (`entity_field_id`) REFERENCES `entity_field` (`entity_field_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `entityrule_entity_fk` FOREIGN KEY (`entity_id`) REFERENCES `entity` (`entity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_rule`
--

LOCK TABLES `entity_rule` WRITE;
/*!40000 ALTER TABLE `entity_rule` DISABLE KEYS */;
INSERT INTO `entity_rule` VALUES (25,92,NULL,'orgCodeRefersToLocalOrgCode','import java.util.Map;\r\nimport java.util.List;\r\nimport net.techreadiness.service.common.ValidationError;\r\nglobal java.util.List errorList\r\n\r\nrule \"orgCodeRefersToLocalOrgCode\"\r\nwhen\r\nMap($map : this)\r\nMap(this[\"code\"] !=  this[\"state\"] + \"-\" + this[\"localCode\"])\r\nthen\r\n    errorList.add(new ValidationError(\"code\", \"orgCodeRefersToLocalOrgCode\", \"The Code must be: State + \'-\' + Local Code\",\"\",\"\"));\r\nend',0,'validation','xxx','','',null,null);
/*!40000 ALTER TABLE `entity_rule` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_rule_before_insert   BEFORE INSERT  ON entity_rule  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.batch_error_message ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','batch_error_message',null, NEW.batch_error_message);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','description',null, NEW.description);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.disabled ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','disabled',null, NEW.disabled);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','entity_field_id',null, NEW.entity_field_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','entity_id',null, NEW.entity_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_rule_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','entity_rule_id',null, NEW.entity_rule_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.error_message ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','error_message',null, NEW.error_message);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.rule ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','rule',null, NEW.rule);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.type ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_rule_id,'I', vChangeDate, vChangeUser, 'entity_rule','type',null, NEW.type);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_rule_before_update   BEFORE UPDATE   ON entity_rule  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.batch_error_message <=> NEW.batch_error_message) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','batch_error_message',OLD.batch_error_message, NEW.batch_error_message);
	END IF;
	IF(NOT OLD.description <=> NEW.description) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','description',OLD.description, NEW.description);
	END IF;
	IF(NOT OLD.disabled <=> NEW.disabled) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','disabled',OLD.disabled, NEW.disabled);
	END IF;
	IF(NOT OLD.entity_field_id <=> NEW.entity_field_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','entity_field_id',OLD.entity_field_id, NEW.entity_field_id);
	END IF;
	IF(NOT OLD.entity_id <=> NEW.entity_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','entity_id',OLD.entity_id, NEW.entity_id);
	END IF;
	IF(NOT OLD.entity_rule_id <=> NEW.entity_rule_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','entity_rule_id',OLD.entity_rule_id, NEW.entity_rule_id);
	END IF;
	IF(NOT OLD.error_message <=> NEW.error_message) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','error_message',OLD.error_message, NEW.error_message);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.rule <=> NEW.rule) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','rule',OLD.rule, NEW.rule);
	END IF;
	IF(NOT OLD.type <=> NEW.type) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'U', vChangeDate, vChangeUser, 'entity_rule','type',OLD.type, NEW.type);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_rule_before_delete   BEFORE DELETE   ON entity_rule  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.batch_error_message ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','batch_error_message',OLD.batch_error_message, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','description',OLD.description, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.disabled ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','disabled',OLD.disabled, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','entity_field_id',OLD.entity_field_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','entity_id',OLD.entity_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_rule_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','entity_rule_id',OLD.entity_rule_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.error_message ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','error_message',OLD.error_message, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.rule ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','rule',OLD.rule, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.type ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_rule_id,'D', vChangeDate, vChangeUser, 'entity_rule','type',OLD.type, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `entity_type`
--

DROP TABLE IF EXISTS `entity_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entity_type` (
  `entity_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(200) NOT NULL COMMENT 'Unique name which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `java_class` varchar(200) NOT NULL COMMENT 'The java class which represents the entity within the core application.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`entity_type_id`),
  UNIQUE KEY `entitytype_code_uc` (`code`),
  UNIQUE KEY `entitytype_name_uc` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entity_type`
--

LOCK TABLES `entity_type` WRITE;
/*!40000 ALTER TABLE `entity_type` DISABLE KEYS */;
INSERT INTO `entity_type` VALUES (4,'ORG','Organization','net.techreadiness.persistence.domain.OrgDO',null,null),(5,'ORG_PART','Organization Participation','net.techreadiness.persistence.domain.OrgPartDO',null,null),(6,'SCOPE','Scope','net.techreadiness.persistence.domain.ScopeDO',null,null),(8,'USER','User','net.techreadiness.persistence.domain.UserDO',null,null),(9,'ROLE','Role','net.techreadiness.persistence.domain.RoleDO',null,null),(10,'CONTACT','Contact','net.techreadiness.persistence.domain.ContactDO',null,null),(13,'DEVICE','Device','net.techreadiness.persistence.domain.DeviceDO',null,null),(15,'SNAPSHOT','Snapshot','net.techreadiness.persistence.domain.SnapshotWindowDO',null,null);
/*!40000 ALTER TABLE `entity_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_type_before_insert   BEFORE INSERT  ON entity_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_type_id,'I', vChangeDate, vChangeUser, 'entity_type','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_type_id,'I', vChangeDate, vChangeUser, 'entity_type','entity_type_id',null, NEW.entity_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.java_class ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_type_id,'I', vChangeDate, vChangeUser, 'entity_type','java_class',null, NEW.java_class);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.entity_type_id,'I', vChangeDate, vChangeUser, 'entity_type','name',null, NEW.name);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_type_before_update   BEFORE UPDATE   ON entity_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_type_id,'U', vChangeDate, vChangeUser, 'entity_type','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.entity_type_id <=> NEW.entity_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_type_id,'U', vChangeDate, vChangeUser, 'entity_type','entity_type_id',OLD.entity_type_id, NEW.entity_type_id);
	END IF;
	IF(NOT OLD.java_class <=> NEW.java_class) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_type_id,'U', vChangeDate, vChangeUser, 'entity_type','java_class',OLD.java_class, NEW.java_class);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_type_id,'U', vChangeDate, vChangeUser, 'entity_type','name',OLD.name, NEW.name);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER entity_type_before_delete   BEFORE DELETE   ON entity_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_type_id,'D', vChangeDate, vChangeUser, 'entity_type','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_type_id,'D', vChangeDate, vChangeUser, 'entity_type','entity_type_id',OLD.entity_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.java_class ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_type_id,'D', vChangeDate, vChangeUser, 'entity_type','java_class',OLD.java_class, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.entity_type_id,'D', vChangeDate, vChangeUser, 'entity_type','name',OLD.name, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file` (
  `file_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(10) unsigned NOT NULL,
  `file_type_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `status` enum('pending','processing','complete','errors','stopped') NOT NULL DEFAULT 'pending' COMMENT 'Indicates the state of the processing of the file.\n<br><u>pending</u>:Processing has not yet started.\n<br><u>processing</u>:Currently processing the file.\n<br><u>complete</u>:Processing completed succesfully.\n<br><u>errors</u>:Processing completed with errors.\n<br><u>stopped</u>:Processing was manually halted.',
  `path` varchar(200) NOT NULL COMMENT 'The file system path used to access the file.',
  `filename` varchar(100) NOT NULL COMMENT 'This is the real (generated) filename used to reference the file within the file system.',
  `display_filename` varchar(100) NOT NULL COMMENT 'The name of the file that will be displayed to the user (ex: original uploaded filename).',
  `error_data_filename` varchar(100) DEFAULT NULL,
  `error_message_filename` varchar(100) DEFAULT NULL,
  `status_message` varchar(1000) DEFAULT NULL COMMENT 'A detailed description (ex: 7 of the 99 rows had errors) of the status of the file.',
  `mode` enum('overwrite','append','replace') DEFAULT 'overwrite' COMMENT 'How should the data in this file be processed? \n<br><u>overwrite</u>: Perform matching... overwrite existing data and insert new data.\n<br><u>append</u>: Simply insert (no updates) the records in the file.\n<br><u>replace</u>: Delete all of the data for this org and then insert the records in this file.\n',
  `request_date` datetime DEFAULT NULL COMMENT 'The date the user requested processing of the file.',
  `description` varchar(500) DEFAULT NULL COMMENT 'An option user provided description of the file.',
  `total_record_count` int(11) DEFAULT NULL COMMENT 'Count of the number of records contained in the file ... provided by the first step in the batch job.',
  `kilobytes` int(11) DEFAULT NULL COMMENT 'The size of the file in kilobytes.',
  `batch_job_execution_id` bigint(20) DEFAULT NULL COMMENT 'The ID of the Spring Batch job execution table ... used to get detailed information about the processing of the file.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`file_id`),
  UNIQUE KEY `file_filename_uc` (`filename`),
  KEY `file_org_fki` (`org_id`),
  KEY `file_filetype_fki` (`file_type_id`),
  KEY `file_user_fki` (`user_id`),
  CONSTRAINT `file_filetype_fk` FOREIGN KEY (`file_type_id`) REFERENCES `file_type` (`file_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `file_org_fk` FOREIGN KEY (`org_id`) REFERENCES `org` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `file_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40872 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER file_before_insert   BEFORE INSERT  ON file  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.batch_job_execution_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','batch_job_execution_id',null, NEW.batch_job_execution_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','description',null, NEW.description);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_filename ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','display_filename',null, NEW.display_filename);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.error_data_filename ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','error_data_filename',null, NEW.error_data_filename);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.error_message_filename ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','error_message_filename',null, NEW.error_message_filename);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.filename ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','filename',null, NEW.filename);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.file_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','file_id',null, NEW.file_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.file_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','file_type_id',null, NEW.file_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.kilobytes ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','kilobytes',null, NEW.kilobytes);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.mode ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','mode',null, NEW.mode);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.org_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','org_id',null, NEW.org_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.path ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','path',null, NEW.path);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.request_date ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','request_date',null, NEW.request_date);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.status ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','status',null, NEW.status);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.status_message ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','status_message',null, NEW.status_message);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.total_record_count ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','total_record_count',null, NEW.total_record_count);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.user_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_id,'I', vChangeDate, vChangeUser, 'file','user_id',null, NEW.user_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER file_before_update   BEFORE UPDATE   ON file  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.batch_job_execution_id <=> NEW.batch_job_execution_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','batch_job_execution_id',OLD.batch_job_execution_id, NEW.batch_job_execution_id);
	END IF;
	IF(NOT OLD.description <=> NEW.description) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','description',OLD.description, NEW.description);
	END IF;
	IF(NOT OLD.display_filename <=> NEW.display_filename) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','display_filename',OLD.display_filename, NEW.display_filename);
	END IF;
	IF(NOT OLD.error_data_filename <=> NEW.error_data_filename) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','error_data_filename',OLD.error_data_filename, NEW.error_data_filename);
	END IF;
	IF(NOT OLD.error_message_filename <=> NEW.error_message_filename) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','error_message_filename',OLD.error_message_filename, NEW.error_message_filename);
	END IF;
	IF(NOT OLD.filename <=> NEW.filename) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','filename',OLD.filename, NEW.filename);
	END IF;
	IF(NOT OLD.file_id <=> NEW.file_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','file_id',OLD.file_id, NEW.file_id);
	END IF;
	IF(NOT OLD.file_type_id <=> NEW.file_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','file_type_id',OLD.file_type_id, NEW.file_type_id);
	END IF;
	IF(NOT OLD.kilobytes <=> NEW.kilobytes) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','kilobytes',OLD.kilobytes, NEW.kilobytes);
	END IF;
	IF(NOT OLD.mode <=> NEW.mode) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','mode',OLD.mode, NEW.mode);
	END IF;
	IF(NOT OLD.org_id <=> NEW.org_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','org_id',OLD.org_id, NEW.org_id);
	END IF;
	IF(NOT OLD.path <=> NEW.path) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','path',OLD.path, NEW.path);
	END IF;
	IF(NOT OLD.request_date <=> NEW.request_date) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','request_date',OLD.request_date, NEW.request_date);
	END IF;
	IF(NOT OLD.status <=> NEW.status) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','status',OLD.status, NEW.status);
	END IF;
	IF(NOT OLD.status_message <=> NEW.status_message) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','status_message',OLD.status_message, NEW.status_message);
	END IF;
	IF(NOT OLD.total_record_count <=> NEW.total_record_count) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','total_record_count',OLD.total_record_count, NEW.total_record_count);
	END IF;
	IF(NOT OLD.user_id <=> NEW.user_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'U', vChangeDate, vChangeUser, 'file','user_id',OLD.user_id, NEW.user_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER file_before_delete   BEFORE DELETE   ON file  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.batch_job_execution_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','batch_job_execution_id',OLD.batch_job_execution_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','description',OLD.description, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_filename ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','display_filename',OLD.display_filename, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.error_data_filename ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','error_data_filename',OLD.error_data_filename, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.error_message_filename ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','error_message_filename',OLD.error_message_filename, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.filename ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','filename',OLD.filename, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.file_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','file_id',OLD.file_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.file_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','file_type_id',OLD.file_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.kilobytes ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','kilobytes',OLD.kilobytes, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.mode ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','mode',OLD.mode, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.org_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','org_id',OLD.org_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.path ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','path',OLD.path, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.request_date ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','request_date',OLD.request_date, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.status ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','status',OLD.status, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.status_message ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','status_message',OLD.status_message, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.total_record_count ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','total_record_count',OLD.total_record_count, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.user_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_id,'D', vChangeDate, vChangeUser, 'file','user_id',OLD.user_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `file_error`
--

DROP TABLE IF EXISTS `file_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_error` (
  `file_error_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `file_id` int(10) unsigned NOT NULL,
  `record_number` int(11) DEFAULT NULL COMMENT 'The record number (line number for CSV, entity number for XML, etc) that has the error.  A null value indicates that the error is global and not specific to any record.',
  `error_code` varchar(100) NOT NULL COMMENT 'Unique code that indicates the specific error being reported.',
  `message` varchar(1000) NOT NULL COMMENT 'The detailed discription of the error to be displayed to the user.',
  PRIMARY KEY (`file_error_id`),
  KEY `fileerror_file_fki` (`file_id`),
  CONSTRAINT `fileerror_file_fk` FOREIGN KEY (`file_id`) REFERENCES `file` (`file_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=679 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_error`
--

LOCK TABLES `file_error` WRITE;
/*!40000 ALTER TABLE `file_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_type`
--

DROP TABLE IF EXISTS `file_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_type` (
  `file_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL COMMENT 'Unique code that can be used by import, exports, etc.',
  `name` varchar(100) NOT NULL COMMENT 'Unique name that is displayed to the user.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`file_type_id`),
  UNIQUE KEY `filetype_code_uc` (`code`),
  UNIQUE KEY `filetype_name_uc` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_type`
--

LOCK TABLES `file_type` WRITE;
/*!40000 ALTER TABLE `file_type` DISABLE KEYS */;
INSERT INTO `file_type` VALUES (1,'DEVICE_IMPORT','Device Import',null,null),(2,'DEVICE_EXPORT','Device Export',null,null),(3,'ORG_IMPORT','Organization Import',null,null),(4,'ORG_EXPORT','Organization Export',null,null),(5,'USER_IMPORT','User Import',null,null),(6,'USER_EXPORT','User Export',null,null),(7,'ORG_INFO_IMPORT','School Survey Import',null,null),(8,'ORG_INFO_EXPORT','School Survey Export',null,null);
/*!40000 ALTER TABLE `file_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER file_type_before_insert   BEFORE INSERT  ON file_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_type_id,'I', vChangeDate, vChangeUser, 'file_type','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.file_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_type_id,'I', vChangeDate, vChangeUser, 'file_type','file_type_id',null, NEW.file_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.file_type_id,'I', vChangeDate, vChangeUser, 'file_type','name',null, NEW.name);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER file_type_before_update   BEFORE UPDATE   ON file_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_type_id,'U', vChangeDate, vChangeUser, 'file_type','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.file_type_id <=> NEW.file_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_type_id,'U', vChangeDate, vChangeUser, 'file_type','file_type_id',OLD.file_type_id, NEW.file_type_id);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_type_id,'U', vChangeDate, vChangeUser, 'file_type','name',OLD.name, NEW.name);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER file_type_before_delete   BEFORE DELETE   ON file_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_type_id,'D', vChangeDate, vChangeUser, 'file_type','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.file_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_type_id,'D', vChangeDate, vChangeUser, 'file_type','file_type_id',OLD.file_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.file_type_id,'D', vChangeDate, vChangeUser, 'file_type','name',OLD.name, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `generic_hist`
--

DROP TABLE IF EXISTS `generic_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `generic_hist` (
  `generic_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  `table_name` varchar(64) NOT NULL,
  `column_name` varchar(64) NOT NULL,
  `primary_key` int(11) NOT NULL,
  `old_value` varchar(10000) DEFAULT NULL,
  `new_value` varchar(10000) DEFAULT NULL,
  PRIMARY KEY (`generic_hist_id`),
  KEY `generichist_tablename_columnname_primarykey_i` (`table_name`,`column_name`,`primary_key`),
  KEY `generichist_primarykey_changedate_i` (`primary_key`,`change_date`)
) ENGINE=InnoDB AUTO_INCREMENT=25274 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `option_list`
--

DROP TABLE IF EXISTS `option_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `option_list` (
  `option_list_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `code` varchar(100) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(200) NOT NULL COMMENT 'Unique name (within a scope) which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `shared` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'true: Can be used by multiple entitites.\nfalse: Only allowed to be used by a single entity',
  `sql_text` varchar(10000) DEFAULT NULL COMMENT 'This can be used if the list of values is not static and needs to be derived from live data.  The SQL should return exactly two columns: name, value.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`option_list_id`),
  UNIQUE KEY `optionlist_code_scopeid_uc` (`code`,`scope_id`),
  UNIQUE KEY `optionlist_name_scopeid_uc` (`name`,`scope_id`),
  KEY `optionlist_scope_fki` (`scope_id`),
  CONSTRAINT `optionlist_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=618 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `option_list`
--

LOCK TABLES `option_list` WRITE;
/*!40000 ALTER TABLE `option_list` DISABLE KEYS */;
INSERT INTO `option_list` VALUES (1,1,'gender','Gender (global)',1,NULL,null,null),(2,1,'test-type','Test Type (Global)',1,NULL,null,null),(3,1,'test-status','Test Status (Global)',1,NULL,null,null),(6,1,'regex','Regular Expression (Global)',1,NULL,null,null),(7,1,'stateAbbreviations','State Abbreviations',1,'',null,null),(11,1,'customCode_1312382814230','customName_1312382814230',0,NULL,null,null),(12,1,'customCode_1312382891837','customName_1312382891837',0,NULL,null,null),(13,1,'customCode_1312383149056','customName_1312383149056',0,NULL,null,null),(14,1,'customCode_1312383213776','customName_1312383213776',0,NULL,null,null),(15,1,'customCode_1312383726902','customName_1312383726902',0,NULL,null,null),(16,1,'customCode_1312383919717','customName_1312383919717',0,NULL,null,null),(17,1,'customCode_1312383993141','customName_1312383993141',0,NULL,null,null),(18,1,'customCode_1312384119694','customName_1312384119694',0,NULL,null,null),(19,1,'customCode_1312384236195','customName_1312384236195',0,NULL,null,null),(20,1,'customCode_1312384382139','customName_1312384382139',0,NULL,null,null),(21,1,'customCode_1312384438349','customName_1312384438349',0,NULL,null,null),(22,1,'customCode_1312385457428','customName_1312385457428',0,NULL,null,null),(23,1,'customCode_1312385615873','customName_1312385615873',0,NULL,null,null),(24,1,'customCode_1312385669700','customName_1312385669700',0,NULL,null,null),(25,1,'customCode_1312385761535','customName_1312385761535',0,NULL,null,null),(26,1,'customCode_1312385789598','customName_1312385789598',0,NULL,null,null),(27,1,'customCode_1312385845092','customName_1312385845092',0,NULL,null,null),(28,1,'customCode_1312385852541','customName_1312385852541',0,NULL,null,null),(505,1,'customCode_1317820246496','customName_1317820246496',0,NULL,null,null),(506,1,'customCode_1317820268405','customName_1317820268405',0,NULL,null,null),(507,1,'customCode_1317820432169','customName_1317820432169',0,NULL,null,null),(546,28,'internalNetworkSpeed','Internal Network Speed',1,'',null,null),(547,28,'screenResolutions','Screen Resolutions',1,'',null,null),(548,28,'operatingSystems','Operating Systems',1,'',null,null),(549,28,'processors','Processors',1,'',null,null),(550,28,'memory','Memory',1,'',null,null),(584,28,'browsers','Browsers',1,'',null,null),(586,28,'customCode_1327425152121','customName_1327425152121',0,NULL,null,null),(587,28,'customCode_1327425200780','customName_1327425200780',0,NULL,null,null),(589,28,'booleanList','True-False(Global)',1,'',null,null),(590,28,'sizeOfTest','Size of Test',1,NULL,null,null),(591,28,'displaySizeTBD','Display Size TBD',1,'',null,null),(594,28,'displaySize','Display Size',1,'',null,null),(596,28,'customCode_1329166501444','customName_1329166501444',0,NULL,null,null),(598,28,'internetBandwidth','Internet Bandwidth',1,'',null,null),(599,28,'surveyRating','Survey Rating',1,'',null,null),(601,28,'supportedIndicator','Supported Indicator',1,'',null,null),(602,28,'internalNetworkSpeedTBD','Internal Network Speed TBD',1,NULL,null,null),(603,28,'screenResolutionsTBD','Screen Resolutions TBD',1,'',null,null),(606,28,'memoryTBD','Memory TBD',1,'',null,null),(607,28,'browsersTBD','Browsers TBD',1,'',null,null),(609,28,'internetBandwidthTBD','Internet Bandwidth TBD',1,NULL,null,null),(612,28,'customCode_1343770978070','customName_1343770978070',0,NULL,null,null),(613,28,'environment','Environment',1,'',null,null),(614,28,'owner','Owner',1,NULL,null,null),(616,28,'schoolType','School Type',1,NULL,null,null),(617,28,'gradeActive','Grade Active',1,'',null,null);
/*!40000 ALTER TABLE `option_list` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER option_list_before_insert   BEFORE INSERT  ON option_list  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_id,'I', vChangeDate, vChangeUser, 'option_list','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_id,'I', vChangeDate, vChangeUser, 'option_list','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.option_list_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_id,'I', vChangeDate, vChangeUser, 'option_list','option_list_id',null, NEW.option_list_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_id,'I', vChangeDate, vChangeUser, 'option_list','scope_id',null, NEW.scope_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.shared ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_id,'I', vChangeDate, vChangeUser, 'option_list','shared',null, NEW.shared);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.sql_text ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_id,'I', vChangeDate, vChangeUser, 'option_list','sql_text',null, NEW.sql_text);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER option_list_before_update   BEFORE UPDATE   ON option_list  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'U', vChangeDate, vChangeUser, 'option_list','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'U', vChangeDate, vChangeUser, 'option_list','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.option_list_id <=> NEW.option_list_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'U', vChangeDate, vChangeUser, 'option_list','option_list_id',OLD.option_list_id, NEW.option_list_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'U', vChangeDate, vChangeUser, 'option_list','scope_id',OLD.scope_id, NEW.scope_id);
	END IF;
	IF(NOT OLD.shared <=> NEW.shared) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'U', vChangeDate, vChangeUser, 'option_list','shared',OLD.shared, NEW.shared);
	END IF;
	IF(NOT OLD.sql_text <=> NEW.sql_text) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'U', vChangeDate, vChangeUser, 'option_list','sql_text',OLD.sql_text, NEW.sql_text);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER option_list_before_delete   BEFORE DELETE   ON option_list  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'D', vChangeDate, vChangeUser, 'option_list','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'D', vChangeDate, vChangeUser, 'option_list','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.option_list_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'D', vChangeDate, vChangeUser, 'option_list','option_list_id',OLD.option_list_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'D', vChangeDate, vChangeUser, 'option_list','scope_id',OLD.scope_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.shared ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'D', vChangeDate, vChangeUser, 'option_list','shared',OLD.shared, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.sql_text ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_id,'D', vChangeDate, vChangeUser, 'option_list','sql_text',OLD.sql_text, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `option_list_value`
--

DROP TABLE IF EXISTS `option_list_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `option_list_value` (
  `option_list_value_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `option_list_id` int(10) unsigned NOT NULL,
  `name` varchar(200) NOT NULL COMMENT 'Unique name (within an option list) which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `value` varchar(200) NOT NULL COMMENT 'Unique value (within an option list) which will be stored to the database.',
  `display_order` int(11) DEFAULT NULL COMMENT 'Controls the order in which the values for an option list are displayed to the user.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`option_list_value_id`),
  UNIQUE KEY `optionlistvalue_name_optionlistid_uc` (`name`,`option_list_id`),
  UNIQUE KEY `optionlistvalue_value_optionlistid_uc` (`value`,`option_list_id`),
  KEY `optionlistvalue_optionlist_fki` (`option_list_id`),
  CONSTRAINT `optionlistvalue_optionlist_fk` FOREIGN KEY (`option_list_id`) REFERENCES `option_list` (`option_list_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3380 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `option_list_value`
--

LOCK TABLES `option_list_value` WRITE;
/*!40000 ALTER TABLE `option_list_value` DISABLE KEYS */;
INSERT INTO `option_list_value` VALUES (1,1,'Male','m',0,null,null),(2,1,'Female','f',1,null,null),(3,2,'Paper','paper',0,null,null),(4,2,'Online','online',1,null,null),(5,3,'Assignment','assign',0,null,null),(6,3,'Testing','testing',1,null,null),(7,3,'Attempt','attempt',2,null,null),(8,3,'Invalid Attempt','invalid_attempt',4,null,null),(21,6,'Alpha (with white space)','^[a-zA-Z\\s]*$',1,null,null),(22,6,'Numeric','^[0-9]*',1,null,null),(23,6,'Alphanumeric (with white space)','^[a-zA-Z0-9\\s]*$',1,null,null),(36,2,'Alternate','alternate',2,null,null),(37,11,'Group 1','1',1,null,null),(38,12,'01','1',1,null,null),(39,13,'Not yet assigned','00',1,null,null),(40,14,'Active','A',1,null,null),(41,15,'Regular Charter School','R',1,null,null),(42,16,'Alternative to Expulsion','A',1,null,null),(43,17,'Title 1 School Wide','S',1,null,null),(44,18,'Title 1 school not in need of improvement','0',1,null,null),(45,19,'Regular Education','R',1,null,null),(46,20,'All fifteen percent (15%) overage to the district','1',1,null,null),(47,21,'Web only','0',1,null,null),(48,22,'03','03',1,null,null),(49,23,'No - No, the student is not of Hispanic/Latino Origin','N',1,null,null),(50,24,'No','N',1,null,null),(51,25,'No','N',1,null,null),(52,26,'No','N',1,null,null),(53,27,'No','N',1,null,null),(54,28,'No','N',2,null,null),(55,26,'Yes','Y',2,null,null),(57,24,'Yes','Y',2,null,null),(58,11,'Group 2','2',2,null,null),(59,25,'Yes','Y',2,null,null),(60,23,'Yes - Yes, the student is of Hispanic/Latino Origin','Y',2,null,null),(61,22,'04','04',2,null,null),(62,11,'Group 3','3',3,null,null),(63,11,'Group 4','4',4,null,null),(64,11,'Group 5','5',5,null,null),(65,12,'02','2',2,null,null),(66,12,'03','3',3,null,null),(67,12,'04','4',4,null,null),(68,12,'05','5',5,null,null),(69,12,'06','6',6,null,null),(70,13,'Elementary','01',2,null,null),(71,13,'Middle/Jr. High','02',3,null,null),(73,13,'Senior High','03',4,null,null),(75,13,'Combination Elementary and Secondary','04',5,null,null),(76,13,'Adult','05',6,null,null),(77,13,'Other','07',7,null,null),(78,13,'DJJ','10',8,null,null),(80,13,'McKay Scholarship/Corporate Tax','11',9,null,null),(81,13,'K-8 Virtual School Continuity Pgm (Florida Virtual Academy)','13',10,null,null),(82,13,'Ahfachkee School','14',11,null,null),(83,13,'Home Ed','99',12,null,null),(84,14,'Inactive','I',2,null,null),(85,14,'Closed','C',3,null,null),(86,14,'Future','F',4,null,null),(87,14,'Temporary','T',5,null,null),(88,15,'Conversion Charter School','C',2,null,null),(89,15,'Lab Charter School','L',3,null,null),(96,15,'Not a Charter School','Z',4,null,null),(106,16,'Adult Education','B',2,null,null),(107,16,'Department of Juvenile Justice (DJJ)','D',3,null,null),(108,16,'Home Education','H',4,null,null),(112,16,'Jail','J',5,null,null),(113,16,'Hospital','L',6,null,null),(114,16,'Hospital/Homebound','M',7,null,null),(115,16,'Title 1 Migrant Non-Enrolled Students','N',8,null,null),(116,16,'Private/Voucher','P',9,null,null),(117,16,'Area Vocational-Technical Center','T',10,null,null),(118,16,'University Laboratory','U',11,null,null),(119,16,'Virtual','V',12,null,null),(120,16,'Not applicable','Z',13,null,null),(121,17,'Title 1 Targeted Assistance','T',2,null,null),(122,17,'Not a Title 1 School','Z',3,null,null),(123,18,'Year 1 in need of improvement','1',2,null,null),(124,18,'Year 2 in need of improvement','2',3,null,null),(125,18,'Year 3 in need of improvement','3',4,null,null),(126,18,'Year 4 in need of improvement','4',5,null,null),(127,18,'Year 5 in need of improvement','5',6,null,null),(128,18,'Year 6 in need of improvement','6',7,null,null),(129,18,'Not a Title 1 School','Z',8,null,null),(130,19,'Special Education','S',2,null,null),(131,19,'Alternative Education','B',3,null,null),(132,19,'Vocational/Technical','V',4,null,null),(133,19,'Adult','A',5,null,null),(134,19,'Superintendent\'s Office','O',6,null,null),(135,19,'Data Reporting (non-school entity)','D',7,null,null),(136,20,'All fifteen percent (15%) overage to the school','2',2,null,null),(137,20,'Ten percent (10%) overage to the district and five percent (5%) overage to the school','3',3,null,null),(138,20,'Five percent (5%) overage to the district and ten percent (10%) overage to the school','4',4,null,null),(139,21,'CD','1',2,null,null),(140,21,'DVD','2',3,null,null),(141,22,'05','05',3,null,null),(142,22,'06','06',4,null,null),(143,22,'07','07',5,null,null),(144,22,'08','08',6,null,null),(145,22,'09','09',7,null,null),(146,22,'10','10',8,null,null),(147,22,'11','11',9,null,null),(148,22,'12','12',10,null,null),(149,22,'13','13',11,null,null),(150,22,'AD','AD',12,null,null),(151,27,'Yes','Y',2,null,null),(1911,505,'Ignore','ignore',1,null,null),(1912,505,'Required','required',2,null,null),(1913,505,'Auto','auto',3,null,null),(1914,506,'Ignore','ignore',1,null,null),(1915,506,'Required','required',2,null,null),(1916,506,'Auto','auto',3,null,null),(1917,507,'0 - no rules required','0',1,null,null),(1918,507,'1 - one rule required','1',2,null,null),(1919,507,'2 - two rules required','2',3,null,null),(1920,507,'3 - three rules required','3',4,null,null),(1921,7,'AL','AL',1,null,null),(1922,7,'AK','AK',2,null,null),(1923,7,'AZ','AZ',3,null,null),(1924,7,'AR','AR',4,null,null),(1925,7,'IA','IA',15,null,null),(2366,584,'Internet Explorer 6','10',1,null,null),(2367,584,'Internet Explorer 7','20',2,null,null),(2368,584,'Internet Explorer 8','30',3,null,null),(2369,584,'Firefox 3.6 or older','31',6,null,null),(2370,584,'Internet Explorer 9','40',4,null,null),(2371,584,'Firefox 4+','41',7,null,null),(2372,584,'Google Chrome','42',8,null,null),(2373,584,'Safari','43',9,null,null),(2381,586,'No','false',1,null,null),(2382,586,'Yes','true',2,null,null),(2383,587,'Desktop','desktop',1,null,null),(2384,587,'Laptop','laptop',2,null,null),(2385,587,'Netbook','netbook',3,null,null),(2386,587,'Tablet','tablet',4,null,null),(2398,589,'Yes','true',0,null,null),(2399,589,'No','false',1,null,null),(2400,590,'1 MB','1024',1,null,null),(2401,590,'2 MB','2048',2,null,null),(2402,590,'3 MB','3072',3,null,null),(2403,591,'7-9.4 in','7',2,null,null),(2404,591,'9.5-12.9 in','9',3,null,null),(2405,591,'13-16.9 in','13',4,null,null),(2406,591,'17-19.9 in','17',5,null,null),(2407,591,'20-24.9 in','20',6,null,null),(2408,591,'25-27 in','25',7,null,null),(2409,591,'> 27 in','99',8,null,null),(2579,550,'512 MB','512',4,null,null),(2580,550,'<128 MB','1',1,null,null),(2581,550,'1 GB','1024',5,null,null),(2582,550,'2 GB','2048',6,null,null),(2583,550,'3 GB','3072',7,null,null),(2584,550,'4 GB','4096',8,null,null),(2585,550,'5 GB','5120',9,null,null),(2586,550,'6 GB','6144',10,null,null),(2587,550,'7 GB','7168',11,null,null),(2588,550,'8 GB','8192',12,null,null),(2589,550,'>8 GB','99999',13,null,null),(2590,594,'<7 in','1',1,null,null),(2591,594,'9.5-12.9 in','9',3,null,null),(2592,594,'13-16.9 in','13',4,null,null),(2593,594,'17-19.9 in','17',5,null,null),(2594,594,'20-24.9 in','20',6,null,null),(2595,594,'25-27 in','25',7,null,null),(2596,594,'> 27 in','99',8,null,null),(2648,547,'<800 x 600','1',1,null,null),(2649,547,'800 x 600','20',2,null,null),(2650,547,'1024 x 768','30',5,null,null),(2651,547,'1280 x 800','40',6,null,null),(2652,547,'1280 x 1024','50',7,null,null),(2653,547,'1366 x 768','60',8,null,null),(2654,547,'1440 x 900','70',9,null,null),(2655,547,'1600 x 1200','80',11,null,null),(2656,547,'1680 x 1050','90',12,null,null),(2657,547,'1920 x 1080','100',13,null,null),(2658,547,'1920 x 1200','110',14,null,null),(2659,547,'2048 x 1536','120',15,null,null),(2660,547,'>2560 x 1600','999',16,null,null),(2663,548,'Windows XP SP3 or greater','windows_xp',2,null,null),(2664,548,'Windows Vista','windows_vista',3,null,null),(2665,548,'Windows 7','windows_7',4,null,null),(2666,548,'Windows 8','windows_8',5,null,null),(2667,548,'Windows Other','windows_other',12,null,null),(2668,548,'Linux Fedora v1 - 5','linux_fedora_1_5',32,null,null),(2669,548,'Linux Ubuntu v9 - 11.04','linux_ubuntu_9_11_04',25,null,null),(2670,548,'Linux SUSE','linux_suse',37,null,null),(2671,548,'Linux Other','linux_other',38,null,null),(2672,548,'Mac OS X 10.1 - 10.4.3','mac_101_1043',42,null,null),(2673,548,'Mac OS X 10.6','mac_106',45,null,null),(2674,548,'Mac Other','mac_other',49,null,null),(2675,548,'iOS 4.x','ios_4_x',53,null,null),(2676,548,'iOS 5.x','ios_5_x',54,null,null),(2677,548,'iOS Other','ios_other',57,null,null),(2678,548,'Android 3.x','android_3_x',64,null,null),(2679,548,'Android 4.x','android_4_x',65,null,null),(2680,548,'Android Other','android_other',66,null,null),(2681,548,'Other OS/Unknown','other',99,null,null),(2682,596,'No','false',1,null,null),(2683,596,'Yes','true',2,null,null),(2717,598,'<2 Mbps (e.g., ADSL/T1/DS1)','2',1,null,null),(2719,598,'10 Mbps (e.g., Ethernet)','10',3,null,null),(2720,598,'11 Mbps (e.g., Wireless 802.11b)','11',4,null,null),(2721,598,'44 Mbps (e.g., T3/DS3)','44',10,null,null),(2722,598,'54 Mbps (e.g., Wireless 802.11g)','54',12,null,null),(2723,598,'100 Mbps  (e.g.,Fast Ethernet)','100',17,null,null),(2724,598,'155 Mbps  (e.g., OC3)','155',18,null,null),(2725,598,'600 Mbps (e.g.,Wireless 802.11n)','600',19,null,null),(2726,598,'622 Mbps (e.g.,OC12)','622',20,null,null),(2727,598,'1 Gbps (e.g.,Gigabit Ethernet)','1000',21,null,null),(2728,598,'2.5 Gbps (e.g., OC48)','2500',22,null,null),(2729,598,'9.6 Gbps (e.g., OC192)','9600',23,null,null),(2730,598,'10 Gbps (e.g.,10 Gigabit Ethernet)','10000',24,null,null),(2731,598,'>10 Gbps','999999',25,null,null),(2757,546,'10 Mbps (e.g., Ethernet)','10',3,null,null),(2758,546,'11 Mbps (e.g., Wireless 802.11b)','11',4,null,null),(2759,546,'54 Mbps (e.g., Wireless 802.11g)','54',6,null,null),(2760,546,'100 Mbps (e.g., Fast Ethernet)','100',7,null,null),(2761,546,'600 Mbps (e.g., Wireless 802.11n)','600',9,null,null),(2762,546,'1 Gbps (e.g., Gigabit Ethernet)','1000',11,null,null),(2763,546,'10 Gbps (e.g., 10 Gigabit Ethernet)','10000',11,null,null),(2764,546,'>10 Gbps','999999',16,null,null),(2778,599,'0 - No Concern','0',1,null,null),(2779,599,'1','1',2,null,null),(2780,599,'2','2',3,null,null),(2781,599,'3','3',4,null,null),(2782,599,'4','4',5,null,null),(2783,599,'5','5',6,null,null),(2789,7,'CA','CA',5,null,null),(2790,7,'CO','CO',6,null,null),(2791,7,'CT','CT',7,null,null),(2792,7,'DE','DE',8,null,null),(2793,7,'FL','FL',9,null,null),(2794,7,'GA','GA',10,null,null),(2795,7,'HI','HI',11,null,null),(2796,7,'ID','ID',12,null,null),(2797,7,'IL','IL',13,null,null),(2798,7,'IN','IN',14,null,null),(2799,7,'KS','KS',16,null,null),(2800,7,'KY','KY',17,null,null),(2801,7,'LA','LA',18,null,null),(2802,7,'ME','ME',19,null,null),(2803,7,'MD','MD',20,null,null),(2804,7,'MA','MA',21,null,null),(2805,7,'MI','MI',22,null,null),(2806,7,'MN','MN',23,null,null),(2807,7,'MS','MS',24,null,null),(2808,7,'MO','MO',25,null,null),(2809,7,'MT','MT',26,null,null),(2810,7,'NE','NE',27,null,null),(2811,7,'NV','NV',28,null,null),(2812,7,'NH','NH',29,null,null),(2813,7,'NJ','NJ',30,null,null),(2814,7,'NM','NM',31,null,null),(2815,7,'NY','NY',32,null,null),(2816,7,'NC','NC',33,null,null),(2817,7,'ND','ND',34,null,null),(2818,7,'OH','OH',35,null,null),(2819,7,'OK','OK',36,null,null),(2820,7,'OR','OR',37,null,null),(2821,7,'PA','PA',38,null,null),(2822,7,'RI','RI',39,null,null),(2823,7,'SC','SC',40,null,null),(2824,7,'SD','SD',41,null,null),(2825,7,'TN','TN',42,null,null),(2826,7,'TX','TX',43,null,null),(2827,7,'UT','UT',44,null,null),(2828,7,'VT','VT',45,null,null),(2829,7,'VA','VA',46,null,null),(2830,7,'WA','WA',47,null,null),(2831,7,'WV','WV',48,null,null),(2832,7,'WI','WI',49,null,null),(2833,7,'WY','WY',50,null,null),(2834,549,'Other','1000',102,null,null),(2835,549,'AMD Athlon','2000',1,null,null),(2836,549,'Intel Pentium','2100',2,null,null),(2837,549,'PowerPC','2300',3,null,null),(2838,549,'Intel Celeron','2500',4,null,null),(2839,549,'AMD Neo/Neo X2','3000',5,null,null),(2840,549,'Intel Atom','3100',6,null,null),(2841,549,'AMD Sempron','3500',7,null,null),(2842,549,'Intel Centrino','3600',8,null,null),(2843,549,'AMD Athlon II X2','4000',9,null,null),(2844,549,'Intel Core Solo/Duo','4100',10,null,null),(2845,549,'AMD Turion II','4500',11,null,null),(2846,549,'Intel Pentium Dual Core','4600',12,null,null),(2847,549,'AMD Phenom I','5000',13,null,null),(2848,549,'Intel Core 2 Duo','5100',14,null,null),(2849,549,'Intel Core 2 Quad','5600',15,null,null),(2850,549,'AMD Phenom II X2/X3/X4','6000',17,null,null),(2851,549,'Intel Core i3/i5','6100',18,null,null),(2852,549,'AMD Phenom II X6','6500',19,null,null),(2853,549,'Intel Core i7','6600',20,null,null),(2854,549,'ARM v4','10000',24,null,null),(2855,549,'Snapdragon S1','10100',25,null,null),(2856,549,'ARM v5','10500',26,null,null),(2857,549,'Snapdragon S2','10600',27,null,null),(2858,549,'ARM v6','11000',28,null,null),(2859,549,'Snapdragon S3','11100',29,null,null),(2860,549,'ARM v7','11500',30,null,null),(2861,549,'Snapdragon S4','11600',31,null,null),(2862,599,'6','6',7,null,null),(2863,599,'7','7',8,null,null),(2864,599,'8','8',9,null,null),(2865,599,'9','9',10,null,null),(2866,599,'10 - Extreme Concern','10',11,null,null),(2867,599,'Don\'t Know','99',0,null,null),(2868,599,'Not Applicable','98',13,null,null),(2874,601,'TBD','tbd',1,null,null),(2875,601,'Yes','yes',2,null,null),(2876,601,'No','no',3,null,null),(2877,602,'10 Mbps (e.g., Ethernet)','10',4,null,null),(2878,602,'11 Mbps (e.g., Wireless 802.11b)','11',5,null,null),(2879,602,'54 Mbps (e.g., Wireless 802.11g)','54',7,null,null),(2880,602,'100 Mbps (e.g., Fast Ethernet)','100',8,null,null),(2881,602,'600 Mbps (e.g., Wireless 802.11n)','600',10,null,null),(2882,602,'1 Gbps (e.g., Gigabit Ethernet)','1000',12,null,null),(2883,602,'10 Gbps (e.g., 10 Gigabit Ethernet)','10000',12,null,null),(2884,602,'>10 Gbps','999999',17,null,null),(2945,606,'512 MB','512',4,null,null),(2946,606,'<128 MB','1',1,null,null),(2947,606,'1 GB','1024',5,null,null),(2948,606,'2 GB','2048',6,null,null),(2949,606,'3 GB','3072',7,null,null),(2950,606,'4 GB','4096',8,null,null),(2951,606,'5 GB','5120',9,null,null),(2952,606,'6 GB','6144',10,null,null),(2953,606,'7 GB','7168',11,null,null),(2954,606,'8 GB','8192',12,null,null),(2955,606,'>8 GB','99999',13,null,null),(3004,590,'TBD','tbd',0,null,null),(3005,591,'TBD','tbd',0,null,null),(3006,606,'TBD','0',0,null,null),(3008,609,'TBD','0',0,null,null),(3009,603,'TBD','0',0,null,null),(3010,602,'TBD','0',0,null,null),(3011,6,'Phone number (###-###-####)','^[0-9]{3}-[0-9]{3}-[0-9]{4}$',1,null,null),(3012,6,'Zip+4 (#####-####)','^[0-9]{5}(-[0-9]{4}){0,1}$',1,null,null),(3013,6,'Alphanumeric','^[a-zA-Z0-9]*$',1,null,null),(3014,6,'Alpha','^[a-zA-Z]*$',1,null,null),(3015,6,'Email Address','^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$',1,null,null),(3016,548,'Linux Mint','linux_mint',23,null,null),(3017,594,'7-9.4 in','7',2,null,null),(3018,591,'<7 in','1',1,null,null),(3019,587,'Thin Client/VDI','thin',5,null,null),(3020,587,'Other','other',6,null,null),(3040,548,'Google Chrome v19 or greater','google_chrome19',75,null,null),(3041,7,'DC','DC',8,null,null),(3043,584,'Other','99',10,null,null),(3046,7,'AS','AS',51,null,null),(3047,7,'FM','FM',52,null,null),(3048,7,'GU','GU',53,null,null),(3049,7,'MH','MH',54,null,null),(3050,7,'MP','MP',55,null,null),(3051,7,'PW','PW',56,null,null),(3052,7,'PR','PR',57,null,null),(3053,7,'VI','VI',58,null,null),(3058,612,'Yes','true',1,null,null),(3059,612,'No','false',2,null,null),(3060,548,'Windows XP SP2 or less','windows_xp2',1,null,null),(3061,548,'Windows RT','windows_rt',6,null,null),(3062,548,'Mac OS X 10.8','mac_108',47,null,null),(3063,548,'iOS 6.x','ios_6_x',55,null,null),(3064,549,'Intel Xeon E3','7000',21,null,null),(3065,549,'Intel Xeon E5','7100',22,null,null),(3066,549,'Intel Xeon E7','7200',23,null,null),(3067,547,'1024 x 576','22',3,null,null),(3068,547,'1024 x 600','23',4,null,null),(3069,547,'1600 x 900','72',10,null,null),(3070,584,'Internet Explorer 10','50',5,null,null),(3071,598,'20 Mbps','20',6,null,null),(3072,598,'30 Mbps','30',8,null,null),(3073,598,'40  Mbps','40',9,null,null),(3074,598,'50 Mbps','50',11,null,null),(3075,598,'60 Mbps','60',13,null,null),(3076,598,'70 Mbps','70',14,null,null),(3077,598,'80 Mbps','80',15,null,null),(3078,598,'90 Mbps','90',16,null,null),(3095,607,'Internet Explorer 6','10',1,null,null),(3096,607,'Internet Explorer 7','20',2,null,null),(3097,607,'Internet Explorer 8','30',3,null,null),(3098,607,'Internet Explorer 9','40',4,null,null),(3099,607,'Internet Explorer 10','50',5,null,null),(3100,607,'Firefox 3.6 or older','31',6,null,null),(3101,607,'Firefox 4+','41',7,null,null),(3102,607,'Google Chrome','42',8,null,null),(3103,607,'Safari','43',9,null,null),(3104,607,'Other','99',10,null,null),(3110,607,'TBD','0',0,null,null),(3142,603,'<800 x 600','1',1,null,null),(3143,603,'800 x 600','20',2,null,null),(3144,603,'1024 x 576','22',3,null,null),(3145,603,'1024 x 600','23',4,null,null),(3146,603,'1024 x 768','30',5,null,null),(3147,603,'1280 x 800','40',6,null,null),(3148,603,'1280 x 1024','50',7,null,null),(3149,603,'1366 x 768','60',8,null,null),(3150,603,'1440 x 900','70',9,null,null),(3151,603,'1600 x 900','72',10,null,null),(3152,603,'1600 x 1200','80',11,null,null),(3153,603,'1680 x 1050','90',12,null,null),(3154,603,'1920 x 1080','100',13,null,null),(3155,603,'1920 x 1200','110',14,null,null),(3156,603,'2048 x 1536','120',15,null,null),(3157,603,'>2560 x 1600','999',16,null,null),(3173,609,'<2 Mbps (e.g., ADSL/T1/DS1)','2',1,null,null),(3174,609,'10 Mbps (e.g., Ethernet)','10',3,null,null),(3175,609,'11 Mbps (e.g., Wireless 802.11b)','11',4,null,null),(3176,609,'20 Mbps','20',6,null,null),(3177,609,'30 Mbps','30',8,null,null),(3178,609,'40  Mbps','40',9,null,null),(3179,609,'44 Mbps (e.g., T3/DS3)','44',10,null,null),(3180,609,'50 Mbps','50',11,null,null),(3181,609,'54 Mbps (e.g., Wireless 802.11g)','54',12,null,null),(3182,609,'60 Mbps','60',13,null,null),(3183,609,'70 Mbps','70',14,null,null),(3184,609,'80 Mbps','80',15,null,null),(3185,609,'90 Mbps','90',16,null,null),(3186,609,'100 Mbps  (e.g.,Fast Ethernet)','100',17,null,null),(3187,609,'155 Mbps  (e.g., OC3)','155',18,null,null),(3188,609,'600 Mbps (e.g.,Wireless 802.11n)','600',19,null,null),(3189,609,'622 Mbps (e.g.,OC12)','622',20,null,null),(3190,609,'1 Gbps (e.g.,Gigabit Ethernet)','1000',21,null,null),(3191,609,'2.5 Gbps (e.g., OC48)','2500',22,null,null),(3192,609,'9.6 Gbps (e.g., OC192)','9600',23,null,null),(3193,609,'10 Gbps (e.g.,10 Gigabit Ethernet)','10000',24,null,null),(3194,609,'>10 Gbps','999999',25,null,null),(3204,613,'Current environment appropriate for assessment','appropriate',1,null,null),(3205,613,'Movable to environment appropriate for assessment','adaptable',2,null,null),(3206,613,'Current environment not appropriate for assessment, and not sufficiently mobile','inappropriate',3,null,null),(3207,614,'School','school',1,null,null),(3208,614,'Student','student',2,null,null),(3209,614,'Other','other',3,null,null),(3210,616,'Public School','public',1,null,null),(3211,616,'Private School','private',2,null,null),(3212,616,'Charter School','charter',3,null,null),(3213,616,'Other Testing Location','other',4,null,null),(3218,549,'AMD Fusion (HSA)','5800',16,null,null),(3219,617,'Not Testing','0',1,null,null),(3220,617,'1','1',2,null,null),(3221,617,'2','2',3,null,null),(3222,617,'3','3',4,null,null),(3223,617,'4','4',5,null,null),(3224,617,'5','5',6,null,null),(3225,617,'6','6',7,null,null),(3226,617,'7','7',8,null,null),(3227,617,'8','8',9,null,null),(3228,617,'9','9',10,null,null),(3349,548,'Windows Server 2003','windows_server_2003',7,null,null),(3350,548,'Windows Server 2008','windows_server_2008',8,null,null),(3351,548,'Windows Server 2012','windows_server_2012',9,null,null),(3352,548,'Linux Ubuntu v4 - 8','linux_ubuntu_4_8',24,null,null),(3353,548,'Linux Ubuntu v11.10 or greater','linux_unbuntu_11_10',26,null,null),(3354,548,'Linux Fedora v6 (K12LTSP 4.2+) thru 15','linux_fedora_6_15',33,null,null),(3355,548,'Linux Fedora v16 or greater','linux_fedora_16',34,null,null),(3356,548,'Mac OS X 10.4.4','mac_1044',43,null,null),(3357,548,'Mac OS X 10.5','mac_105',44,null,null),(3358,548,'Mac OS X 10.7','mac_107',46,null,null),(3359,548,'Google Chrome  v18 or less','google_chrome',74,null,null),(3360,546,'No Connection Available','0',1,null,null),(3361,598,'No Connection Available','0',0,null,null),(3365,550,'128 MB','128',2,null,null),(3366,550,'256 MB','256',3,null,null),(3367,606,'128 MB','128',2,null,null),(3368,606,'256 MB','256',3,null,null),(3369,606,'Not Supported','-1',-1,null,null),(3370,598,'5 Mbps','5',2,null,null),(3371,598,'15 Mbps','15',5,null,null),(3372,598,'25 Mbps','25',7,null,null),(3373,609,'5 Mbps','5',2,null,null),(3374,609,'15 Mbps','15',5,null,null),(3375,609,'25 Mbps','25',7,null,null),(3376,548,'Windows MultiPoint Server 2010-2011','Windows_MultiPoint_server_2010_2011',10,null,null),(3377,548,'Windows MultiPoint Server 2012','Windows_MultiPoint_server_2012',11,null,null),(3378,548,'Mac OS X 10.9','mac_109',48,null,null),(3379,548,'iOS 7.x','ios_7_x',56,null,null);
/*!40000 ALTER TABLE `option_list_value` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER option_list_value_before_insert   BEFORE INSERT  ON option_list_value  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_value_id,'I', vChangeDate, vChangeUser, 'option_list_value','display_order',null, NEW.display_order);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_value_id,'I', vChangeDate, vChangeUser, 'option_list_value','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.option_list_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_value_id,'I', vChangeDate, vChangeUser, 'option_list_value','option_list_id',null, NEW.option_list_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.option_list_value_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_value_id,'I', vChangeDate, vChangeUser, 'option_list_value','option_list_value_id',null, NEW.option_list_value_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.value ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.option_list_value_id,'I', vChangeDate, vChangeUser, 'option_list_value','value',null, NEW.value);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER option_list_value_before_update   BEFORE UPDATE   ON option_list_value  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.display_order <=> NEW.display_order) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'U', vChangeDate, vChangeUser, 'option_list_value','display_order',OLD.display_order, NEW.display_order);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'U', vChangeDate, vChangeUser, 'option_list_value','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.option_list_id <=> NEW.option_list_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'U', vChangeDate, vChangeUser, 'option_list_value','option_list_id',OLD.option_list_id, NEW.option_list_id);
	END IF;
	IF(NOT OLD.option_list_value_id <=> NEW.option_list_value_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'U', vChangeDate, vChangeUser, 'option_list_value','option_list_value_id',OLD.option_list_value_id, NEW.option_list_value_id);
	END IF;
	IF(NOT OLD.value <=> NEW.value) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'U', vChangeDate, vChangeUser, 'option_list_value','value',OLD.value, NEW.value);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER option_list_value_before_delete   BEFORE DELETE   ON option_list_value  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'D', vChangeDate, vChangeUser, 'option_list_value','display_order',OLD.display_order, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'D', vChangeDate, vChangeUser, 'option_list_value','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.option_list_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'D', vChangeDate, vChangeUser, 'option_list_value','option_list_id',OLD.option_list_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.option_list_value_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'D', vChangeDate, vChangeUser, 'option_list_value','option_list_value_id',OLD.option_list_value_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.value ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.option_list_value_id,'D', vChangeDate, vChangeUser, 'option_list_value','value',OLD.value, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `org`
--

DROP TABLE IF EXISTS `org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org` (
  `org_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_type_id` int(10) unsigned NOT NULL,
  `scope_id` int(10) unsigned NOT NULL,
  `parent_org_id` int(10) unsigned DEFAULT NULL,
  `code` varchar(50) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(100) NOT NULL COMMENT 'Name which is displayed to the user...for example "Grant Wood Elementary School". This descriptive name can change and should not be be used as a key.',
  `local_code` varchar(50) DEFAULT NULL COMMENT 'The customer provided identifier for this organization.  This field might be used by the customer to "help" identify an organization, however is not guaranteed to be unique.',
  `address_line1` varchar(100) DEFAULT NULL COMMENT 'Address line #1',
  `address_line2` varchar(100) DEFAULT NULL COMMENT 'Address line #2',
  `city` varchar(50) DEFAULT NULL COMMENT 'City',
  `state` varchar(2) DEFAULT NULL COMMENT 'State',
  `zip` varchar(10) DEFAULT NULL COMMENT 'Zip code',
  `phone` varchar(20) DEFAULT NULL COMMENT 'Phone number',
  `phone_extension` varchar(10) DEFAULT NULL COMMENT 'Extension',
  `fax` varchar(20) DEFAULT NULL COMMENT 'Fax number',
  `inactive` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Indicates that the organization should no longer be active for this scope and is therefore not relevant to any future tasks.  Similar to a logical delete.',
  `data_entry_complete` varchar(100) DEFAULT NULL,
  `data_entry_complete_date` datetime DEFAULT NULL,
  `data_entry_complete_user` varchar(100) DEFAULT NULL,
  `school_type` varchar(50) DEFAULT NULL,
  `nces_code` varchar(50) DEFAULT NULL,
  `survey_techstaff_count` varchar(50) DEFAULT NULL,
  `survey_techstaff_understanding` varchar(50) DEFAULT NULL,
  `survey_techstaff_training` varchar(50) DEFAULT NULL,
  `survey_admin_count` varchar(50) DEFAULT NULL,
  `survey_admin_understanding` varchar(50) DEFAULT NULL,
  `survey_admin_training` varchar(50) DEFAULT NULL,
  `enrollment_countk` varchar(50) DEFAULT NULL,
  `enrollment_count1` varchar(50) DEFAULT NULL,
  `enrollment_count2` varchar(50) DEFAULT NULL,
  `enrollment_count3` varchar(50) DEFAULT NULL,
  `enrollment_count4` varchar(50) DEFAULT NULL,
  `enrollment_count5` varchar(50) DEFAULT NULL,
  `enrollment_count6` varchar(50) DEFAULT NULL,
  `enrollment_count7` varchar(50) DEFAULT NULL,
  `enrollment_count8` varchar(50) DEFAULT NULL,
  `enrollment_count9` varchar(50) DEFAULT NULL,
  `enrollment_count10` varchar(50) DEFAULT NULL,
  `enrollment_count11` varchar(50) DEFAULT NULL,
  `enrollment_count12` varchar(50) DEFAULT NULL,
  `student_count` varchar(50) DEFAULT NULL,
  `wireless_access_points` varchar(50) DEFAULT NULL,
  `simultaneous_testers` varchar(50) DEFAULT NULL,
  `sessions_per_day` varchar(50) DEFAULT NULL,
  `testing_window_length` varchar(50) DEFAULT NULL,
  `internet_speed` varchar(50) DEFAULT NULL,
  `internet_utilization` varchar(50) DEFAULT NULL,
  `network_speed` varchar(50) DEFAULT NULL,
  `network_utilization` varchar(50) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_id`),
  UNIQUE KEY `org_code_scopeid_uc` (`code`,`scope_id`),
  KEY `org_orgtype_fki` (`org_type_id`),
  KEY `org_scope_fki` (`scope_id`),
  KEY `org_self_fki` (`parent_org_id`),
  KEY `org_changedate_i` (`change_date`),
  CONSTRAINT `org_orgtype_fk` FOREIGN KEY (`org_type_id`) REFERENCES `org_type` (`org_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `org_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `org_self_fk` FOREIGN KEY (`parent_org_id`) REFERENCES `org` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=171731 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org`
--

LOCK TABLES `org` WRITE;
/*!40000 ALTER TABLE `org` DISABLE KEYS */;
INSERT INTO `org` VALUES (57950,20,28,NULL,'readiness','Readiness','readiness','addr1',NULL,'city','','52402','319-393-1342',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,null,'odmtools.larski');
/*!40000 ALTER TABLE `org` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_before_insert   BEFORE INSERT  ON org  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_after_insert AFTER INSERT ON org FOR EACH ROW
BEGIN
  call org_tree_update(NEW.org_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_before_update   BEFORE UPDATE   ON org  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.address_line1 <=> NEW.address_line1   
	  AND OLD.address_line2 <=> NEW.address_line2   
	  AND OLD.city <=> NEW.city   
	  AND OLD.code <=> NEW.code   
	  AND OLD.data_entry_complete <=> NEW.data_entry_complete   
	  AND OLD.data_entry_complete_date <=> NEW.data_entry_complete_date   
	  AND OLD.data_entry_complete_user <=> NEW.data_entry_complete_user   
	  AND OLD.enrollment_count1 <=> NEW.enrollment_count1   
	  AND OLD.enrollment_count10 <=> NEW.enrollment_count10   
	  AND OLD.enrollment_count11 <=> NEW.enrollment_count11   
	  AND OLD.enrollment_count12 <=> NEW.enrollment_count12   
	  AND OLD.enrollment_count2 <=> NEW.enrollment_count2   
	  AND OLD.enrollment_count3 <=> NEW.enrollment_count3   
	  AND OLD.enrollment_count4 <=> NEW.enrollment_count4   
	  AND OLD.enrollment_count5 <=> NEW.enrollment_count5   
	  AND OLD.enrollment_count6 <=> NEW.enrollment_count6   
	  AND OLD.enrollment_count7 <=> NEW.enrollment_count7   
	  AND OLD.enrollment_count8 <=> NEW.enrollment_count8   
	  AND OLD.enrollment_count9 <=> NEW.enrollment_count9   
	  AND OLD.enrollment_countk <=> NEW.enrollment_countk   
	  AND OLD.fax <=> NEW.fax   
	  AND OLD.inactive <=> NEW.inactive   
	  AND OLD.internet_speed <=> NEW.internet_speed   
	  AND OLD.internet_utilization <=> NEW.internet_utilization   
	  AND OLD.local_code <=> NEW.local_code   
	  AND OLD.name <=> NEW.name   
	  AND OLD.nces_code <=> NEW.nces_code   
	  AND OLD.network_speed <=> NEW.network_speed   
	  AND OLD.network_utilization <=> NEW.network_utilization   
	  AND OLD.org_id <=> NEW.org_id   
	  AND OLD.org_type_id <=> NEW.org_type_id   
	  AND OLD.parent_org_id <=> NEW.parent_org_id   
	  AND OLD.phone <=> NEW.phone   
	  AND OLD.phone_extension <=> NEW.phone_extension   
	  AND OLD.school_type <=> NEW.school_type   
	  AND OLD.scope_id <=> NEW.scope_id   
	  AND OLD.sessions_per_day <=> NEW.sessions_per_day   
	  AND OLD.simultaneous_testers <=> NEW.simultaneous_testers   
	  AND OLD.state <=> NEW.state   
	  AND OLD.student_count <=> NEW.student_count   
	  AND OLD.survey_admin_count <=> NEW.survey_admin_count   
	  AND OLD.survey_admin_training <=> NEW.survey_admin_training   
	  AND OLD.survey_admin_understanding <=> NEW.survey_admin_understanding   
	  AND OLD.survey_techstaff_count <=> NEW.survey_techstaff_count   
	  AND OLD.survey_techstaff_training <=> NEW.survey_techstaff_training   
	  AND OLD.survey_techstaff_understanding <=> NEW.survey_techstaff_understanding   
	  AND OLD.testing_window_length <=> NEW.testing_window_length   
	  AND OLD.wireless_access_points <=> NEW.wireless_access_points   
	  AND OLD.zip <=> NEW.zip   
    ) ) THEN
	  INSERT INTO org_hist (change_type,change_date,change_user,
	     address_line1
	     , address_line2
	     , city
	     , code
	     , data_entry_complete
	     , data_entry_complete_date
	     , data_entry_complete_user
	     , enrollment_count1
	     , enrollment_count10
	     , enrollment_count11
	     , enrollment_count12
	     , enrollment_count2
	     , enrollment_count3
	     , enrollment_count4
	     , enrollment_count5
	     , enrollment_count6
	     , enrollment_count7
	     , enrollment_count8
	     , enrollment_count9
	     , enrollment_countk
	     , fax
	     , inactive
	     , internet_speed
	     , internet_utilization
	     , local_code
	     , name
	     , nces_code
	     , network_speed
	     , network_utilization
	     , org_id
	     , org_type_id
	     , parent_org_id
	     , phone
	     , phone_extension
	     , school_type
	     , scope_id
	     , sessions_per_day
	     , simultaneous_testers
	     , state
	     , student_count
	     , survey_admin_count
	     , survey_admin_training
	     , survey_admin_understanding
	     , survey_techstaff_count
	     , survey_techstaff_training
	     , survey_techstaff_understanding
	     , testing_window_length
	     , wireless_access_points
	     , zip
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.address_line1
	     , OLD.address_line2
	     , OLD.city
	     , OLD.code
	     , OLD.data_entry_complete
	     , OLD.data_entry_complete_date
	     , OLD.data_entry_complete_user
	     , OLD.enrollment_count1
	     , OLD.enrollment_count10
	     , OLD.enrollment_count11
	     , OLD.enrollment_count12
	     , OLD.enrollment_count2
	     , OLD.enrollment_count3
	     , OLD.enrollment_count4
	     , OLD.enrollment_count5
	     , OLD.enrollment_count6
	     , OLD.enrollment_count7
	     , OLD.enrollment_count8
	     , OLD.enrollment_count9
	     , OLD.enrollment_countk
	     , OLD.fax
	     , OLD.inactive
	     , OLD.internet_speed
	     , OLD.internet_utilization
	     , OLD.local_code
	     , OLD.name
	     , OLD.nces_code
	     , OLD.network_speed
	     , OLD.network_utilization
	     , OLD.org_id
	     , OLD.org_type_id
	     , OLD.parent_org_id
	     , OLD.phone
	     , OLD.phone_extension
	     , OLD.school_type
	     , OLD.scope_id
	     , OLD.sessions_per_day
	     , OLD.simultaneous_testers
	     , OLD.state
	     , OLD.student_count
	     , OLD.survey_admin_count
	     , OLD.survey_admin_training
	     , OLD.survey_admin_understanding
	     , OLD.survey_techstaff_count
	     , OLD.survey_techstaff_training
	     , OLD.survey_techstaff_understanding
	     , OLD.testing_window_length
	     , OLD.wireless_access_points
	     , OLD.zip
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_after_update AFTER UPDATE ON org FOR EACH ROW
BEGIN
  IF IFNULL(NEW.code,'') != IFNULL(OLD.code,'')
     OR IFNULL(NEW.parent_org_id,'') != IFNULL(OLD.parent_org_id,'') THEN
    CALL org_tree_update(NEW.org_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_before_delete   BEFORE DELETE   ON org  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO org_hist (change_type,change_date,change_user,
	     address_line1
	     , address_line2
	     , city
	     , code
	     , data_entry_complete
	     , data_entry_complete_date
	     , data_entry_complete_user
	     , enrollment_count1
	     , enrollment_count10
	     , enrollment_count11
	     , enrollment_count12
	     , enrollment_count2
	     , enrollment_count3
	     , enrollment_count4
	     , enrollment_count5
	     , enrollment_count6
	     , enrollment_count7
	     , enrollment_count8
	     , enrollment_count9
	     , enrollment_countk
	     , fax
	     , inactive
	     , internet_speed
	     , internet_utilization
	     , local_code
	     , name
	     , nces_code
	     , network_speed
	     , network_utilization
	     , org_id
	     , org_type_id
	     , parent_org_id
	     , phone
	     , phone_extension
	     , school_type
	     , scope_id
	     , sessions_per_day
	     , simultaneous_testers
	     , state
	     , student_count
	     , survey_admin_count
	     , survey_admin_training
	     , survey_admin_understanding
	     , survey_techstaff_count
	     , survey_techstaff_training
	     , survey_techstaff_understanding
	     , testing_window_length
	     , wireless_access_points
	     , zip
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.address_line1
	    , OLD.address_line2
	    , OLD.city
	    , OLD.code
	    , OLD.data_entry_complete
	    , OLD.data_entry_complete_date
	    , OLD.data_entry_complete_user
	    , OLD.enrollment_count1
	    , OLD.enrollment_count10
	    , OLD.enrollment_count11
	    , OLD.enrollment_count12
	    , OLD.enrollment_count2
	    , OLD.enrollment_count3
	    , OLD.enrollment_count4
	    , OLD.enrollment_count5
	    , OLD.enrollment_count6
	    , OLD.enrollment_count7
	    , OLD.enrollment_count8
	    , OLD.enrollment_count9
	    , OLD.enrollment_countk
	    , OLD.fax
	    , OLD.inactive
	    , OLD.internet_speed
	    , OLD.internet_utilization
	    , OLD.local_code
	    , OLD.name
	    , OLD.nces_code
	    , OLD.network_speed
	    , OLD.network_utilization
	    , OLD.org_id
	    , OLD.org_type_id
	    , OLD.parent_org_id
	    , OLD.phone
	    , OLD.phone_extension
	    , OLD.school_type
	    , OLD.scope_id
	    , OLD.sessions_per_day
	    , OLD.simultaneous_testers
	    , OLD.state
	    , OLD.student_count
	    , OLD.survey_admin_count
	    , OLD.survey_admin_training
	    , OLD.survey_admin_understanding
	    , OLD.survey_techstaff_count
	    , OLD.survey_techstaff_training
	    , OLD.survey_techstaff_understanding
	    , OLD.testing_window_length
	    , OLD.wireless_access_points
	    , OLD.zip
	);

    INSERT INTO org_hist (change_type,change_date,change_user,
	     address_line1
	     , address_line2
	     , city
	     , code
	     , data_entry_complete
	     , data_entry_complete_date
	     , data_entry_complete_user
	     , enrollment_count1
	     , enrollment_count10
	     , enrollment_count11
	     , enrollment_count12
	     , enrollment_count2
	     , enrollment_count3
	     , enrollment_count4
	     , enrollment_count5
	     , enrollment_count6
	     , enrollment_count7
	     , enrollment_count8
	     , enrollment_count9
	     , enrollment_countk
	     , fax
	     , inactive
	     , internet_speed
	     , internet_utilization
	     , local_code
	     , name
	     , nces_code
	     , network_speed
	     , network_utilization
	     , org_id
	     , org_type_id
	     , parent_org_id
	     , phone
	     , phone_extension
	     , school_type
	     , scope_id
	     , sessions_per_day
	     , simultaneous_testers
	     , state
	     , student_count
	     , survey_admin_count
	     , survey_admin_training
	     , survey_admin_understanding
	     , survey_techstaff_count
	     , survey_techstaff_training
	     , survey_techstaff_understanding
	     , testing_window_length
	     , wireless_access_points
	     , zip
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.address_line1
	    , OLD.address_line2
	    , OLD.city
	    , OLD.code
	    , OLD.data_entry_complete
	    , OLD.data_entry_complete_date
	    , OLD.data_entry_complete_user
	    , OLD.enrollment_count1
	    , OLD.enrollment_count10
	    , OLD.enrollment_count11
	    , OLD.enrollment_count12
	    , OLD.enrollment_count2
	    , OLD.enrollment_count3
	    , OLD.enrollment_count4
	    , OLD.enrollment_count5
	    , OLD.enrollment_count6
	    , OLD.enrollment_count7
	    , OLD.enrollment_count8
	    , OLD.enrollment_count9
	    , OLD.enrollment_countk
	    , OLD.fax
	    , OLD.inactive
	    , OLD.internet_speed
	    , OLD.internet_utilization
	    , OLD.local_code
	    , OLD.name
	    , OLD.nces_code
	    , OLD.network_speed
	    , OLD.network_utilization
	    , OLD.org_id
	    , OLD.org_type_id
	    , OLD.parent_org_id
	    , OLD.phone
	    , OLD.phone_extension
	    , OLD.school_type
	    , OLD.scope_id
	    , OLD.sessions_per_day
	    , OLD.simultaneous_testers
	    , OLD.state
	    , OLD.student_count
	    , OLD.survey_admin_count
	    , OLD.survey_admin_training
	    , OLD.survey_admin_understanding
	    , OLD.survey_techstaff_count
	    , OLD.survey_techstaff_training
	    , OLD.survey_techstaff_understanding
	    , OLD.testing_window_length
	    , OLD.wireless_access_points
	    , OLD.zip
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `org_ext`
--

DROP TABLE IF EXISTS `org_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_ext` (
  `org_ext_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(10) unsigned NOT NULL,
  `entity_field_id` int(10) unsigned NOT NULL,
  `value` varchar(500) NOT NULL COMMENT 'Value for the "key" referenced by the entity_field_id.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_ext_id`),
  UNIQUE KEY `orgext_orgid_entityfieldid_uc` (`org_id`,`entity_field_id`),
  KEY `orgext_value_entityfieldid_i` (`value`(10),`entity_field_id`),
  KEY `orgext_entityfield_fki` (`entity_field_id`),
  KEY `orgext_org_fki` (`org_id`),
  CONSTRAINT `orgext_entityfield_fk` FOREIGN KEY (`entity_field_id`) REFERENCES `entity_field` (`entity_field_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orgext_org_fk` FOREIGN KEY (`org_id`) REFERENCES `org` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9686 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_ext`
--

LOCK TABLES `org_ext` WRITE;
/*!40000 ALTER TABLE `org_ext` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_ext` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_ext_before_insert   BEFORE INSERT  ON org_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_ext_before_update   BEFORE UPDATE   ON org_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.entity_field_id <=> NEW.entity_field_id   
	  AND OLD.org_ext_id <=> NEW.org_ext_id   
	  AND OLD.org_id <=> NEW.org_id   
	  AND OLD.value <=> NEW.value   
    ) ) THEN
	  INSERT INTO org_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , org_ext_id
	     , org_id
	     , value
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.entity_field_id
	     , OLD.org_ext_id
	     , OLD.org_id
	     , OLD.value
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_ext_before_delete   BEFORE DELETE   ON org_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO org_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , org_ext_id
	     , org_id
	     , value
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.entity_field_id
	    , OLD.org_ext_id
	    , OLD.org_id
	    , OLD.value
	);

    INSERT INTO org_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , org_ext_id
	     , org_id
	     , value
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.entity_field_id
	    , OLD.org_ext_id
	    , OLD.org_id
	    , OLD.value
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `org_ext_hist`
--

DROP TABLE IF EXISTS `org_ext_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_ext_hist` (
  `org_ext_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `org_ext_id` int(10) unsigned DEFAULT NULL,
  `org_id` int(10) unsigned DEFAULT NULL,
  `entity_field_id` int(10) unsigned DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_ext_hist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_ext_hist`
--

LOCK TABLES `org_ext_hist` WRITE;
/*!40000 ALTER TABLE `org_ext_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_ext_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_hist`
--

DROP TABLE IF EXISTS `org_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_hist` (
  `org_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(10) unsigned DEFAULT NULL,
  `org_type_id` int(10) unsigned DEFAULT NULL,
  `scope_id` int(10) unsigned DEFAULT NULL,
  `parent_org_id` int(10) unsigned DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `local_code` varchar(50) DEFAULT NULL,
  `address_line1` varchar(100) DEFAULT NULL,
  `address_line2` varchar(100) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(2) DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `phone_extension` varchar(10) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `inactive` tinyint(1) DEFAULT '0',
  `data_entry_complete` varchar(100) DEFAULT NULL,
  `data_entry_complete_date` datetime DEFAULT NULL,
  `data_entry_complete_user` varchar(100) DEFAULT NULL,
  `school_type` varchar(50) DEFAULT NULL,
  `nces_code` varchar(50) DEFAULT NULL,
  `survey_techstaff_count` varchar(50) DEFAULT NULL,
  `survey_techstaff_understanding` varchar(50) DEFAULT NULL,
  `survey_techstaff_training` varchar(50) DEFAULT NULL,
  `survey_admin_count` varchar(50) DEFAULT NULL,
  `survey_admin_understanding` varchar(50) DEFAULT NULL,
  `survey_admin_training` varchar(50) DEFAULT NULL,
  `enrollment_countk` varchar(50) DEFAULT NULL,
  `enrollment_count1` varchar(50) DEFAULT NULL,
  `enrollment_count2` varchar(50) DEFAULT NULL,
  `enrollment_count3` varchar(50) DEFAULT NULL,
  `enrollment_count4` varchar(50) DEFAULT NULL,
  `enrollment_count5` varchar(50) DEFAULT NULL,
  `enrollment_count6` varchar(50) DEFAULT NULL,
  `enrollment_count7` varchar(50) DEFAULT NULL,
  `enrollment_count8` varchar(50) DEFAULT NULL,
  `enrollment_count9` varchar(50) DEFAULT NULL,
  `enrollment_count10` varchar(50) DEFAULT NULL,
  `enrollment_count11` varchar(50) DEFAULT NULL,
  `enrollment_count12` varchar(50) DEFAULT NULL,
  `student_count` varchar(50) DEFAULT NULL,
  `wireless_access_points` varchar(50) DEFAULT NULL,
  `simultaneous_testers` varchar(50) DEFAULT NULL,
  `sessions_per_day` varchar(50) DEFAULT NULL,
  `testing_window_length` varchar(50) DEFAULT NULL,
  `internet_speed` varchar(50) DEFAULT NULL,
  `internet_utilization` varchar(50) DEFAULT NULL,
  `network_speed` varchar(50) DEFAULT NULL,
  `network_utilization` varchar(50) DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_hist_id`),
  KEY `orghist_changedate_changetype_i` (`change_date`,`change_type`)
) ENGINE=InnoDB AUTO_INCREMENT=22853 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_hist`
--

LOCK TABLES `org_hist` WRITE;
/*!40000 ALTER TABLE `org_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_part`
--

DROP TABLE IF EXISTS `org_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_part` (
  `org_part_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` int(10) unsigned NOT NULL,
  `scope_id` int(10) unsigned NOT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_part_id`),
  UNIQUE KEY `orgpart_orgid_scopeid_uc` (`org_id`,`scope_id`),
  KEY `orgpart_scope_fki` (`scope_id`),
  KEY `orgpart_org_fki` (`org_id`),
  KEY `orgpart_changedate_i` (`change_date`),
  CONSTRAINT `orgpart_org_fk` FOREIGN KEY (`org_id`) REFERENCES `org` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orgpart_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=326869 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_part`
--

LOCK TABLES `org_part` WRITE;
/*!40000 ALTER TABLE `org_part` DISABLE KEYS */;
INSERT INTO `org_part` VALUES (168114,57950,65,null,'odmtools.larski'),(168115,57950,66,null,'odmtools.larski');
/*!40000 ALTER TABLE `org_part` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_part_before_insert   BEFORE INSERT  ON org_part  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_part_before_update   BEFORE UPDATE   ON org_part  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.org_id <=> NEW.org_id   
	  AND OLD.org_part_id <=> NEW.org_part_id   
	  AND OLD.scope_id <=> NEW.scope_id   
    ) ) THEN
	  INSERT INTO org_part_hist (change_type,change_date,change_user,
	     org_id
	     , org_part_id
	     , scope_id
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.org_id
	     , OLD.org_part_id
	     , OLD.scope_id
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_part_before_delete   BEFORE DELETE   ON org_part  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO org_part_hist (change_type,change_date,change_user,
	     org_id
	     , org_part_id
	     , scope_id
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.org_id
	    , OLD.org_part_id
	    , OLD.scope_id
	);

    INSERT INTO org_part_hist (change_type,change_date,change_user,
	     org_id
	     , org_part_id
	     , scope_id
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.org_id
	    , OLD.org_part_id
	    , OLD.scope_id
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `org_part_ext`
--

DROP TABLE IF EXISTS `org_part_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_part_ext` (
  `org_part_ext_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `org_part_id` int(10) unsigned NOT NULL,
  `entity_field_id` int(10) unsigned NOT NULL,
  `value` varchar(500) NOT NULL COMMENT 'Value for the "key" referenced by the entity_field_id.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_part_ext_id`),
  UNIQUE KEY `orgpartext_orgpartid_entityfieldid_uc` (`org_part_id`,`entity_field_id`),
  KEY `orgpartext_value_entityfieldid_i` (`value`(10),`entity_field_id`),
  KEY `orgpartext_entityfield_fki` (`entity_field_id`),
  KEY `orgpartext_orgpart_fki` (`org_part_id`),
  CONSTRAINT `orgpartext_entityfield_fk` FOREIGN KEY (`entity_field_id`) REFERENCES `entity_field` (`entity_field_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orgpartext_orgpart_fk` FOREIGN KEY (`org_part_id`) REFERENCES `org_part` (`org_part_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_part_ext`
--

LOCK TABLES `org_part_ext` WRITE;
/*!40000 ALTER TABLE `org_part_ext` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_part_ext` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_part_ext_before_insert   BEFORE INSERT  ON org_part_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_part_ext_before_update   BEFORE UPDATE   ON org_part_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.entity_field_id <=> NEW.entity_field_id   
	  AND OLD.org_part_ext_id <=> NEW.org_part_ext_id   
	  AND OLD.org_part_id <=> NEW.org_part_id   
	  AND OLD.value <=> NEW.value   
    ) ) THEN
	  INSERT INTO org_part_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , org_part_ext_id
	     , org_part_id
	     , value
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.entity_field_id
	     , OLD.org_part_ext_id
	     , OLD.org_part_id
	     , OLD.value
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_part_ext_before_delete   BEFORE DELETE   ON org_part_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO org_part_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , org_part_ext_id
	     , org_part_id
	     , value
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.entity_field_id
	    , OLD.org_part_ext_id
	    , OLD.org_part_id
	    , OLD.value
	);

    INSERT INTO org_part_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , org_part_ext_id
	     , org_part_id
	     , value
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.entity_field_id
	    , OLD.org_part_ext_id
	    , OLD.org_part_id
	    , OLD.value
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `org_part_ext_hist`
--

DROP TABLE IF EXISTS `org_part_ext_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_part_ext_hist` (
  `org_part_ext_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `org_part_ext_id` int(10) unsigned DEFAULT NULL,
  `org_part_id` int(10) unsigned DEFAULT NULL,
  `entity_field_id` int(10) unsigned DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_part_ext_hist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_part_ext_hist`
--

LOCK TABLES `org_part_ext_hist` WRITE;
/*!40000 ALTER TABLE `org_part_ext_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_part_ext_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_part_hist`
--

DROP TABLE IF EXISTS `org_part_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_part_hist` (
  `org_part_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `org_part_id` int(10) unsigned DEFAULT NULL,
  `org_id` int(10) unsigned DEFAULT NULL,
  `scope_id` int(10) unsigned DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_part_hist_id`),
  KEY `orgparthist_changedate_changetype_i` (`change_date`,`change_type`)
) ENGINE=InnoDB AUTO_INCREMENT=15879 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_part_hist`
--

LOCK TABLES `org_part_hist` WRITE;
/*!40000 ALTER TABLE `org_part_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_part_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_tree`
--

DROP TABLE IF EXISTS `org_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_tree` (
  `org_tree_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ancestor_org_id` int(10) unsigned DEFAULT NULL COMMENT 'Identifies the organization which is the ancestor of the relation.',
  `ancestor_path` varchar(2000) NOT NULL COMMENT 'Provides a unique string representation of the ancestor organization similar to the path of a file.  ex "/TN"',
  `org_id` int(10) unsigned NOT NULL COMMENT 'Identifies the organization which is the descendant of the relation.',
  `path` varchar(2000) NOT NULL COMMENT 'Provides a unique string representation of the descendant organization similar to the path of a file.  ex "/TN/district1/school1"',
  `distance` smallint(6) NOT NULL COMMENT 'The number of levels in the tree between the ancestor and descendant organizations.  A level of "0" indicates that this is an association of the organization to itself.',
  `depth` smallint(6) NOT NULL COMMENT 'The depth of the the descendant organization within the organization hierarchy.  Depth is zero based, so the root of the tree is zero.',
  PRIMARY KEY (`org_tree_id`),
  KEY `orgtree_org_fki` (`org_id`),
  KEY `orgtree_ancestororg_fki` (`ancestor_org_id`),
  KEY `orgtree_ancestorpath_i` (`ancestor_path`(255)),
  KEY `orgtree_path_i` (`path`(255)),
  CONSTRAINT `orgtree_ancestororg_fk` FOREIGN KEY (`ancestor_org_id`) REFERENCES `org` (`org_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `orgtree_org_fk` FOREIGN KEY (`org_id`) REFERENCES `org` (`org_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1204623 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_tree`
--

LOCK TABLES `org_tree` WRITE;
/*!40000 ALTER TABLE `org_tree` DISABLE KEYS */;
INSERT INTO `org_tree` VALUES (1204621,NULL,'/',57950,'/readiness',1,1),(1204622,57950,'/readiness',57950,'/readiness',0,1);
/*!40000 ALTER TABLE `org_tree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_type`
--

DROP TABLE IF EXISTS `org_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_type` (
  `org_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `parent_org_type_id` int(10) unsigned DEFAULT NULL,
  `code` varchar(50) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(100) NOT NULL COMMENT 'Unique name (within a scope) which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `allow_device` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Devices can only be created/modified by orgs which have the allow_device flag.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`org_type_id`),
  UNIQUE KEY `orgtype_code_scopeid_uc` (`code`,`scope_id`),
  UNIQUE KEY `orgtype_name_parentorgtypeid_uc` (`name`,`parent_org_type_id`),
  KEY `orgtype_scope_fki` (`scope_id`),
  KEY `orgtype_self_fki` (`parent_org_type_id`),
  CONSTRAINT `orgtype_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orgtype_self_fk` FOREIGN KEY (`parent_org_type_id`) REFERENCES `org_type` (`org_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_type`
--

LOCK TABLES `org_type` WRITE;
/*!40000 ALTER TABLE `org_type` DISABLE KEYS */;
INSERT INTO `org_type` VALUES (20,28,NULL,'readiness','Readiness',0,null,null),(29,28,20,'state','State',0,null,null),(30,28,29,'district','District',0,null,null),(31,28,30,'school','School',1,null,null);
/*!40000 ALTER TABLE `org_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_type_before_insert   BEFORE INSERT  ON org_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.allow_device ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.org_type_id,'I', vChangeDate, vChangeUser, 'org_type','allow_device',null, NEW.allow_device);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.org_type_id,'I', vChangeDate, vChangeUser, 'org_type','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.org_type_id,'I', vChangeDate, vChangeUser, 'org_type','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.org_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.org_type_id,'I', vChangeDate, vChangeUser, 'org_type','org_type_id',null, NEW.org_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.parent_org_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.org_type_id,'I', vChangeDate, vChangeUser, 'org_type','parent_org_type_id',null, NEW.parent_org_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.org_type_id,'I', vChangeDate, vChangeUser, 'org_type','scope_id',null, NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_type_before_update   BEFORE UPDATE   ON org_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.allow_device <=> NEW.allow_device) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'U', vChangeDate, vChangeUser, 'org_type','allow_device',OLD.allow_device, NEW.allow_device);
	END IF;
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'U', vChangeDate, vChangeUser, 'org_type','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'U', vChangeDate, vChangeUser, 'org_type','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.org_type_id <=> NEW.org_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'U', vChangeDate, vChangeUser, 'org_type','org_type_id',OLD.org_type_id, NEW.org_type_id);
	END IF;
	IF(NOT OLD.parent_org_type_id <=> NEW.parent_org_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'U', vChangeDate, vChangeUser, 'org_type','parent_org_type_id',OLD.parent_org_type_id, NEW.parent_org_type_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'U', vChangeDate, vChangeUser, 'org_type','scope_id',OLD.scope_id, NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER org_type_before_delete   BEFORE DELETE   ON org_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.allow_device ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'D', vChangeDate, vChangeUser, 'org_type','allow_device',OLD.allow_device, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'D', vChangeDate, vChangeUser, 'org_type','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'D', vChangeDate, vChangeUser, 'org_type','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.org_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'D', vChangeDate, vChangeUser, 'org_type','org_type_id',OLD.org_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.parent_org_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'D', vChangeDate, vChangeUser, 'org_type','parent_org_type_id',OLD.parent_org_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.org_type_id,'D', vChangeDate, vChangeUser, 'org_type','scope_id',OLD.scope_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `permission_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `code` varchar(50) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(100) NOT NULL COMMENT 'nique name which is displayed to the user. This descriptive name can change and should not be be used as a key.',
  `description` varchar(1000) DEFAULT NULL COMMENT 'A longer description of the field which is displayed to the user as context sensitive help.',
  `display_order` smallint(6) DEFAULT NULL COMMENT 'Can be used to control the order the permissions are displayed on our web pages.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`permission_id`),
  UNIQUE KEY `permission_code_scopeid_uc` (`code`,`scope_id`),
  UNIQUE KEY `permission_name_scopeid_uc` (`name`,`scope_id`),
  KEY `permission_scope_fki` (`scope_id`),
  CONSTRAINT `permission_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (6,1,'core_customer_user_access','core_customer_user_access','grants access to customer user management',NULL,NULL,NULL),(7,1,'core_customer_file_loading_access','core_customer_file_loading_access','core_customer_file_loading_access',NULL,NULL,NULL),(11,1,'core_customer_organization_access','core_customer_organization_access','core_customer_organization_access',NULL,NULL,NULL),(13,1,'core_customer_cache_access','core_customer_cache_access','core_customer_cache_access',NULL,NULL,NULL),(34,1,'core_customer_org_part_update','core_customer_org_part_update','core_customer_org_part_update',NULL,NULL,NULL),(35,1,'core_customer_org_contact_update','core_customer_org_contact_update','core_customer_org_contact_update',NULL,NULL,NULL),(36,1,'core_customer_org_delete','core_customer_org_delete','core_customer_org_delete',NULL,NULL,NULL),(37,1,'core_customer_user_create','core_customer_user_create','core_customer_user_create',NULL,NULL,NULL),(38,1,'core_customer_user_update','core_customer_user_update','core_customer_user_update',NULL,NULL,NULL),(39,1,'core_customer_user_org_update','core_customer_user_org_update','core_customer_user_org_update',NULL,NULL,NULL),(40,1,'core_customer_user_role_update','core_customer_user_role_update','core_customer_user_role_update',NULL,NULL,NULL),(41,1,'core_customer_user_reset_password','core_customer_user_reset_password','core_customer_user_reset_password',NULL,NULL,NULL),(42,1,'core_customer_org_update','core_customer_org_update','core_customer_org_update',NULL,NULL,NULL),(43,1,'core_customer_org_create','core_customer_org_create','core_customer_org_create',NULL,NULL,NULL),(44,1,'core_customer_admin_access','core_customer_admin_access','core_customer_admin_access',NULL,NULL,NULL),(53,1,'core_search_all_orgs','core_search_all_orgs','',NULL,NULL,NULL),(56,1,'core_search_ignore_roleconfer','core_search_ignore_roleconfer','core_search_ignore_roleconfer',NULL,NULL,NULL),(57,1,'core_customer_change_global_scope','core_change_global_scope','Ability to change global scope',NULL,NULL,NULL),(58,1,'core_customer_file_device','core_customer_file_device','core_customer_file_device',NULL,NULL,NULL),(59,1,'core_customer_file_org','core_customer_file_org','core_customer_file_org',NULL,NULL,NULL),(60,1,'core_customer_file_user','core_customer_file_user','core_customer_file_user',NULL,NULL,NULL),(62,28,'ready_customer_device_ file_import','ready_customer_device_ file_import','This permission controls the ability to upload a Device file which will populate the devices into the Readiness system.  This is done via the Import Devices task on the Devices page.',NULL,NULL,NULL),(63,28,'ready_customer_device_ file_export','ready_customer_device_ file_export','This permission controls the ability to export a Device file from the Readiness system.  This is done via the Export Devices task on the Devices page.',NULL,NULL,NULL),(64,28,'ready_customer_network_infrastructure','ready_customer_network_infrastructure','This permission controls the ability to enter network information for an organization within the Readiness system.',NULL,NULL,NULL),(65,28,'ready_customer_device_tester','ready_customer_device_tester','This permission controls the ability to enter device-tester information that will be used to help determine readiness. ',NULL,NULL,NULL),(66,28,'ready_customer_device_rpt_access','ready_customer_device_rpt_access','This permission controls the ability to view Device reports.  This is done via the Results page.',NULL,NULL,NULL),(67,28,'core_customer_change_global_scope','core_change_global_scope','core_change_global_scope',NULL,NULL,NULL),(68,28,'ready_customer_scope_min_spec','ready_customer_scope_min_spec','ready_customer_scope_min_spec',NULL,NULL,NULL),(69,28,'ready_customer_readiness_access','ready_customer_readiness_access','This permission controls access to the Devices sub-navigation under the  tab. ',NULL,NULL,NULL),(70,28,'ready_customer_device_create','ready_customer_device_create','This permission controls the ability to Add a new device.  This is done via the Add Device button on the Devices page.',NULL,NULL,NULL),(71,28,'ready_customer_device_analyze','ready_customer_device_analyze','This permission controls the ability to Analyze the device that is currently being used.  This is done via the Add Device feature and Edit Device feature.  ',NULL,NULL,NULL),(72,28,'ready_customer_device_update','ready_customer_device_update','This permission controls the ability to Edit an existing device.  This is done via the Devices page.',NULL,NULL,NULL),(73,28,'ready_customer_device_delete','ready_customer_device_delete','This permission controls the ability to Delete an existing device.  This is done via the Devices page.',NULL,NULL,NULL),(74,28,'ready_customer_device_assessment_rpt','ready_customer_device_assessment_rpt','This permission controls the ability to view the Device Assessment Report.',NULL,NULL,NULL),(75,28,'ready_customer_network_assessment_rpt','ready_customer_network_assessment_rpt','This permission controls the ability to view the Network Assessment Report.',NULL,NULL,NULL),(76,28,'ready_customer_device_to_test_rpt','ready_customer_device_to_test_rpt','This permission controls the ability to view the Device to Test Taker Assessment Report.',NULL,NULL,NULL),(77,28,'ready_customer_assessment_completion_rpt','ready_customer_assessment_completion_rpt','This permission controls the ability to view the Completion Status Report.',NULL,NULL,NULL),(78,28,'ready_customer_staff_personnel_rpt','ready_customer_staff_personnel_rpt','This permission controls the ability to view the Staff and Personnel Readiness  Report.',NULL,NULL,NULL),(79,1,'ready_customer_file_org_info','School Survey Import/Export Permission','This permission controls the ability to upload/download a School Survey file which will populate the network and survey information into the Readiness system.',NULL,NULL,NULL),(80,28,'ready_customer_snapshot','ready_customer_snapshot','This permission controls access to the snapshot tab menu.',NULL,NULL,NULL),(81,28,'ready_customer_snapshot_create','ready_customer_snapshot_create','This permission controls access to the create snapshot task.',NULL,NULL,NULL),(82,28,'ready_customer_snapshot_edit','ready_customer_snapshot_edit','This permission controls access to the edit snapshot task.',NULL,NULL,NULL),(83,28,'ready_customer_snapshot_delete','ready_customer_snapshot_delete',NULL,NULL,NULL,NULL),(84,28,'ready_customer_device_assessment_sample_rpt','ready_customer_device_assessment_sample_rpt',NULL,NULL,NULL,NULL),(85,28,'ready_customer_device_to_test_sample_rpt','ready_customer_device_to_test_sample_rpt',NULL,NULL,NULL,NULL),(86,28,'ready_customer_network_assessment_sample_rpt','ready_customer_network_assessment_sample_rpt',NULL,NULL,NULL,NULL),(87,28,'ready_customer_staff_personnel_sample_rpt','ready_customer_staff_personnel_sample_rpt',NULL,NULL,NULL,NULL),(88,28,'ready_customer_assessment_completion_sample_rpt','ready_customer_assessment_completion_sample_rpt',NULL,NULL,NULL,NULL),(89,1,'core_customer_user_enable','core_customer_user_enable','core_customer_user_enable',NULL,NULL,NULL),(90,1,'core_customer_user_delete','core_customer_user_delete','core_customer_user_delete',NULL,NULL,NULL),(91,1,'core_customer_user_password_token_access','core_customer_user_password_token_access','core_customer_user_password_token_access',NULL,NULL,NULL),(92,28,'ready_customer_progess_device_rpt','ready_customer_progess_device_rpt','Controls the ability to view the Progress Report - Device Indicators',NULL,NULL,NULL),(93,28,'ready_customer_progess_device_test_taker_rpt','ready_customer_progess_device_test_taker_rpt ','Controls the ability view the Progress Report - Device to Test Taker',NULL,NULL,NULL),(94,28,'ready_customer_progess_network_rpt','ready_customer_progess_network_rpt ','Controls the ability to view the Progress Report - Network Indicator',NULL,NULL,NULL),(95,28,'ready_customer_school_exception_rpt','ready_customer_school_exception_rpt','This permission controls the ability to view the School Exception Report',NULL,NULL,NULL),(96,28,'ready_customer_dataentry_access','ready_customer_dataentry_access','ready_customer_dataentry_access',NULL,NULL,NULL),(97,28,'ready_customer_survey_access','ready_customer_survey_access','ready_customer_survey_access',NULL,NULL,NULL),(100,28,'ready_customer_overall_assessment_rpt','ready_customer_overall_assessment_rpt','Overall Readiness Report',NULL,NULL,NULL);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER permission_before_insert   BEFORE INSERT  ON permission  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.permission_id,'I', vChangeDate, vChangeUser, 'permission','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.permission_id,'I', vChangeDate, vChangeUser, 'permission','description',null, NEW.description);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.permission_id,'I', vChangeDate, vChangeUser, 'permission','display_order',null, NEW.display_order);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.permission_id,'I', vChangeDate, vChangeUser, 'permission','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.permission_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.permission_id,'I', vChangeDate, vChangeUser, 'permission','permission_id',null, NEW.permission_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.permission_id,'I', vChangeDate, vChangeUser, 'permission','scope_id',null, NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER permission_before_update   BEFORE UPDATE   ON permission  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'U', vChangeDate, vChangeUser, 'permission','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.description <=> NEW.description) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'U', vChangeDate, vChangeUser, 'permission','description',OLD.description, NEW.description);
	END IF;
	IF(NOT OLD.display_order <=> NEW.display_order) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'U', vChangeDate, vChangeUser, 'permission','display_order',OLD.display_order, NEW.display_order);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'U', vChangeDate, vChangeUser, 'permission','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.permission_id <=> NEW.permission_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'U', vChangeDate, vChangeUser, 'permission','permission_id',OLD.permission_id, NEW.permission_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'U', vChangeDate, vChangeUser, 'permission','scope_id',OLD.scope_id, NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER permission_before_delete   BEFORE DELETE   ON permission  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'D', vChangeDate, vChangeUser, 'permission','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'D', vChangeDate, vChangeUser, 'permission','description',OLD.description, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'D', vChangeDate, vChangeUser, 'permission','display_order',OLD.display_order, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'D', vChangeDate, vChangeUser, 'permission','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.permission_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'D', vChangeDate, vChangeUser, 'permission','permission_id',OLD.permission_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.permission_id,'D', vChangeDate, vChangeUser, 'permission','scope_id',OLD.scope_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `query_sql`
--

DROP TABLE IF EXISTS `query_sql`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_sql` (
  `query_sql_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned DEFAULT NULL,
  `entity_type_id` int(10) unsigned NOT NULL,
  `code` varchar(100) NOT NULL COMMENT 'Unique identifier (within a scope) which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(200) NOT NULL COMMENT 'Unique name (within a scope) which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `sql_text` longtext NOT NULL COMMENT 'SQL query (select statement) text',
  `description` varchar(1000) NOT NULL COMMENT 'Detail description of the query that can be displayed to the user.',
  `keywords` varchar(1000) DEFAULT NULL COMMENT 'Comma separated list of key words that can be used by a developer to search for a query to use.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`query_sql_id`),
  UNIQUE KEY `querysql_code_scopeid_uc` (`code`,`scope_id`),
  UNIQUE KEY `querysql_name_scopeid_uc` (`name`,`scope_id`),
  KEY `querysql_entitytype_fki` (`entity_type_id`),
  KEY `querysql_scope_fki` (`scope_id`),
  CONSTRAINT `querysql_entitytype_fk` FOREIGN KEY (`entity_type_id`) REFERENCES `entity_type` (`entity_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `querysql_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_sql`
--

LOCK TABLES `query_sql` WRITE;
/*!40000 ALTER TABLE `query_sql` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_sql` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER query_sql_before_insert   BEFORE INSERT  ON query_sql  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.query_sql_id,'I', vChangeDate, vChangeUser, 'query_sql','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.query_sql_id,'I', vChangeDate, vChangeUser, 'query_sql','description',null, NEW.description);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.query_sql_id,'I', vChangeDate, vChangeUser, 'query_sql','entity_type_id',null, NEW.entity_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.keywords ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.query_sql_id,'I', vChangeDate, vChangeUser, 'query_sql','keywords',null, NEW.keywords);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.query_sql_id,'I', vChangeDate, vChangeUser, 'query_sql','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.query_sql_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.query_sql_id,'I', vChangeDate, vChangeUser, 'query_sql','query_sql_id',null, NEW.query_sql_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.query_sql_id,'I', vChangeDate, vChangeUser, 'query_sql','scope_id',null, NEW.scope_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.sql_text ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.query_sql_id,'I', vChangeDate, vChangeUser, 'query_sql','sql_text',null, NEW.sql_text);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER query_sql_before_update   BEFORE UPDATE   ON query_sql  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'U', vChangeDate, vChangeUser, 'query_sql','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.description <=> NEW.description) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'U', vChangeDate, vChangeUser, 'query_sql','description',OLD.description, NEW.description);
	END IF;
	IF(NOT OLD.entity_type_id <=> NEW.entity_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'U', vChangeDate, vChangeUser, 'query_sql','entity_type_id',OLD.entity_type_id, NEW.entity_type_id);
	END IF;
	IF(NOT OLD.keywords <=> NEW.keywords) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'U', vChangeDate, vChangeUser, 'query_sql','keywords',OLD.keywords, NEW.keywords);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'U', vChangeDate, vChangeUser, 'query_sql','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.query_sql_id <=> NEW.query_sql_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'U', vChangeDate, vChangeUser, 'query_sql','query_sql_id',OLD.query_sql_id, NEW.query_sql_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'U', vChangeDate, vChangeUser, 'query_sql','scope_id',OLD.scope_id, NEW.scope_id);
	END IF;
	IF(NOT OLD.sql_text <=> NEW.sql_text) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'U', vChangeDate, vChangeUser, 'query_sql','sql_text',OLD.sql_text, NEW.sql_text);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER query_sql_before_delete   BEFORE DELETE   ON query_sql  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'D', vChangeDate, vChangeUser, 'query_sql','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'D', vChangeDate, vChangeUser, 'query_sql','description',OLD.description, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'D', vChangeDate, vChangeUser, 'query_sql','entity_type_id',OLD.entity_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.keywords ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'D', vChangeDate, vChangeUser, 'query_sql','keywords',OLD.keywords, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'D', vChangeDate, vChangeUser, 'query_sql','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.query_sql_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'D', vChangeDate, vChangeUser, 'query_sql','query_sql_id',OLD.query_sql_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'D', vChangeDate, vChangeUser, 'query_sql','scope_id',OLD.scope_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.sql_text ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.query_sql_id,'D', vChangeDate, vChangeUser, 'query_sql','sql_text',OLD.sql_text, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `code` varchar(50) NOT NULL COMMENT 'Unique identifier which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(100) NOT NULL COMMENT 'Unique name which is displayed to the user. This descriptive name can change and should not be be used as a key.',
  `category` varchar(100) DEFAULT NULL COMMENT 'Display only ... used to group related roles on edit pages',
  `short_name` varchar(50) DEFAULT NULL COMMENT 'A short name that can be used on the site when space is limited.  If not provided name will be displayed.',
  `description` varchar(1000) DEFAULT NULL COMMENT 'A longer description of the role which is displayed to the user as context sensitive help.',
  `display_order` smallint(6) DEFAULT NULL COMMENT 'Can be used to control the order the features are displayed on our web pages.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_code_scopeid_uc` (`code`,`scope_id`),
  UNIQUE KEY `role_name_scopeid_uc` (`name`,`scope_id`),
  KEY `role_scope_fki` (`scope_id`),
  CONSTRAINT `role_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,1,'system_admin','System Admin','Admin','','Super user role',1,null,null),(92,28,'parcc_admin','PARCC Admin','','PARCC Admin','',5,null,null),(93,28,'readiness_admin','Readiness Admin','','','',2,null,null),(94,28,'sbac_admin','SBAC Admin','','SBAC Admin','',4,null,null),(95,28,'state','State','','State','',6,null,null),(96,28,'district_admin','District Admin','','District Admin','',7,null,null),(97,28,'district','District','','District','',8,null,null),(98,28,'school','School','','School','',9,null,null),(99,28,'csc','CSC','','CSC','Customer Service Center',3,null,null),(100,28,'datamanagement','Data Management','','','',10,null,null),(101,28,'reports','Reports','','','',11,null,null);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_before_insert   BEFORE INSERT  ON role  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.category ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_id,'I', vChangeDate, vChangeUser, 'role','category',null, NEW.category);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_id,'I', vChangeDate, vChangeUser, 'role','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_id,'I', vChangeDate, vChangeUser, 'role','description',null, NEW.description);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_id,'I', vChangeDate, vChangeUser, 'role','display_order',null, NEW.display_order);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_id,'I', vChangeDate, vChangeUser, 'role','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.role_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_id,'I', vChangeDate, vChangeUser, 'role','role_id',null, NEW.role_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_id,'I', vChangeDate, vChangeUser, 'role','scope_id',null, NEW.scope_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.short_name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_id,'I', vChangeDate, vChangeUser, 'role','short_name',null, NEW.short_name);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_before_update   BEFORE UPDATE   ON role  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.category <=> NEW.category) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'U', vChangeDate, vChangeUser, 'role','category',OLD.category, NEW.category);
	END IF;
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'U', vChangeDate, vChangeUser, 'role','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.description <=> NEW.description) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'U', vChangeDate, vChangeUser, 'role','description',OLD.description, NEW.description);
	END IF;
	IF(NOT OLD.display_order <=> NEW.display_order) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'U', vChangeDate, vChangeUser, 'role','display_order',OLD.display_order, NEW.display_order);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'U', vChangeDate, vChangeUser, 'role','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.role_id <=> NEW.role_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'U', vChangeDate, vChangeUser, 'role','role_id',OLD.role_id, NEW.role_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'U', vChangeDate, vChangeUser, 'role','scope_id',OLD.scope_id, NEW.scope_id);
	END IF;
	IF(NOT OLD.short_name <=> NEW.short_name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'U', vChangeDate, vChangeUser, 'role','short_name',OLD.short_name, NEW.short_name);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_before_delete   BEFORE DELETE   ON role  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.category ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'D', vChangeDate, vChangeUser, 'role','category',OLD.category, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'D', vChangeDate, vChangeUser, 'role','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'D', vChangeDate, vChangeUser, 'role','description',OLD.description, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'D', vChangeDate, vChangeUser, 'role','display_order',OLD.display_order, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'D', vChangeDate, vChangeUser, 'role','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.role_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'D', vChangeDate, vChangeUser, 'role','role_id',OLD.role_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'D', vChangeDate, vChangeUser, 'role','scope_id',OLD.scope_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.short_name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_id,'D', vChangeDate, vChangeUser, 'role','short_name',OLD.short_name, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `role_delegation`
--

DROP TABLE IF EXISTS `role_delegation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_delegation` (
  `role_delegation_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) unsigned NOT NULL,
  `delegated_role_id` int(10) unsigned NOT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_delegation_id`),
  KEY `roledelegation_role_fki` (`role_id`),
  KEY `roledelegation_delegatedrole_fki` (`delegated_role_id`),
  CONSTRAINT `roledelegation_delegatedrole_fk` FOREIGN KEY (`delegated_role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `roledelegation_role_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_delegation`
--

LOCK TABLES `role_delegation` WRITE;
/*!40000 ALTER TABLE `role_delegation` DISABLE KEYS */;
INSERT INTO `role_delegation` VALUES (10,1,1,null,null),(16,94,94,null,null),(18,99,97,null,null),(20,99,98,null,null),(22,99,99,null,null),(24,99,94,null,null),(25,99,95,null,null),(27,99,96,null,null),(28,99,92,null,null),(29,92,92,null,null),(30,1,92,null,null),(31,1,93,null,null),(32,95,97,null,null),(33,95,98,null,null),(34,93,94,null,null),(35,93,93,null,null),(36,93,92,null,null),(37,1,98,null,null),(38,97,98,null,null),(39,1,99,null,null),(40,96,97,null,null),(41,1,96,null,null),(42,96,96,null,null),(43,1,97,null,null),(44,1,94,null,null),(45,1,95,null,null),(46,96,98,null,null),(47,93,99,null,null),(48,93,97,null,null),(49,93,98,null,null),(50,93,95,null,null),(51,93,96,null,null),(52,95,96,null,null),(53,95,95,null,null),(54,98,98,null,null),(93,97,97,null,null),(94,101,101,null,null),(95,100,100,null,null),(96,95,101,null,null),(97,93,100,null,null),(98,93,101,null,null),(99,95,100,null,null),(100,96,101,null,null),(101,96,100,null,null),(102,97,100,null,null),(103,97,101,null,null),(104,99,100,null,null),(105,99,101,null,null),(106,98,101,null,null),(107,98,100,null,null);
/*!40000 ALTER TABLE `role_delegation` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_delegation_before_insert   BEFORE INSERT  ON role_delegation  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.delegated_role_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_delegation_id,'I', vChangeDate, vChangeUser, 'role_delegation','delegated_role_id',null, NEW.delegated_role_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.role_delegation_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_delegation_id,'I', vChangeDate, vChangeUser, 'role_delegation','role_delegation_id',null, NEW.role_delegation_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.role_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_delegation_id,'I', vChangeDate, vChangeUser, 'role_delegation','role_id',null, NEW.role_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_delegation_before_update   BEFORE UPDATE   ON role_delegation  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.delegated_role_id <=> NEW.delegated_role_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_delegation_id,'U', vChangeDate, vChangeUser, 'role_delegation','delegated_role_id',OLD.delegated_role_id, NEW.delegated_role_id);
	END IF;
	IF(NOT OLD.role_delegation_id <=> NEW.role_delegation_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_delegation_id,'U', vChangeDate, vChangeUser, 'role_delegation','role_delegation_id',OLD.role_delegation_id, NEW.role_delegation_id);
	END IF;
	IF(NOT OLD.role_id <=> NEW.role_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_delegation_id,'U', vChangeDate, vChangeUser, 'role_delegation','role_id',OLD.role_id, NEW.role_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_delegation_before_delete   BEFORE DELETE   ON role_delegation  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.delegated_role_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_delegation_id,'D', vChangeDate, vChangeUser, 'role_delegation','delegated_role_id',OLD.delegated_role_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.role_delegation_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_delegation_id,'D', vChangeDate, vChangeUser, 'role_delegation','role_delegation_id',OLD.role_delegation_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.role_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_delegation_id,'D', vChangeDate, vChangeUser, 'role_delegation','role_id',OLD.role_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `role_permission_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) unsigned NOT NULL,
  `permission_id` int(10) unsigned NOT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_permission_id`),
  UNIQUE KEY `rolepermission_permissionid_roleid_uc` (`permission_id`,`role_id`),
  KEY `rolepermission_permission_fki` (`permission_id`),
  KEY `rolepermission_role_fki` (`role_id`),
  CONSTRAINT `rolepermission_permission_fk` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`permission_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `rolepermission_role_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1611 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (6,1,6,NULL,NULL),(7,1,11,NULL,NULL),(9,1,13,NULL,NULL),(29,1,35,NULL,NULL),(30,1,34,NULL,NULL),(37,1,41,NULL,NULL),(38,1,40,NULL,NULL),(39,1,39,NULL,NULL),(40,1,38,NULL,NULL),(41,1,37,NULL,NULL),(80,1,42,NULL,NULL),(81,1,43,NULL,NULL),(83,1,44,NULL,NULL),(624,92,6,NULL,NULL),(654,92,38,NULL,NULL),(657,92,37,NULL,NULL),(661,92,41,NULL,NULL),(694,1,36,NULL,NULL),(817,1,53,NULL,NULL),(902,1,56,NULL,NULL),(936,1,7,NULL,NULL),(937,1,57,NULL,NULL),(938,1,59,NULL,NULL),(939,1,58,NULL,NULL),(941,93,70,NULL,NULL),(942,93,71,NULL,NULL),(943,93,69,NULL,NULL),(944,93,42,NULL,NULL),(945,93,43,NULL,NULL),(947,95,41,NULL,NULL),(948,95,40,NULL,NULL),(949,93,34,NULL,NULL),(950,93,35,NULL,NULL),(951,93,36,NULL,NULL),(953,96,11,NULL,NULL),(954,96,36,NULL,NULL),(956,96,35,NULL,NULL),(958,97,70,NULL,NULL),(959,97,71,NULL,NULL),(960,97,69,NULL,NULL),(961,96,43,NULL,NULL),(963,97,65,NULL,NULL),(964,97,64,NULL,NULL),(965,96,42,NULL,NULL),(966,97,73,NULL,NULL),(967,97,72,NULL,NULL),(968,96,39,NULL,NULL),(969,96,38,NULL,NULL),(970,96,37,NULL,NULL),(973,93,41,NULL,NULL),(974,93,40,NULL,NULL),(975,96,40,NULL,NULL),(976,96,41,NULL,NULL),(978,95,65,NULL,NULL),(979,95,64,NULL,NULL),(982,95,73,NULL,NULL),(983,95,72,NULL,NULL),(984,93,39,NULL,NULL),(985,93,38,NULL,NULL),(987,93,37,NULL,NULL),(991,93,13,NULL,NULL),(997,95,69,NULL,NULL),(998,95,70,NULL,NULL),(999,93,6,NULL,NULL),(1000,95,71,NULL,NULL),(1002,98,6,NULL,NULL),(1004,93,44,NULL,NULL),(1006,97,11,NULL,NULL),(1007,98,11,NULL,NULL),(1009,98,73,NULL,NULL),(1010,98,72,NULL,NULL),(1011,95,11,NULL,NULL),(1015,98,65,NULL,NULL),(1016,98,64,NULL,NULL),(1027,95,42,NULL,NULL),(1028,95,43,NULL,NULL),(1029,95,35,NULL,NULL),(1030,95,36,NULL,NULL),(1035,96,72,NULL,NULL),(1036,96,73,NULL,NULL),(1038,96,64,NULL,NULL),(1039,96,65,NULL,NULL),(1040,93,53,NULL,NULL),(1041,96,6,NULL,NULL),(1042,97,6,NULL,NULL),(1044,95,6,NULL,NULL),(1045,98,69,NULL,NULL),(1046,98,70,NULL,NULL),(1047,98,71,NULL,NULL),(1048,96,70,NULL,NULL),(1049,96,69,NULL,NULL),(1050,96,71,NULL,NULL),(1052,1,65,NULL,NULL),(1053,1,66,NULL,NULL),(1054,1,63,NULL,NULL),(1055,1,64,NULL,NULL),(1056,1,73,NULL,NULL),(1057,1,62,NULL,NULL),(1058,1,72,NULL,NULL),(1059,1,71,NULL,NULL),(1060,1,70,NULL,NULL),(1061,1,69,NULL,NULL),(1067,93,66,NULL,NULL),(1068,95,39,NULL,NULL),(1069,93,11,NULL,NULL),(1070,95,37,NULL,NULL),(1071,95,38,NULL,NULL),(1078,92,77,NULL,NULL),(1080,92,75,NULL,NULL),(1081,92,68,NULL,NULL),(1082,1,78,NULL,NULL),(1083,1,74,NULL,NULL),(1084,1,75,NULL,NULL),(1085,1,76,NULL,NULL),(1086,1,77,NULL,NULL),(1087,1,68,NULL,NULL),(1088,98,58,NULL,NULL),(1089,93,74,NULL,NULL),(1090,93,77,NULL,NULL),(1091,98,7,NULL,NULL),(1094,95,58,NULL,NULL),(1096,93,59,NULL,NULL),(1097,93,58,NULL,NULL),(1100,95,59,NULL,NULL),(1104,93,60,NULL,NULL),(1105,95,60,NULL,NULL),(1107,96,7,NULL,NULL),(1108,97,7,NULL,NULL),(1109,95,7,NULL,NULL),(1110,95,53,NULL,NULL),(1111,94,77,NULL,NULL),(1113,93,7,NULL,NULL),(1114,94,34,NULL,NULL),(1128,92,53,NULL,NULL),(1129,94,11,NULL,NULL),(1139,92,69,NULL,NULL),(1140,94,69,NULL,NULL),(1153,93,73,NULL,NULL),(1154,93,64,NULL,NULL),(1156,93,72,NULL,NULL),(1157,93,65,NULL,NULL),(1159,96,53,NULL,NULL),(1168,93,68,NULL,NULL),(1169,93,78,NULL,NULL),(1170,93,76,NULL,NULL),(1171,93,62,NULL,NULL),(1172,93,63,NULL,NULL),(1173,93,75,NULL,NULL),(1174,99,7,NULL,NULL),(1175,94,6,NULL,NULL),(1176,97,53,NULL,NULL),(1177,94,37,NULL,NULL),(1178,94,38,NULL,NULL),(1179,94,41,NULL,NULL),(1180,94,40,NULL,NULL),(1183,99,34,NULL,NULL),(1186,99,53,NULL,NULL),(1187,99,65,NULL,NULL),(1188,99,69,NULL,NULL),(1192,99,11,NULL,NULL),(1193,92,40,NULL,NULL),(1195,99,57,NULL,NULL),(1196,94,68,NULL,NULL),(1198,94,75,NULL,NULL),(1199,93,57,NULL,NULL),(1202,96,59,NULL,NULL),(1203,99,77,NULL,NULL),(1204,99,75,NULL,NULL),(1207,96,58,NULL,NULL),(1211,96,60,NULL,NULL),(1212,94,53,NULL,NULL),(1213,92,34,NULL,NULL),(1214,92,11,NULL,NULL),(1215,94,78,NULL,NULL),(1216,92,78,NULL,NULL),(1217,99,78,NULL,NULL),(1220,93,79,NULL,NULL),(1221,94,7,NULL,NULL),(1223,1,83,NULL,NULL),(1224,1,81,NULL,NULL),(1225,1,82,NULL,NULL),(1226,1,80,NULL,NULL),(1231,93,82,NULL,NULL),(1232,93,83,NULL,NULL),(1233,93,80,NULL,NULL),(1234,93,81,NULL,NULL),(1239,99,6,NULL,NULL),(1240,99,42,NULL,NULL),(1241,99,43,NULL,NULL),(1242,99,36,NULL,NULL),(1243,99,35,NULL,NULL),(1244,99,41,NULL,NULL),(1245,99,40,NULL,NULL),(1246,99,38,NULL,NULL),(1247,99,39,NULL,NULL),(1248,99,37,NULL,NULL),(1261,98,79,NULL,NULL),(1262,1,79,NULL,NULL),(1264,96,79,NULL,NULL),(1272,99,79,NULL,NULL),(1274,95,79,NULL,NULL),(1286,93,56,NULL,NULL),(1306,98,62,NULL,NULL),(1307,98,63,NULL,NULL),(1308,98,57,NULL,NULL),(1309,97,57,NULL,NULL),(1313,96,62,NULL,NULL),(1314,96,63,NULL,NULL),(1315,96,57,NULL,NULL),(1318,99,64,NULL,NULL),(1319,99,63,NULL,NULL),(1321,99,62,NULL,NULL),(1322,99,68,NULL,NULL),(1323,95,57,NULL,NULL),(1324,95,63,NULL,NULL),(1325,95,62,NULL,NULL),(1327,99,71,NULL,NULL),(1328,99,72,NULL,NULL),(1329,99,70,NULL,NULL),(1330,99,73,NULL,NULL),(1331,94,39,NULL,NULL),(1332,92,39,NULL,NULL),(1333,97,79,NULL,NULL),(1335,99,58,NULL,NULL),(1336,99,59,NULL,NULL),(1337,99,60,NULL,NULL),(1338,1,89,NULL,NULL),(1339,93,89,NULL,NULL),(1340,94,89,NULL,NULL),(1342,92,89,NULL,NULL),(1343,99,89,NULL,NULL),(1344,96,89,NULL,NULL),(1345,95,89,NULL,NULL),(1347,97,62,NULL,NULL),(1348,97,63,NULL,NULL),(1350,97,58,NULL,NULL),(1358,98,53,NULL,NULL),(1370,97,75,NULL,NULL),(1372,97,77,NULL,NULL),(1373,97,78,NULL,NULL),(1392,95,75,NULL,NULL),(1395,96,77,NULL,NULL),(1396,96,75,NULL,NULL),(1399,95,77,NULL,NULL),(1400,96,78,NULL,NULL),(1401,95,78,NULL,NULL),(1404,1,60,NULL,NULL),(1411,92,7,NULL,NULL),(1412,98,78,NULL,NULL),(1413,98,77,NULL,NULL),(1415,98,75,NULL,NULL),(1416,1,90,NULL,NULL),(1417,94,90,NULL,NULL),(1418,92,90,NULL,NULL),(1419,93,90,NULL,NULL),(1420,95,90,NULL,NULL),(1421,99,90,NULL,NULL),(1422,96,90,NULL,NULL),(1423,94,91,NULL,NULL),(1424,1,91,NULL,NULL),(1425,92,91,NULL,NULL),(1426,93,91,NULL,NULL),(1427,95,91,NULL,NULL),(1429,99,91,NULL,NULL),(1430,98,94,NULL,NULL),(1433,93,92,NULL,NULL),(1434,93,94,NULL,NULL),(1435,93,93,NULL,NULL),(1437,95,94,NULL,NULL),(1439,97,94,NULL,NULL),(1442,1,93,NULL,NULL),(1443,1,92,NULL,NULL),(1444,1,94,NULL,NULL),(1447,96,94,NULL,NULL),(1448,94,94,NULL,NULL),(1454,99,94,NULL,NULL),(1455,92,94,NULL,NULL),(1457,94,83,NULL,NULL),(1458,94,82,NULL,NULL),(1459,94,81,NULL,NULL),(1460,94,80,NULL,NULL),(1461,99,80,NULL,NULL),(1462,99,81,NULL,NULL),(1463,99,82,NULL,NULL),(1464,99,83,NULL,NULL),(1465,92,80,NULL,NULL),(1466,92,83,NULL,NULL),(1467,92,82,NULL,NULL),(1468,92,81,NULL,NULL),(1473,1,95,NULL,NULL),(1478,98,95,NULL,NULL),(1479,93,95,NULL,NULL),(1480,95,95,NULL,NULL),(1481,97,95,NULL,NULL),(1482,96,95,NULL,NULL),(1483,94,95,NULL,NULL),(1484,92,95,NULL,NULL),(1485,99,95,NULL,NULL),(1488,100,7,NULL,NULL),(1490,100,64,NULL,NULL),(1491,100,63,NULL,NULL),(1493,100,65,NULL,NULL),(1494,100,62,NULL,NULL),(1495,100,57,NULL,NULL),(1497,100,69,NULL,NULL),(1498,100,70,NULL,NULL),(1499,100,71,NULL,NULL),(1500,100,72,NULL,NULL),(1501,100,73,NULL,NULL),(1503,100,75,NULL,NULL),(1505,100,77,NULL,NULL),(1506,100,79,NULL,NULL),(1507,100,78,NULL,NULL),(1508,100,58,NULL,NULL),(1511,100,94,NULL,NULL),(1512,100,95,NULL,NULL),(1514,101,75,NULL,NULL),(1515,101,78,NULL,NULL),(1516,101,77,NULL,NULL),(1520,101,94,NULL,NULL),(1521,101,95,NULL,NULL),(1523,98,97,NULL,NULL),(1524,98,96,NULL,NULL),(1525,93,96,NULL,NULL),(1526,93,97,NULL,NULL),(1527,95,97,NULL,NULL),(1528,95,96,NULL,NULL),(1529,100,96,NULL,NULL),(1530,100,97,NULL,NULL),(1531,97,97,NULL,NULL),(1532,97,96,NULL,NULL),(1533,1,97,NULL,NULL),(1534,1,96,NULL,NULL),(1535,96,97,NULL,NULL),(1536,96,96,NULL,NULL),(1537,94,97,NULL,NULL),(1538,94,96,NULL,NULL),(1539,99,97,NULL,NULL),(1540,99,96,NULL,NULL),(1541,92,96,NULL,NULL),(1542,92,97,NULL,NULL),(1543,98,93,NULL,NULL),(1544,98,92,NULL,NULL),(1545,98,66,NULL,NULL),(1546,98,76,NULL,NULL),(1547,98,74,NULL,NULL),(1548,96,66,NULL,NULL),(1549,96,76,NULL,NULL),(1550,96,74,NULL,NULL),(1551,95,66,NULL,NULL),(1553,101,76,NULL,NULL),(1554,101,66,NULL,NULL),(1555,95,93,NULL,NULL),(1556,95,92,NULL,NULL),(1557,101,93,NULL,NULL),(1558,101,92,NULL,NULL),(1559,95,76,NULL,NULL),(1560,95,74,NULL,NULL),(1561,100,66,NULL,NULL),(1562,100,74,NULL,NULL),(1563,100,76,NULL,NULL),(1564,97,66,NULL,NULL),(1565,97,74,NULL,NULL),(1566,100,92,NULL,NULL),(1567,100,93,NULL,NULL),(1568,97,76,NULL,NULL),(1569,97,93,NULL,NULL),(1570,97,92,NULL,NULL),(1571,99,66,NULL,NULL),(1572,94,66,NULL,NULL),(1573,96,93,NULL,NULL),(1574,96,92,NULL,NULL),(1575,94,76,NULL,NULL),(1576,94,74,NULL,NULL),(1577,94,93,NULL,NULL),(1578,94,92,NULL,NULL),(1579,92,66,NULL,NULL),(1580,92,74,NULL,NULL),(1581,92,76,NULL,NULL),(1582,99,76,NULL,NULL),(1583,99,74,NULL,NULL),(1584,92,93,NULL,NULL),(1585,99,93,NULL,NULL),(1586,92,92,NULL,NULL),(1587,99,92,NULL,NULL),(1599,1,100,NULL,NULL),(1600,100,100,NULL,NULL),(1601,101,100,NULL,NULL),(1602,98,100,NULL,NULL),(1603,99,100,NULL,NULL),(1604,96,100,NULL,NULL),(1605,97,100,NULL,NULL),(1606,93,100,NULL,NULL),(1607,92,100,NULL,NULL),(1608,95,100,NULL,NULL),(1609,94,100,NULL,NULL),(1610,101,74,NULL,NULL);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_permission_before_insert   BEFORE INSERT  ON role_permission  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.permission_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_permission_id,'I', vChangeDate, vChangeUser, 'role_permission','permission_id',null, NEW.permission_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.role_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_permission_id,'I', vChangeDate, vChangeUser, 'role_permission','role_id',null, NEW.role_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.role_permission_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.role_permission_id,'I', vChangeDate, vChangeUser, 'role_permission','role_permission_id',null, NEW.role_permission_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_permission_before_update   BEFORE UPDATE   ON role_permission  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.permission_id <=> NEW.permission_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_permission_id,'U', vChangeDate, vChangeUser, 'role_permission','permission_id',OLD.permission_id, NEW.permission_id);
	END IF;
	IF(NOT OLD.role_id <=> NEW.role_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_permission_id,'U', vChangeDate, vChangeUser, 'role_permission','role_id',OLD.role_id, NEW.role_id);
	END IF;
	IF(NOT OLD.role_permission_id <=> NEW.role_permission_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_permission_id,'U', vChangeDate, vChangeUser, 'role_permission','role_permission_id',OLD.role_permission_id, NEW.role_permission_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER role_permission_before_delete   BEFORE DELETE   ON role_permission  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.permission_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_permission_id,'D', vChangeDate, vChangeUser, 'role_permission','permission_id',OLD.permission_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.role_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_permission_id,'D', vChangeDate, vChangeUser, 'role_permission','role_id',OLD.role_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.role_permission_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.role_permission_id,'D', vChangeDate, vChangeUser, 'role_permission','role_permission_id',OLD.role_permission_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `scope`
--

DROP TABLE IF EXISTS `scope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scope` (
  `scope_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parent_scope_id` int(10) unsigned DEFAULT NULL,
  `scope_type_id` int(10) unsigned NOT NULL,
  `code` varchar(50) NOT NULL COMMENT 'Unique identifier (within parent scope) which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(100) NOT NULL COMMENT 'Unique identifier (within parent scope) which is displayed to the users.',
  `description` varchar(1000) DEFAULT NULL COMMENT 'Optional description of the scope.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`scope_id`),
  UNIQUE KEY `scope_code_parentscopeid_uc` (`code`,`parent_scope_id`),
  UNIQUE KEY `scope_name_parentscopeid_uc` (`name`,`parent_scope_id`),
  KEY `scope_scopetype_fki` (`scope_type_id`),
  KEY `scope_self_fki` (`parent_scope_id`),
  CONSTRAINT `scope_scopetype_fk` FOREIGN KEY (`scope_type_id`) REFERENCES `scope_type` (`scope_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `scope_self_fk` FOREIGN KEY (`parent_scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scope`
--

LOCK TABLES `scope` WRITE;
/*!40000 ALTER TABLE `scope` DISABLE KEYS */;
INSERT INTO `scope` VALUES (1,NULL,1,'root','Root','The root of the entire scope tree.',null,null),(28,1,10,'readiness','Readiness','Readiness',null,null),(65,28,11,'parcc','PARCC','Parcc scope',null,null),(66,28,11,'smart','Smarter Balanced','Smarter Balanced scope',null,null);
/*!40000 ALTER TABLE `scope` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_before_insert   BEFORE INSERT  ON scope  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_id,'I', vChangeDate, vChangeUser, 'scope','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_id,'I', vChangeDate, vChangeUser, 'scope','description',null, NEW.description);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_id,'I', vChangeDate, vChangeUser, 'scope','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.parent_scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_id,'I', vChangeDate, vChangeUser, 'scope','parent_scope_id',null, NEW.parent_scope_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_id,'I', vChangeDate, vChangeUser, 'scope','scope_id',null, NEW.scope_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_id,'I', vChangeDate, vChangeUser, 'scope','scope_type_id',null, NEW.scope_type_id);
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
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_after_insert AFTER INSERT ON scope FOR EACH ROW
BEGIN
  call rebuild_scope_tree(NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_before_update   BEFORE UPDATE   ON scope  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'U', vChangeDate, vChangeUser, 'scope','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.description <=> NEW.description) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'U', vChangeDate, vChangeUser, 'scope','description',OLD.description, NEW.description);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'U', vChangeDate, vChangeUser, 'scope','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.parent_scope_id <=> NEW.parent_scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'U', vChangeDate, vChangeUser, 'scope','parent_scope_id',OLD.parent_scope_id, NEW.parent_scope_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'U', vChangeDate, vChangeUser, 'scope','scope_id',OLD.scope_id, NEW.scope_id);
	END IF;
	IF(NOT OLD.scope_type_id <=> NEW.scope_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'U', vChangeDate, vChangeUser, 'scope','scope_type_id',OLD.scope_type_id, NEW.scope_type_id);
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
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_after_update AFTER UPDATE ON scope FOR EACH ROW
BEGIN
  IF IFNULL(NEW.code,'') != IFNULL(OLD.code,'')
     OR IFNULL(NEW.scope_id,'') != IFNULL(OLD.scope_id,'')
     OR IFNULL(NEW.parent_scope_id,'') != IFNULL(OLD.parent_scope_id,'') THEN
    CALL rebuild_scope_tree(NEW.scope_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_before_delete   BEFORE DELETE   ON scope  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'D', vChangeDate, vChangeUser, 'scope','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'D', vChangeDate, vChangeUser, 'scope','description',OLD.description, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'D', vChangeDate, vChangeUser, 'scope','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.parent_scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'D', vChangeDate, vChangeUser, 'scope','parent_scope_id',OLD.parent_scope_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'D', vChangeDate, vChangeUser, 'scope','scope_id',OLD.scope_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_id,'D', vChangeDate, vChangeUser, 'scope','scope_type_id',OLD.scope_type_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `scope_ext`
--

DROP TABLE IF EXISTS `scope_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scope_ext` (
  `scope_ext_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `entity_field_id` int(10) unsigned NOT NULL,
  `value` varchar(500) NOT NULL COMMENT 'Value for the "key" referenced by the entity_field_id.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`scope_ext_id`),
  UNIQUE KEY `scopeext_scopeid_entityfieldid_uc` (`scope_id`,`entity_field_id`),
  KEY `scopeext_value_entityfieldid_i` (`value`(10),`entity_field_id`),
  KEY `scopeext_entityfield_fki` (`entity_field_id`),
  KEY `scopeext_scope_fki` (`scope_id`),
  CONSTRAINT `scopeext_entityfield_fk` FOREIGN KEY (`entity_field_id`) REFERENCES `entity_field` (`entity_field_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `scopeext_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=548 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scope_ext`
--

LOCK TABLES `scope_ext` WRITE;
/*!40000 ALTER TABLE `scope_ext` DISABLE KEYS */;
INSERT INTO `scope_ext` VALUES (10,28,2264,'true',null,null),(31,28,2276,'true',null,null),(226,28,193,'3',null,null);
/*!40000 ALTER TABLE `scope_ext` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_ext_before_insert   BEFORE INSERT  ON scope_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.entity_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_ext_id,'I', vChangeDate, vChangeUser, 'scope_ext','entity_field_id',null, NEW.entity_field_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_ext_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_ext_id,'I', vChangeDate, vChangeUser, 'scope_ext','scope_ext_id',null, NEW.scope_ext_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_ext_id,'I', vChangeDate, vChangeUser, 'scope_ext','scope_id',null, NEW.scope_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.value ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_ext_id,'I', vChangeDate, vChangeUser, 'scope_ext','value',null, NEW.value);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_ext_before_update   BEFORE UPDATE   ON scope_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.entity_field_id <=> NEW.entity_field_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_ext_id,'U', vChangeDate, vChangeUser, 'scope_ext','entity_field_id',OLD.entity_field_id, NEW.entity_field_id);
	END IF;
	IF(NOT OLD.scope_ext_id <=> NEW.scope_ext_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_ext_id,'U', vChangeDate, vChangeUser, 'scope_ext','scope_ext_id',OLD.scope_ext_id, NEW.scope_ext_id);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_ext_id,'U', vChangeDate, vChangeUser, 'scope_ext','scope_id',OLD.scope_id, NEW.scope_id);
	END IF;
	IF(NOT OLD.value <=> NEW.value) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_ext_id,'U', vChangeDate, vChangeUser, 'scope_ext','value',OLD.value, NEW.value);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_ext_before_delete   BEFORE DELETE   ON scope_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.entity_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_ext_id,'D', vChangeDate, vChangeUser, 'scope_ext','entity_field_id',OLD.entity_field_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_ext_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_ext_id,'D', vChangeDate, vChangeUser, 'scope_ext','scope_ext_id',OLD.scope_ext_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_ext_id,'D', vChangeDate, vChangeUser, 'scope_ext','scope_id',OLD.scope_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.value ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_ext_id,'D', vChangeDate, vChangeUser, 'scope_ext','value',OLD.value, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `scope_tree`
--

DROP TABLE IF EXISTS `scope_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scope_tree` (
  `scope_tree_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ancestor_scope_id` int(10) unsigned NOT NULL COMMENT 'Identifies the scope which is the ancestor of the relation.',
  `ancestor_path` varchar(2000) NOT NULL COMMENT 'Provides a unique string representation of the ancestor scope similar to the path of a file.\nex "/va"',
  `ancestor_depth` smallint(6) NOT NULL COMMENT 'The depth of the the descendant scope within the scope hierarchy. Depth is zero based, so the root of the tree is zero.',
  `scope_id` int(10) unsigned NOT NULL COMMENT 'Identifies the scope which is the descendant of the relation.',
  `path` varchar(2000) NOT NULL COMMENT 'Provides a unique string representation of the descendant scope similar to the path of a file.  ex "/va/nwspg11"',
  `distance` smallint(6) NOT NULL COMMENT 'The number of levels in the tree between the ancestor and descendant scopes.  A level of "0" indicates that this is an association of the scope to itself.',
  `depth` smallint(6) NOT NULL COMMENT 'The depth of the the descendant scope within the scope hierarchy. Depth is zero based, so the root of the tree is zero.',
  PRIMARY KEY (`scope_tree_id`),
  UNIQUE KEY `scopetree_ancestorscopeid_scopeid_uc` (`ancestor_scope_id`,`scope_id`),
  KEY `scopetree_ancestorscope_fki` (`ancestor_scope_id`),
  KEY `scopetree_scope_fki` (`scope_id`),
  KEY `scopetree_ancestorpath_i` (`ancestor_path`(255)),
  KEY `scopetree_path_i` (`path`(255)),
  CONSTRAINT `scopetree_ancestorscope_fk` FOREIGN KEY (`ancestor_scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `scopetree_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2404 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scope_tree`
--

LOCK TABLES `scope_tree` WRITE;
/*!40000 ALTER TABLE `scope_tree` DISABLE KEYS */;
INSERT INTO `scope_tree` VALUES (2392,1,'/',0,1,'/',0,0),(2393,28,'/readiness',1,28,'/readiness',0,1),(2394,1,'/',0,28,'/readiness',1,1),(2395,65,'/readiness/parcc',2,65,'/readiness/parcc',0,2),(2396,28,'/readiness',1,65,'/readiness/parcc',1,2),(2397,1,'/',0,65,'/readiness/parcc',2,2),(2401,66,'/readiness/smart',2,66,'/readiness/smart',0,2),(2402,28,'/readiness',1,66,'/readiness/smart',1,2),(2403,1,'/',0,66,'/readiness/smart',2,2);
/*!40000 ALTER TABLE `scope_tree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scope_type`
--

DROP TABLE IF EXISTS `scope_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scope_type` (
  `scope_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parent_scope_type_id` int(10) unsigned DEFAULT NULL,
  `code` varchar(50) NOT NULL COMMENT 'Unique identifier (within a hierarchy type) which can be used by applications as a key to retrieve a specific row.',
  `name` varchar(100) NOT NULL COMMENT 'Unique name (within a hierarchy type) which is displayed to the user.  This descriptive name can change and should not be be used as a key.',
  `allow_org` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Are scopes of this type allowed to own organizations?',
  `allow_org_part` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Can organizations participate in scopes of this type?',
  `allow_user` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Can users be associated to scopes of this type?',
  `description` varchar(1000) DEFAULT NULL COMMENT 'Detailed description of the scope type to be displayed as context sensitive help to the user.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`scope_type_id`),
  UNIQUE KEY `scopetype_code_parentscopetypeid_uc` (`code`,`parent_scope_type_id`),
  UNIQUE KEY `scopetype_name_parentscopetypeid_uc` (`name`,`parent_scope_type_id`),
  KEY `scopetype_self_fki` (`parent_scope_type_id`),
  CONSTRAINT `scopetype_self_fk` FOREIGN KEY (`parent_scope_type_id`) REFERENCES `scope_type` (`scope_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scope_type`
--

LOCK TABLES `scope_type` WRITE;
/*!40000 ALTER TABLE `scope_type` DISABLE KEYS */;
INSERT INTO `scope_type` VALUES (1,NULL,'root','Root',0,0,1,'Type of the root(/) scope.',null,null),(10,1,'readiness','Readiness Check',1,0,1,NULL,null,null),(11,10,'admin','Consortia',0,1,0,NULL,null,null);
/*!40000 ALTER TABLE `scope_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_type_before_insert   BEFORE INSERT  ON scope_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.allow_org ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_type_id,'I', vChangeDate, vChangeUser, 'scope_type','allow_org',null, NEW.allow_org);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.allow_org_part ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_type_id,'I', vChangeDate, vChangeUser, 'scope_type','allow_org_part',null, NEW.allow_org_part);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.allow_user ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_type_id,'I', vChangeDate, vChangeUser, 'scope_type','allow_user',null, NEW.allow_user);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_type_id,'I', vChangeDate, vChangeUser, 'scope_type','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_type_id,'I', vChangeDate, vChangeUser, 'scope_type','description',null, NEW.description);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_type_id,'I', vChangeDate, vChangeUser, 'scope_type','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.parent_scope_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_type_id,'I', vChangeDate, vChangeUser, 'scope_type','parent_scope_type_id',null, NEW.parent_scope_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.scope_type_id,'I', vChangeDate, vChangeUser, 'scope_type','scope_type_id',null, NEW.scope_type_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_type_before_update   BEFORE UPDATE   ON scope_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;

	IF(NOT OLD.allow_org <=> NEW.allow_org) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'U', vChangeDate, vChangeUser, 'scope_type','allow_org',OLD.allow_org, NEW.allow_org);
	END IF;
	IF(NOT OLD.allow_org_part <=> NEW.allow_org_part) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'U', vChangeDate, vChangeUser, 'scope_type','allow_org_part',OLD.allow_org_part, NEW.allow_org_part);
	END IF;
	IF(NOT OLD.allow_user <=> NEW.allow_user) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'U', vChangeDate, vChangeUser, 'scope_type','allow_user',OLD.allow_user, NEW.allow_user);
	END IF;
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'U', vChangeDate, vChangeUser, 'scope_type','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.description <=> NEW.description) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'U', vChangeDate, vChangeUser, 'scope_type','description',OLD.description, NEW.description);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'U', vChangeDate, vChangeUser, 'scope_type','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.parent_scope_type_id <=> NEW.parent_scope_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'U', vChangeDate, vChangeUser, 'scope_type','parent_scope_type_id',OLD.parent_scope_type_id, NEW.parent_scope_type_id);
	END IF;
	IF(NOT OLD.scope_type_id <=> NEW.scope_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'U', vChangeDate, vChangeUser, 'scope_type','scope_type_id',OLD.scope_type_id, NEW.scope_type_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER scope_type_before_delete   BEFORE DELETE   ON scope_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

	IF CHAR_LENGTH(IFNULL(OLD.allow_org ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'D', vChangeDate, vChangeUser, 'scope_type','allow_org',OLD.allow_org, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.allow_org_part ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'D', vChangeDate, vChangeUser, 'scope_type','allow_org_part',OLD.allow_org_part, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.allow_user ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'D', vChangeDate, vChangeUser, 'scope_type','allow_user',OLD.allow_user, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'D', vChangeDate, vChangeUser, 'scope_type','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.description ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'D', vChangeDate, vChangeUser, 'scope_type','description',OLD.description, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'D', vChangeDate, vChangeUser, 'scope_type','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.parent_scope_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'D', vChangeDate, vChangeUser, 'scope_type','parent_scope_type_id',OLD.parent_scope_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.scope_type_id,'D', vChangeDate, vChangeUser, 'scope_type','scope_type_id',OLD.scope_type_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `temp_tree`
--

DROP TABLE IF EXISTS `temp_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temp_tree` (
  `root_id` int(10) unsigned NOT NULL,
  `descendant_id` int(10) unsigned NOT NULL,
  `distance` int(10) unsigned NOT NULL,
  `table_name` varchar(64) NOT NULL,
  PRIMARY KEY (`root_id`,`descendant_id`,`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temp_tree`
--

LOCK TABLES `temp_tree` WRITE;
/*!40000 ALTER TABLE `temp_tree` DISABLE KEYS */;
/*!40000 ALTER TABLE `temp_tree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `username` varchar(100) NOT NULL COMMENT 'The username (namespaced by the scope path) corresponds to the authenticated username used to access the application.',
  `last_name` varchar(50) DEFAULT NULL COMMENT 'Last name',
  `first_name` varchar(50) DEFAULT NULL COMMENT 'First name',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email addres',
  `active_begin_date` datetime DEFAULT NULL COMMENT 'If the current date is before the active_begin_date, the user is unauthorized to access the application.',
  `active_end_date` datetime DEFAULT NULL COMMENT 'If the current date is after the active_end_date, the user is unauthorized to access the application.',
  `disable_date` datetime DEFAULT NULL COMMENT 'If this date is non-null, the user is unauthorized to access the application.',
  `disable_reason` varchar(1000) DEFAULT NULL COMMENT 'A descriptive field which allows the admin to indicate why the user was disabled.',
  `delete_date` datetime DEFAULT NULL COMMENT 'If this date is non-null, the user is unauthorized to access the application.',
  `reset_token_1` varchar(100) DEFAULT NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)',
  `reset_token_2` varchar(100) DEFAULT NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)',
  `reset_token_3` varchar(100) DEFAULT NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)',
  `reset_token_4` varchar(100) DEFAULT NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)',
  `reset_token_5` varchar(100) DEFAULT NULL COMMENT 'The unique token sent to the user in emails to allow the user to reset their password (last 5 are saved)',
  `selected_scope_id` int(10) unsigned DEFAULT NULL COMMENT 'Scope that should be the default selected global scope for this user at login.',
  `password_history` varchar(2000) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_username_scopeid_uc` (`username`,`scope_id`),
  KEY `user_scope_fki` (`scope_id`),
  KEY `user_selectedscope_fki` (`selected_scope_id`),
  CONSTRAINT `user_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_selectedscope_fk` FOREIGN KEY (`selected_scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=55223 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (55222,28,'ready_admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,null,null);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_before_insert   BEFORE INSERT  ON user  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_before_update   BEFORE UPDATE   ON user  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.active_begin_date <=> NEW.active_begin_date   
	  AND OLD.active_end_date <=> NEW.active_end_date   
	  AND OLD.delete_date <=> NEW.delete_date   
	  AND OLD.disable_date <=> NEW.disable_date   
	  AND OLD.disable_reason <=> NEW.disable_reason   
	  AND OLD.email <=> NEW.email   
	  AND OLD.first_name <=> NEW.first_name   
	  AND OLD.last_name <=> NEW.last_name   
	  AND OLD.password_history <=> NEW.password_history   
	  AND OLD.reset_token_1 <=> NEW.reset_token_1   
	  AND OLD.reset_token_2 <=> NEW.reset_token_2   
	  AND OLD.reset_token_3 <=> NEW.reset_token_3   
	  AND OLD.reset_token_4 <=> NEW.reset_token_4   
	  AND OLD.reset_token_5 <=> NEW.reset_token_5   
	  AND OLD.scope_id <=> NEW.scope_id   
	  AND OLD.selected_scope_id <=> NEW.selected_scope_id   
	  AND OLD.username <=> NEW.username   
	  AND OLD.user_id <=> NEW.user_id   
    ) ) THEN
	  INSERT INTO user_hist (change_type,change_date,change_user,
	     active_begin_date
	     , active_end_date
	     , delete_date
	     , disable_date
	     , disable_reason
	     , email
	     , first_name
	     , last_name
	     , password_history
	     , reset_token_1
	     , reset_token_2
	     , reset_token_3
	     , reset_token_4
	     , reset_token_5
	     , scope_id
	     , selected_scope_id
	     , username
	     , user_id
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.active_begin_date
	     , OLD.active_end_date
	     , OLD.delete_date
	     , OLD.disable_date
	     , OLD.disable_reason
	     , OLD.email
	     , OLD.first_name
	     , OLD.last_name
	     , OLD.password_history
	     , OLD.reset_token_1
	     , OLD.reset_token_2
	     , OLD.reset_token_3
	     , OLD.reset_token_4
	     , OLD.reset_token_5
	     , OLD.scope_id
	     , OLD.selected_scope_id
	     , OLD.username
	     , OLD.user_id
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_before_delete   BEFORE DELETE   ON user  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO user_hist (change_type,change_date,change_user,
	     active_begin_date
	     , active_end_date
	     , delete_date
	     , disable_date
	     , disable_reason
	     , email
	     , first_name
	     , last_name
	     , password_history
	     , reset_token_1
	     , reset_token_2
	     , reset_token_3
	     , reset_token_4
	     , reset_token_5
	     , scope_id
	     , selected_scope_id
	     , username
	     , user_id
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.active_begin_date
	    , OLD.active_end_date
	    , OLD.delete_date
	    , OLD.disable_date
	    , OLD.disable_reason
	    , OLD.email
	    , OLD.first_name
	    , OLD.last_name
	    , OLD.password_history
	    , OLD.reset_token_1
	    , OLD.reset_token_2
	    , OLD.reset_token_3
	    , OLD.reset_token_4
	    , OLD.reset_token_5
	    , OLD.scope_id
	    , OLD.selected_scope_id
	    , OLD.username
	    , OLD.user_id
	);

    INSERT INTO user_hist (change_type,change_date,change_user,
	     active_begin_date
	     , active_end_date
	     , delete_date
	     , disable_date
	     , disable_reason
	     , email
	     , first_name
	     , last_name
	     , password_history
	     , reset_token_1
	     , reset_token_2
	     , reset_token_3
	     , reset_token_4
	     , reset_token_5
	     , scope_id
	     , selected_scope_id
	     , username
	     , user_id
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.active_begin_date
	    , OLD.active_end_date
	    , OLD.delete_date
	    , OLD.disable_date
	    , OLD.disable_reason
	    , OLD.email
	    , OLD.first_name
	    , OLD.last_name
	    , OLD.password_history
	    , OLD.reset_token_1
	    , OLD.reset_token_2
	    , OLD.reset_token_3
	    , OLD.reset_token_4
	    , OLD.reset_token_5
	    , OLD.scope_id
	    , OLD.selected_scope_id
	    , OLD.username
	    , OLD.user_id
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_ext`
--

DROP TABLE IF EXISTS `user_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_ext` (
  `user_ext_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `entity_field_id` int(10) unsigned NOT NULL,
  `value` varchar(500) NOT NULL COMMENT 'Value for the "key" referenced by the entity_field_id.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_ext_id`),
  UNIQUE KEY `userext_userid_entityfieldid_uc` (`user_id`,`entity_field_id`),
  KEY `userext_user_fki` (`user_id`),
  KEY `userext_entityfield_fki` (`entity_field_id`),
  KEY `userext_value_entityfieldid_i` (`value`(255),`entity_field_id`),
  CONSTRAINT `userext_entityfield_fk` FOREIGN KEY (`entity_field_id`) REFERENCES `entity_field` (`entity_field_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userext_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_ext`
--

LOCK TABLES `user_ext` WRITE;
/*!40000 ALTER TABLE `user_ext` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_ext` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_ext_before_insert   BEFORE INSERT  ON user_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_ext_before_update   BEFORE UPDATE   ON user_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.entity_field_id <=> NEW.entity_field_id   
	  AND OLD.user_ext_id <=> NEW.user_ext_id   
	  AND OLD.user_id <=> NEW.user_id   
	  AND OLD.value <=> NEW.value   
    ) ) THEN
	  INSERT INTO user_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , user_ext_id
	     , user_id
	     , value
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.entity_field_id
	     , OLD.user_ext_id
	     , OLD.user_id
	     , OLD.value
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_ext_before_delete   BEFORE DELETE   ON user_ext  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO user_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , user_ext_id
	     , user_id
	     , value
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.entity_field_id
	    , OLD.user_ext_id
	    , OLD.user_id
	    , OLD.value
	);

    INSERT INTO user_ext_hist (change_type,change_date,change_user,
	     entity_field_id
	     , user_ext_id
	     , user_id
	     , value
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.entity_field_id
	    , OLD.user_ext_id
	    , OLD.user_id
	    , OLD.value
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_ext_hist`
--

DROP TABLE IF EXISTS `user_ext_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_ext_hist` (
  `user_ext_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `user_ext_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `entity_field_id` int(10) unsigned DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_ext_hist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_ext_hist`
--

LOCK TABLES `user_ext_hist` WRITE;
/*!40000 ALTER TABLE `user_ext_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_ext_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_hist`
--

DROP TABLE IF EXISTS `user_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_hist` (
  `user_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `scope_id` int(10) unsigned DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `active_begin_date` datetime DEFAULT NULL,
  `active_end_date` datetime DEFAULT NULL,
  `disable_date` datetime DEFAULT NULL,
  `disable_reason` varchar(1000) DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `reset_token_1` varchar(100) DEFAULT NULL,
  `reset_token_2` varchar(100) DEFAULT NULL,
  `reset_token_3` varchar(100) DEFAULT NULL,
  `reset_token_4` varchar(100) DEFAULT NULL,
  `reset_token_5` varchar(100) DEFAULT NULL,
  `selected_scope_id` int(10) unsigned DEFAULT NULL,
  `password_history` varchar(2000) DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_hist_id`)
) ENGINE=InnoDB AUTO_INCREMENT=79324 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_hist`
--

LOCK TABLES `user_hist` WRITE;
/*!40000 ALTER TABLE `user_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_org`
--

DROP TABLE IF EXISTS `user_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_org` (
  `user_org_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `org_id` int(10) unsigned NOT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_org_id`),
  UNIQUE KEY `userorg_orgid_userid_uc` (`org_id`,`user_id`),
  KEY `userorg_user_fki` (`user_id`),
  KEY `userorg_org_fki` (`org_id`),
  CONSTRAINT `userorg_org_fk` FOREIGN KEY (`org_id`) REFERENCES `org` (`org_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userorg_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=75953 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_org`
--

LOCK TABLES `user_org` WRITE;
/*!40000 ALTER TABLE `user_org` DISABLE KEYS */;
INSERT INTO `user_org` VALUES (75952,55222,57950,null,null);
/*!40000 ALTER TABLE `user_org` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_org_before_insert   BEFORE INSERT  ON user_org  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_org_before_update   BEFORE UPDATE   ON user_org  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.org_id <=> NEW.org_id   
	  AND OLD.user_id <=> NEW.user_id   
	  AND OLD.user_org_id <=> NEW.user_org_id   
    ) ) THEN
	  INSERT INTO user_org_hist (change_type,change_date,change_user,
	     org_id
	     , user_id
	     , user_org_id
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.org_id
	     , OLD.user_id
	     , OLD.user_org_id
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_org_before_delete   BEFORE DELETE   ON user_org  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO user_org_hist (change_type,change_date,change_user,
	     org_id
	     , user_id
	     , user_org_id
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.org_id
	    , OLD.user_id
	    , OLD.user_org_id
	);

    INSERT INTO user_org_hist (change_type,change_date,change_user,
	     org_id
	     , user_id
	     , user_org_id
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.org_id
	    , OLD.user_id
	    , OLD.user_org_id
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_org_hist`
--

DROP TABLE IF EXISTS `user_org_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_org_hist` (
  `user_org_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `user_org_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `org_id` int(10) unsigned DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_org_hist_id`)
) ENGINE=InnoDB AUTO_INCREMENT=123891 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_org_hist`
--

LOCK TABLES `user_org_hist` WRITE;
/*!40000 ALTER TABLE `user_org_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_org_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_role_id`),
  UNIQUE KEY `userrole_roleid_userid_uc` (`role_id`,`user_id`),
  KEY `userrole_user_fki` (`user_id`),
  KEY `userrole_role_fki` (`role_id`),
  CONSTRAINT `userrole_role_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userrole_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=57589 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (57588,55222,93,null,null);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_role_before_insert   BEFORE INSERT  ON user_role  FOR EACH ROW 
BEGIN   
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_date = NOW();
	SET NEW.change_user = vChangeUser;
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_role_before_update   BEFORE UPDATE   ON user_role  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   
	
	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;

    IF( NOT( 1=1
	  AND OLD.role_id <=> NEW.role_id   
	  AND OLD.user_id <=> NEW.user_id   
	  AND OLD.user_role_id <=> NEW.user_role_id   
    ) ) THEN
	  INSERT INTO user_role_hist (change_type,change_date,change_user,
	     role_id
	     , user_id
	     , user_role_id
	  )    
	  VALUES ('U',OLD.change_date, OLD.change_user,  
	     OLD.role_id
	     , OLD.user_id
	     , OLD.user_role_id
	  );
	  SET NEW.change_date = vChangeDate;  
	  SET NEW.change_user = vChangeUser;  
	ELSE    
	  SET NEW.change_date = OLD.change_date;    
	  SET NEW.change_user = OLD.change_user;  
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER user_role_before_delete   BEFORE DELETE   ON user_role  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   

	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
    INSERT INTO user_role_hist (change_type,change_date,change_user,
	     role_id
	     , user_id
	     , user_role_id
	)    
	VALUES ('U',OLD.change_date, OLD.change_user,  
	    OLD.role_id
	    , OLD.user_id
	    , OLD.user_role_id
	);

    INSERT INTO user_role_hist (change_type,change_date,change_user,
	     role_id
	     , user_id
	     , user_role_id
	)    
	VALUES ('D',vChangeDate, vChangeUser,  
	    OLD.role_id
	    , OLD.user_id
	    , OLD.user_role_id
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_role_hist`
--

DROP TABLE IF EXISTS `user_role_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role_hist` (
  `user_role_hist_id` bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  `user_role_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `role_id` int(10) unsigned DEFAULT NULL,
  `change_type` char(1) DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_role_hist_id`)
) ENGINE=InnoDB AUTO_INCREMENT=85283 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role_hist`
--

LOCK TABLES `user_role_hist` WRITE;
/*!40000 ALTER TABLE `user_role_hist` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role_hist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `view_def`
--

DROP TABLE IF EXISTS `view_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `view_def` (
  `view_def_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `view_def_type_id` int(10) unsigned NOT NULL,
  `name` varchar(200) DEFAULT NULL COMMENT 'Optional name that can be provided to override the default (hard coded) view name displayed on a web page.',
  `collapsible` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Can the area on the screen displaying this view definition be collapsed.',
  `collapsed_by_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Should the area on the screen displaying this view definition be collapsed by default.',
  `column1_width` varchar(10) DEFAULT NULL COMMENT 'The width of the first column.',
  `column1_label_width` varchar(10) DEFAULT NULL COMMENT 'The width of the labels in the first column.',
  `column2_width` varchar(10) DEFAULT NULL COMMENT 'The width of the second column.',
  `column2_label_width` varchar(10) DEFAULT NULL COMMENT 'The width of the labels in the second column.',
  `column3_width` varchar(10) DEFAULT NULL COMMENT 'The width of the third column.',
  `column3_label_width` varchar(10) DEFAULT NULL COMMENT 'The width of the labels in the third column.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`view_def_id`),
  KEY `viewdef_scope_fki` (`scope_id`),
  KEY `viewdef_viewdeftype_fki` (`view_def_type_id`),
  CONSTRAINT `viewdef_scope_fk` FOREIGN KEY (`scope_id`) REFERENCES `scope` (`scope_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `viewdef_viewdeftype_fk` FOREIGN KEY (`view_def_type_id`) REFERENCES `view_def_type` (`view_def_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `view_def`
--

LOCK TABLES `view_def` WRITE;
/*!40000 ALTER TABLE `view_def` DISABLE KEYS */;
INSERT INTO `view_def` VALUES (21,1,18,NULL,0,0,'40em','20em','','','','',null,null),(160,28,13,NULL,0,0,'','','','','','',null,null),(183,28,24,NULL,0,0,'','','','','','',null,null),(204,28,27,NULL,0,0,'31em','21em','16em','1em','','1em',null,null),(205,28,28,NULL,0,0,'37em','17em','57em','45em','','',null,null),(206,28,21,NULL,0,0,'','','','','','',null,null),(207,28,14,NULL,0,0,'','','','','','',null,null),(208,28,20,NULL,0,0,'','','','','','',null,null),(210,28,23,NULL,0,0,'55em','13em','','','','',null,null),(214,28,22,NULL,0,0,'','','','','','',null,null),(215,28,19,NULL,0,0,'','','','','','',null,null),(216,28,30,NULL,0,0,'','','','','','',null,null),(218,28,34,NULL,0,0,'','','','','','',null,null),(219,28,32,NULL,0,0,'','','','','','',null,null),(220,28,35,NULL,0,0,'','','','','','',null,null),(223,28,36,NULL,0,0,'','320px','','','','',null,null);
/*!40000 ALTER TABLE `view_def` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_before_insert   BEFORE INSERT  ON view_def  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.collapsed_by_default ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','collapsed_by_default',null, NEW.collapsed_by_default);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.collapsible ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','collapsible',null, NEW.collapsible);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.column1_label_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','column1_label_width',null, NEW.column1_label_width);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.column1_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','column1_width',null, NEW.column1_width);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.column2_label_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','column2_label_width',null, NEW.column2_label_width);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.column2_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','column2_width',null, NEW.column2_width);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.column3_label_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','column3_label_width',null, NEW.column3_label_width);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.column3_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','column3_width',null, NEW.column3_width);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','scope_id',null, NEW.scope_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.view_def_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','view_def_id',null, NEW.view_def_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.view_def_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_id,'I', vChangeDate, vChangeUser, 'view_def','view_def_type_id',null, NEW.view_def_type_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_before_update   BEFORE UPDATE   ON view_def  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.collapsed_by_default <=> NEW.collapsed_by_default) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','collapsed_by_default',OLD.collapsed_by_default, NEW.collapsed_by_default);
	END IF;
	IF(NOT OLD.collapsible <=> NEW.collapsible) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','collapsible',OLD.collapsible, NEW.collapsible);
	END IF;
	IF(NOT OLD.column1_label_width <=> NEW.column1_label_width) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','column1_label_width',OLD.column1_label_width, NEW.column1_label_width);
	END IF;
	IF(NOT OLD.column1_width <=> NEW.column1_width) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','column1_width',OLD.column1_width, NEW.column1_width);
	END IF;
	IF(NOT OLD.column2_label_width <=> NEW.column2_label_width) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','column2_label_width',OLD.column2_label_width, NEW.column2_label_width);
	END IF;
	IF(NOT OLD.column2_width <=> NEW.column2_width) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','column2_width',OLD.column2_width, NEW.column2_width);
	END IF;
	IF(NOT OLD.column3_label_width <=> NEW.column3_label_width) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','column3_label_width',OLD.column3_label_width, NEW.column3_label_width);
	END IF;
	IF(NOT OLD.column3_width <=> NEW.column3_width) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','column3_width',OLD.column3_width, NEW.column3_width);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.scope_id <=> NEW.scope_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','scope_id',OLD.scope_id, NEW.scope_id);
	END IF;
	IF(NOT OLD.view_def_id <=> NEW.view_def_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','view_def_id',OLD.view_def_id, NEW.view_def_id);
	END IF;
	IF(NOT OLD.view_def_type_id <=> NEW.view_def_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'U', vChangeDate, vChangeUser, 'view_def','view_def_type_id',OLD.view_def_type_id, NEW.view_def_type_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_before_delete   BEFORE DELETE   ON view_def  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.collapsed_by_default ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','collapsed_by_default',OLD.collapsed_by_default, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.collapsible ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','collapsible',OLD.collapsible, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.column1_label_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','column1_label_width',OLD.column1_label_width, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.column1_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','column1_width',OLD.column1_width, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.column2_label_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','column2_label_width',OLD.column2_label_width, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.column2_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','column2_width',OLD.column2_width, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.column3_label_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','column3_label_width',OLD.column3_label_width, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.column3_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','column3_width',OLD.column3_width, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.scope_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','scope_id',OLD.scope_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.view_def_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','view_def_id',OLD.view_def_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.view_def_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_id,'D', vChangeDate, vChangeUser, 'view_def','view_def_type_id',OLD.view_def_type_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `view_def_field`
--

DROP TABLE IF EXISTS `view_def_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `view_def_field` (
  `view_def_field_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `view_def_id` int(10) unsigned NOT NULL,
  `entity_field_id` int(10) unsigned NOT NULL,
  `read_only` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'If "true" the field will be displayed as static text and the user will be unable to edit the field.',
  `display_entity_rule_id` int(10) unsigned DEFAULT NULL,
  `edit_entity_rule_id` int(10) unsigned DEFAULT NULL,
  `override_name` varchar(200) DEFAULT NULL COMMENT 'Can be used to display a different field label than the name defined on the entity.',
  `input_type` enum('dropdown','radio') DEFAULT NULL COMMENT 'For fields with option lists, determines which control should be displayed.',
  `label_position` enum('hidden','after') DEFAULT NULL COMMENT 'How should the label for this field be positioned (see enum for possible values)',
  `display_width` varchar(10) DEFAULT NULL COMMENT 'What should the CSS width for this input. (ex: 60px, 20em)',
  `label_style` varchar(300) DEFAULT NULL,
  `input_style` varchar(300) DEFAULT NULL,
  `column_number` int(11) DEFAULT NULL COMMENT 'Which column should this field be displayed.',
  `display_order` int(11) DEFAULT NULL COMMENT 'Controls the order of the fields displayed on the page.  Fields with lower numbers are displayed first.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`view_def_field_id`),
  KEY `viewdeffield_entityfield_fki` (`entity_field_id`),
  KEY `viewdeffield_viewdef_fki` (`view_def_id`),
  KEY `viewdeffield_displayentityrule_fki` (`display_entity_rule_id`),
  KEY `viewdeffield_editentityrule_fki` (`edit_entity_rule_id`),
  CONSTRAINT `viewdeffield_displayentityrule_fk` FOREIGN KEY (`display_entity_rule_id`) REFERENCES `entity_rule` (`entity_rule_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `viewdeffield_editentityrule_fk` FOREIGN KEY (`edit_entity_rule_id`) REFERENCES `entity_rule` (`entity_rule_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `viewdeffield_entityfield_fk` FOREIGN KEY (`entity_field_id`) REFERENCES `entity_field` (`entity_field_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `viewdeffield_viewdef_fk` FOREIGN KEY (`view_def_id`) REFERENCES `view_def` (`view_def_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3012 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `view_def_field`
--

LOCK TABLES `view_def_field` WRITE;
/*!40000 ALTER TABLE `view_def_field` DISABLE KEYS */;
INSERT INTO `view_def_field` VALUES (762,160,1275,0,NULL,NULL,'',NULL,NULL,'200px','','',1,1,NULL,NULL),(763,160,1276,0,NULL,NULL,'',NULL,NULL,'200px','','',1,2,NULL,NULL),(2282,183,2068,0,NULL,NULL,'',NULL,NULL,'','','',1,5,NULL,NULL),(2283,183,2067,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2284,183,2069,0,NULL,NULL,'Location or IP',NULL,NULL,'','','',2,8,NULL,NULL),(2288,183,2073,0,NULL,NULL,'',NULL,NULL,'','','',1,7,NULL,NULL),(2289,183,2238,0,NULL,NULL,'Device Count',NULL,NULL,'','','',1,1,NULL,NULL),(2290,183,2242,0,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2514,183,2243,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2517,183,2244,0,NULL,NULL,'',NULL,NULL,'','','',1,6,NULL,NULL),(2522,204,2253,0,NULL,NULL,'Screen Resolution',NULL,NULL,'','','',1,43,NULL,NULL),(2530,183,2258,0,NULL,NULL,'Display Size',NULL,NULL,'','','',2,11,NULL,NULL),(2531,183,2259,0,NULL,NULL,'',NULL,NULL,'','','',2,10,NULL,NULL),(2532,183,2260,0,NULL,NULL,'',NULL,NULL,'','','',2,9,NULL,NULL),(2542,204,2266,0,NULL,NULL,'Monitor Size',NULL,NULL,'','','',1,42,NULL,NULL),(2543,21,2264,0,NULL,NULL,'',NULL,NULL,'','','',1,8,NULL,NULL),(2544,21,2237,0,NULL,NULL,'',NULL,NULL,'','','',1,7,NULL,NULL),(2545,21,182,0,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2546,21,183,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2547,21,184,0,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2550,21,193,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2555,205,2263,0,NULL,NULL,'~Internal Network Bandwidth Utilization (%)',NULL,NULL,'','','',1,20,NULL,NULL),(2563,206,2520,0,NULL,NULL,'Contact Name',NULL,NULL,'20em','','',1,1,NULL,NULL),(2564,206,2524,0,NULL,NULL,'',NULL,NULL,'','','',2,5,NULL,NULL),(2565,206,2521,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2566,206,2525,0,NULL,NULL,'',NULL,NULL,'','','',2,6,NULL,NULL),(2567,206,2526,0,NULL,NULL,'',NULL,NULL,'','','',2,7,NULL,NULL),(2568,206,2522,0,NULL,NULL,'',NULL,NULL,'','','',2,8,NULL,NULL),(2569,206,2523,0,NULL,NULL,'',NULL,NULL,'','','',2,9,NULL,NULL),(2571,206,2528,0,NULL,NULL,'',NULL,NULL,'','','',2,10,NULL,NULL),(2572,206,2529,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2573,206,2530,0,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2574,206,2531,0,NULL,NULL,'',NULL,NULL,'','','',2,11,NULL,NULL),(2576,205,2262,0,NULL,NULL,'Wireless Access Points Count',NULL,NULL,'','','',1,21,NULL,NULL),(2590,21,2276,0,NULL,NULL,'',NULL,NULL,'','','',1,9,NULL,NULL),(2591,205,2277,0,NULL,NULL,'<b>1.</b> Having a sufficient <u>number of test administrators</u> to support online testing.',NULL,NULL,'','text-align:left;','',2,3,NULL,NULL),(2592,205,2278,0,NULL,NULL,'<b>4.</b> Having a sufficient <u>number of technology support staff</u> to support online testing.',NULL,NULL,'','text-align:left;','',2,7,NULL,NULL),(2597,205,2279,0,NULL,NULL,'<b>2.</b> Test administrators having <u>sufficient technical understanding</u> to support online testing.',NULL,NULL,'','text-align:left;','',2,4,NULL,NULL),(2599,205,2281,0,NULL,NULL,'<b>3.</b> Providing all <u>appropriate training needed</u> for test administrators.',NULL,NULL,'','text-align:left;','',2,5,NULL,NULL),(2600,205,2282,0,NULL,NULL,'<b>6.</b> Providing all <u>appropriate training needed</u> for technology support staff.',NULL,NULL,'','text-align:left;','',2,9,NULL,NULL),(2601,205,2283,0,NULL,NULL,'~Testing Window (# school days)',NULL,NULL,'','','',1,23,NULL,NULL),(2602,205,2284,0,NULL,NULL,'~Sessions per Day',NULL,NULL,'','','',1,24,NULL,NULL),(2603,205,2286,0,NULL,NULL,'~Internal Network Bandwidth',NULL,NULL,'','','',1,19,NULL,NULL),(2604,205,2285,0,NULL,NULL,'~Internet Bandwidth',NULL,NULL,'','','',1,17,NULL,NULL),(2657,205,2280,0,NULL,NULL,'<b>5.</b> Technology support staff having <u>sufficient technical understanding</u> to support online testing.',NULL,NULL,'','text-align:left;','',2,8,NULL,NULL),(2658,207,1275,0,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2659,207,1276,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2660,208,159,0,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2661,208,160,0,NULL,NULL,'',NULL,NULL,'15em','','',1,2,NULL,NULL),(2662,208,161,0,NULL,NULL,'',NULL,NULL,'18em','','',1,3,NULL,NULL),(2663,208,162,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2664,208,2016,0,NULL,NULL,'',NULL,NULL,'','','',1,5,NULL,NULL),(2665,208,2041,0,NULL,NULL,'',NULL,NULL,'','','',1,6,NULL,NULL),(2674,204,2414,0,NULL,NULL,'Windows XP SP3 or greater',NULL,NULL,'','','',1,2,NULL,NULL),(2675,204,2415,0,NULL,NULL,'Windows Vista',NULL,NULL,'','','',1,3,NULL,NULL),(2676,204,2416,0,NULL,NULL,'Windows 7',NULL,NULL,'','','',1,4,NULL,NULL),(2677,204,2417,0,NULL,NULL,'Windows 8',NULL,NULL,'','','',1,5,NULL,NULL),(2679,204,2418,0,NULL,NULL,'Windows - other',NULL,NULL,'','','',1,7,NULL,NULL),(2694,204,2419,0,NULL,NULL,'Linux - Mint',NULL,NULL,'','','',1,13,NULL,NULL),(2696,204,2421,0,NULL,NULL,'Linux - SUSE',NULL,NULL,'','','',1,20,NULL,NULL),(2697,204,2422,0,NULL,NULL,'Linux - other',NULL,NULL,'','','',1,21,NULL,NULL),(2700,204,2425,0,NULL,NULL,'Mac Other',NULL,NULL,'','','',1,29,NULL,NULL),(2701,204,2426,0,NULL,NULL,'iOS 4.x',NULL,NULL,'','','',1,30,NULL,NULL),(2702,204,2427,0,NULL,NULL,'iOS 5.x',NULL,NULL,'','','',1,31,NULL,NULL),(2703,204,2428,0,NULL,NULL,'iOS Other',NULL,NULL,'','','',1,36,NULL,NULL),(2704,204,2429,0,NULL,NULL,'Android 3.x Honeycomb',NULL,NULL,'','','',1,37,NULL,NULL),(2705,204,2430,0,NULL,NULL,'Android 4.x Ice Cream Sandwich',NULL,NULL,'','','',1,38,NULL,NULL),(2706,204,2431,0,NULL,NULL,'Android Other',NULL,NULL,'','','',1,39,NULL,NULL),(2707,204,2432,0,NULL,NULL,'Other OS',NULL,NULL,'','','',1,40,NULL,NULL),(2708,210,2238,0,NULL,NULL,'Device Count',NULL,NULL,'','','',1,10,NULL,NULL),(2709,210,2242,0,NULL,NULL,'~Operating System',NULL,NULL,'','','',1,4,NULL,NULL),(2710,210,2243,0,NULL,NULL,'Processor',NULL,NULL,'','','',1,5,NULL,NULL),(2711,210,2244,0,NULL,NULL,'~Screen Resolution',NULL,NULL,'','','',1,7,NULL,NULL),(2712,210,2258,0,NULL,NULL,'~Monitor/Display Size',NULL,NULL,'','','',1,15,NULL,NULL),(2713,210,2259,0,NULL,NULL,'',NULL,NULL,'','','',1,14,NULL,NULL),(2714,210,2260,0,NULL,NULL,'',NULL,NULL,'','','',1,11,NULL,NULL),(2715,210,2067,0,NULL,NULL,'',NULL,NULL,'20em','','',1,2,NULL,NULL),(2716,210,2069,0,NULL,NULL,'Location or IP',NULL,NULL,'','','',1,3,NULL,NULL),(2717,210,2068,0,NULL,NULL,'~Memory',NULL,NULL,'','','',1,6,NULL,NULL),(2718,210,2073,0,NULL,NULL,'',NULL,NULL,'','','',1,8,NULL,NULL),(2758,214,159,0,NULL,NULL,'',NULL,NULL,'','','',1,0,NULL,NULL),(2759,214,160,0,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2760,214,161,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2761,214,162,0,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2762,214,2016,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2763,214,2041,0,NULL,NULL,'',NULL,NULL,'','','',1,5,NULL,NULL),(2764,215,159,0,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2765,215,160,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2766,215,161,0,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2767,215,162,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2768,215,2016,0,NULL,NULL,'',NULL,NULL,'','','',1,5,NULL,NULL),(2769,215,2041,0,NULL,NULL,'',NULL,NULL,'','','',1,6,NULL,NULL),(2770,216,159,0,NULL,NULL,'',NULL,NULL,'',NULL,NULL,1,2,NULL,NULL),(2771,216,160,0,NULL,NULL,'',NULL,NULL,'',NULL,NULL,1,3,NULL,NULL),(2772,216,161,0,NULL,NULL,'',NULL,NULL,'',NULL,NULL,1,4,NULL,NULL),(2773,216,162,0,NULL,NULL,'',NULL,NULL,'',NULL,NULL,1,1,NULL,NULL),(2774,216,2016,0,NULL,NULL,'',NULL,NULL,'',NULL,NULL,1,5,NULL,NULL),(2775,216,2041,0,NULL,NULL,'',NULL,NULL,'',NULL,NULL,1,6,NULL,NULL),(2776,218,2446,0,NULL,NULL,'',NULL,NULL,'','','',1,0,NULL,NULL),(2777,218,2447,0,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2778,218,2448,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2779,218,2449,0,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2780,219,2446,0,NULL,NULL,'',NULL,NULL,'','','',1,0,NULL,NULL),(2782,205,2453,0,NULL,NULL,'~Internet Bandwidth Utilization (%)',NULL,NULL,'','','',1,18,NULL,NULL),(2784,204,2464,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,2,NULL,NULL),(2785,204,2465,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,3,NULL,NULL),(2786,204,2466,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,4,NULL,NULL),(2787,204,2467,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,5,NULL,NULL),(2788,204,2468,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,7,NULL,NULL),(2789,204,2469,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,13,NULL,NULL),(2791,204,2471,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,20,NULL,NULL),(2792,204,2472,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,21,NULL,NULL),(2795,204,2475,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,29,NULL,NULL),(2796,204,2476,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,30,NULL,NULL),(2797,204,2477,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,31,NULL,NULL),(2798,204,2478,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,36,NULL,NULL),(2799,204,2479,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,37,NULL,NULL),(2800,204,2480,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,38,NULL,NULL),(2801,204,2481,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,39,NULL,NULL),(2802,204,2482,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,40,NULL,NULL),(2824,204,2503,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,43,NULL,NULL),(2826,204,2507,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,42,NULL,NULL),(2837,160,2454,0,NULL,NULL,'',NULL,NULL,'200px','','',1,5,NULL,NULL),(2838,160,2455,0,NULL,NULL,'',NULL,NULL,'200px','','',1,6,NULL,NULL),(2839,160,2456,0,NULL,NULL,'',NULL,NULL,'200px','','',1,7,NULL,NULL),(2840,160,2457,0,NULL,NULL,'',NULL,NULL,'','','',1,8,NULL,NULL),(2841,160,2458,0,NULL,NULL,'',NULL,NULL,'','','',1,9,NULL,NULL),(2842,160,2459,0,NULL,NULL,'',NULL,NULL,'','','',1,10,NULL,NULL),(2843,160,2460,0,NULL,NULL,'',NULL,NULL,'','','',1,11,NULL,NULL),(2844,160,2461,0,NULL,NULL,'',NULL,NULL,'','','',1,12,NULL,NULL),(2845,160,2462,0,NULL,NULL,'',NULL,NULL,'200px','','',1,3,NULL,NULL),(2846,207,2454,0,NULL,NULL,'',NULL,NULL,'','','',2,4,NULL,NULL),(2847,207,2455,0,NULL,NULL,'',NULL,NULL,'','','',2,5,NULL,NULL),(2848,207,2456,0,NULL,NULL,'',NULL,NULL,'','','',2,6,NULL,NULL),(2849,207,2457,0,NULL,NULL,'',NULL,NULL,'','','',2,7,NULL,NULL),(2850,207,2458,0,NULL,NULL,'',NULL,NULL,'','','',2,8,NULL,NULL),(2851,207,2459,0,NULL,NULL,'',NULL,NULL,'','','',2,9,NULL,NULL),(2852,207,2460,0,NULL,NULL,'',NULL,NULL,'','','',2,10,NULL,NULL),(2853,207,2461,0,NULL,NULL,'',NULL,NULL,'','','',2,11,NULL,NULL),(2854,207,2462,0,NULL,NULL,'',NULL,NULL,'','','',2,3,NULL,NULL),(2855,220,2518,0,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2856,220,2519,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2857,160,2532,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2858,219,2447,0,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2859,219,2448,0,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2860,219,2449,1,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2861,219,2447,1,NULL,NULL,'',NULL,NULL,'','','',1,1,NULL,NULL),(2862,219,2448,1,NULL,NULL,'',NULL,NULL,'','','',1,2,NULL,NULL),(2863,219,2449,1,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2864,204,2533,0,NULL,NULL,'Google Chrome V18 or less',NULL,NULL,'','','',1,34,NULL,NULL),(2865,204,2534,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,34,NULL,NULL),(2889,215,2567,1,NULL,NULL,'',NULL,NULL,'','','',1,9,NULL,NULL),(2890,215,2518,1,NULL,NULL,'',NULL,NULL,'','','',1,7,NULL,NULL),(2891,215,2519,1,NULL,NULL,'',NULL,NULL,'','','',1,8,NULL,NULL),(2892,208,2567,0,NULL,NULL,'',NULL,NULL,'','','',1,9,NULL,NULL),(2893,208,2518,0,NULL,NULL,'',NULL,NULL,'','','',1,7,NULL,NULL),(2894,208,2519,0,NULL,NULL,'',NULL,NULL,'','','',1,8,NULL,NULL),(2895,214,2567,1,NULL,NULL,'',NULL,NULL,'','','',1,8,NULL,NULL),(2896,214,2518,1,NULL,NULL,'',NULL,NULL,'','','',1,6,NULL,NULL),(2897,214,2519,1,NULL,NULL,'',NULL,NULL,'','','',1,7,NULL,NULL),(2899,219,2571,0,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2900,218,2571,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2905,204,2576,0,NULL,NULL,'Windows XP SP2 or less',NULL,NULL,'','','',1,1,NULL,NULL),(2906,204,2577,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,1,NULL,NULL),(2908,204,2579,0,NULL,NULL,'Windows RT',NULL,NULL,'','','',1,6,NULL,NULL),(2909,204,2580,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,6,NULL,NULL),(2911,204,2582,0,NULL,NULL,'Mac OS X 10.8',NULL,NULL,'','','',1,27,NULL,NULL),(2912,204,2583,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,27,NULL,NULL),(2914,204,2585,0,NULL,NULL,'iOS 6.x',NULL,NULL,'','','',1,32,NULL,NULL),(2915,204,2586,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,32,NULL,NULL),(2916,210,2587,0,NULL,NULL,'~Assessment Environment',NULL,NULL,'','','',1,12,NULL,NULL),(2917,210,2588,0,NULL,NULL,'',NULL,NULL,'','','',1,13,NULL,NULL),(2918,183,2587,0,NULL,NULL,'',NULL,NULL,'','','',2,13,NULL,NULL),(2919,183,2588,0,NULL,NULL,'',NULL,NULL,'','','',2,12,NULL,NULL),(2920,204,2590,0,NULL,NULL,'K',NULL,NULL,'100px','','',1,47,NULL,NULL),(2921,204,2591,0,NULL,NULL,'1',NULL,NULL,'100px','','',1,48,NULL,NULL),(2922,204,2592,0,NULL,NULL,'2',NULL,NULL,'100px','','',1,49,NULL,NULL),(2923,204,2593,0,NULL,NULL,'3',NULL,NULL,'100px','','',1,50,NULL,NULL),(2924,204,2594,0,NULL,NULL,'4',NULL,NULL,'100px','','',1,51,NULL,NULL),(2925,204,2595,0,NULL,NULL,'5',NULL,NULL,'100px','','',1,52,NULL,NULL),(2926,204,2596,0,NULL,NULL,'6',NULL,NULL,'100px','','',1,53,NULL,NULL),(2927,204,2597,0,NULL,NULL,'7',NULL,NULL,'100px','','',2,47,NULL,NULL),(2928,204,2598,0,NULL,NULL,'8',NULL,NULL,'100px','','',2,48,NULL,NULL),(2929,204,2599,0,NULL,NULL,'9',NULL,NULL,'100px','','',2,49,NULL,NULL),(2930,204,2600,0,NULL,NULL,'10',NULL,NULL,'100px','','',2,50,NULL,NULL),(2931,204,2601,0,NULL,NULL,'11',NULL,NULL,'100px','','',2,51,NULL,NULL),(2932,204,2602,0,NULL,NULL,'12',NULL,NULL,'100px','','',2,52,NULL,NULL),(2933,205,2589,0,NULL,NULL,'Classification of School',NULL,NULL,'','','',1,1,NULL,NULL),(2934,205,2603,0,NULL,NULL,'',NULL,NULL,'','','',1,3,NULL,NULL),(2935,205,2604,0,NULL,NULL,'',NULL,NULL,'','','',1,4,NULL,NULL),(2936,205,2605,0,NULL,NULL,'',NULL,NULL,'','','',1,5,NULL,NULL),(2937,205,2606,0,NULL,NULL,'',NULL,NULL,'','','',1,6,NULL,NULL),(2938,205,2607,0,NULL,NULL,'',NULL,NULL,'','','',1,7,NULL,NULL),(2939,205,2608,0,NULL,NULL,'',NULL,NULL,'','','',1,8,NULL,NULL),(2940,205,2609,0,NULL,NULL,'',NULL,NULL,'','','',1,9,NULL,NULL),(2941,205,2610,0,NULL,NULL,'',NULL,NULL,'','','',1,10,NULL,NULL),(2942,205,2611,0,NULL,NULL,'',NULL,NULL,'','','',1,11,NULL,NULL),(2943,205,2612,0,NULL,NULL,'',NULL,NULL,'','','',1,12,NULL,NULL),(2944,205,2613,0,NULL,NULL,'',NULL,NULL,'','','',1,13,NULL,NULL),(2945,205,2614,0,NULL,NULL,'',NULL,NULL,'','','',1,14,NULL,NULL),(2946,205,2615,0,NULL,NULL,'',NULL,NULL,'','','',1,15,NULL,NULL),(2948,223,2433,0,NULL,NULL,'Have you completed and verified all data entry for Devices, Testing Network Information and Survey Questions?',NULL,NULL,'','','',1,1,NULL,NULL),(2950,204,2617,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,44,NULL,NULL),(2954,204,2621,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,45,NULL,NULL),(2955,204,2616,0,NULL,NULL,'Max Testing Window Length(days)',NULL,NULL,'','','',1,44,NULL,NULL),(2957,204,2620,0,NULL,NULL,'Throughput Required per Student(Kbps)',NULL,NULL,'','','',1,45,NULL,NULL),(2958,204,2622,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,17,NULL,NULL),(2959,204,2637,0,NULL,NULL,'Linux - Fedora v1 - 5',NULL,NULL,'','','',1,17,NULL,NULL),(2975,204,2627,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,8,NULL,NULL),(2976,204,2628,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,9,NULL,NULL),(2977,204,2629,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,10,NULL,NULL),(2978,204,2642,0,NULL,NULL,'Windows Server 2003',NULL,NULL,'','','',1,8,NULL,NULL),(2979,204,2643,0,NULL,NULL,'Windows Server 2008',NULL,NULL,'','','',1,9,NULL,NULL),(2980,204,2644,0,NULL,NULL,'Windows Server 2012',NULL,NULL,'','','',1,10,NULL,NULL),(2982,204,2623,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,15,NULL,NULL),(2983,204,2630,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,14,NULL,NULL),(2984,204,2631,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,16,NULL,NULL),(2985,204,2638,0,NULL,NULL,'Linux Ubuntu v9 - 11.04',NULL,NULL,'','','',1,15,NULL,NULL),(2986,204,2645,0,NULL,NULL,'Linux Ubuntu v4 - 8',NULL,NULL,'','','',1,14,NULL,NULL),(2987,204,2646,0,NULL,NULL,'Linux Ubuntu v11.10 or greater',NULL,NULL,'','','',1,16,NULL,NULL),(2988,204,2624,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,22,NULL,NULL),(2989,204,2625,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,25,NULL,NULL),(2990,204,2626,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,35,NULL,NULL),(2991,204,2632,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,18,NULL,NULL),(2992,204,2633,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,19,NULL,NULL),(2993,204,2634,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,23,NULL,NULL),(2994,204,2635,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,24,NULL,NULL),(2995,204,2636,0,NULL,NULL,'&nbsp',NULL,NULL,'','','',2,26,NULL,NULL),(2996,204,2639,0,NULL,NULL,'Mac OS X 10.1 - 10.4.3',NULL,NULL,'','','',1,22,NULL,NULL),(2997,204,2640,0,NULL,NULL,'Mac OS X 10.6',NULL,NULL,'','','',1,25,NULL,NULL),(2998,204,2641,0,NULL,NULL,'Google Chrome V19 or greater',NULL,NULL,'','','',1,35,NULL,NULL),(2999,204,2647,0,NULL,NULL,'Linux Fedora v6 thru 15',NULL,NULL,'','','',1,18,NULL,NULL),(3000,204,2648,0,NULL,NULL,'Linux Fedora v16 or greater',NULL,NULL,'','','',1,19,NULL,NULL),(3001,204,2649,0,NULL,NULL,'Mac OS X 10.4.4',NULL,NULL,'','','',1,23,NULL,NULL),(3002,204,2650,0,NULL,NULL,'Mac OS X 10.5',NULL,NULL,'','','',1,24,NULL,NULL),(3003,204,2651,0,NULL,NULL,'Mac OS X 10.7',NULL,NULL,'','','',1,26,NULL,NULL),(3004,204,2687,0,NULL,NULL,'&nbsp;',NULL,NULL,'','','',2,12,NULL,NULL),(3005,204,2688,0,NULL,NULL,'Windows MultiPoint Server 2012',NULL,NULL,'','','',1,12,NULL,NULL),(3006,204,2690,0,NULL,NULL,'Windows MultiPoint Server 2010-2011',NULL,NULL,'','','',1,11,NULL,NULL),(3007,204,2691,0,NULL,NULL,'&nbsp;',NULL,NULL,'','','',2,11,NULL,NULL),(3008,204,2692,0,NULL,NULL,'&nbsp;',NULL,NULL,'','','',2,33,NULL,NULL),(3009,204,2693,0,NULL,NULL,'iOS 7.x',NULL,NULL,'','','',1,33,NULL,NULL),(3010,204,2694,0,NULL,NULL,'Mac OS X 10.9',NULL,NULL,'','','',1,28,NULL,NULL),(3011,204,2695,0,NULL,NULL,'&nbsp;',NULL,NULL,'','','',2,28,NULL,NULL);
/*!40000 ALTER TABLE `view_def_field` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_field_before_insert   BEFORE INSERT  ON view_def_field  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.column_number ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','column_number',null, NEW.column_number);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_entity_rule_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','display_entity_rule_id',null, NEW.display_entity_rule_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','display_order',null, NEW.display_order);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','display_width',null, NEW.display_width);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.edit_entity_rule_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','edit_entity_rule_id',null, NEW.edit_entity_rule_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','entity_field_id',null, NEW.entity_field_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.input_style ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','input_style',null, NEW.input_style);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.input_type ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','input_type',null, NEW.input_type);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.label_position ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','label_position',null, NEW.label_position);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.label_style ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','label_style',null, NEW.label_style);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.override_name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','override_name',null, NEW.override_name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.read_only ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','read_only',null, NEW.read_only);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.view_def_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','view_def_field_id',null, NEW.view_def_field_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.view_def_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_field_id,'I', vChangeDate, vChangeUser, 'view_def_field','view_def_id',null, NEW.view_def_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_field_before_update   BEFORE UPDATE   ON view_def_field  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.column_number <=> NEW.column_number) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','column_number',OLD.column_number, NEW.column_number);
	END IF;
	IF(NOT OLD.display_entity_rule_id <=> NEW.display_entity_rule_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','display_entity_rule_id',OLD.display_entity_rule_id, NEW.display_entity_rule_id);
	END IF;
	IF(NOT OLD.display_order <=> NEW.display_order) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','display_order',OLD.display_order, NEW.display_order);
	END IF;
	IF(NOT OLD.display_width <=> NEW.display_width) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','display_width',OLD.display_width, NEW.display_width);
	END IF;
	IF(NOT OLD.edit_entity_rule_id <=> NEW.edit_entity_rule_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','edit_entity_rule_id',OLD.edit_entity_rule_id, NEW.edit_entity_rule_id);
	END IF;
	IF(NOT OLD.entity_field_id <=> NEW.entity_field_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','entity_field_id',OLD.entity_field_id, NEW.entity_field_id);
	END IF;
	IF(NOT OLD.input_style <=> NEW.input_style) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','input_style',OLD.input_style, NEW.input_style);
	END IF;
	IF(NOT OLD.input_type <=> NEW.input_type) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','input_type',OLD.input_type, NEW.input_type);
	END IF;
	IF(NOT OLD.label_position <=> NEW.label_position) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','label_position',OLD.label_position, NEW.label_position);
	END IF;
	IF(NOT OLD.label_style <=> NEW.label_style) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','label_style',OLD.label_style, NEW.label_style);
	END IF;
	IF(NOT OLD.override_name <=> NEW.override_name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','override_name',OLD.override_name, NEW.override_name);
	END IF;
	IF(NOT OLD.read_only <=> NEW.read_only) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','read_only',OLD.read_only, NEW.read_only);
	END IF;
	IF(NOT OLD.view_def_field_id <=> NEW.view_def_field_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','view_def_field_id',OLD.view_def_field_id, NEW.view_def_field_id);
	END IF;
	IF(NOT OLD.view_def_id <=> NEW.view_def_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'U', vChangeDate, vChangeUser, 'view_def_field','view_def_id',OLD.view_def_id, NEW.view_def_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_field_before_delete   BEFORE DELETE   ON view_def_field  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.column_number ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','column_number',OLD.column_number, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_entity_rule_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','display_entity_rule_id',OLD.display_entity_rule_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','display_order',OLD.display_order, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_width ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','display_width',OLD.display_width, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.edit_entity_rule_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','edit_entity_rule_id',OLD.edit_entity_rule_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','entity_field_id',OLD.entity_field_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.input_style ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','input_style',OLD.input_style, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.input_type ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','input_type',OLD.input_type, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.label_position ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','label_position',OLD.label_position, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.label_style ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','label_style',OLD.label_style, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.override_name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','override_name',OLD.override_name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.read_only ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','read_only',OLD.read_only, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.view_def_field_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','view_def_field_id',OLD.view_def_field_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.view_def_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_field_id,'D', vChangeDate, vChangeUser, 'view_def_field','view_def_id',OLD.view_def_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `view_def_text`
--

DROP TABLE IF EXISTS `view_def_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `view_def_text` (
  `view_def_text_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `view_def_id` int(10) unsigned NOT NULL,
  `text` varchar(5000) NOT NULL COMMENT 'The text (HTML is supported) to be displayed to the user.',
  `column_number` int(11) DEFAULT NULL COMMENT 'Which column should this text be displayed.',
  `display_order` int(11) DEFAULT NULL COMMENT 'The order in which this text should be displayed ... used in conjunction with view_def_field.display_order.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`view_def_text_id`),
  KEY `viewdeftext_viewdef_fki` (`view_def_id`),
  CONSTRAINT `viewdeftext_viewdef_fk` FOREIGN KEY (`view_def_id`) REFERENCES `view_def` (`view_def_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `view_def_text`
--

LOCK TABLES `view_def_text` WRITE;
/*!40000 ALTER TABLE `view_def_text` DISABLE KEYS */;
INSERT INTO `view_def_text` VALUES (31,205,'																																																																																																																																																																																<P><SPAN style=\"FONT-SIZE: large\"><STRONG><BR></STRONG></SPAN></P>\r\n<P><SPAN style=\"FONT-SIZE: large\"><STRONG><BR></STRONG></SPAN></P>\r\n<P><STRONG style=\"FONT-SIZE: large\">Network Information (Estimated)<BR></STRONG></P>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,16,null,null),(33,205,'																																																																																																																																																																																																																																																																																																																																																																																								<P><SPAN style=\"FONT-SIZE: large\"><STRONG><BR></STRONG></SPAN></P>\r\n<P><SPAN style=\"FONT-SIZE: large\"><STRONG><BR></STRONG></SPAN></P>\r\n<P><SPAN style=\"FONT-SIZE: large\"><STRONG>Testing Information</STRONG></SPAN></P>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,22,null,null),(34,205,'																																																																																																				<P><SPAN style=\"FONT-SIZE: large\"><STRONG>Staff / Personnel&nbsp;</STRONG><STRONG>Issues &amp; Roadblocks</STRONG></SPAN></P>\r\n<P><SPAN style=\"FONT-SIZE: small\"><STRONG><BR></STRONG></SPAN></P>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',2,0,null,null),(50,204,'																												<p style=\"margin-top: 0.21em; font-size: large; font-weight: bold;\">&nbsp;</p>\r\n<p><span style=\"margin-left: 1.5em;\"><strong>Recommended</strong></span></p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',2,0,null,null),(51,210,'																																																								<p><span style=\"font-size: medium;\"><strong>Auto Populated Items</strong></span></p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,1,null,null),(52,210,'																																																<p><span style=\"font-size: medium;\"><strong><br></strong></span></p>\r\n<p><span style=\"font-size: medium;\"><strong>Survey Items</strong></span></p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,9,null,null),(53,210,'																																																<p>&nbsp;</p>\r\n<p><span style=\"font-size: xx-small;\">~ Denotes an assessment readiness determinant</span></p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,16,null,null),(68,205,'																																																																																																																																																																												<P>&nbsp;</P>\r\n<P><STRONG><SPAN style=\"FONT-SIZE: large\"><SPAN style=\"FONT-SIZE: medium\"><SPAN style=\"FONT-SIZE: small\">Technology Support Staff</SPAN><BR></SPAN></SPAN></STRONG></P>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',2,6,null,null),(69,205,'																																																																																																																																																																												\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,25,null,null),(70,205,'<p>&nbsp;</p>\r\n<p><strong><span style=\"text-decoration: underline;\">Why is this information needed?</span></strong></p>\r\n<p style=\"line-height: 1.2em; font-size: small;\">The information derived from this survey will assist the consortia and individual states in understanding the categories of potential concerns in each district and school(s) from a staff and personnel standpoint. <strong>Please take the survey once for each school in the district or allow one person in each school to take the survey.</strong>&nbsp; Staff/Personnel information is <span style=\"text-decoration: underline;\">not</span> used to calculate readiness indicators for the school.</p>\r\n<p>&nbsp;</p>\r\n<p>&nbsp;</p>\r\n<p style=\"line-height: 1.2em; font-size: small;\"><strong><span style=\"text-decoration: underline;\">Who should answer these survey questions?</span></strong></p>\r\n<p style=\"line-height: 1.2em; font-size: small;\">The following questions should be answered by a person at the district level (once for each school) or the school level, who has knowledge or the knowledge and capabilities of the staff and personnel responsible for installing, configuring, and using technology to administer student tests.</p>\r\n<p>&nbsp;</p>\r\n<p>&nbsp;</p>\r\n<p style=\"line-height: 1.2em; font-size: small;\"><strong><span style=\"text-decoration: underline;\">How to answer these survey questions?</span></strong></p>\r\n<p style=\"line-height: 1.2em; font-size: small;\">Please rank the following items from 0-10 in regards to level of concern; 0 being of no concern and 10 being of extreme concern. If you are uncertain whether or not it will be a roadblock for your school, please answer Don\'t Know.</p>',2,1,null,null),(71,205,'																																																																																																																																																								<P><STRONG><SPAN style=\"FONT-SIZE: large\"><SPAN style=\"FONT-SIZE: small\"><BR></SPAN></SPAN></STRONG></P>\r\n<P><STRONG><SPAN style=\"FONT-SIZE: large\"><SPAN style=\"FONT-SIZE: small\">Test Administrators</SPAN></SPAN></STRONG></P>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',2,2,null,null),(72,205,'																																																																																																												<P>&nbsp;</P>\r\n<P>&nbsp;</P>\r\n<P>&nbsp;</P>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,28,null,null),(73,204,'																																								<p style=\"font-size: large; font-weight: bold;\">&nbsp; Operating Systems</p>\r\n<p><span style=\"margin-left: 20.5em;\"><strong>Minimum</strong></span></p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,0,null,null),(75,204,'																																												<p style=\"font-size: large; font-weight: bold;\">&nbsp; Network and Display</p>\r\n<p><span style=\"margin-left: 20.5em;\"><strong>Minimum</strong></span></p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,41,null,null),(76,204,'																																												<p style=\"font-size: large; font-weight: bold;\">&nbsp;</p>\r\n<p><span style=\"margin-left: 1.5em;\"><strong>Recommended</strong></span></p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',2,41,null,null),(78,204,'																																																																																																																								<p style=\"font-weight: bold;\">&nbsp;</p>\r\n<p style=\"font-size: large; font-weight: bold;\">&nbsp; Grades to be Tested</p>\r\n<p style=\"font-size: x-small;\">&nbsp; &nbsp; &nbsp; (# of test starts required per grade)</p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,46,null,null),(79,204,'																																																																																								<p style=\"font-weight: bold;\">&nbsp;</p>\r\n<p style=\"font-size: large; font-weight: bold;\">&nbsp;</p>\r\n<p style=\"font-size: x-small;\">&nbsp;</p>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',2,46,null,null),(81,205,'																																																												\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,27,null,null),(82,205,'																																																												<P><SPAN style=\"FONT-SIZE: large\">&nbsp;&nbsp;</SPAN></P>\r\n<P><SPAN style=\"FONT-SIZE: large\"><BR></SPAN></P>\r\n<P><STRONG><SPAN style=\"FONT-SIZE: large\">&nbsp;School Information</SPAN></STRONG></P>\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				\r\n				',1,0,null,null),(83,205,'																\r\n				\r\n				\r\n				\r\n				',1,29,null,null),(84,205,'												<P>&nbsp;</P>\r\n<P><SPAN style=\"FONT-SIZE: xx-small\">~ Denotes an assessment readiness determinant</SPAN></P>\r\n<P>&nbsp;</P>\r\n				\r\n				\r\n				',1,26,null,null),(85,205,'				<P>&nbsp;</P>\r\n<P><SPAN style=\"FONT-SIZE: small\">&nbsp;~Enrollment Counts</SPAN></P>\r\n				',1,2,null,null);
/*!40000 ALTER TABLE `view_def_text` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_text_before_insert   BEFORE INSERT  ON view_def_text  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.column_number ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_text_id,'I', vChangeDate, vChangeUser, 'view_def_text','column_number',null, NEW.column_number);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_text_id,'I', vChangeDate, vChangeUser, 'view_def_text','display_order',null, NEW.display_order);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.text ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_text_id,'I', vChangeDate, vChangeUser, 'view_def_text','text',null, NEW.text);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.view_def_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_text_id,'I', vChangeDate, vChangeUser, 'view_def_text','view_def_id',null, NEW.view_def_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.view_def_text_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_text_id,'I', vChangeDate, vChangeUser, 'view_def_text','view_def_text_id',null, NEW.view_def_text_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_text_before_update   BEFORE UPDATE   ON view_def_text  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.column_number <=> NEW.column_number) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'U', vChangeDate, vChangeUser, 'view_def_text','column_number',OLD.column_number, NEW.column_number);
	END IF;
	IF(NOT OLD.display_order <=> NEW.display_order) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'U', vChangeDate, vChangeUser, 'view_def_text','display_order',OLD.display_order, NEW.display_order);
	END IF;
	IF(NOT OLD.text <=> NEW.text) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'U', vChangeDate, vChangeUser, 'view_def_text','text',OLD.text, NEW.text);
	END IF;
	IF(NOT OLD.view_def_id <=> NEW.view_def_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'U', vChangeDate, vChangeUser, 'view_def_text','view_def_id',OLD.view_def_id, NEW.view_def_id);
	END IF;
	IF(NOT OLD.view_def_text_id <=> NEW.view_def_text_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'U', vChangeDate, vChangeUser, 'view_def_text','view_def_text_id',OLD.view_def_text_id, NEW.view_def_text_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_text_before_delete   BEFORE DELETE   ON view_def_text  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.column_number ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'D', vChangeDate, vChangeUser, 'view_def_text','column_number',OLD.column_number, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.display_order ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'D', vChangeDate, vChangeUser, 'view_def_text','display_order',OLD.display_order, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.text ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'D', vChangeDate, vChangeUser, 'view_def_text','text',OLD.text, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.view_def_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'D', vChangeDate, vChangeUser, 'view_def_text','view_def_id',OLD.view_def_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.view_def_text_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_text_id,'D', vChangeDate, vChangeUser, 'view_def_text','view_def_text_id',OLD.view_def_text_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `view_def_type`
--

DROP TABLE IF EXISTS `view_def_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `view_def_type` (
  `view_def_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `entity_type_id` int(10) unsigned NOT NULL,
  `code` varchar(100) NOT NULL COMMENT 'Unique identifier that can be used code to lookup a specific row.',
  `name` varchar(200) NOT NULL COMMENT 'Unique (within scope) name that is displayed to the user.',
  `category` enum('form','datagrid','group') NOT NULL DEFAULT 'form' COMMENT 'What category is this view type (see enum for possible values)',
  `defaultView` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Is this the default view for the specified "entity_type_id"?  There should be only one default view per entity type.',
  `change_date` datetime DEFAULT NULL,
  `change_user` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`view_def_type_id`),
  UNIQUE KEY `viewdeftype_name_uc` (`name`),
  UNIQUE KEY `viewdeftype_code_uc` (`code`),
  KEY `viewdeftype_entitytype_fki` (`entity_type_id`),
  CONSTRAINT `viewdeftype_entitytype_fk` FOREIGN KEY (`entity_type_id`) REFERENCES `entity_type` (`entity_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `view_def_type`
--

LOCK TABLES `view_def_type` WRITE;
/*!40000 ALTER TABLE `view_def_type` DISABLE KEYS */;
INSERT INTO `view_def_type` VALUES (13,4,'ORG','Organization','form',1,null,null),(14,4,'ORG_DATAGRID','Organization Datagrid','datagrid',1,null,null),(15,5,'ORG_PART','Organization Participation','form',1,null,null),(16,5,'ORG_PART_DATAGRID','Organization Participation Datagrid','datagrid',1,null,null),(17,6,'SCOPE_DATAGRID','Scope DataGrid','datagrid',1,null,null),(18,6,'SCOPE','Scope','form',1,null,null),(19,8,'USER','User Details','form',1,null,null),(20,8,'USER_DATAGRID','User Datagrid','datagrid',1,null,null),(21,10,'CONTACT_DATAGRID','Contact Datagrid','datagrid',1,null,null),(22,8,'USER_DATAGRID_EDIT','User Edit Datagrid','datagrid',1,null,null),(23,13,'DEVICE','Device Details','form',0,null,null),(24,13,'DEVICE_DATAGRID','Device Datagrid','datagrid',1,null,null),(25,9,'ROLE_DATAGRID','Role DataGrid','datagrid',1,null,null),(27,6,'SCOPE_MINS','Scope Minimums','form',0,null,null),(28,4,'ORG_NETWORK_TASK','Org Network Task','form',0,null,null),(30,8,'USER_CREATE','Create User Details','form',1,null,null),(32,15,'SNAPSHOT','Snapshot','form',0,null,null),(34,15,'SNAPSHOT_DATAGRID','Snapshot Datagrid','datagrid',1,null,null),(35,8,'USER_ENABLE','User Enable','datagrid',1,null,null),(36,4,'ORG_DATA_ENTRY_TASK','Org Data Entry Task','form',1,null,null);
/*!40000 ALTER TABLE `view_def_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_type_before_insert   BEFORE INSERT  ON view_def_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF CHAR_LENGTH(IFNULL(NEW.category ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_type_id,'I', vChangeDate, vChangeUser, 'view_def_type','category',null, NEW.category);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_type_id,'I', vChangeDate, vChangeUser, 'view_def_type','code',null, NEW.code);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.defaultView ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_type_id,'I', vChangeDate, vChangeUser, 'view_def_type','defaultView',null, NEW.defaultView);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.entity_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_type_id,'I', vChangeDate, vChangeUser, 'view_def_type','entity_type_id',null, NEW.entity_type_id);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_type_id,'I', vChangeDate, vChangeUser, 'view_def_type','name',null, NEW.name);
	END IF;
	IF CHAR_LENGTH(IFNULL(NEW.view_def_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(NEW.view_def_type_id,'I', vChangeDate, vChangeUser, 'view_def_type','view_def_type_id',null, NEW.view_def_type_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_type_before_update   BEFORE UPDATE   ON view_def_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT NEW.change_user;   

	IF CHAR_LENGTH(IFNULL(@audit_username,''))>0 THEN 
		SET vChangeUser = @audit_username;
	END IF;
	
	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	SET NEW.change_user = vChangeUser;
	SET NEW.change_date = vChangeDate;
	
	IF(NOT OLD.category <=> NEW.category) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'U', vChangeDate, vChangeUser, 'view_def_type','category',OLD.category, NEW.category);
	END IF;
	IF(NOT OLD.code <=> NEW.code) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'U', vChangeDate, vChangeUser, 'view_def_type','code',OLD.code, NEW.code);
	END IF;
	IF(NOT OLD.defaultView <=> NEW.defaultView) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'U', vChangeDate, vChangeUser, 'view_def_type','defaultView',OLD.defaultView, NEW.defaultView);
	END IF;
	IF(NOT OLD.entity_type_id <=> NEW.entity_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'U', vChangeDate, vChangeUser, 'view_def_type','entity_type_id',OLD.entity_type_id, NEW.entity_type_id);
	END IF;
	IF(NOT OLD.name <=> NEW.name) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'U', vChangeDate, vChangeUser, 'view_def_type','name',OLD.name, NEW.name);
	END IF;
	IF(NOT OLD.view_def_type_id <=> NEW.view_def_type_id) THEN   
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'U', vChangeDate, vChangeUser, 'view_def_type','view_def_type_id',OLD.view_def_type_id, NEW.view_def_type_id);
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
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50003 TRIGGER view_def_type_before_delete   BEFORE DELETE   ON view_def_type  FOR EACH ROW 
BEGIN   
	DECLARE vChangeDate TIMESTAMP DEFAULT NOW();
	DECLARE vChangeUser VARCHAR(200) DEFAULT @audit_username;   
	
	IF CHAR_LENGTH(IFNULL(@audit_delete_username,''))>0 THEN 
		SET vChangeUser = @audit_delete_username;
	END IF;

	IF CHAR_LENGTH(IFNULL(vChangeUser,''))=0 THEN 
		SET vChangeUser = USER();
	END IF;
	
	IF CHAR_LENGTH(IFNULL(OLD.category ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'D', vChangeDate, vChangeUser, 'view_def_type','category',OLD.category, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.code ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'D', vChangeDate, vChangeUser, 'view_def_type','code',OLD.code, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.defaultView ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'D', vChangeDate, vChangeUser, 'view_def_type','defaultView',OLD.defaultView, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.entity_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'D', vChangeDate, vChangeUser, 'view_def_type','entity_type_id',OLD.entity_type_id, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.name ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'D', vChangeDate, vChangeUser, 'view_def_type','name',OLD.name, NULL);
	END IF;
	IF CHAR_LENGTH(IFNULL(OLD.view_def_type_id ,''))>0 THEN 
		INSERT INTO generic_hist(primary_key,change_type,change_date,change_user,table_name,column_name,old_value, new_value)  
		VALUES(OLD.view_def_type_id,'D', vChangeDate, vChangeUser, 'view_def_type','view_def_type_id',OLD.view_def_type_id, NULL);
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping routines for database 'core'
--
/*!50003 DROP FUNCTION IF EXISTS `set_audit_delete_info` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE FUNCTION `set_audit_delete_info`(p_username VARCHAR(255)) RETURNS varchar(255) CHARSET utf8
    DETERMINISTIC
BEGIN
  set @audit_delete_username:= p_username;
  RETURN @audit_delete_username;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `set_audit_info` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE FUNCTION `set_audit_info`(p_username VARCHAR(255)) RETURNS varchar(255) CHARSET utf8
    DETERMINISTIC
BEGIN
  set @audit_username:= p_username;
  RETURN @audit_username;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `org_tree_reload` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `org_tree_reload`()
begin
  declare v_org_id int unsigned;
  declare v_cur1 cursor for select org_id from org where parent_org_id is null;
  open v_cur1;
 
  begin
    declare exit handler for not found begin end;
    loop
      fetch v_cur1 into v_org_id;
      select concat('call org_tree_update(', v_org_id, ')') status;
      call org_tree_update(v_org_id);
    end loop;
  end;

  close v_cur1;
  commit;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `org_tree_update` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `org_tree_update`(in p_org_id bigint)
begin

  declare v_table_name varchar(64) default('org');

  declare v_parent_org_id int unsigned;
  declare v_child_org_id int unsigned;

  declare v_loop_num int unsigned default(1);
  declare v_loop_max int unsigned default(0);
  declare v_row_count int unsigned default(1);

  if p_org_id is null then
    signal sqlstate '45000' set message_text = 'Error: input parameter p_org_id cannot be null';
  end if;

  
  delete from temp_tree
  where root_id = p_org_id
    and table_name = v_table_name;

  
  insert into temp_tree (root_id, descendant_id, distance, table_name)
    select org_id, org_id, 0, v_table_name from org where org_id = p_org_id;
  set v_row_count = row_count();
  
  
  while v_row_count > 0 do
    insert into temp_tree (root_id, descendant_id, distance, table_name)
      select w.root_id, w.org_id, w.distance, v_table_name from
      (select distinct p.root_id, org.org_id, p.distance + 1 distance
       from temp_tree as p inner join org on p.descendant_id = org.parent_org_id
       where p.table_name = v_table_name
      ) w
       left join temp_tree as tt on (w.root_id = tt.root_id and w.org_id = tt.descendant_id
                                 and tt.table_name = v_table_name)
       where tt.root_id is null;
    set v_row_count = row_count();
  end while;

  
  delete ot.* from org_tree ot
  inner join temp_tree tt on tt.descendant_id = ot.org_id
  where tt.root_id = p_org_id
    and tt.table_name = v_table_name;

  
  select parent_org_id into v_parent_org_id from org where org_id = p_org_id;

  if v_parent_org_id is null then 

    
    insert into org_tree
      (ancestor_org_id, ancestor_path, org_id, path, distance, depth)
      select null, '/', org_id, concat('/', code), 1, 1 from org where org_id = p_org_id;

    
    insert into org_tree
      (ancestor_org_id, ancestor_path,   org_id, path, distance, depth)
      select org_id, concat('/', code), org_id, concat('/', code), 0, 1 from org where org_id = p_org_id;

    set v_loop_num = 1;
    set v_row_count = 1;
    
    
    while v_row_count > 0 do
    
      set v_row_count = 0;

      
      if v_loop_num > 1 then
        
        insert into org_tree (ancestor_org_id, ancestor_path, org_id, path, distance, depth)
          select ot.org_id, ot.path, ot.org_id, ot.path, 0, ot.depth
          from org_tree ot
          inner join temp_tree tt on tt.descendant_id = ot.org_id
          left join org_tree ot2 on (ot2.org_id = ot.org_id and ot2.ancestor_org_id = ot.org_id)
          where tt.root_id = p_org_id
            and tt.table_name = v_table_name
            and ot.ancestor_org_id = p_org_id
            and ot2.org_tree_id is null;
            
      end if;

      
      insert into org_tree (ancestor_org_id, ancestor_path, org_id, path, distance, depth)
        select ot.ancestor_org_id, ot.ancestor_path, o.org_id,
               concat(ot.path, '/', o.code), ot.distance + 1, ot.depth + 1
        from org o
        inner join temp_tree tt on tt.descendant_id = o.org_id
        inner join org_tree ot on ot.org_id = o.parent_org_id
        where tt.root_id = p_org_id
          and tt.table_name = v_table_name
          and ot.depth = v_loop_num;

      set v_row_count = row_count();
      set v_loop_num = 1 + v_loop_num;
      
    end while;

  else
  
    
    insert into org_tree
      (ancestor_org_id, ancestor_path, org_id, path, distance, depth)
      select o.org_id, concat(t.path, '/', o.code),
             o.org_id, concat(t.path, '/', o.code), 0, t.depth + 1
      from org o inner join org_tree t on o.parent_org_id = t.org_id
      where t.distance = 0
        and o.org_id = p_org_id;

    
    select org_id into v_child_org_id from org where parent_org_id = p_org_id limit 1;
    
    if v_child_org_id is null then 

      
      insert into org_tree
        (ancestor_org_id, ancestor_path, org_id, path, distance, depth)
        select t.ancestor_org_id, t.ancestor_path, p_org_id, concat(t.path, '/', o.code), t.distance + 1, t.depth + 1
        from org_tree t inner join org o on o.parent_org_id = t.org_id
        where o.org_id = p_org_id;
        
    else 

      set v_loop_num = 1;
      set v_row_count = 1;
      
      
      select 2 + depth into v_loop_max from org_tree where org_id = p_org_id and ancestor_org_id = p_org_id;
      while v_loop_num <= v_loop_max do
      
        set v_row_count = 0;
  
        
        if v_loop_num > 1 then
          
          insert into org_tree (ancestor_org_id, ancestor_path, org_id, path, distance, depth)
            select ot.org_id, ot.path, ot.org_id, ot.path, 0, ot.depth
            from org_tree ot
            inner join temp_tree tt on tt.descendant_id = ot.org_id
            left join org_tree ot2 on (ot2.org_id = ot.org_id and ot2.ancestor_org_id = ot.org_id)
            where tt.root_id = p_org_id
              and tt.table_name = v_table_name
              and ot.ancestor_org_id = p_org_id
              and ot2.org_tree_id is null;
              
        end if;
  
        
        if v_loop_num < v_loop_max then
          insert into org_tree (ancestor_org_id, ancestor_path, org_id, path, distance, depth)
          select ot.ancestor_org_id, ot.ancestor_path, o.org_id,
                 concat(ot.path, '/', o.code), ot.distance + 1, ot.depth + 1
          from org o
          inner join temp_tree tt on tt.descendant_id = o.org_id
          inner join org_tree ot on ot.org_id = o.parent_org_id
          left join org_tree ot2 on (ot2.org_id = ot.org_id
                                 and ot2.distance = ot.distance + 1
                                 and ot2.depth = ot.depth + 1
                                 and if(ot2.ancestor_org_id is null, -1, ot2.ancestor_org_id)
                                   = if(ot.ancestor_org_id is null, -1, ot.ancestor_org_id))
          where tt.root_id = p_org_id
            and tt.table_name = v_table_name
            and ot2.org_tree_id is null
            and ot.depth = v_loop_num;
    
          set v_row_count = row_count();
        end if;

        set v_loop_num = 1 + v_loop_num;
  
      end while;
  
    end if;

  end if;

  
  delete from temp_tree where root_id = p_org_id and table_name = v_table_name;

end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `rebuild_scope_tree` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `rebuild_scope_tree`(in p_scope_id BIGINT)
BEGIN
  DECLARE v_count int unsigned default(0);
  DECLARE v_done tinyint unsigned default(0);
  DECLARE v_id BIGINT;
  DECLARE v_ancestor_scope_id BIGINT;
  DECLARE v_ancestor_path VARCHAR(700);
  DECLARE v_ancestor_depth TINYINT;
  DECLARE v_path VARCHAR(700);
  DECLARE v_depth TINYINT;
  DECLARE v_code VARCHAR(700);
  DECLARE v_scope_id BIGINT;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

  drop temporary table if exists temp_scope_hier;
  create temporary table temp_scope_hier(
    id bigint NOT NULL AUTO_INCREMENT,
    ancestor_scope_id bigint, 
    scope_id bigint,
    PRIMARY KEY (id)
  )engine = memory;

  
  IF p_scope_id IS NULL THEN
    DELETE FROM scope_tree;
 ELSE
    DELETE FROM scope_tree
    WHERE scope_id in 
    (
      select scope_id from
      (select 
        scope_id 
      FROM
        scope_tree
      WHERE
        ancestor_scope_id=p_scope_id) as x
    );
  END IF;
  
  
  if p_scope_id is not null then
    insert into temp_scope_hier
      (ancestor_scope_id, scope_id)
      (select parent_scope_id, scope_id from scope where scope_id = p_scope_id);
  else
    insert into temp_scope_hier
      (ancestor_scope_id, scope_id)
      (select parent_scope_id, scope_id from scope where parent_scope_id is null);
  end if;
  
  select count(*), if(count(*)>0,FALSE,TRUE) into v_count, v_done from temp_scope_hier;

  
  while not v_done DO
  BEGIN
    DECLARE cur1 CURSOR FOR SELECT id, ancestor_scope_id, scope_id FROM temp_scope_hier;

    OPEN cur1;
    read_loop: LOOP
      FETCH cur1 INTO v_id, v_ancestor_scope_id, v_scope_id;
      IF v_done THEN
        LEAVE read_loop;
      END IF;
      
      SELECT s.code INTO v_code FROM scope s WHERE s.scope_id=v_scope_id;
      SET v_ancestor_path=null;
      SELECT sr.path INTO v_ancestor_path
        FROM scope_tree sr
        WHERE sr.scope_id=v_ancestor_scope_id
        LIMIT 1;
      
      
      
      IF v_code='root' THEN
        SET v_path='/';
        SET v_ancestor_path='/';
        SET v_depth=0;
	SET v_ancestor_depth = 0;
      ELSE
        SET v_path=CONCAT(IF(v_ancestor_path='/','',v_ancestor_path),'/',v_code);
        SET v_depth=LENGTH(v_path) - LENGTH(REPLACE(v_path,'/',''));
        SET v_ancestor_path=IFNULL(v_ancestor_path,'/');
        IF v_ancestor_path='/' THEN
	   SET v_ancestor_depth = 0;
        ELSE
	   SET v_ancestor_depth = LENGTH(v_ancestor_path) - LENGTH(REPLACE(v_ancestor_path,'/',''));
        END IF;
      END IF;
      
      
      
      insert into scope_tree
        (ancestor_scope_id,ancestor_path, ancestor_depth, scope_id,path, distance,depth)
        values (v_scope_id, v_path, v_depth,  v_scope_id, v_path, 0,v_depth);

      
      IF v_code != 'root' THEN
        
        insert into scope_tree
          (ancestor_scope_id,ancestor_path, ancestor_depth,scope_id,path, distance, depth)
          values (v_ancestor_scope_id, v_ancestor_path, v_ancestor_depth, v_scope_id, v_path,1,v_depth);
  
        
        insert into scope_tree
          (ancestor_scope_id,ancestor_path, ancestor_depth,scope_id,path, distance,depth)
          (select ancestor_scope_id, ancestor_path, 
            case when ancestor_path='/' then 0 else LENGTH(ancestor_path) - LENGTH(REPLACE(ancestor_path,'/',''))end anc_depth,
            v_scope_id, CONCAT(path,'/',v_code), distance+1,v_depth
            from scope_tree where scope_id = v_ancestor_scope_id and distance>0);
      END IF;

      
      delete from temp_scope_hier where id=v_id;
      
      
      insert into temp_scope_hier
        (ancestor_scope_id, scope_id)
        (select parent_scope_id, scope_id from scope where parent_scope_id = v_scope_id);

    END LOOP;
    CLOSE cur1;

    select count(*), if(count(*)>0,FALSE,TRUE) into v_count, v_done from temp_scope_hier;
    END;
  END WHILE;
    
  drop temporary table if exists temp_scope_hier;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Current Database: `core_batch`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `core_batch` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `core_batch`;

--
-- Table structure for table `batch_job_execution`
--

DROP TABLE IF EXISTS `batch_job_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_execution` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `EXIT_CODE` varchar(100) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  KEY `JOB_INST_EXEC_FK` (`JOB_INSTANCE_ID`),
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `batch_job_instance` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_execution`
--

LOCK TABLES `batch_job_execution` WRITE;
/*!40000 ALTER TABLE `batch_job_execution` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_job_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_execution_context`
--

DROP TABLE IF EXISTS `batch_job_execution_context`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_execution_context` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_execution_context`
--

LOCK TABLES `batch_job_execution_context` WRITE;
/*!40000 ALTER TABLE `batch_job_execution_context` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_job_execution_context` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_execution_seq`
--

DROP TABLE IF EXISTS `batch_job_execution_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_execution_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_execution_seq`
--

LOCK TABLES `batch_job_execution_seq` WRITE;
/*!40000 ALTER TABLE `batch_job_execution_seq` DISABLE KEYS */;
INSERT INTO `batch_job_execution_seq` VALUES (1);
/*!40000 ALTER TABLE `batch_job_execution_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_instance`
--

DROP TABLE IF EXISTS `batch_job_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_instance` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_instance`
--

LOCK TABLES `batch_job_instance` WRITE;
/*!40000 ALTER TABLE `batch_job_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_job_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_params`
--

DROP TABLE IF EXISTS `batch_job_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_params` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) NOT NULL,
  `KEY_NAME` varchar(100) NOT NULL,
  `STRING_VAL` varchar(250) DEFAULT NULL,
  `DATE_VAL` datetime DEFAULT NULL,
  `LONG_VAL` bigint(20) DEFAULT NULL,
  `DOUBLE_VAL` double(11,11) DEFAULT NULL,
  KEY `JOB_INST_PARAMS_FK` (`JOB_INSTANCE_ID`),
  CONSTRAINT `JOB_INST_PARAMS_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `batch_job_instance` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_params`
--

LOCK TABLES `batch_job_params` WRITE;
/*!40000 ALTER TABLE `batch_job_params` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_job_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_seq`
--

DROP TABLE IF EXISTS `batch_job_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_seq`
--

LOCK TABLES `batch_job_seq` WRITE;
/*!40000 ALTER TABLE `batch_job_seq` DISABLE KEYS */;
INSERT INTO `batch_job_seq` VALUES (1);
/*!40000 ALTER TABLE `batch_job_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_step_execution`
--

DROP TABLE IF EXISTS `batch_step_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_step_execution` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `STEP_NAME` varchar(100) NOT NULL,
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `COMMIT_COUNT` bigint(20) DEFAULT NULL,
  `READ_COUNT` bigint(20) DEFAULT NULL,
  `FILTER_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_COUNT` bigint(20) DEFAULT NULL,
  `READ_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `PROCESS_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `ROLLBACK_COUNT` bigint(20) DEFAULT NULL,
  `EXIT_CODE` varchar(100) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  KEY `JOB_EXEC_STEP_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_step_execution`
--

LOCK TABLES `batch_step_execution` WRITE;
/*!40000 ALTER TABLE `batch_step_execution` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_step_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_step_execution_context`
--

DROP TABLE IF EXISTS `batch_step_execution_context`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_step_execution_context` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `batch_step_execution` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_step_execution_context`
--

LOCK TABLES `batch_step_execution_context` WRITE;
/*!40000 ALTER TABLE `batch_step_execution_context` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_step_execution_context` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_step_execution_seq`
--

DROP TABLE IF EXISTS `batch_step_execution_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_step_execution_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_step_execution_seq`
--

LOCK TABLES `batch_step_execution_seq` WRITE;
/*!40000 ALTER TABLE `batch_step_execution_seq` DISABLE KEYS */;
INSERT INTO `batch_step_execution_seq` VALUES (1);
/*!40000 ALTER TABLE `batch_step_execution_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_blob_triggers`
--

LOCK TABLES `qrtz_blob_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_blob_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_blob_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_calendars`
--

LOCK TABLES `qrtz_calendars` WRITE;
/*!40000 ALTER TABLE `qrtz_calendars` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_calendars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_cron_triggers`
--

LOCK TABLES `qrtz_cron_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_cron_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_cron_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_fired_triggers`
--

LOCK TABLES `qrtz_fired_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_fired_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_fired_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_job_details`
--

LOCK TABLES `qrtz_job_details` WRITE;
/*!40000 ALTER TABLE `qrtz_job_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_locks`
--

LOCK TABLES `qrtz_locks` WRITE;
/*!40000 ALTER TABLE `qrtz_locks` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_locks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_paused_trigger_grps`
--

LOCK TABLES `qrtz_paused_trigger_grps` WRITE;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_scheduler_state`
--

LOCK TABLES `qrtz_scheduler_state` WRITE;
/*!40000 ALTER TABLE `qrtz_scheduler_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_scheduler_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simple_triggers`
--

LOCK TABLES `qrtz_simple_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simple_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simple_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simprop_triggers`
--

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simprop_triggers`
--

LOCK TABLES `qrtz_simprop_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_triggers`
--

LOCK TABLES `qrtz_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'core_batch'
--

--
-- Current Database: `readiness`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `readiness` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `readiness`;

--
-- Table structure for table `snapshot_config`
--

DROP TABLE IF EXISTS `snapshot_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `snapshot_config` (
  `snapshot_config_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `snapshot_window_id` int(10) unsigned NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `code` varchar(100) NOT NULL,
  `name` varchar(200) NOT NULL,
  `value` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`snapshot_config_id`),
  KEY `snapshotconfig_snapshotwindow_fki` (`snapshot_window_id`),
  KEY `snapshotconfig_snapshotwindowid_type_code_i` (`snapshot_window_id`,`type`,`code`),
  CONSTRAINT `snapshotconfig_snapshotwindow_fk` FOREIGN KEY (`snapshot_window_id`) REFERENCES `snapshot_window` (`snapshot_window_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1536524 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `snapshot_config`
--

LOCK TABLES `snapshot_config` WRITE;
/*!40000 ALTER TABLE `snapshot_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `snapshot_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `snapshot_device`
--

DROP TABLE IF EXISTS `snapshot_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `snapshot_device` (
  `snapshot_window_id` int(10) unsigned NOT NULL,
  `org_id` int(10) unsigned NOT NULL,
  `device_id` int(10) unsigned NOT NULL,
  `name` varchar(200) NOT NULL,
  `location` varchar(200) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `operating_system` varchar(200) DEFAULT NULL,
  `memory` varchar(200) DEFAULT NULL,
  `screen_resolution` varchar(200) DEFAULT NULL,
  `display_size` varchar(200) DEFAULT NULL,
  `environment` varchar(200) DEFAULT NULL,
  `processor` varchar(200) DEFAULT NULL,
  `browser` varchar(200) DEFAULT NULL,
  `owner` varchar(200) DEFAULT NULL,
  `environment_compliant` char(3) DEFAULT NULL,
  `min_compliant_operating_system` char(3) DEFAULT NULL,
  `min_compliant` char(3) DEFAULT NULL,
  `min_compliant_memory` char(3) DEFAULT NULL,
  `min_compliant_screen_resolution` char(3) DEFAULT NULL,
  `min_compliant_display_size` char(3) DEFAULT NULL,
  `rec_compliant_operating_system` char(3) DEFAULT NULL,
  `rec_compliant` char(3) DEFAULT NULL,
  `rec_compliant_memory` char(3) DEFAULT NULL,
  `rec_compliant_screen_resolution` char(3) DEFAULT NULL,
  `rec_compliant_display_size` char(3) DEFAULT NULL,
  PRIMARY KEY (`snapshot_window_id`,`device_id`),
  KEY `snapshotdevice_deviceid_i` (`device_id`),
  KEY `snapshotdevice_snapshotwindowid_orgid_i` (`snapshot_window_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY KEY (snapshot_window_id)
PARTITIONS 6 */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `snapshot_device`
--

LOCK TABLES `snapshot_device` WRITE;
/*!40000 ALTER TABLE `snapshot_device` DISABLE KEYS */;
/*!40000 ALTER TABLE `snapshot_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `snapshot_org`
--

DROP TABLE IF EXISTS `snapshot_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `snapshot_org` (
  `snapshot_org_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `snapshot_window_id` int(10) unsigned NOT NULL,
  `org_id` int(10) unsigned NOT NULL,
  `org_type` varchar(50) DEFAULT NULL,
  `data_entry_complete` varchar(100) DEFAULT NULL,
  `data_entry_complete_date` datetime DEFAULT NULL,
  `data_entry_complete_user` varchar(100) DEFAULT NULL,
  `school_type` varchar(100) DEFAULT NULL,
  `nces_code` varchar(50) DEFAULT NULL,
  `survey_admin_count` varchar(50) DEFAULT NULL,
  `survey_admin_understanding` varchar(50) DEFAULT NULL,
  `survey_admin_training` varchar(50) DEFAULT NULL,
  `survey_techstaff_count` varchar(50) DEFAULT NULL,
  `survey_techstaff_understanding` varchar(50) DEFAULT NULL,
  `survey_techstaff_training` varchar(50) DEFAULT NULL,
  `enrollment_countk` varchar(50) DEFAULT NULL,
  `enrollment_count1` varchar(50) DEFAULT NULL,
  `enrollment_count2` varchar(50) DEFAULT NULL,
  `enrollment_count3` varchar(50) DEFAULT NULL,
  `enrollment_count4` varchar(50) DEFAULT NULL,
  `enrollment_count5` varchar(50) DEFAULT NULL,
  `enrollment_count6` varchar(50) DEFAULT NULL,
  `enrollment_count7` varchar(50) DEFAULT NULL,
  `enrollment_count8` varchar(50) DEFAULT NULL,
  `enrollment_count9` varchar(50) DEFAULT NULL,
  `enrollment_count10` varchar(50) DEFAULT NULL,
  `enrollment_count11` varchar(50) DEFAULT NULL,
  `enrollment_count12` varchar(50) DEFAULT NULL,
  `student_count` varchar(50) DEFAULT NULL,
  `wireless_access_points` varchar(50) DEFAULT NULL,
  `simultaneous_testers` varchar(50) DEFAULT NULL,
  `sessions_per_day` varchar(50) DEFAULT NULL,
  `testing_window_length` varchar(50) DEFAULT NULL,
  `internet_speed` varchar(50) DEFAULT NULL,
  `internet_utilization` varchar(50) DEFAULT NULL,
  `network_speed` varchar(50) DEFAULT NULL,
  `network_utilization` varchar(50) DEFAULT NULL,
  `calc_internet_speed_display` varchar(100) DEFAULT NULL,
  `calc_network_speed_display` varchar(100) DEFAULT NULL,
  `calc_enrollment_count_total` int(11) DEFAULT NULL,
  `calc_not_applicable` tinyint(1) DEFAULT NULL,
  `calc_percent_complete` decimal(9,6) DEFAULT NULL,
  `calc_device_count` int(11) DEFAULT NULL,
  `calc_testing_student_count` int(11) DEFAULT NULL,
  `calc_testing_teststart_count` int(11) DEFAULT NULL,
  `calc_survey_answered_count` int(11) DEFAULT NULL,
  `calc_survey_unanswered_count` int(11) DEFAULT NULL,
  `calc_survey_admin_count_display` varchar(100) DEFAULT NULL,
  `calc_survey_admin_count_0to3` int(11) DEFAULT NULL,
  `calc_survey_admin_count_4to5` int(11) DEFAULT NULL,
  `calc_survey_admin_count_6to7` int(11) DEFAULT NULL,
  `calc_survey_admin_count_8to10` int(11) DEFAULT NULL,
  `calc_survey_admin_count_average` decimal(9,6) DEFAULT NULL,
  `calc_survey_admin_understanding_display` varchar(100) DEFAULT NULL,
  `calc_survey_admin_understanding_0to3` int(11) DEFAULT NULL,
  `calc_survey_admin_understanding_4to5` int(11) DEFAULT NULL,
  `calc_survey_admin_understanding_6to7` int(11) DEFAULT NULL,
  `calc_survey_admin_understanding_8to10` int(11) DEFAULT NULL,
  `calc_survey_admin_understanding_average` decimal(9,6) DEFAULT NULL,
  `calc_survey_admin_training_display` varchar(100) DEFAULT NULL,
  `calc_survey_admin_training_0to3` int(11) DEFAULT NULL,
  `calc_survey_admin_training_4to5` int(11) DEFAULT NULL,
  `calc_survey_admin_training_6to7` int(11) DEFAULT NULL,
  `calc_survey_admin_training_8to10` int(11) DEFAULT NULL,
  `calc_survey_admin_training_average` decimal(9,6) DEFAULT NULL,
  `calc_survey_techstaff_count_display` varchar(100) DEFAULT NULL,
  `calc_survey_techstaff_count_0to3` int(11) DEFAULT NULL,
  `calc_survey_techstaff_count_4to5` int(11) DEFAULT NULL,
  `calc_survey_techstaff_count_6to7` int(11) DEFAULT NULL,
  `calc_survey_techstaff_count_8to10` int(11) DEFAULT NULL,
  `calc_survey_techstaff_count_average` decimal(9,6) DEFAULT NULL,
  `calc_survey_techstaff_understanding_display` varchar(100) DEFAULT NULL,
  `calc_survey_techstaff_understanding_0to3` int(11) DEFAULT NULL,
  `calc_survey_techstaff_understanding_4to5` int(11) DEFAULT NULL,
  `calc_survey_techstaff_understanding_6to7` int(11) DEFAULT NULL,
  `calc_survey_techstaff_understanding_8to10` int(11) DEFAULT NULL,
  `calc_survey_techstaff_understanding_average` decimal(9,6) DEFAULT NULL,
  `calc_survey_techstaff_training_display` varchar(100) DEFAULT NULL,
  `calc_survey_techstaff_training_0to3` int(11) DEFAULT NULL,
  `calc_survey_techstaff_training_4to5` int(11) DEFAULT NULL,
  `calc_survey_techstaff_training_6to7` int(11) DEFAULT NULL,
  `calc_survey_techstaff_training_8to10` int(11) DEFAULT NULL,
  `calc_survey_techstaff_training_average` decimal(9,6) DEFAULT NULL,
  `min_testing_window_length` int(11) DEFAULT NULL,
  `min_device_tbd_count` int(11) DEFAULT NULL,
  `min_device_passing_count` int(11) DEFAULT NULL,
  `min_device_passing_percent` decimal(9,6) DEFAULT NULL,
  `min_testtaker_possible_test_count` int(11) DEFAULT NULL,
  `min_testtaker_percent_students_testable` decimal(9,6) DEFAULT NULL,
  `min_testtaker_percent` decimal(9,6) DEFAULT NULL,
  `min_testtaker_0to25` int(11) DEFAULT NULL,
  `min_testtaker_26to50` int(11) DEFAULT NULL,
  `min_testtaker_51to75` int(11) DEFAULT NULL,
  `min_testtaker_76to100` int(11) DEFAULT NULL,
  `min_network_possible_test_count` int(11) DEFAULT NULL,
  `min_network_percent_students_testable` decimal(9,6) DEFAULT NULL,
  `min_network_percent` decimal(9,6) DEFAULT NULL,
  `min_network_0to25` int(11) DEFAULT NULL,
  `min_network_26to50` int(11) DEFAULT NULL,
  `min_network_51to75` int(11) DEFAULT NULL,
  `min_network_76to100` int(11) DEFAULT NULL,
  `rec_testing_window_length` int(11) DEFAULT NULL,
  `rec_device_tbd_count` int(11) DEFAULT NULL,
  `rec_device_passing_count` int(11) DEFAULT NULL,
  `rec_device_passing_percent` decimal(9,6) DEFAULT NULL,
  `rec_testtaker_possible_test_count` int(11) DEFAULT NULL,
  `rec_testtaker_percent_students_testable` decimal(9,6) DEFAULT NULL,
  `rec_testtaker_percent` decimal(9,6) DEFAULT NULL,
  `rec_testtaker_0to25` int(11) DEFAULT NULL,
  `rec_testtaker_26to50` int(11) DEFAULT NULL,
  `rec_testtaker_51to75` int(11) DEFAULT NULL,
  `rec_testtaker_76to100` int(11) DEFAULT NULL,
  `rec_network_possible_test_count` int(11) DEFAULT NULL,
  `rec_network_percent_students_testable` decimal(9,6) DEFAULT NULL,
  `rec_network_percent` decimal(9,6) DEFAULT NULL,
  `rec_network_0to25` int(11) DEFAULT NULL,
  `rec_network_26to50` int(11) DEFAULT NULL,
  `rec_network_51to75` int(11) DEFAULT NULL,
  `rec_network_76to100` int(11) DEFAULT NULL,
  PRIMARY KEY (`snapshot_org_id`),
  UNIQUE KEY `snapshotorg_snapshotwindowid_orgid_uc` (`snapshot_window_id`,`org_id`),
  KEY `snapshotorg_snapshotwindow_fki` (`snapshot_window_id`),
  CONSTRAINT `snapshotorg_snapshotwindow_fk` FOREIGN KEY (`snapshot_window_id`) REFERENCES `snapshot_window` (`snapshot_window_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=26313928 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `snapshot_org`
--

LOCK TABLES `snapshot_org` WRITE;
/*!40000 ALTER TABLE `snapshot_org` DISABLE KEYS */;
/*!40000 ALTER TABLE `snapshot_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `snapshot_window`
--

DROP TABLE IF EXISTS `snapshot_window`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `snapshot_window` (
  `snapshot_window_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scope_id` int(10) unsigned NOT NULL,
  `name` varchar(100) NOT NULL,
  `rec_network_tbd` tinyint(4) NOT NULL DEFAULT '0',
  `min_network_tbd` tinyint(4) NOT NULL DEFAULT '0',
  `visible` tinyint(1) DEFAULT NULL,
  `request_user` varchar(100) DEFAULT NULL,
  `request_date` datetime DEFAULT NULL,
  `execute_date` datetime DEFAULT NULL,
  `calc_enrollment_countk_median` int(11) DEFAULT NULL,
  `calc_enrollment_countk_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count1_median` int(11) DEFAULT NULL,
  `calc_enrollment_count1_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count2_median` int(11) DEFAULT NULL,
  `calc_enrollment_count2_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count3_median` int(11) DEFAULT NULL,
  `calc_enrollment_count3_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count4_median` int(11) DEFAULT NULL,
  `calc_enrollment_count4_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count5_median` int(11) DEFAULT NULL,
  `calc_enrollment_count5_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count6_median` int(11) DEFAULT NULL,
  `calc_enrollment_count6_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count7_median` int(11) DEFAULT NULL,
  `calc_enrollment_count7_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count8_median` int(11) DEFAULT NULL,
  `calc_enrollment_count8_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count9_median` int(11) DEFAULT NULL,
  `calc_enrollment_count9_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count10_median` int(11) DEFAULT NULL,
  `calc_enrollment_count10_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count11_median` int(11) DEFAULT NULL,
  `calc_enrollment_count11_stddev` decimal(12,6) DEFAULT NULL,
  `calc_enrollment_count12_median` int(11) DEFAULT NULL,
  `calc_enrollment_count12_stddev` decimal(12,6) DEFAULT NULL,
  PRIMARY KEY (`snapshot_window_id`),
  UNIQUE KEY `snapshotwindow_name_scopeid_uc` (`name`,`scope_id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `snapshot_window`
--

LOCK TABLES `snapshot_window` WRITE;
/*!40000 ALTER TABLE `snapshot_window` DISABLE KEYS */;
INSERT INTO `snapshot_window` VALUES (1,65,'default',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,66,'default',0,0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `snapshot_window` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'readiness'
--
/*!50003 DROP PROCEDURE IF EXISTS `snapshot` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `snapshot`(in p_snapshot_window_id BIGINT, in p_full_refresh TINYINT)
BEGIN
  DECLARE v_execute_date TIMESTAMP;
  DECLARE v_last_execute_date TIMESTAMP;

  DECLARE v_last_config_change_date TIMESTAMP;
  DECLARE v_scope_id INT;
  DECLARE v_snapshot_window_id INT;
  DECLARE v_total_survey_count INT DEFAULT 0;
  DECLARE const_totalSurveyQuestionCount INT DEFAULT 15;
  DECLARE v_changed_org_count INT;
  DECLARE v_changed_device_count INT;
  DECLARE v_startTime INT;

  set v_startTime = UNIX_TIMESTAMP();
  set v_snapshot_window_id = p_snapshot_window_id;
  set v_execute_date = now();

  select scope_id, execute_date from snapshot_window where snapshot_window_id=p_snapshot_window_id into v_scope_id, v_last_execute_date;

  -- ===============================================================================
  -- pivot the scope configuration attributes to make them easy to access
  drop temporary table if exists scope_config;
  create temporary table scope_config
  select
    v_scope_id scope_id,
    MAX(se.change_date) last_config_change_date,
    CAST(max(if(ef.code='includeGradeK',se.value,0)) as UNSIGNED) tests_per_gradeK,
    CAST(max(if(ef.code='includeGrade1',se.value,0)) as UNSIGNED) tests_per_grade1,
    CAST(max(if(ef.code='includeGrade2',se.value,0)) as UNSIGNED) tests_per_grade2,
    CAST(max(if(ef.code='includeGrade3',se.value,0)) as UNSIGNED) tests_per_grade3,
    CAST(max(if(ef.code='includeGrade4',se.value,0)) as UNSIGNED) tests_per_grade4,
    CAST(max(if(ef.code='includeGrade5',se.value,0)) as UNSIGNED) tests_per_grade5,
    CAST(max(if(ef.code='includeGrade6',se.value,0)) as UNSIGNED) tests_per_grade6,
    CAST(max(if(ef.code='includeGrade7',se.value,0)) as UNSIGNED) tests_per_grade7,
    CAST(max(if(ef.code='includeGrade8',se.value,0)) as UNSIGNED) tests_per_grade8,
    CAST(max(if(ef.code='includeGrade9',se.value,0)) as UNSIGNED) tests_per_grade9,
    CAST(max(if(ef.code='includeGrade10',se.value,0)) as UNSIGNED) tests_per_grade10,
    CAST(max(if(ef.code='includeGrade11',se.value,0)) as UNSIGNED) tests_per_grade11,
    CAST(max(if(ef.code='includeGrade12',se.value,0)) as UNSIGNED) tests_per_grade12,
    CAST(max(if(ef.code='minimumTestingWindowLength',se.value,null)) as UNSIGNED) min_testing_window_length,
    CAST(max(if(ef.code='minimumThroughputRequiredPerStudent',se.value,null)) as UNSIGNED) min_throughput_per_student,
    CAST(max(if(ef.code='recommendedTestingWindowLength',se.value,null)) as UNSIGNED) rec_testing_window_length,
    CAST(max(if(ef.code='recommendedThroughputRequiredPerStudent',se.value,null)) as UNSIGNED) rec_throughput_per_student
  from core.scope_ext se
    join core.entity_field ef on ef.entity_field_id = se.entity_field_id
  where se.scope_id = v_scope_id
  group by se.scope_id;

  -- force a full refresh if the scope configuration has changed since the last time or if it has never executed.
  select greatest(last_config_change_date,last_scope_ext_delete_date)
  from scope_config,
   (
      SELECT max(change_date) last_scope_ext_delete_date
      from core.generic_hist ge
      WHERE ge.table_name='scope_ext'
        AND ge.column_name='scope_id'
        and ge.old_value = v_scope_id
   ) se into v_last_config_change_date;

  if(v_last_config_change_date > v_last_execute_date or v_last_execute_date is null) then
    set p_full_refresh = true;
  end if;

  -- Copy any organization/device data that has changed to the snapshot.
  call snapshot_copy_data(p_snapshot_window_id, v_scope_id, p_full_refresh,v_changed_org_count, v_changed_device_count);

  -- If anything has changed ... re-calculate all report values for all organizations.
  IF p_full_refresh or v_changed_org_count > 0 or v_changed_device_count > 0 THEN

      -- update the minimum and recommended network TBD flags on snapshot_window
      update snapshot_window sw, scope_config
      set sw.min_network_tbd = if(ifnull(min_testing_window_length,0)=0 or ifnull(min_throughput_per_student,0)=0,1,0),
          sw.rec_network_tbd = if(ifnull(rec_testing_window_length,0)=0 or ifnull(rec_throughput_per_student,0)=0,1,0)
      where snapshot_window_id=p_snapshot_window_id;

      delete from snapshot_config where snapshot_window_id = p_snapshot_window_id;

      -- save off the scope extenstion values
      insert into snapshot_config (snapshot_window_id, type, code, name, value)
      SELECT sw.snapshot_window_id, 'scope_ext', ef.code, ifnull(olv.name, se.value), se.value
      FROM core.scope_ext se
        join core.entity_field ef ON ef.entity_field_id = se.entity_field_id
        join readiness.snapshot_window sw on sw.scope_id = se.scope_id
        left join core.option_list_value olv on (olv.option_list_id = ef.option_list_id and olv.value = se.value)
      WHERE sw.snapshot_window_id = p_snapshot_window_id;

      -- save off the configured options lists
      insert into snapshot_config (snapshot_window_id, type, code, name, value)
      select p_snapshot_window_id, 'option_list', ol.code, olv.name, olv.value
      from core.option_list_value olv
        join core.option_list ol on ol.option_list_id = olv.option_list_id and ol.shared = true
        join core.scope s on s.scope_id = ol.scope_id and s.code = 'readiness';

      -- Count the devices for all schools.
      drop temporary table if exists device_counts ;
      create temporary table device_counts (
        org_id INT UNSIGNED NOT NULL,
        device_count INT,
        min_passed INT,
        min_tbd INT,
        rec_passed INT,
        rec_tbd INT,
        PRIMARY KEY (org_id)
      );
      insert into device_counts
      select
        sd.org_id,
        sum(count),
        sum(if(min_compliant='yes',count,0)),
        sum(if(min_compliant='tbd',count,0)),
        sum(if(rec_compliant='yes',count,0)),
        sum(if(rec_compliant='tbd',count,0))
      from snapshot_device sd
      where sd.snapshot_window_id = p_snapshot_window_id
      group by sd.org_id;

      -- Store the the enrollment and device counts to the snapshot org.
      update snapshot_org so
        left join device_counts dc on dc.org_id = so.org_id,
        scope_config
      set
        so.calc_enrollment_count_total = (
          if( enrollment_countk is null
            and enrollment_count1 is null
            and enrollment_count2 is null
            and enrollment_count3 is null
            and enrollment_count4 is null
            and enrollment_count5 is null
            and enrollment_count6 is null
            and enrollment_count7 is null
            and enrollment_count8 is null
            and enrollment_count9 is null
            and enrollment_count10 is null
            and enrollment_count11 is null
            and enrollment_count12 is null
          , null -- all enollments are null so the sum is null
          , (
                ifnull(enrollment_countk,0)
              + ifnull(enrollment_count1,0)
              + ifnull(enrollment_count2,0)
              + ifnull(enrollment_count3,0)
              + ifnull(enrollment_count4,0)
              + ifnull(enrollment_count5,0)
              + ifnull(enrollment_count6,0)
              + ifnull(enrollment_count7,0)
              + ifnull(enrollment_count8,0)
              + ifnull(enrollment_count9,0)
              + ifnull(enrollment_count10,0)
              + ifnull(enrollment_count11,0)
              + ifnull(enrollment_count12,0)
            ) -- at least one field is not null, so add them up
          )
        ),
        so.calc_testing_student_count = (
          if( enrollment_countk is null
            and enrollment_count1 is null
            and enrollment_count2 is null
            and enrollment_count3 is null
            and enrollment_count4 is null
            and enrollment_count5 is null
            and enrollment_count6 is null
            and enrollment_count7 is null
            and enrollment_count8 is null
            and enrollment_count9 is null
            and enrollment_count10 is null
            and enrollment_count11 is null
            and enrollment_count12 is null
          , null -- all enollments are null so the sum is null
          , (
              if(tests_per_gradeK>0,ifnull(enrollment_countk,0),0)
              + if(tests_per_grade1>0,ifnull(enrollment_count1,0),0)
              + if(tests_per_grade2>0,ifnull(enrollment_count2,0),0)
              + if(tests_per_grade3>0,ifnull(enrollment_count3,0),0)
              + if(tests_per_grade4>0,ifnull(enrollment_count4,0),0)
              + if(tests_per_grade5>0,ifnull(enrollment_count5,0),0)
              + if(tests_per_grade6>0,ifnull(enrollment_count6,0),0)
              + if(tests_per_grade7>0,ifnull(enrollment_count7,0),0)
              + if(tests_per_grade8>0,ifnull(enrollment_count8,0),0)
              + if(tests_per_grade9>0,ifnull(enrollment_count9,0),0)
              + if(tests_per_grade10>0,ifnull(enrollment_count10,0),0)
              + if(tests_per_grade11>0,ifnull(enrollment_count11,0),0)
              + if(tests_per_grade12>0,ifnull(enrollment_count12,0),0)
            ) -- at least one field is not null, so add them up
          )
        ),
        so.calc_testing_teststart_count = (
          if( enrollment_countk is null
            and enrollment_count1 is null
            and enrollment_count2 is null
            and enrollment_count3 is null
            and enrollment_count4 is null
            and enrollment_count5 is null
            and enrollment_count6 is null
            and enrollment_count7 is null
            and enrollment_count8 is null
            and enrollment_count9 is null
            and enrollment_count10 is null
            and enrollment_count11 is null
            and enrollment_count12 is null
          , null -- all enollments are null so the sum is null
          , (
              if(tests_per_gradeK>0,tests_per_gradeK*ifnull(enrollment_countk,0),0)
              + if(tests_per_grade1>0,tests_per_grade1*ifnull(enrollment_count1,0),0)
              + if(tests_per_grade2>0,tests_per_grade2*ifnull(enrollment_count2,0),0)
              + if(tests_per_grade3>0,tests_per_grade3*ifnull(enrollment_count3,0),0)
              + if(tests_per_grade4>0,tests_per_grade4*ifnull(enrollment_count4,0),0)
              + if(tests_per_grade5>0,tests_per_grade5*ifnull(enrollment_count5,0),0)
              + if(tests_per_grade6>0,tests_per_grade6*ifnull(enrollment_count6,0),0)
              + if(tests_per_grade7>0,tests_per_grade7*ifnull(enrollment_count7,0),0)
              + if(tests_per_grade8>0,tests_per_grade8*ifnull(enrollment_count8,0),0)
              + if(tests_per_grade9>0,tests_per_grade9*ifnull(enrollment_count9,0),0)
              + if(tests_per_grade10>0,tests_per_grade10*ifnull(enrollment_count10,0),0)
              + if(tests_per_grade11>0,tests_per_grade11*ifnull(enrollment_count11,0),0)
              + if(tests_per_grade12>0,tests_per_grade12*ifnull(enrollment_count12,0),0)
            ) -- at least one field is not null, so add them up
          )
        ),
        so.calc_device_count = ifnull(dc.device_count,0),
        so.min_device_tbd_count = ifnull(dc.min_tbd,0),
        so.min_device_passing_count = ifnull(dc.min_passed,0),
        so.min_device_passing_percent = ifnull( (dc.min_passed / dc.device_count) * 100,0),
        so.min_testing_window_length = least(scope_config.min_testing_window_length,if(testing_window_length=0,null,testing_window_length)),
        so.rec_device_tbd_count = ifnull(dc.rec_tbd,0),
        so.rec_device_passing_count = ifnull(dc.rec_passed,0),
        so.rec_device_passing_percent = ifnull( (dc.rec_passed / dc.device_count) * 100,0),
        so.rec_testing_window_length = least(scope_config.rec_testing_window_length,if(testing_window_length=0,null,testing_window_length))
      where so.snapshot_window_id = p_snapshot_window_id
        and so.org_type = 'school';

      -- Calculate the more complex values for the school using previously calculated device and enrollment values.
      drop temporary table if exists school_detail ;
      create temporary table school_detail (
        org_id INT UNSIGNED NOT NULL,
        not_applicable TINYINT,
        min_testtaker_possible_test_count INT,
        min_testtaker_percent DECIMAL(15,6),
        min_network_count INT,
        min_network_percent DECIMAL(15,6),
        rec_testtaker_possible_test_count INT,
        rec_testtaker_percent DECIMAL(15,6),
        rec_network_count INT,
        rec_network_percent DECIMAL(15,6),
        survey_questions_answered INT,
        PRIMARY KEY (org_id)
      );
      insert into school_detail
        select
          so.org_id,
          if( so.calc_testing_teststart_count = 0 and ifnull(so.calc_enrollment_count_total,0) > 0, 1, 0) not_applicable,
          so.sessions_per_day*so.min_testing_window_length*so.min_device_passing_count min_testtaker_count,
          so.sessions_per_day*so.min_testing_window_length*so.min_device_passing_count/so.calc_testing_teststart_count min_testtaker_percent,
          (least (
            ((100-so.internet_utilization)/100) * so.internet_speed * 1000,
            ((100-so.network_utilization)/100) * so.network_speed * 1000
          ) / scope_config.min_throughput_per_student) * least(so.min_testing_window_length,scope_config.min_testing_window_length) min_network_count,
          round(((least (
            ((100-so.internet_utilization)/100) * so.internet_speed * 1000,
            ((100-so.network_utilization)/100) * so.network_speed * 1000
          ) / scope_config.min_throughput_per_student) * least(so.min_testing_window_length,scope_config.min_testing_window_length))) / so.calc_testing_teststart_count min_network_percent,
          so.sessions_per_day*so.rec_testing_window_length*so.rec_device_passing_count rec_testtaker_count,
          so.sessions_per_day*so.rec_testing_window_length*so.rec_device_passing_count/so.calc_testing_teststart_count rec_testtaker_percent,
          (least (
            ((100-so.internet_utilization)/100) * so.internet_speed * 1000,
            ((100-so.network_utilization)/100) * so.network_speed * 1000
          ) / scope_config.rec_throughput_per_student) * least(so.rec_testing_window_length,scope_config.rec_testing_window_length) rec_network_count,
          round(((least (
            ((100-so.internet_utilization)/100) * so.internet_speed * 1000,
            ((100-so.network_utilization)/100) * so.network_speed * 1000
          ) / scope_config.rec_throughput_per_student) * least(so.rec_testing_window_length,scope_config.rec_testing_window_length))) / so.calc_testing_teststart_count rec_network_percent,
          (
                if(so.school_type is null, 0, 1) +
                if(so.survey_admin_count is null, 0, 1) +
                if(so.survey_admin_understanding is null, 0, 1) +
                if(so.survey_admin_training is null, 0, 1) +
                if(so.survey_techstaff_count is null, 0, 1) +
                if(so.survey_techstaff_understanding is null, 0, 1) +
                if(so.survey_techstaff_training is null, 0, 1) +
                if(so.internet_speed is null, 0, 1) +
                if(so.internet_utilization is null, 0, 1) +
                if(so.network_speed is null, 0, 1) +
                if(so.network_utilization is null, 0, 1) +
                if(so.testing_window_length is null, 0, 1) +
                if(so.sessions_per_day is null, 0, 1) +
                if(so.wireless_access_points is null, 0, 1) +
                greatest(
                    if(so.enrollment_countk is null, 0, 1),
                    if(so.enrollment_count1 is null, 0, 1),
                    if(so.enrollment_count2 is null, 0, 1),
                    if(so.enrollment_count3 is null, 0, 1),
                    if(so.enrollment_count4 is null, 0, 1),
                    if(so.enrollment_count5 is null, 0, 1),
                    if(so.enrollment_count6 is null, 0, 1),
                    if(so.enrollment_count7 is null, 0, 1),
                    if(so.enrollment_count8 is null, 0, 1),
                    if(so.enrollment_count9 is null, 0, 1),
                    if(so.enrollment_count10 is null, 0, 1),
                    if(so.enrollment_count11 is null, 0, 1),
                    if(so.enrollment_count12 is null, 0, 1)
                )
          ) survey_questions_answered
        from snapshot_org so,
          scope_config
        where
          so.org_type = 'school'
          and so.snapshot_window_id = v_snapshot_window_id;

      -- Use the information above to update the schools in the snapshot.
      update snapshot_org so
        left join school_detail sd on sd.org_id = so.org_id,
        scope_config
      set
        so.calc_not_applicable = sd.not_applicable,
        so.calc_percent_complete = if(ifnull(so.data_entry_complete,'no')='yes',100,0),
        so.calc_enrollment_count_total = if(ifnull(so.calc_enrollment_count_total,0)>0,so.calc_enrollment_count_total,null), -- needed to match orig query

        so.min_testtaker_possible_test_count = sd.min_testtaker_possible_test_count,
        so.min_testtaker_percent_students_testable = if(so.calc_device_count > 0, least(sd.min_testtaker_percent*100,999), null),
        so.min_testtaker_0to25 = if(round(sd.min_testtaker_percent,2) is null || (round(sd.min_testtaker_percent,2) >= 0 and round(sd.min_testtaker_percent,2)<=0.25), 1,0),
        so.min_testtaker_26to50 = if(round(sd.min_testtaker_percent,2) > 0.25 and round(sd.min_testtaker_percent,2)<=0.50, 1,0),
        so.min_testtaker_51to75 = if(round(sd.min_testtaker_percent,2) > 0.50 and round(sd.min_testtaker_percent,2)<=0.75, 1,0),
        so.min_testtaker_76to100 = if(round(sd.min_testtaker_percent,2) > 0.75,1,0) ,

        so.min_network_possible_test_count = sd.min_network_count,
        so.min_network_percent_students_testable = least(sd.min_network_percent*100,999),
        so.min_network_0to25 = if(round(sd.min_network_percent,2) is null || (round(sd.min_network_percent,2) >= 0 and round(sd.min_network_percent,2)<=0.25), 1,0),
        so.min_network_26to50 = if(round(sd.min_network_percent,2) > 0.25 and round(sd.min_network_percent,2)<=0.50, 1,0),
        so.min_network_51to75 = if(round(sd.min_network_percent,2) > 0.50 and round(sd.min_network_percent,2)<=0.75, 1,0),
        so.min_network_76to100 = if(round(sd.min_network_percent,2) > 0.75,1,0),

        so.rec_testtaker_possible_test_count = sd.rec_testtaker_possible_test_count,
        so.rec_testtaker_percent_students_testable = if(so.calc_device_count > 0, least(sd.rec_testtaker_percent*100,999), null),
        so.rec_testtaker_0to25 = if(round(sd.rec_testtaker_percent,2) is null || (round(sd.rec_testtaker_percent,2) >= 0 and round(sd.rec_testtaker_percent,2)<=0.25), 1,0),
        so.rec_testtaker_26to50 = if(round(sd.rec_testtaker_percent,2) > 0.25 and round(sd.rec_testtaker_percent,2)<=0.50, 1,0),
        so.rec_testtaker_51to75 = if(round(sd.rec_testtaker_percent,2) > 0.50 and round(sd.rec_testtaker_percent,2)<=0.75, 1,0),
        so.rec_testtaker_76to100 = if(round(sd.rec_testtaker_percent,2) > 0.75,1,0) ,

        so.rec_network_possible_test_count = sd.rec_network_count,
        so.rec_network_percent_students_testable = least(sd.rec_network_percent*100,999),
        so.rec_network_0to25 = if(round(sd.rec_network_percent,2) is null || (round(sd.rec_network_percent,2) >= 0 and round(sd.rec_network_percent,2)<=0.25), 1,0),
        so.rec_network_26to50 = if(round(sd.rec_network_percent,2) > 0.25 and round(sd.rec_network_percent,2)<=0.50, 1,0),
        so.rec_network_51to75 = if(round(sd.rec_network_percent,2) > 0.50 and round(sd.rec_network_percent,2)<=0.75, 1,0),
        so.rec_network_76to100 = if(round(sd.rec_network_percent,2) > 0.75,1,0),

        calc_survey_admin_count_0to3 = if(so.survey_admin_count<=3, 1, 0) ,
        calc_survey_admin_count_4to5 = if(so.survey_admin_count>=4 and so.survey_admin_count<=5, 1, 0) ,
        calc_survey_admin_count_6to7 = if(so.survey_admin_count>=6 and so.survey_admin_count<=7, 1, 0) ,
        calc_survey_admin_count_8to10 = if(so.survey_admin_count>=8 and so.survey_admin_count<=10, 1, 0),
        calc_survey_admin_count_average = if(ifnull(so.survey_admin_count,0)<=10,so.survey_admin_count,0),

        calc_survey_admin_understanding_0to3 = if(so.survey_admin_understanding<=3, 1, 0) ,
        calc_survey_admin_understanding_4to5 = if(so.survey_admin_understanding>=4 and so.survey_admin_understanding<=5, 1, 0) ,
        calc_survey_admin_understanding_6to7 = if(so.survey_admin_understanding>=6 and so.survey_admin_understanding<=7, 1, 0) ,
        calc_survey_admin_understanding_8to10 = if(so.survey_admin_understanding>=8 and so.survey_admin_understanding<=10, 1, 0),
        calc_survey_admin_understanding_average = if(ifnull(so.survey_admin_understanding,0)<=10,so.survey_admin_understanding,0),

        calc_survey_admin_training_0to3 = if(so.survey_admin_training<=3, 1, 0) ,
        calc_survey_admin_training_4to5 = if(so.survey_admin_training>=4 and so.survey_admin_training<=5, 1, 0) ,
        calc_survey_admin_training_6to7 = if(so.survey_admin_training>=6 and so.survey_admin_training<=7, 1, 0) ,
        calc_survey_admin_training_8to10 = if(so.survey_admin_training>=8 and so.survey_admin_training<=10, 1, 0),
        calc_survey_admin_training_average = if(ifnull(so.survey_admin_training,0)<=10,so.survey_admin_training,0),

        calc_survey_techstaff_count_0to3 = if(so.survey_techstaff_count<=3, 1, 0) ,
        calc_survey_techstaff_count_4to5 = if(so.survey_techstaff_count>=4 and so.survey_techstaff_count<=5, 1, 0) ,
        calc_survey_techstaff_count_6to7 = if(so.survey_techstaff_count>=6 and so.survey_techstaff_count<=7, 1, 0) ,
        calc_survey_techstaff_count_8to10 = if(so.survey_techstaff_count>=8 and so.survey_techstaff_count<=10, 1, 0),
        calc_survey_techstaff_count_average = if(ifnull(so.survey_techstaff_count,0)<=10,so.survey_techstaff_count,0),

        calc_survey_techstaff_understanding_0to3 = if(so.survey_techstaff_understanding<=3, 1, 0) ,
        calc_survey_techstaff_understanding_4to5 = if(so.survey_techstaff_understanding>=4 and so.survey_techstaff_understanding<=5, 1, 0) ,
        calc_survey_techstaff_understanding_6to7 = if(so.survey_techstaff_understanding>=6 and so.survey_techstaff_understanding<=7, 1, 0) ,
        calc_survey_techstaff_understanding_8to10 = if(so.survey_techstaff_understanding>=8 and so.survey_techstaff_understanding<=10, 1, 0),
        calc_survey_techstaff_understanding_average = if(ifnull(so.survey_techstaff_understanding,0)<=10,so.survey_techstaff_understanding,0),

        calc_survey_techstaff_training_0to3 = if(so.survey_techstaff_training<=3, 1, 0) ,
        calc_survey_techstaff_training_4to5 = if(so.survey_techstaff_training>=4 and so.survey_techstaff_training<=5, 1, 0) ,
        calc_survey_techstaff_training_6to7 = if(so.survey_techstaff_training>=6 and so.survey_techstaff_training<=7, 1, 0) ,
        calc_survey_techstaff_training_8to10 = if(so.survey_techstaff_training>=8 and so.survey_techstaff_training<=10, 1, 0),
        calc_survey_techstaff_training_average = if(ifnull(so.survey_techstaff_training,0)<=10,so.survey_techstaff_training,0),

        so.calc_survey_answered_count = ifnull(sd.survey_questions_answered,0),
        so.calc_survey_unanswered_count = const_totalSurveyQuestionCount - ifnull(sd.survey_questions_answered,0)
      where
        so.org_type = 'school'
        and so.snapshot_window_id = p_snapshot_window_id;

      -- Now calculate the roll-up values for the non-school organizations.
      drop temporary table if exists school_detail_rollup ;
      create temporary table school_detail_rollup(
        org_id INT UNSIGNED NOT NULL,
        school_count INT,
        complete_school_count INT,
        test_start_count INT,
        device_count INT,

        calc_survey_admin_count_total INT,
        calc_survey_admin_count_provided INT,
        calc_survey_admin_count_0to3 INT,
        calc_survey_admin_count_4to5 INT,
        calc_survey_admin_count_6to7 INT,
        calc_survey_admin_count_8to10 INT,

        calc_survey_admin_understanding_total INT,
        calc_survey_admin_understanding_provided INT,
        calc_survey_admin_understanding_0to3 INT,
        calc_survey_admin_understanding_4to5 INT,
        calc_survey_admin_understanding_6to7 INT,
        calc_survey_admin_understanding_8to10 INT,

        calc_survey_admin_training_total INT,
        calc_survey_admin_training_provided INT,
        calc_survey_admin_training_0to3 INT,
        calc_survey_admin_training_4to5 INT,
        calc_survey_admin_training_6to7 INT,
        calc_survey_admin_training_8to10 INT,

        calc_survey_techstaff_count_total INT,
        calc_survey_techstaff_count_provided INT,
        calc_survey_techstaff_count_0to3 INT,
        calc_survey_techstaff_count_4to5 INT,
        calc_survey_techstaff_count_6to7 INT,
        calc_survey_techstaff_count_8to10 INT,

        calc_survey_techstaff_understanding_total INT,
        calc_survey_techstaff_understanding_provided INT,
        calc_survey_techstaff_understanding_0to3 INT,
        calc_survey_techstaff_understanding_4to5 INT,
        calc_survey_techstaff_understanding_6to7 INT,
        calc_survey_techstaff_understanding_8to10 INT,

        calc_survey_techstaff_training_total INT,
        calc_survey_techstaff_training_provided INT,
        calc_survey_techstaff_training_0to3 INT,
        calc_survey_techstaff_training_4to5 INT,
        calc_survey_techstaff_training_6to7 INT,
        calc_survey_techstaff_training_8to10 INT,

        min_device_passing_count INT,
        min_device_tbd_count INT,
        min_testtaker_possible_test_count INT,
        min_testtaker_0to25 INT,
        min_testtaker_26to50 INT,
        min_testtaker_51to75 INT,
        min_testtaker_76to100 INT,
        min_network_possible_test_count INT,
        min_network_0to25 INT,
        min_network_26to50 INT,
        min_network_51to75 INT,
        min_network_76to100 INT,
        rec_device_passing_count INT,
        rec_device_tbd_count INT,
        rec_testtaker_possible_test_count INT,
        rec_testtaker_0to25 INT,
        rec_testtaker_26to50 INT,
        rec_testtaker_51to75 INT,
        rec_testtaker_76to100 INT,
        rec_network_possible_test_count INT,
        rec_network_0to25 INT,
        rec_network_26to50 INT,
        rec_network_51to75 INT,
        rec_network_76to100 INT,
        PRIMARY KEY (org_id)
      );
      insert into school_detail_rollup
        select
          aso.org_id,
          count(sd.org_id) school_count,
          sum(if(ifnull(so.data_entry_complete,'no') = 'yes', 1, 0) ) complete_school_count,
          sum(so.calc_testing_teststart_count) test_start_count,
          sum(ifnull(so.calc_device_count,0)) device_count,

          -- Survey
          sum(if(ifnull(so.survey_admin_count,0)<=10,so.survey_admin_count,0)) calc_survey_admin_count_total,
          sum(if(ifnull(so.survey_admin_count,99)<=10,1,0)) calc_survey_admin_count_provided,
          sum(ifnull(so.calc_survey_admin_count_0to3,0)) calc_survey_admin_count_0to3,
          sum(ifnull(so.calc_survey_admin_count_4to5,0)) calc_survey_admin_count_4to5,
          sum(ifnull(so.calc_survey_admin_count_6to7,0)) calc_survey_admin_count_6to7,
          sum(ifnull(so.calc_survey_admin_count_8to10,0)) calc_survey_admin_count_8to10,

          sum(if(ifnull(so.survey_admin_understanding,0)<=10,so.survey_admin_understanding,0)) calc_survey_admin_understanding_total,
          sum(if(ifnull(so.survey_admin_understanding,99)<=10,1,0)) calc_survey_admin_understanding_provided,
          sum(ifnull(so.calc_survey_admin_understanding_0to3,0)) calc_survey_admin_understanding_0to3,
          sum(ifnull(so.calc_survey_admin_understanding_4to5,0)) calc_survey_admin_understanding_4to5,
          sum(ifnull(so.calc_survey_admin_understanding_6to7,0)) calc_survey_admin_understanding_6to7,
          sum(ifnull(so.calc_survey_admin_understanding_8to10,0)) calc_survey_admin_understanding_8to10,

          sum(if(ifnull(so.survey_admin_training,0)<=10,so.survey_admin_training,0)) calc_survey_admin_training_total,
          sum(if(ifnull(so.survey_admin_training,99)<=10,1,0)) calc_survey_admin_training_provided,
          sum(ifnull(so.calc_survey_admin_training_0to3,0)) calc_survey_admin_training_0to3,
          sum(ifnull(so.calc_survey_admin_training_4to5,0)) calc_survey_admin_training_4to5,
          sum(ifnull(so.calc_survey_admin_training_6to7,0)) calc_survey_admin_training_6to7,
          sum(ifnull(so.calc_survey_admin_training_8to10,0)) calc_survey_admin_training_8to10,

          sum(if(ifnull(so.survey_techstaff_count,0)<=10,so.survey_techstaff_count,0)) calc_survey_techstaff_count_total,
          sum(if(ifnull(so.survey_techstaff_count,99)<=10,1,0)) calc_survey_techstaff_count_provided,
          sum(ifnull(so.calc_survey_techstaff_count_0to3,0)) calc_survey_techstaff_count_0to3,
          sum(ifnull(so.calc_survey_techstaff_count_4to5,0)) calc_survey_techstaff_count_4to5,
          sum(ifnull(so.calc_survey_techstaff_count_6to7,0)) calc_survey_techstaff_count_6to7,
          sum(ifnull(so.calc_survey_techstaff_count_8to10,0)) calc_survey_techstaff_count_8to10,

          sum(if(ifnull(so.survey_techstaff_understanding,0)<=10,so.survey_techstaff_understanding,0)) calc_survey_techstaff_understanding_total,
          sum(if(ifnull(so.survey_techstaff_understanding,99)<=10,1,0)) calc_survey_techstaff_understanding_provided,
          sum(ifnull(so.calc_survey_techstaff_understanding_0to3,0)) calc_survey_techstaff_understanding_0to3,
          sum(ifnull(so.calc_survey_techstaff_understanding_4to5,0)) calc_survey_techstaff_understanding_4to5,
          sum(ifnull(so.calc_survey_techstaff_understanding_6to7,0)) calc_survey_techstaff_understanding_6to7,
          sum(ifnull(so.calc_survey_techstaff_understanding_8to10,0)) calc_survey_techstaff_understanding_8to10,

          sum(if(ifnull(so.survey_techstaff_training,0)<=10,so.survey_techstaff_training,0)) calc_survey_techstaff_training_total,
          sum(if(ifnull(so.survey_techstaff_training,99)<=10,1,0)) calc_survey_techstaff_training_provided,
          sum(ifnull(so.calc_survey_techstaff_training_0to3,0)) calc_survey_techstaff_training_0to3,
          sum(ifnull(so.calc_survey_techstaff_training_4to5,0)) calc_survey_techstaff_training_4to5,
          sum(ifnull(so.calc_survey_techstaff_training_6to7,0)) calc_survey_techstaff_training_6to7,
          sum(ifnull(so.calc_survey_techstaff_training_8to10,0)) calc_survey_techstaff_training_8to10,

          sum(so.min_device_passing_count) min_device_passing_count,
          sum(so.min_device_tbd_count) min_device_tbd_count,

          -- Test taker
          sum(least(ifnull(so.min_testtaker_possible_test_count,0),ifnull(so.calc_testing_teststart_count,0))) min_testtaker_possible_test_count,
          sum(ifnull(so.min_testtaker_0to25,0)) min_testtaker_0to25,
          sum(ifnull(so.min_testtaker_26to50,0)) min_testtaker_26to50,
          sum(ifnull(so.min_testtaker_51to75,0)) min_testtaker_51to75,
          sum(ifnull(so.min_testtaker_76to100,0)) min_testtaker_76to100,

          -- Network
          sum(least(ifnull(so.min_network_possible_test_count,0),ifnull(so.calc_testing_teststart_count,0))) min_network_possible_test_count,
          sum(ifnull(so.min_network_0to25,0)) min_network_0to25,
          sum(ifnull(so.min_network_26to50,0)) min_network_26to50,
          sum(ifnull(so.min_network_51to75,0)) min_network_51to75,
          sum(ifnull(so.min_network_76to100,0)) min_network_76to100,

          sum(so.rec_device_passing_count) rec_device_passing_count,
          sum(so.rec_device_tbd_count) rec_device_tbd_count,

          -- Test taker
          sum(least(ifnull(so.rec_testtaker_possible_test_count,0),ifnull(so.calc_testing_teststart_count,0))) rec_testtaker_possible_test_count,
          sum(ifnull(so.rec_testtaker_0to25,0)) rec_testtaker_0to25,
          sum(ifnull(so.rec_testtaker_26to50,0)) rec_testtaker_26to50,
          sum(ifnull(so.rec_testtaker_51to75,0)) rec_testtaker_51to75,
          sum(ifnull(so.rec_testtaker_76to100,0)) rec_testtaker_76to100,

          -- Network
          sum(least(ifnull(so.rec_network_possible_test_count,0),ifnull(so.calc_testing_teststart_count,0))) rec_network_possible_test_count,
          sum(ifnull(so.rec_network_0to25,0)) rec_network_0to25,
          sum(ifnull(so.rec_network_26to50,0)) rec_network_26to50,
          sum(ifnull(so.rec_network_51to75,0)) rec_network_51to75,
          sum(ifnull(so.rec_network_76to100,0)) rec_network_76to100
        from snapshot_org aso
          left join core.org_tree otree on otree.ancestor_org_id = aso.org_id and otree.distance > 0
          left join snapshot_org so on so.org_id = otree.org_id and so.snapshot_window_id = aso.snapshot_window_id and so.org_type = 'school'
          left join school_detail sd on sd.org_id = otree.org_id
        where aso.snapshot_window_id = p_snapshot_window_id
          and aso.org_type <> 'school'
        group by aso.org_id;

      -- Use the values calculated above to update the non-school organizations.
      update snapshot_org so
        left join school_detail_rollup sdr on sdr.org_id = so.org_id,
        scope_config
      set
        so.calc_percent_complete = if(complete_school_count is null or ifnull(school_count,0)=0, 0, (complete_school_count/school_count)*100),
        so.calc_device_count = ifnull(sdr.device_count,0),
        so.calc_testing_teststart_count = sdr.test_start_count,

        so.calc_survey_admin_count_average = (calc_survey_admin_count_total/calc_survey_admin_count_provided),
        so.calc_survey_admin_count_0to3 = sdr.calc_survey_admin_count_0to3,
        so.calc_survey_admin_count_4to5 = sdr.calc_survey_admin_count_4to5,
        so.calc_survey_admin_count_6to7 = sdr.calc_survey_admin_count_6to7,
        so.calc_survey_admin_count_8to10 = sdr.calc_survey_admin_count_8to10,

        so.calc_survey_admin_understanding_average = (calc_survey_admin_understanding_total/calc_survey_admin_understanding_provided),
        so.calc_survey_admin_understanding_0to3 = sdr.calc_survey_admin_understanding_0to3,
        so.calc_survey_admin_understanding_4to5 = sdr.calc_survey_admin_understanding_4to5,
        so.calc_survey_admin_understanding_6to7 = sdr.calc_survey_admin_understanding_6to7,
        so.calc_survey_admin_understanding_8to10 = sdr.calc_survey_admin_understanding_8to10,

        so.calc_survey_admin_training_average = (calc_survey_admin_training_total/calc_survey_admin_training_provided),
        so.calc_survey_admin_training_0to3 = sdr.calc_survey_admin_training_0to3,
        so.calc_survey_admin_training_4to5 = sdr.calc_survey_admin_training_4to5,
        so.calc_survey_admin_training_6to7 = sdr.calc_survey_admin_training_6to7,
        so.calc_survey_admin_training_8to10 = sdr.calc_survey_admin_training_8to10,

        so.calc_survey_techstaff_count_average = (calc_survey_techstaff_count_total/calc_survey_techstaff_count_provided),
        so.calc_survey_techstaff_count_0to3 = sdr.calc_survey_techstaff_count_0to3,
        so.calc_survey_techstaff_count_4to5 = sdr.calc_survey_techstaff_count_4to5,
        so.calc_survey_techstaff_count_6to7 = sdr.calc_survey_techstaff_count_6to7,
        so.calc_survey_techstaff_count_8to10 = sdr.calc_survey_techstaff_count_8to10,

        so.calc_survey_techstaff_understanding_average = (calc_survey_techstaff_understanding_total/calc_survey_techstaff_understanding_provided),
        so.calc_survey_techstaff_understanding_0to3 = sdr.calc_survey_techstaff_understanding_0to3,
        so.calc_survey_techstaff_understanding_4to5 = sdr.calc_survey_techstaff_understanding_4to5,
        so.calc_survey_techstaff_understanding_6to7 = sdr.calc_survey_techstaff_understanding_6to7,
        so.calc_survey_techstaff_understanding_8to10 = sdr.calc_survey_techstaff_understanding_8to10,

        so.calc_survey_techstaff_training_average = (calc_survey_techstaff_training_total/calc_survey_techstaff_training_provided),
        so.calc_survey_techstaff_training_0to3 = sdr.calc_survey_techstaff_training_0to3,
        so.calc_survey_techstaff_training_4to5 = sdr.calc_survey_techstaff_training_4to5,
        so.calc_survey_techstaff_training_6to7 = sdr.calc_survey_techstaff_training_6to7,
        so.calc_survey_techstaff_training_8to10 = sdr.calc_survey_techstaff_training_8to10,

        so.min_device_passing_count = ifnull(sdr.min_device_passing_count,0),
        so.min_device_tbd_count = ifnull(sdr.min_device_tbd_count,0),
        so.min_device_passing_percent = if(sdr.min_device_passing_count is null or sdr.device_count is null,0,(sdr.min_device_passing_count/sdr.device_count) * 100),
        so.min_testtaker_possible_test_count = ifnull(sdr.min_testtaker_possible_test_count,0),
        so.min_testtaker_percent_students_testable = least((ifnull(sdr.min_testtaker_possible_test_count,0)/ifnull(sdr.test_start_count,0))*100,999),
        so.min_testtaker_0to25 = sdr.min_testtaker_0to25,
        so.min_testtaker_26to50 = sdr.min_testtaker_26to50,
        so.min_testtaker_51to75 = sdr.min_testtaker_51to75,
        so.min_testtaker_76to100 = sdr.min_testtaker_76to100,
        so.min_network_possible_test_count = sdr.min_network_possible_test_count,
        so.min_network_percent_students_testable = least((ifnull(sdr.min_network_possible_test_count,0)/ifnull(sdr.test_start_count,0))*100,999),
        so.min_network_0to25 = sdr.min_network_0to25,
        so.min_network_26to50 = sdr.min_network_26to50,
        so.min_network_51to75 = sdr.min_network_51to75,
        so.min_network_76to100 = sdr.min_network_76to100,

        so.rec_device_passing_count = ifnull(sdr.rec_device_passing_count,0),
        so.rec_device_tbd_count = ifnull(sdr.rec_device_tbd_count,0),
        so.rec_device_passing_percent = if(sdr.rec_device_passing_count is null or sdr.device_count is null,0,(sdr.rec_device_passing_count/sdr.device_count) * 100),
        so.rec_testtaker_possible_test_count = ifnull(sdr.rec_testtaker_possible_test_count,0),
        so.rec_testtaker_percent_students_testable = least((ifnull(sdr.rec_testtaker_possible_test_count,0)/ifnull(sdr.test_start_count,0))*100,999),
        so.rec_testtaker_0to25 = sdr.rec_testtaker_0to25,
        so.rec_testtaker_26to50 = sdr.rec_testtaker_26to50,
        so.rec_testtaker_51to75 = sdr.rec_testtaker_51to75,
        so.rec_testtaker_76to100 = sdr.rec_testtaker_76to100,
        so.rec_network_possible_test_count = sdr.rec_network_possible_test_count,
        so.rec_network_percent_students_testable = least((ifnull(sdr.rec_network_possible_test_count,0)/ifnull(sdr.test_start_count,0))*100,999),
        so.rec_network_0to25 = sdr.rec_network_0to25,
        so.rec_network_26to50 = sdr.rec_network_26to50,
        so.rec_network_51to75 = sdr.rec_network_51to75,
        so.rec_network_76to100 = sdr.rec_network_76to100
      where so.snapshot_window_id = p_snapshot_window_id
        and so.org_type <> 'school';

    -- Calculate the median and standard deviation for each grade.
    call snapshot_enroll_median(p_snapshot_window_id);
  END IF;

  update snapshot_window set
    execute_date = v_execute_date
  where snapshot_window_id=p_snapshot_window_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `snapshot_copy_data` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `snapshot_copy_data`(p_snapshot_window_id INT, p_scope_id INT, p_full_refresh TINYINT, OUT p_changed_org_count INT, OUT p_changed_device_count INT)
BEGIN
DECLARE v_last_execute_date TIMESTAMP;
DECLARE v_snapshot_name VARCHAR(100);

select execute_date,name from snapshot_window where snapshot_window_id=p_snapshot_window_id into v_last_execute_date, v_snapshot_name;

-- ===============================================================================
-- Update the organization data in the snapshot
-- ===============================================================================

-- Determine which orgs have changed
drop temporary table if exists changed_orgs;
if( p_full_refresh ) then
  -- Add all of the orgs to the temporary table
  create temporary table changed_orgs
  select org_id from core.org;

  -- Delete all orgs from the snapshot org table
  delete so from snapshot_org so
  where so.snapshot_window_id = p_snapshot_window_id;
else
  -- Add all of the orgs that have changed since the last execute to the temporary table
  create temporary table changed_orgs
  select distinct org_id from (
    select org_id from core.org where change_date >= v_last_execute_date
    union
    select org_id from core.org_hist where change_date >= v_last_execute_date
    union
    select org_id from core.org_part where change_date >= v_last_execute_date
    union
    select org_id from core.org_part_hist where change_date >= v_last_execute_date
  ) x;

  -- Delete the modified orgs from the snapshot org table
  delete so from snapshot_org so
  join changed_orgs co on co.org_id = so.org_id
  where so.snapshot_window_id = p_snapshot_window_id;
end if;

select count(*) from changed_orgs into p_changed_org_count;

-- Copy the data to the snapshot for all changed org
insert into snapshot_org
(
  snapshot_window_id,
  org_id,
  org_type,
  enrollment_count1,
  enrollment_count10,
  enrollment_count11,
  enrollment_count12,
  enrollment_count2,
  enrollment_count3,
  enrollment_count4,
  enrollment_count5,
  enrollment_count6,
  enrollment_count7,
  enrollment_count8,
  enrollment_count9,
  enrollment_countk,
  internet_utilization,
  network_utilization,
  testing_window_length,
  sessions_per_day,
  simultaneous_testers,
  wireless_access_points,
  school_type,
  data_entry_complete,
  network_speed,
  internet_speed,
  survey_admin_count,
  survey_admin_understanding,
  survey_admin_training,
  survey_techstaff_count,
  survey_techstaff_understanding,
  survey_techstaff_training,

  calc_network_speed_display,
  calc_internet_speed_display,
  calc_survey_admin_count_display,
  calc_survey_admin_understanding_display,
  calc_survey_admin_training_display,
  calc_survey_techstaff_count_display,
  calc_survey_techstaff_understanding_display,
  calc_survey_techstaff_training_display
)
select
  p_snapshot_window_id snapshot_window_id,
  o.org_id,
  ot.code org_type,
  if(enrollment_count1='',null,enrollment_count1),
  if(enrollment_count10='',null,enrollment_count10),
  if(enrollment_count11='',null,enrollment_count11),
  if(enrollment_count12='',null,enrollment_count12),
  if(enrollment_count2='',null,enrollment_count2),
  if(enrollment_count3='',null,enrollment_count3),
  if(enrollment_count4='',null,enrollment_count4),
  if(enrollment_count5='',null,enrollment_count5),
  if(enrollment_count6='',null,enrollment_count6),
  if(enrollment_count7='',null,enrollment_count7),
  if(enrollment_count8='',null,enrollment_count8),
  if(enrollment_count9='',null,enrollment_count9),
  if(enrollment_countk='',null,enrollment_countk),
  if(internet_utilization='',null,internet_utilization),
  if(network_utilization='',null,network_utilization),
  if(testing_window_length='',null,testing_window_length),
  if(sessions_per_day='',null,sessions_per_day),
  if(simultaneous_testers='',null,simultaneous_testers),
  if(wireless_access_points='',null,wireless_access_points),
  olv_school_type.name school_type,
  case o.data_entry_complete when 'true' then 'Yes' when 'false' then 'No' else null end data_entry_complete,
  if(network_speed='',null,network_speed),
  if(internet_speed='',null,internet_speed),
  if(survey_admin_count='',null,survey_admin_count),
  if(survey_admin_understanding='',null,survey_admin_understanding),
  if(survey_admin_training='',null,survey_admin_training),
  if(survey_techstaff_count='',null,survey_techstaff_count),
  if(survey_techstaff_understanding='',null,survey_techstaff_understanding),
  if(survey_techstaff_training='',null,survey_techstaff_training),

  olv_internal_network.name,
  olv_internet_network.name,
  olv_admin_count.name,
  olv_admin_understanding.name,
  olv_admin_training.name,
  olv_techstaff_count.name,
  olv_techstaff_understanding.name,
  olv_techstaff_training.name
from core.org o
  join changed_orgs co on co.org_id = o.org_id
  join core.org_part op on (op.org_id = o.org_id and op.scope_id=p_scope_id)
  join core.org_type ot on ot.org_type_id = o.org_type_id
  left join core.option_list_value olv_school_type on (olv_school_type.value=school_type and olv_school_type.option_list_id=(select option_list_id from core.option_list where code='schoolType'))
  left join core.option_list_value olv_internal_network on (olv_internal_network.value=network_speed and olv_internal_network.option_list_id=(select option_list_id from core.option_list where code='internalNetworkSpeed'))
  left join core.option_list_value olv_internet_network on (olv_internet_network.value=internet_speed and olv_internet_network.option_list_id=(select option_list_id from core.option_list where code='internetBandwidth'))
  left join core.option_list_value olv_admin_count on (olv_admin_count.value=survey_admin_count and olv_admin_count.option_list_id=(select option_list_id from core.option_list where code='surveyRating'))
  left join core.option_list_value olv_admin_understanding on (olv_admin_understanding.value=o.survey_admin_understanding and olv_admin_understanding.option_list_id=(select option_list_id from core.option_list where code='surveyRating'))
  left join core.option_list_value olv_admin_training on (olv_admin_training.value=o.survey_admin_training and olv_admin_training.option_list_id=(select option_list_id from core.option_list where code='surveyRating'))
  left join core.option_list_value olv_techstaff_count on (olv_techstaff_count.value=o.survey_techstaff_count and olv_techstaff_count.option_list_id=(select option_list_id from core.option_list where code='surveyRating'))
  left join core.option_list_value olv_techstaff_understanding on (olv_techstaff_understanding.value=o.survey_techstaff_understanding and olv_techstaff_understanding.option_list_id=(select option_list_id from core.option_list where code='surveyRating'))
  left join core.option_list_value olv_techstaff_training on (olv_techstaff_training.value=o.survey_techstaff_training and olv_techstaff_training.option_list_id=(select option_list_id from core.option_list where code='surveyRating'))
where ifnull(o.inactive,0) <> 1;


-- ===============================================================================
-- Update the device data in the snapshot
-- ===============================================================================

-- Determine which devices have changed
drop temporary table if exists changed_devices;
if( p_full_refresh ) then
  -- Delete all devices for this snapshot
  delete sd from snapshot_device sd
  where sd.snapshot_window_id = p_snapshot_window_id;

  -- Add all devices to the temporary table
  create temporary table changed_devices
  select device_id
  from core.device;
else
  -- add all of the modified devices to the temporary table
  create temporary table changed_devices
  select distinct device_id from (
    select device_id from core.device where change_date >= v_last_execute_date
    union
    select device_id from core.device_hist where change_date >= v_last_execute_date
    union
    select device_id from core.device d
      join changed_orgs co on co.org_id = d.org_id
  ) x;

  -- Delete the modified devices from the snapshot device
  delete sd from snapshot_device sd
    join changed_devices cd on cd.device_id = sd.device_id
  where sd.snapshot_window_id = p_snapshot_window_id;
end if;


select count(*) from changed_devices into p_changed_device_count;

-- Create the compliance_filters temp table
drop temporary table if exists compliance_filters;
create temporary table compliance_filters
select
  CAST(operating_system as CHAR(100)) operating_system,
  CAST(min_memory as CHAR(100)) min_memory,
  CAST(rec_memory as CHAR(100)) rec_memory,
  case(min_memory)
    when 'TBD' then 'TBD'
    when '-1' then 'no'
    else 'yes'
  end min_operating_system,
  CAST(min_monitor_display_size as CHAR(100)) min_monitor_display_size      ,
  CAST(min_screen_resolution as CHAR(100)) min_screen_resolution      ,
  case(rec_memory)
    when 'TBD' then 'TBD'
    when '-1' then 'no'
    else 'yes'
  end rec_operating_system,
  CAST(rec_monitor_display_size as CHAR(100)) rec_monitor_display_size,
  CAST(rec_screen_resolution as CHAR(100)) rec_screen_resolution
from (
  select
    SUBSTRING(ef.code,(LENGTH(SUBSTRING_INDEX(ef.code,'_',1)))+2) operating_system,
    ifnull(max(if(SUBSTRING_INDEX(ef.code,'_',1)='minMemory',if(olv.name='TBD','TBD',se.value),null)),'TBD') min_memory,
    ifnull(max(if(SUBSTRING_INDEX(ef.code,'_',1)='recommendedMemory',if(olv.name='TBD','TBD',se.value),null)),'TBD') rec_memory
  from core.scope_ext se
    join core.entity_field ef on (ef.entity_field_id = se.entity_field_id)
    join core.entity e on (e.entity_id = ef.entity_id)
    left join core.option_list_value olv on (olv.option_list_id = ef.option_list_id and olv.value = se.value)
  where se.scope_id=p_scope_id
  group by operating_system
) os_specific,
(
  select
    ifnull(max(if(ef.code='minimumMonitorDisplaySize',if(olv.name='TBD','TBD',se.value),null)),'TBD') min_monitor_display_size,
    ifnull(max(if(ef.code='minimumScreenResolution',if(olv.name='TBD','TBD',se.value),null)),'TBD') min_screen_resolution,
    ifnull(max(if(ef.code='recommendedMonitorDisplaySize',if(olv.name='TBD','TBD',se.value),null)),'TBD') rec_monitor_display_size,
    ifnull(max(if(ef.code='recommendedScreenResolution',if(olv.name='TBD','TBD',se.value),null)),'TBD') rec_screen_resolution
  from core.scope_ext se
    join core.entity_field ef on (ef.entity_field_id = se.entity_field_id)
    join core.entity e on (e.entity_id = ef.entity_id)
    left join core.option_list_value olv on (olv.option_list_id = ef.option_list_id and olv.value = se.value)
  where se.scope_id=p_scope_id
) scope_specific;

if(p_changed_device_count > 0 ) then
  -- Copy the modified devices to the snapshot device table
    insert into readiness.snapshot_device(
      snapshot_window_id, org_id, device_id, name, location, count,owner,
      operating_system, processor, browser,memory, screen_resolution, display_size, environment,
      environment_compliant,
      min_compliant,
      min_compliant_operating_system,
      min_compliant_memory,
      min_compliant_screen_resolution,
      min_compliant_display_size,
      rec_compliant,
      rec_compliant_operating_system,
      rec_compliant_memory,
      rec_compliant_screen_resolution,
      rec_compliant_display_size
    )
      select
        so.snapshot_window_id,
        d.org_id,
        d.device_id,
        d.name,
        d.location,
        d.count,
        d.owner,
        d.operating_system,
        d.processor,
        d.browser,
        d.memory,
        d.screen_resolution,
        d.display_size,
        d.environment,
        if(d.environment = 'inappropriate','no','yes') environment_compliant,
        case
          when
            d.environment is null
            or d.environment = 'inappropriate'
            or cf.min_operating_system = 'no'
          then 'no'
          when
            cf.min_operating_system = 'TBD'
            or cf.min_memory = 'TBD'
            or cf.min_monitor_display_size = 'TBD'
            or cf.min_screen_resolution = 'TBD'
          then 'TBD'
          when
            cf.min_operating_system = 'yes'
            and d.memory >= cf.min_memory
            and d.display_size >= cf.min_monitor_display_size
            and d.screen_resolution >= cf.min_screen_resolution
          then 'yes'
          else 'no'
        end min_compliant,
        ifnull(min_operating_system,'no'),
        case
          when min_operating_system = 'no' then 'yes'
          when cf.min_memory = 'TBD' then 'TBD'
          when d.memory >= cf.min_memory then 'yes'
          else 'no'
        end min_compliant_memory,
        case
          when cf.min_screen_resolution = 'TBD' then 'TBD'
          when d.screen_resolution >= cf.min_screen_resolution then 'yes'
          else 'no'
        end min_compliant_screen_resolution,
        case
          when cf.min_monitor_display_size = 'TBD' then 'TBD'
          when d.display_size >= cf.min_monitor_display_size then 'yes'
          else 'no'
        end min_compliant_display_size,
        case
          when
            d.environment is null
            or d.environment = 'inappropriate'
            or cf.rec_operating_system = 'no'
          then 'no'
          when
            cf.rec_operating_system = 'TBD'
            or cf.rec_memory = 'TBD'
            or cf.rec_monitor_display_size = 'TBD'
            or cf.rec_screen_resolution = 'TBD'
          then 'TBD'
          when
            cf.rec_operating_system = 'yes'
            and d.memory >= cf.rec_memory
            and d.display_size >= cf.rec_monitor_display_size
            and d.screen_resolution >= cf.rec_screen_resolution
          then 'yes'
          else 'no'
        end rec_compliant,
        ifnull(rec_operating_system,'no'),
        case
          when rec_operating_system = 'no' then 'yes'
          when cf.rec_memory = 'TBD' then 'TBD'
          when d.memory >= cf.rec_memory then 'yes'
          else 'no'
        end rec_compliant_memory,
        case
          when cf.rec_screen_resolution = 'TBD' then 'TBD'
          when d.screen_resolution >= cf.rec_screen_resolution then 'yes'
          else 'no'
        end rec_compliant_screen_resolution,
        case
          when cf.rec_monitor_display_size = 'TBD' then 'TBD'
          when d.display_size >= cf.rec_monitor_display_size then 'yes'
          else 'no'
        end rec_compliant_display_size
      from core.device d
        join changed_devices cd on cd.device_id = d.device_id
        join snapshot_org so on so.org_id = d.org_id and so.snapshot_window_id = p_snapshot_window_id
        left join compliance_filters cf ON (cf.operating_system = d.operating_system);
end if;

-- Clean up on aisle 3
drop temporary table if exists changed_devices;
drop temporary table if exists changed_orgs;
drop temporary table if exists compliance_filters;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `snapshot_delete` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `snapshot_delete`(in p_snapshot_window_id BIGINT)
BEGIN
  delete from snapshot_config where snapshot_window_id = p_snapshot_window_id;
  delete from snapshot_device where snapshot_window_id = p_snapshot_window_id;
  delete from snapshot_org where snapshot_window_id = p_snapshot_window_id;
  delete from snapshot_window where snapshot_window_id = p_snapshot_window_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `snapshot_enroll_median` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `snapshot_enroll_median`(in p_snapshot_window_id BIGINT)
BEGIN
DECLARE v_median INT;
DECLARE v_totaldiff DECIMAL(12,6);
DECLARE v_current_time INT;

DECLARE v_rowcountk INT;
DECLARE v_rowcount1 INT;
DECLARE v_rowcount2 INT;
DECLARE v_rowcount3 INT;
DECLARE v_rowcount4 INT;
DECLARE v_rowcount5 INT;
DECLARE v_rowcount6 INT;
DECLARE v_rowcount7 INT;
DECLARE v_rowcount8 INT;
DECLARE v_rowcount9 INT;
DECLARE v_rowcount10 INT;
DECLARE v_rowcount11 INT;
DECLARE v_rowcount12 INT;

DECLARE v_mediank INT;
DECLARE v_median1 INT;
DECLARE v_median2 INT;
DECLARE v_median3 INT;
DECLARE v_median4 INT;
DECLARE v_median5 INT;
DECLARE v_median6 INT;
DECLARE v_median7 INT;
DECLARE v_median8 INT;
DECLARE v_median9 INT;
DECLARE v_median10 INT;
DECLARE v_median11 INT;
DECLARE v_median12 INT;

DECLARE v_totaldiffk INT;
DECLARE v_totaldiff1 INT;
DECLARE v_totaldiff2 INT;
DECLARE v_totaldiff3 INT;
DECLARE v_totaldiff4 INT;
DECLARE v_totaldiff5 INT;
DECLARE v_totaldiff6 INT;
DECLARE v_totaldiff7 INT;
DECLARE v_totaldiff8 INT;
DECLARE v_totaldiff9 INT;
DECLARE v_totaldiff10 INT;
DECLARE v_totaldiff11 INT;
DECLARE v_totaldiff12 INT;

DECLARE v_stddevk DECIMAL(12,6);
DECLARE v_stddev1 DECIMAL(12,6);
DECLARE v_stddev2 DECIMAL(12,6);
DECLARE v_stddev3 DECIMAL(12,6);
DECLARE v_stddev4 DECIMAL(12,6);
DECLARE v_stddev5 DECIMAL(12,6);
DECLARE v_stddev6 DECIMAL(12,6);
DECLARE v_stddev7 DECIMAL(12,6);
DECLARE v_stddev8 DECIMAL(12,6);
DECLARE v_stddev9 DECIMAL(12,6);
DECLARE v_stddev10 DECIMAL(12,6);
DECLARE v_stddev11 DECIMAL(12,6);
DECLARE v_stddev12 DECIMAL(12,6);

drop temporary table if exists median_calc;

create temporary table median_calc
  select
    enrollment_countk,
    enrollment_count1,
    enrollment_count2,
    enrollment_count3,
    enrollment_count4,
    enrollment_count5,
    enrollment_count6,
    enrollment_count7,
    enrollment_count8,
    enrollment_count9,
    enrollment_count10,
    enrollment_count11,
    enrollment_count12
  FROM snapshot_org
  where snapshot_window_id = p_snapshot_window_id
    and org_type='school';

SELECT
  sum(ifnull(enrollment_countk,0)>0) as enrollment_countk,
  sum(ifnull(enrollment_count1,0)>0) as enrollment_count1,
  sum(ifnull(enrollment_count2,0)>0) as enrollment_count2,
  sum(ifnull(enrollment_count3,0)>0) as enrollment_count3,
  sum(ifnull(enrollment_count4,0)>0) as enrollment_count4,
  sum(ifnull(enrollment_count5,0)>0) as enrollment_count5,
  sum(ifnull(enrollment_count6,0)>0) as enrollment_count6,
  sum(ifnull(enrollment_count7,0)>0) as enrollment_count7,
  sum(ifnull(enrollment_count8,0)>0) as enrollment_count8,
  sum(ifnull(enrollment_count9,0)>0) as enrollment_count9,
  sum(ifnull(enrollment_count10,0)>0) as enrollment_count10,
  sum(ifnull(enrollment_count11,0)>0) as enrollment_count11,
  sum(ifnull(enrollment_count12,0)>0) as enrollment_count12
FROM median_calc
into
    v_rowcountk,
    v_rowcount1,
    v_rowcount2,
    v_rowcount3,
    v_rowcount4,
    v_rowcount5,
    v_rowcount6,
    v_rowcount7,
    v_rowcount8,
    v_rowcount9,
    v_rowcount10,
    v_rowcount11,
    v_rowcount12;

/*
SELECT
  @rownum:=@rownum+1 as row_number,
  ifnull(enrollment_countk,0) as enrollment_countk_median
FROM median_calc,
  (SELECT @rownum:=0) r
WHERE
  ifnull(enrollment_countk,0) > 0
ORDER BY enrollment_countk;
*/
select enrollment_countk from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_countk,0) as enrollment_countk
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_countk,0) > 0
    ORDER BY CAST(enrollment_countk as unsigned)
) x where x.row_number = floor(v_rowcountk/2)+1 into v_mediank;

select enrollment_count1 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count1,0) as enrollment_count1
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count1,0) > 0
    ORDER BY CAST(enrollment_count1 as unsigned)
) x where x.row_number = floor(v_rowcount1/2)+1 into v_median1;

select enrollment_count2 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count2,0) as enrollment_count2
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count2,0) > 0
    ORDER BY CAST(enrollment_count2 as unsigned)
) x where x.row_number = floor(v_rowcount2/2)+1 into v_median2;

select enrollment_count3 from ( 
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count3,0) as enrollment_count3
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count3,0) > 0
    ORDER BY CAST(enrollment_count3 as unsigned)
) x where x.row_number = floor(v_rowcount3/2)+1 into v_median3;

select enrollment_count4 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count4,0) as enrollment_count4
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count4,0) > 0
    ORDER BY CAST(enrollment_count4 as unsigned)
) x where x.row_number = floor(v_rowcount4/2)+1 into v_median4;

select enrollment_count5 from ( 
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count5,0) as enrollment_count5
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count5,0) > 0
    ORDER BY CAST(enrollment_count5 as unsigned)
) x where x.row_number = floor(v_rowcount5/2)+1 into v_median5;

select enrollment_count6 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count6,0) as enrollment_count6
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count6,0) > 0
    ORDER BY CAST(enrollment_count6 as unsigned)
) x where x.row_number = floor(v_rowcount6/2)+1 into v_median6;

select enrollment_count7 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count7,0) as enrollment_count7
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count7,0) > 0
    ORDER BY CAST(enrollment_count7 as unsigned)
) x where x.row_number = floor(v_rowcount7/2)+1 into v_median7;

select enrollment_count8 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count8,0) as enrollment_count8
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count8,0) > 0
    ORDER BY CAST(enrollment_count8 as unsigned)
) x where x.row_number = floor(v_rowcount8/2)+1 into v_median8;

select enrollment_count9 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count9,0) as enrollment_count9
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count9,0) > 0
    ORDER BY CAST(enrollment_count9 as unsigned)
) x where x.row_number = floor(v_rowcount9/2)+1 into v_median9;

select enrollment_count10 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count10,0) as enrollment_count10
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count10,0) > 0
    ORDER BY CAST(enrollment_count10 as unsigned)
) x where x.row_number = floor(v_rowcount10/2)+1 into v_median10;

select enrollment_count11 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count11,0) as enrollment_count11
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count11,0) > 0
    ORDER BY CAST(enrollment_count11 as unsigned)
) x where x.row_number = floor(v_rowcount11/2)+1 into v_median11;

select enrollment_count12 from (
    SELECT
      @rownum:=@rownum+1 as row_number,
      ifnull(enrollment_count12,0) as enrollment_count12
    FROM median_calc,
      (SELECT @rownum:=0) r
    WHERE
      ifnull(enrollment_count12,0) > 0
    ORDER BY CAST(enrollment_count12 as unsigned)
) x where x.row_number = floor(v_rowcount12/2)+1 into v_median12;

SELECT
  CAST(sum(if(enrollment_countk>0,pow(enrollment_countk-v_mediank,2),0)) AS SIGNED INTEGER) enrollment_countk,
  CAST(sum(if(enrollment_count1>0,pow(enrollment_count1-v_median1,2),0)) AS SIGNED INTEGER) enrollment_count1,
  CAST(sum(if(enrollment_count2>0,pow(enrollment_count2-v_median2,2),0)) AS SIGNED INTEGER) enrollment_count2,
  CAST(sum(if(enrollment_count3>0,pow(enrollment_count3-v_median3,2),0)) AS SIGNED INTEGER) enrollment_count3,
  CAST(sum(if(enrollment_count4>0,pow(enrollment_count4-v_median4,2),0)) AS SIGNED INTEGER) enrollment_count4,
  CAST(sum(if(enrollment_count5>0,pow(enrollment_count5-v_median5,2),0)) AS SIGNED INTEGER) enrollment_count5,
  CAST(sum(if(enrollment_count6>0,pow(enrollment_count6-v_median6,2),0)) AS SIGNED INTEGER) enrollment_count6,
  CAST(sum(if(enrollment_count7>0,pow(enrollment_count7-v_median7,2),0)) AS SIGNED INTEGER) enrollment_count7,
  CAST(sum(if(enrollment_count8>0,pow(enrollment_count8-v_median8,2),0)) AS SIGNED INTEGER) enrollment_count8,
  CAST(sum(if(enrollment_count9>0,pow(enrollment_count9-v_median9,2),0)) AS SIGNED INTEGER) enrollment_count9,
  CAST(sum(if(enrollment_count10>0,pow(enrollment_count10-v_median10,2),0)) AS SIGNED INTEGER) enrollment_count10,
  CAST(sum(if(enrollment_count11>0,pow(enrollment_count11-v_median11,2),0)) AS SIGNED INTEGER) enrollment_count11,
  CAST(sum(if(enrollment_count12>0,pow(enrollment_count12-v_median12,2),0)) AS SIGNED INTEGER) enrollment_count12
FROM readiness.snapshot_org so
WHERE
  so.snapshot_window_id = p_snapshot_window_id
  AND so.org_type='school'
into
  v_totaldiffk,
  v_totaldiff1,
  v_totaldiff2,
  v_totaldiff3,
  v_totaldiff4,
  v_totaldiff5,
  v_totaldiff6,
  v_totaldiff7,
  v_totaldiff8,
  v_totaldiff9,
  v_totaldiff10,
  v_totaldiff11,
  v_totaldiff12;

SET v_stddevk  = sqrt(v_totaldiffk/v_rowcountk );
SET v_stddev1  = sqrt(v_totaldiff1/v_rowcount1 );
SET v_stddev2  = sqrt(v_totaldiff2/v_rowcount2 );
SET v_stddev3  = sqrt(v_totaldiff3/v_rowcount3 );
SET v_stddev4  = sqrt(v_totaldiff4/v_rowcount4 );
SET v_stddev5  = sqrt(v_totaldiff5/v_rowcount5 );
SET v_stddev6  = sqrt(v_totaldiff6/v_rowcount6 );
SET v_stddev7  = sqrt(v_totaldiff7/v_rowcount7 );
SET v_stddev8  = sqrt(v_totaldiff8/v_rowcount8 );
SET v_stddev9  = sqrt(v_totaldiff9/v_rowcount9 );
SET v_stddev10 = sqrt(v_totaldiff10/v_rowcount10);
SET v_stddev11 = sqrt(v_totaldiff11/v_rowcount11);
SET v_stddev12 = sqrt(v_totaldiff12/v_rowcount12);

update snapshot_window
SET
  calc_enrollment_countk_median  = v_mediank,
  calc_enrollment_count1_median  = v_median1,
  calc_enrollment_count2_median  = v_median2,
  calc_enrollment_count3_median  = v_median3,
  calc_enrollment_count4_median  = v_median4,
  calc_enrollment_count5_median  = v_median5,
  calc_enrollment_count6_median  = v_median6,
  calc_enrollment_count7_median  = v_median7,
  calc_enrollment_count8_median  = v_median8,
  calc_enrollment_count9_median  = v_median9,
  calc_enrollment_count10_median  = v_median10,
  calc_enrollment_count11_median  = v_median11,
  calc_enrollment_count12_median  = v_median12,
  calc_enrollment_countk_stddev  = v_stddevk,
  calc_enrollment_count1_stddev  = v_stddev1,
  calc_enrollment_count2_stddev  = v_stddev2,
  calc_enrollment_count3_stddev  = v_stddev3,
  calc_enrollment_count4_stddev  = v_stddev4,
  calc_enrollment_count5_stddev  = v_stddev5,
  calc_enrollment_count6_stddev  = v_stddev6,
  calc_enrollment_count7_stddev  = v_stddev7,
  calc_enrollment_count8_stddev  = v_stddev8,
  calc_enrollment_count9_stddev  = v_stddev9,
  calc_enrollment_count10_stddev  = v_stddev10,
  calc_enrollment_count11_stddev  = v_stddev11,
  calc_enrollment_count12_stddev  = v_stddev12
where snapshot_window_id = p_snapshot_window_id;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `temp_S64896_network_retro` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE PROCEDURE `temp_S64896_network_retro`(in p_snapshot_window_id BIGINT)
BEGIN
  DECLARE v_scope_id INT;
  DECLARE v_last_execute_date TIMESTAMP;

  select scope_id, execute_date from snapshot_window where snapshot_window_id=p_snapshot_window_id into v_scope_id, v_last_execute_date;

  -- ===============================================================================
  -- pivot the scope configuration attributes to make them easy to access
  drop temporary table if exists scope_config;
  create temporary table scope_config
  select
    CAST(max(if(sc.code='includeGradeK',sc.value,0)) as UNSIGNED) tests_per_gradeK,
    CAST(max(if(sc.code='includeGrade1',sc.value,0)) as UNSIGNED) tests_per_grade1,
    CAST(max(if(sc.code='includeGrade2',sc.value,0)) as UNSIGNED) tests_per_grade2,
    CAST(max(if(sc.code='includeGrade3',sc.value,0)) as UNSIGNED) tests_per_grade3,
    CAST(max(if(sc.code='includeGrade4',sc.value,0)) as UNSIGNED) tests_per_grade4,
    CAST(max(if(sc.code='includeGrade5',sc.value,0)) as UNSIGNED) tests_per_grade5,
    CAST(max(if(sc.code='includeGrade6',sc.value,0)) as UNSIGNED) tests_per_grade6,
    CAST(max(if(sc.code='includeGrade7',sc.value,0)) as UNSIGNED) tests_per_grade7,
    CAST(max(if(sc.code='includeGrade8',sc.value,0)) as UNSIGNED) tests_per_grade8,
    CAST(max(if(sc.code='includeGrade9',sc.value,0)) as UNSIGNED) tests_per_grade9,
    CAST(max(if(sc.code='includeGrade10',sc.value,0)) as UNSIGNED) tests_per_grade10,
    CAST(max(if(sc.code='includeGrade11',sc.value,0)) as UNSIGNED) tests_per_grade11,
    CAST(max(if(sc.code='includeGrade12',sc.value,0)) as UNSIGNED) tests_per_grade12,
    CAST(max(if(sc.code='minimumTestingWindowLength',sc.value,null)) as UNSIGNED) min_testing_window_length,
    CAST(max(if(sc.code='minimumThroughputRequiredPerStudent',sc.value,null)) as UNSIGNED) min_throughput_per_student,
    CAST(max(if(sc.code='recommendedTestingWindowLength',sc.value,null)) as UNSIGNED) rec_testing_window_length,
    CAST(max(if(sc.code='recommendedThroughputRequiredPerStudent',sc.value,null)) as UNSIGNED) rec_throughput_per_student
  from snapshot_config sc
  where sc.snapshot_window_id = p_snapshot_window_id
  group by sc.snapshot_window_id;


  drop temporary table if exists school_detail ;
  create temporary table school_detail (
    org_id INT UNSIGNED NOT NULL,
    min_testtaker_possible_test_count INT,
    min_testtaker_percent DECIMAL(15,6),
    min_network_count INT,
    min_network_percent DECIMAL(15,6),
    rec_testtaker_possible_test_count INT,
    rec_testtaker_percent DECIMAL(15,6),
    rec_network_count INT,
    rec_network_percent DECIMAL(15,6),
    PRIMARY KEY (org_id)
  );
  insert into school_detail
    select
      so.org_id,
      so.sessions_per_day*so.min_testing_window_length*so.min_device_passing_count min_testtaker_count,
      so.sessions_per_day*so.min_testing_window_length*so.min_device_passing_count/so.calc_testing_teststart_count min_testtaker_percent,
      (least (
        ((100-so.internet_utilization)/100) * so.internet_speed * 1000,
        ((100-so.network_utilization)/100) * so.network_speed * 1000
      ) / scope_config.min_throughput_per_student) * least(so.min_testing_window_length,scope_config.min_testing_window_length) min_network_count,
      round(((least (
        ((100-so.internet_utilization)/100) * so.internet_speed * 1000,
        ((100-so.network_utilization)/100) * so.network_speed * 1000
      ) / scope_config.min_throughput_per_student) * least(so.min_testing_window_length,scope_config.min_testing_window_length))) / so.calc_testing_teststart_count min_network_percent,
      so.sessions_per_day*so.rec_testing_window_length*so.rec_device_passing_count rec_testtaker_count,
      so.sessions_per_day*so.rec_testing_window_length*so.rec_device_passing_count/so.calc_testing_teststart_count rec_testtaker_percent,
      (least (
        ((100-so.internet_utilization)/100) * so.internet_speed * 1000,
        ((100-so.network_utilization)/100) * so.network_speed * 1000
      ) / scope_config.rec_throughput_per_student) * least(so.rec_testing_window_length,scope_config.rec_testing_window_length) rec_network_count,
      round(((least (
        ((100-so.internet_utilization)/100) * so.internet_speed * 1000,
        ((100-so.network_utilization)/100) * so.network_speed * 1000
      ) / scope_config.rec_throughput_per_student) * least(so.rec_testing_window_length,scope_config.rec_testing_window_length))) / so.calc_testing_teststart_count rec_network_percent
    from snapshot_org so,
      scope_config
    where
      so.org_type = 'school'
      and so.snapshot_window_id = p_snapshot_window_id;

  update snapshot_org so
    left join school_detail sd on sd.org_id = so.org_id,
    scope_config
  set
    so.min_testtaker_possible_test_count = sd.min_testtaker_possible_test_count,
    so.min_testtaker_percent_students_testable = if(so.calc_device_count > 0, least(sd.min_testtaker_percent*100,999), null),
    so.min_network_possible_test_count = sd.min_network_count,
    so.min_network_percent_students_testable = least(sd.min_network_percent*100,999),
    so.rec_testtaker_possible_test_count = sd.rec_testtaker_possible_test_count,
    so.rec_testtaker_percent_students_testable = if(so.calc_device_count > 0, least(sd.rec_testtaker_percent*100,999), null),
    so.rec_network_possible_test_count = sd.rec_network_count,
    so.rec_network_percent_students_testable = least(sd.rec_network_percent*100,999)
  where
    so.org_type = 'school'
    and so.snapshot_window_id = p_snapshot_window_id;

  drop temporary table if exists school_detail_rollup ;
  create temporary table school_detail_rollup(
    org_id INT UNSIGNED NOT NULL,
    test_start_count INT,
    min_testtaker_possible_test_count INT,
    min_network_possible_test_count INT,
    rec_testtaker_possible_test_count INT,
    rec_network_possible_test_count INT,
    PRIMARY KEY (org_id)
  );

  insert into school_detail_rollup
    select
      aso.org_id,
      sum(so.calc_testing_teststart_count) test_start_count,
      sum(least(ifnull(so.min_testtaker_possible_test_count,0),ifnull(so.calc_testing_teststart_count,0))) min_testtaker_possible_test_count,
      sum(least(ifnull(so.min_network_possible_test_count,0),ifnull(so.calc_testing_teststart_count,0))) min_network_possible_test_count,
      sum(least(ifnull(so.rec_testtaker_possible_test_count,0),ifnull(so.calc_testing_teststart_count,0))) rec_testtaker_possible_test_count,
      sum(least(ifnull(so.rec_network_possible_test_count,0),ifnull(so.calc_testing_teststart_count,0))) rec_network_possible_test_count
    from snapshot_org aso
      left join core.org_tree otree on otree.ancestor_org_id = aso.org_id and otree.distance > 0
      left join snapshot_org so on so.org_id = otree.org_id and so.snapshot_window_id = aso.snapshot_window_id and so.org_type = 'school'
      left join school_detail sd on sd.org_id = otree.org_id
    where aso.snapshot_window_id = p_snapshot_window_id
      and aso.org_type <> 'school'
    group by aso.org_id;

  update snapshot_org so
    left join school_detail_rollup sdr on sdr.org_id = so.org_id,
    scope_config
  set
    so.calc_testing_teststart_count = sdr.test_start_count,
    so.min_testtaker_possible_test_count = ifnull(sdr.min_testtaker_possible_test_count,0),
    so.min_testtaker_percent_students_testable = least((ifnull(sdr.min_testtaker_possible_test_count,0)/ifnull(sdr.test_start_count,0))*100,999),
    so.min_network_possible_test_count = sdr.min_network_possible_test_count,
    so.min_network_percent_students_testable = least((ifnull(sdr.min_network_possible_test_count,0)/ifnull(sdr.test_start_count,0))*100,999),
    so.rec_network_possible_test_count = sdr.rec_network_possible_test_count,
    so.rec_network_percent_students_testable = least((ifnull(sdr.rec_network_possible_test_count,0)/ifnull(sdr.test_start_count,0))*100,999),
    so.rec_testtaker_possible_test_count = ifnull(sdr.rec_testtaker_possible_test_count,0),
    so.rec_testtaker_percent_students_testable = least((ifnull(sdr.rec_testtaker_possible_test_count,0)/ifnull(sdr.test_start_count,0))*100,999)
  where so.snapshot_window_id = p_snapshot_window_id
    and so.org_type <> 'school';
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

-- Dump completed on 2014-02-07 15:05:41
