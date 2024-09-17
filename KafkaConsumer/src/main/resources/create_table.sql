create schema purchased

create table purchased.ticket(id bigserial primary key,
route varchar,
datetime timestamp,
seat int2,
price numeric,
client varchar
)