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


	primary key(user_id)
);