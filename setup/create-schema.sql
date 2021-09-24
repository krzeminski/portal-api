-- -----------------------------------------------------
-- Schema notes-portal
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `notes-portal`;

CREATE SCHEMA `notes-portal`;
USE `notes-portal` ;

-- -----------------------------------------------------
-- Table `notes-portal`.`award`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `award`;
CREATE TABLE `award` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `rank` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `notes-portal`.`user`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `join_date` datetime(6) DEFAULT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `locked` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `notes-portal`.`note`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `value` int DEFAULT NULL,
  `creation_date` datetime(6) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_user_id` (`user_id`),
  CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `notes-portal`.`distributed_awards`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `distributed_awards`;
CREATE TABLE `distributed_awards` (
  `user_id` bigint NOT NULL,
  `award_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`award_id`),
  KEY `FK_award_id` (`award_id`),
  CONSTRAINT `FK_award_id` FOREIGN KEY (`award_id`) REFERENCES `award` (`id`),
  CONSTRAINT `FK_awarded_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Fill award table
-- -----------------------------------------------------

INSERT INTO `notes-portal`.`award` (`id`, `name`, `type`, `rank`) VALUES ('1', 'legenda', 'legendarny', '5');
INSERT INTO `notes-portal`.`award` (`id`, `name`, `type`, `rank`) VALUES ('2', 'inspirujący', 'unikalny', '4');
INSERT INTO `notes-portal`.`award` (`id`, `name`, `type`, `rank`) VALUES ('3', 'interesujący', 'epicki', '3');
INSERT INTO `notes-portal`.`award` (`id`, `name`, `type`, `rank`) VALUES ('4', 'ciekawy', 'rzadki', '2');
INSERT INTO `notes-portal`.`award` (`id`, `name`, `type`, `rank`) VALUES ('5', 'pomocny', 'normalny', '1');


-- -----------------------------------------------------
-- Fill user table
-- -----------------------------------------------------

INSERT INTO `notes-portal`.`user` (`id`, `email`, `user_name`, `first_name`, `last_name`, `password`, `join_date`, `profile_image_url`, `role`, `active`, `locked`)
VALUES ('1', 'admin@portal.com', 'Admin', 'Adrian', 'Krzeminski', 'admin123',  '2020-09-20 22:00:00.000000', null, 'ADMIN', b'1', b'0');
INSERT INTO `notes-portal`.`user` (`id`, `email`, `user_name`, `first_name`, `last_name`, `password`, `join_date`, `profile_image_url`, `role`, `active`, `locked`)
 VALUES ('2', 'moderator@portal.com', 'Moderator', 'Adrian', 'Krzeminski', 'admin123', '20.09.2021', null , 'MODERATOR', b'1', b'0');
INSERT INTO `notes-portal`.`user` (`id`, `email`, `user_name`, `first_name`, `last_name`, `password`, `join_date`, `profile_image_url`, `role`, `active`, `locked`)
 VALUES ('3', 'user@portal.com', 'Użytkownik', 'Adrian', 'Krzeminski', 'admin123', '20.09.2021', null, 'USER', b'1', b'0');

-- -----------------------------------------------------
-- Fill notes table
-- -----------------------------------------------------

INSERT INTO `note` VALUES
(1,'Pierwsza notatka','Opis pierwszej notatki',5,'2020-09-20 22:00:00.000000','2020-09-20 22:00:00.000000',1),
(2,'Ciekawy tytuł','Jeszcze ciekawsza treść',2,'2020-09-20 21:00:00.000000',NULL,1),
(3,'Cudowny dzień dla nauki','Potrafimy odzyskiwać ponad 95% tworzyw z paneli słonecznych',6,'2020-09-20 21:00:00.000000',NULL,2);

-- -----------------------------------------------------
-- Fill award-user relation table
-- -----------------------------------------------------

INSERT INTO `notes-portal`.`distributed_awards` (`user_id`, `award_id`) VALUES ('1', '1');
INSERT INTO `notes-portal`.`distributed_awards` (`user_id`, `award_id`) VALUES ('1', '2');
INSERT INTO `notes-portal`.`distributed_awards` (`user_id`, `award_id`) VALUES ('1', '3');
INSERT INTO `notes-portal`.`distributed_awards` (`user_id`, `award_id`) VALUES ('1', '4');
INSERT INTO `notes-portal`.`distributed_awards` (`user_id`, `award_id`) VALUES ('1', '5');
INSERT INTO `notes-portal`.`distributed_awards` (`user_id`, `award_id`) VALUES ('2', '1');
INSERT INTO `notes-portal`.`distributed_awards` (`user_id`, `award_id`) VALUES ('2', '3');
INSERT INTO `notes-portal`.`distributed_awards` (`user_id`, `award_id`) VALUES ('3', '5');
