create table event
(
    id     serial primary key,
    name varchar not null CHECK ( char_length(name) >= 1),
    date   timestamp        default current_timestamp,
    budget numeric not null default 0
);