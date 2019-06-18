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
  owner varchar(255) NULL,
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
