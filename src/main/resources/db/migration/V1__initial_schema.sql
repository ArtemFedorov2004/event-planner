create table t_event
(
    id       serial primary key,
    c_name   varchar not null CHECK ( char_length(c_name) >= 1),
    c_date   timestamp,
    c_budget numeric not null default 0
);

create table t_task
(
    id         serial primary key,
    c_name     varchar not null CHECK ( char_length(c_name) >= 1),
    c_note     varchar,
    c_category varchar,
    c_date     timestamp,
    c_status   varchar,
    id_event   int     not null,
    constraint fk__t_task__t_event
        foreign key (id_event)
            references t_event (id)
            on delete cascade
);