

create table if not exists users (
--  id serial not null
  id varchar(150) not null default random_uuid()
--  , mid varchar(150) not null
  , user_id varchar(350) not null
  , name varchar(350) not null
  , public_key text
  , private_key text
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , unique (user_id)
);
create index if not exists users_idx1_user_id on users(user_id);


