


create table if not exists applications (
--  id serial not null
  id varchar(150) not null default random_uuid()
  , application_id varchar(150) not null
  , name varchar(350) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
--  , unique (id)
  , unique (application_id)
);
create unique index if not exists applications_idx1_id on applications(application_id);


create table if not exists app_resources (
--  id serial not null
  id varchar(150) not null default random_uuid()
  , app_resource_id varchar(150) not null
  , name varchar(350) not null
  , application_id varchar(150) not null
--  , access_right varchar(150) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
--  , unique (application_id, id)
  , unique (app_resource_id)
  , constraint app_resources_fk1_application_id foreign key(application_id) references applications(application_id) on delete set null
);
--comment on column app_resources.access_right is 'Resource requires access right';
--create unique index if not exists app_resources_idx1_id on app_resources(application_id, id);
create unique index if not exists app_resources_idx1_app_resource_id on app_resources(app_resource_id);


