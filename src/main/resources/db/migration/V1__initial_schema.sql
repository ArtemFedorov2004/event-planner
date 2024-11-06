create table event
(
    id     serial primary key,
    name   varchar not null,
    date   timestamp        default current_timestamp,
    budget numeric not null default 0
);