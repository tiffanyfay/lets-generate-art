create table if not exists image_prompt
(
    prompt   text  not null,
    url      text  not null,
    image_id UUID  not null primary key,
);