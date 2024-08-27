create table if not exists image_prompt
(
    prompt text not null,
    url    text not null,
    id     serial primary key
);