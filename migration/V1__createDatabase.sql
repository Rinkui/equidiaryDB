create table horse (
 id serial primary key not null,
 horse_name varchar(150) not null,
 height int not null,
 weight int,
 birth_date date not null
);

create table appointment (
 id serial primary key not null,
 appointment_date date not null,
 type varchar(50) not null,
 comment varchar (512),
 horseId int,
 FOREIGN KEY (horseId) REFERENCES horse(id)
);

create table equidiary_user (
 user_name varchar(100) not null,
 horseID int,
 FOREIGN KEY (horseID) REFERENCES horse(id)
);