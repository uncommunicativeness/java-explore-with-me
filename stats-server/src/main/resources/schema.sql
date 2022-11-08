create sequence statistic_seq
    increment by 50;

alter sequence statistic_seq owner to "user";

create table public.statistics
(
    id        bigint       not null
        primary key,
    app       varchar(255) not null,
    ip        varchar(255) not null,
    timestamp timestamp    not null,
    uri       varchar(255) not null
);

alter table public.statistics
    owner to "user";

