DROP TABLE IF EXISTS customers;
CREATE TABLE IF NOT EXISTS `customers` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
);
