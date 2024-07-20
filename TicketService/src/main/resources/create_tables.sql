create table ticket(id bigserial primary key,
route bigint,
datetime timestamp,
seat int2,
price numeric,
client bigint
)

create table client(id bigserial primary key,
login varchar unique,
password varchar,
fullname varchar,
roles varchar
)

create table company(id bigserial primary key,
title varchar,
phone varchar
)

create table route(id bigserial primary key,
destination varchar,
departure varchar,
company bigint,
duration int
)


