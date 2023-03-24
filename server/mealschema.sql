use foodjournal;

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