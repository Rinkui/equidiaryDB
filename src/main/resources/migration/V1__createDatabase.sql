create table HORSE
(
    id         serial primary key not null,
    horse_name varchar(150)       not null,
    height     int                not null,
    weight     int,
    birth_date date               not null,
    uuid       varchar(50)        not null
);

create table APPOINTMENT
(
    id               serial primary key not null,
    appointment_date date               not null,
    appointment_type varchar(50)        not null,
    comment          varchar(512),
    horseId          int,
    uuid             varchar(50)        not null,
    FOREIGN KEY (horseId) REFERENCES horse (id)
);

create table EQUIDIARY_USER
(
    user_name varchar(100) not null,
    horseID   int,
    FOREIGN KEY (horseID) REFERENCES horse (id)
);