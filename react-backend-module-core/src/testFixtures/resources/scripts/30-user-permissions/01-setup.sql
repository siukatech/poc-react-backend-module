


create table if not exists user_roles (
--  id serial not null
  id varchar(150) not null default random_uuid()
  , user_role_id varchar(150) not null
  , name varchar(350) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , unique (user_role_id)
);
create unique index if not exists user_roles_idx1_id on user_roles(user_role_id);



create table if not exists user_permissions (
--  id serial not null
  id varchar(150) not null default random_uuid()
  , user_role_id varchar(150) not null
  , application_id varchar(150) not null
  , app_resource_id varchar(150) not null
  , access_right varchar(150) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , constraint user_permissions_fk1_user_role_id foreign key(user_role_id) references user_roles(user_role_id) on delete set null
--  , constraint user_permissions_fk2_app_resource_id foreign key(application_id, app_resource_id) references app_resources(application_id, id) on delete set null
  , constraint user_permissions_fk2_app_resource_id foreign key(app_resource_id) references app_resources(app_resource_id) on delete set null
  , constraint user_permissions_fk3_application_id foreign key(application_id) references applications(application_id) on delete set null
);


create table if not exists user_role_users (
--  id serial not null
  id varchar(150) not null default random_uuid()
--  , user_role_id int not null
--  , user_id int not null
  , user_role_id varchar(150) not null
  , user_id varchar(150) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , constraint user_role_users_fk1_user_role_id foreign key(user_role_id) references user_roles(user_role_id) on delete set null
  , constraint user_role_users_fk2_user_id foreign key(user_id) references users(user_id) on delete set null
);

