create table equidiary_user
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
    FOREIGN KEY (userUuid) REFERENCES equidiary_user (uuid)
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

create table professional
(
    uuid       varchar(50) primary key not null,
    first_name varchar(50)             not null,
    last_name  varchar(50)             not null,
    profession varchar(50)             not null
);

create table horse_professional
(
    pro_uuid   varchar(50) not null,
    horse_uuid varchar(50) not null,
    FOREIGN KEY (pro_uuid) REFERENCES professional (uuid),
    FOREIGN KEY (horse_uuid) REFERENCES horse (uuid)
);

create table user_professional
(
    pro_uuid  varchar(50) not null,
    user_uuid varchar(50) not null,
    FOREIGN KEY (pro_uuid) REFERENCES professional (uuid),
    FOREIGN KEY (user_uuid) REFERENCES equidiary_user (uuid)
);