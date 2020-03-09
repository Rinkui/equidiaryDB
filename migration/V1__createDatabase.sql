--create database if not exists equidiary

create table horse (
 id serial primary key not null,
 horse_name varchar(150) not null,
 height int not null,
 weight int,
 birth_date date not null
)
--
--create table `equidiary`.`horse` (
--  `horseId` INT NOT NULL AUTO_INCREMENT,
--  `name` VARCHAR(64) NOT NULL,
--  `height` INT NOT NULL,
--  `width` INT NOT NULL,
--  `weight` INT NOT NULL,
--  `birthdate` DATE NOT NULL,
--  PRIMARY KEY (`horseId`));
--
--create table `equidiary`.`user_horse` (
--  `userId` INT NOT NULL,
--  `horseId` INT NOT NULL,
--  PRIMARY KEY (`userId`, `horseId`));
--
--create table `equidiary`.`appointment` (
--  `appointmentId` INT NOT NULL AUTO_INCREMENT,
--  `horseId` INT NOT NULL,
--  `date` DATETIME NOT NULL,
--  `type` VARCHAR(45) NOT NULL,
--  `comments` TEXT(512) NOT NULL,
--  PRIMARY KEY (`appointmentId`));
--
--alter table `equidiary`.`user_horse`
--ADD INDEX `fk_horseId_idx` (`horseId` ASC) VISIBLE;
--;
--alter table `equidiary`.`user_horse`
--ADD CONSTRAINT `fk_iduser`
--  FOREIGN KEY (`userId`)
--  REFERENCES `equidiary`.`user` (`userId`)
--  ON delete NO ACTION
--  ON update NO ACTION,
--ADD CONSTRAINT `fk_horseId`
--  FOREIGN KEY (`horseId`)
--  REFERENCES `equidiary`.`horse` (`horseId`)
--  ON delete NO ACTION
--  ON update NO ACTION;
--
--alter table `equidiary`.`appointment`
--ADD INDEX `fk_horseId_idx` (`horseId` ASC) VISIBLE;
--;
--alter table `equidiary`.`appointment`
--ADD CONSTRAINT `fk_horseId2`
--  FOREIGN KEY (`horseId`)
--  REFERENCES `equidiary`.`horse` (`horseId`)
--  ON delete NO ACTION
--  ON update NO ACTION;