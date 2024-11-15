insert into t_event(id, c_name, c_date, c_budget)
values (1, 'event_1', '2025-10-16 14:30:00', 100),
       (2, 'event_2', '2025-10-16 14:30:00', 1000);

insert into t_task(c_name, id_event)
values ('task_1', 1),
       ('task_2', 1),
       ('task_3', 1)