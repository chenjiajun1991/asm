alter table user_follow add column follower_name varchar(32) after battery_id;

CREATE TABLE `battery_info_newest` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `battery_id` int(11) NOT NULL,
  `longitude` varchar(30) NOT NULL,
  `latitude` varchar(30) NOT NULL,
  `temperature` varchar(30),
  `voltage` varchar(30) NOT NULL,
  `status` char(1) NOT NULL DEFAULT '0',
  `extension` varchar(200),
  `receive_date` datetime NOT NULL,
  `sample_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_BTYINFOS` (`battery_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;