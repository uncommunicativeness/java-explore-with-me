create sequence public.category_seq
    increment by 50;

alter sequence public.category_seq owner to "user";

create sequence public.compilation_seq
    increment by 50;

alter sequence public.compilation_seq owner to "user";

create sequence public.event_seq
    increment by 50;

alter sequence public.event_seq owner to "user";

create sequence public.request_seq
    increment by 50;

alter sequence public.request_seq owner to "user";

create sequence public.users_seq
    increment by 50;

alter sequence public.users_seq owner to "user";


create table public.categories
(
    id   bigint       not null
        primary key,
    name varchar(255) not null
        constraint categories_name_unique
            unique
);

alter table public.categories
    owner to "user";

create table public.compilations
(
    id     bigint       not null
        primary key,
    pinned boolean,
    title  varchar(255) not null
);

alter table public.compilations
    owner to "user";

create table public.users
(
    id    bigint       not null
        primary key,
    email varchar(255) not null
        constraint users_email_unique
            unique,
    name  varchar(255) not null
);

alter table public.users
    owner to "user";

create table public.events
(
    id                 bigint       not null
        primary key,
    annotation         text         not null,
    confirmed_requests integer,
    created_on         timestamp,
    description        text         not null,
    event_date         timestamp,
    location_lat       real,
    location_lon       real,
    paid               boolean,
    participant_limit  integer,
    published_on       timestamp,
    request_moderation boolean,
    state              varchar(255),
    title              varchar(255) not null,
    views              integer,
    category_id        bigint
        constraint events_categories_const
            references public.categories,
    initiator_id       bigint
        constraint events_users_const
            references public.users
);

alter table public.events
    owner to "user";

create table public.compilations_events
(
    compilation_id bigint not null
        constraint compilations_events_compilations_const
            references public.compilations,
    events_id      bigint not null
        constraint compilations_events_events_const
            references public.events,
    primary key (compilation_id, events_id)
);

alter table public.compilations_events
    owner to "user";

create table public.requests
(
    id       bigint not null
        primary key,
    created  timestamp,
    state    varchar(255),
    event_id bigint
        constraint requests_events_const
            references public.events,
    user_id  bigint
        constraint requests_user_const
            references public.users,
    constraint uc_request_user_id_event_id
        unique (user_id, event_id)
);

alter table public.requests
    owner to "user";

