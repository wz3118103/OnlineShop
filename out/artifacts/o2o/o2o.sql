DROP TABLE IF EXISTS `tb_user_award_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_award_map` (
  `user_award_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `award_id` int(10) NOT NULL,
  `shop_id` int(10) NOT NULL,
  `operator_id` int(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `used_status` int(2) NOT NULL DEFAULT '0',
  `point` int(10) DEFAULT NULL,
  PRIMARY KEY (`user_award_id`),
  KEY `fk_user_award_map_profile` (`user_id`),
  KEY `fk_user_award_map_award` (`award_id`),
  KEY `fk_user_award_map_shop` (`shop_id`),
  KEY `fk_user_award_map_operator` (`operator_id`),
  CONSTRAINT `fk_user_award_map_award` FOREIGN KEY (`award_id`) REFERENCES `tb_award` (`award_id`),
  CONSTRAINT `fk_user_award_map_operator` FOREIGN KEY (`operator_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_user_award_map_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_user_award_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `tb_user_product_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_product_map` (
  `user_product_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) DEFAULT NULL,
  `product_id` int(100) DEFAULT NULL,
  `shop_id` int(10) DEFAULT NULL,
  `operator_id` int(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `point` int(10) DEFAULT '0',
  PRIMARY KEY (`user_product_id`),
  KEY `fk_user_product_map_profile` (`user_id`),
  KEY `fk_user_product_map_product` (`product_id`),
  KEY `fk_user_product_map_shop` (`shop_id`),
  KEY `fk_user_product_map_operator` (`operator_id`),
  CONSTRAINT `fk_user_product_map_operator` FOREIGN KEY (`operator_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_user_product_map_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`),
  CONSTRAINT `fk_user_product_map_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_user_product_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tb_user_shop_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_shop_map` (
  `user_shop_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `shop_id` int(10) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `point` int(10) DEFAULT NULL,
  PRIMARY KEY (`user_shop_id`),
  UNIQUE KEY `uq_user_shop` (`user_id`,`shop_id`),
  KEY `fk_user_shop_shop` (`shop_id`),
  CONSTRAINT `fk_user_shop_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`),
  CONSTRAINT `fk_user_shop_user` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tb_product_sell_daily`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_product_sell_daily` (
  `product_sell_daily_id` int(100) NOT NULL AUTO_INCREMENT,
  `product_id` int(100) DEFAULT NULL,
  `shop_id` int(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `total` int(10) DEFAULT '0',
  PRIMARY KEY (`product_sell_daily_id`),
  UNIQUE KEY `uc_product_sell` (`product_id`,`shop_id`,`create_time`),
  KEY `fk_product_sell_product` (`product_id`),
  KEY `fk_product_sell_shop` (`shop_id`),
  CONSTRAINT `fk_product_sell_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`),
  CONSTRAINT `fk_product_sell_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_product_sell_daily`
--

LOCK TABLES `tb_product_sell_daily` WRITE;
/*!40000 ALTER TABLE `tb_product_sell_daily` DISABLE KEYS */;
INSERT INTO `tb_product_sell_daily` VALUES (87,7,28,'2019-11-18 00:00:00',1),(88,7,28,'2019-11-17 00:00:00',3),(89,7,28,'2019-11-16 00:00:00',2),(90,7,28,'2019-11-15 00:00:00',5),(91,7,28,'2019-11-14 00:00:00',3),(92,7,28,'2019-11-13 00:00:00',4),(93,7,28,'2019-11-12 00:00:00',2),(96,8,28,'2019-11-18 00:00:00',2),(97,8,28,'2019-11-17 00:00:00',2),(98,8,28,'2019-11-16 00:00:00',3),(99,8,28,'2019-11-15 00:00:00',3),(100,8,28,'2019-11-14 00:00:00',4),(101,8,28,'2019-11-13 00:00:00',4),(102,8,28,'2019-11-12 00:00:00',5),(103,9,28,'2019-11-18 00:00:00',7),(104,9,28,'2019-11-17 00:00:00',6),(105,9,28,'2019-11-16 00:00:00',5),(106,9,28,'2019-11-15 00:00:00',4),(107,9,28,'2019-11-14 00:00:00',1),(108,9,28,'2019-11-13 00:00:00',2),(109,9,28,'2019-11-12 00:00:00',3),(116,10,28,'2019-11-18 00:00:00',0),(117,11,28,'2019-11-18 00:00:00',0),(131,10,28,'2019-11-07 00:00:00',0),(132,11,28,'2019-11-07 00:00:00',0),(133,12,28,'2019-11-07 00:00:00',0),(134,8,28,'2019-11-07 00:00:00',0),(135,9,28,'2019-11-07 00:00:00',0);
/*!40000 ALTER TABLE `tb_product_sell_daily` ENABLE KEYS */;
UNLOCK TABLES;




DROP TABLE IF EXISTS `tb_shop_auth_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_shop_auth_map` (
  `shop_auth_id` int(10) NOT NULL AUTO_INCREMENT,
  `employee_id` int(10) NOT NULL,
  `shop_id` int(10) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `title_flag` int(2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`shop_auth_id`),
  KEY `fk_shop_auth_map_shop` (`shop_id`),
  KEY `uk_shop_auth_map` (`employee_id`,`shop_id`),
  CONSTRAINT `fk_shop_auth_map_employee` FOREIGN KEY (`employee_id`) REFERENCES `tb_person_info` (`user_id`),
  CONSTRAINT `fk_shop_auth_map_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;