drop database if exists foodjournal;

create database foodjournal;

use foodjournal;

drop table if exists users;

create table users (

	-- Generate a unique 8 character order id for the order
	user_id varchar(8),

	-- Your name as in your NRIC
	name varchar(128) not null,

	email varchar(256) not null,

	password varchar(128) not null,

	role enum("USER", "ADMIN"),

	target integer not null,


	primary key(user_id)
);


drop table if exists days;

create table days (

	day_id varchar(8),

    day date not null,

    calories decimal(6,1),

	primary key(day_id),

    user_id varchar(8),

    constraint fk_user_id
        foreign key(user_id)
        references users(user_id)
    
);