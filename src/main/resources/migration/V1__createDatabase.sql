create table owner
(
    uuid      varchar(50) primary key not null,
    user_name varchar(100) unique     not null,
    password  bytea                   not null
);

create table horse
(
    uuid       varchar(50) primary key not null,
    horse_name varchar(150)            not null,
    height     int                     not null,
    weight     int,
    birth_date date                    not null,
    userUuid   varchar(50),
    FOREIGN KEY (userUuid) REFERENCES owner (uuid)
);

create table appointment
(
    uuid             varchar(50) primary key not null,
    appointment_date date                    not null,
    appointment_type varchar(50)             not null,
    comment          varchar(512),
    horseUuid        varchar(50),
    FOREIGN KEY (horseUuid) REFERENCES horse (uuid)
);