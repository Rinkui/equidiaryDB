CREATE SCHEMA `equidiary`;

CREATE TABLE `equidiary`.`user` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `password` CHAR(64) NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);

CREATE TABLE `equidiary`.`horse` (
  `horseId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `height` INT NOT NULL,
  `width` INT NOT NULL,
  `weight` INT NOT NULL,
  `birthdate` DATE NOT NULL,
  PRIMARY KEY (`horseId`));

CREATE TABLE `equidiary`.`user_horse` (
  `userId` INT NOT NULL,
  `horseId` INT NOT NULL,
  PRIMARY KEY (`userId`, `horseId`));

CREATE TABLE `equidiary`.`appointment` (
  `appointmentId` INT NOT NULL AUTO_INCREMENT,
  `horseId` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `comments` TEXT(512) NOT NULL,
  PRIMARY KEY (`appointmentId`));

ALTER TABLE `equidiary`.`user_horse` 
ADD INDEX `fk_horseId_idx` (`horseId` ASC) VISIBLE;
;
ALTER TABLE `equidiary`.`user_horse` 
ADD CONSTRAINT `fk_iduser`
  FOREIGN KEY (`userId`)
  REFERENCES `equidiary`.`user` (`userId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_horseId`
  FOREIGN KEY (`horseId`)
  REFERENCES `equidiary`.`horse` (`horseId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `equidiary`.`appointment` 
ADD INDEX `fk_horseId_idx` (`horseId` ASC) VISIBLE;
;
ALTER TABLE `equidiary`.`appointment` 
ADD CONSTRAINT `fk_horseId2`
  FOREIGN KEY (`horseId`)
  REFERENCES `equidiary`.`horse` (`horseId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;