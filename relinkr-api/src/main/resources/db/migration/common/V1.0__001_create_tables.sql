CREATE TABLE click (
  id bigint NOT NULL,
  link_id bigint NULL,
  visit_date date NULL,
  visit_day_of_month smallint NOT NULL,
  visit_day_of_week smallint NOT NULL,
  visit_hour smallint NOT NULL,
  visit_month smallint NOT NULL,
  visit_timestamp timestamp NULL,
  visitor_id bigint NULL,
  user_id bigint NULL,
  ip_address varchar(255) NULL,
  ip_address_type smallint NULL,
  country varchar(255) NULL,
  created_date timestamp NULL,
  last_modified_date timestamp NULL,
  version_ integer not null,
  CONSTRAINT click_pk PRIMARY KEY (id)
);

CREATE TABLE link (
  id bigint NOT NULL,
  link_status varchar(255) NULL,
  long_url varchar(255) NULL,
  utm_campaign varchar(255) NULL,
  utm_content varchar(255) NULL,
  utm_medium varchar(255) NULL,
  utm_source varchar(255) NULL,
  utm_term varchar(255) NULL,
  path_ varchar(255) NOT NULL,
  user_id bigint NULL,
  created_date timestamp NULL,
  last_modified_date timestamp NULL,
  version_ integer not null,
  CONSTRAINT link_pk PRIMARY KEY (id)
);

-- TODO: define tag as a standalone ownable entity
CREATE TABLE link_tags (
  link_id bigint NOT NULL,
  tag_name varchar(255) NULL,
  CONSTRAINT link_tags_link_id_fk FOREIGN KEY (link_id) REFERENCES link(id)
);

CREATE TABLE user_ (
  id bigint NOT NULL,
  email_address varchar(255) NULL,
  encrypted_password varchar(255) NULL,
  time_zone VARCHAR(30),
  locale VARCHAR(5),
  confirmed boolean NOT NULL,
  locked boolean NOT NULL,
  created_date timestamp NULL,
  last_modified_date timestamp NULL,
  version_ integer not null,
  CONSTRAINT user_pk PRIMARY KEY (id),
  CONSTRAINT user_uk UNIQUE (email_address)
);

CREATE TABLE user_profile (
  user_id bigint NOT NULL,
  user_profile_type VARCHAR(10) NOT NULL,
  user_profile_id VARCHAR(50) NOT NULL,
  full_name VARCHAR(150),
  given_name VARCHAR(50),
  middle_name VARCHAR(50),
  family_name VARCHAR(50),
  profile_url VARCHAR(255),
  picture_url VARCHAR(255),
  gender VARCHAR(10),
  birth_date DATE,
  CONSTRAINT user_profile_pk PRIMARY KEY (user_id, user_profile_type),
  CONSTRAINT user_profile_uk UNIQUE (user_profile_type, user_profile_id),
  CONSTRAINT user_profile_user_id_fk FOREIGN KEY (user_id) REFERENCES user_(id)
);

CREATE TABLE user_roles (
  user_id bigint NOT NULL,
  role VARCHAR(255) NOT NULL,
  CONSTRAINT user_roles_pk PRIMARY KEY (user_id, role),
  CONSTRAINT user_roles_user_id_fk FOREIGN KEY (user_id) REFERENCES user_(id)
);

CREATE TABLE visitor (
  id bigint NOT NULL,
  created_date timestamp NULL,
  last_modified_date timestamp NULL,
  version_ integer not null,
  CONSTRAINT visitor_pk PRIMARY KEY (id)
);
