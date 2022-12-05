create sequence statistic_seq
    increment by 50;

alter sequence statistic_seq owner to "user";

create table public.statistics
(
    id        BIGINT        NOT NULL,
    app       VARCHAR(255)  NOT NULL,
    uri       VARCHAR(2000) NOT NULL,
    ip        VARCHAR(45)   NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_statistics PRIMARY KEY (id)
);

alter table public.statistics
    owner to "user";