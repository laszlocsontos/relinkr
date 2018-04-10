-- As of Spring Security 5.0.4, a separate DB schema for the H2 database hasn't been provided.
-- Nevertheless, Microsoft SQL Server's dialect seems to be working fine for H2.
-- https://docs.spring.io/spring-security/site/docs/5.0.4.RELEASE/reference/html/appendix-schema.html#microsoft-sql-server

CREATE TABLE acl_sid (
	id BIGINT NOT NULL IDENTITY PRIMARY KEY,
	principal BIT NOT NULL,
	sid VARCHAR(100) NOT NULL,
	CONSTRAINT unique_acl_sid UNIQUE (sid, principal)
);

CREATE TABLE acl_class (
	id BIGINT NOT NULL IDENTITY PRIMARY KEY,
	class VARCHAR(100) NOT NULL,
	CONSTRAINT uk_acl_class UNIQUE (class)
);

CREATE TABLE acl_object_identity (
	id BIGINT NOT NULL IDENTITY PRIMARY KEY,
	object_id_class BIGINT NOT NULL,
	object_id_identity VARCHAR(36) NOT NULL,
	parent_object BIGINT,
	owner_sid BIGINT,
	entries_inheriting BIT NOT NULL,
	CONSTRAINT uk_acl_object_identity UNIQUE (object_id_class, object_id_identity),
	CONSTRAINT fk_acl_object_identity_parent FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),
	CONSTRAINT fk_acl_object_identity_class FOREIGN KEY (object_id_class) REFERENCES acl_class (id),
	CONSTRAINT fk_acl_object_identity_owner FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)
);

CREATE TABLE acl_entry (
	id BIGINT NOT NULL IDENTITY PRIMARY KEY,
	acl_object_identity BIGINT NOT NULL,
	ace_order INTEGER NOT NULL,
	sid BIGINT NOT NULL,
	mask INTEGER NOT NULL,
	granting BIT NOT NULL,
	audit_success BIT NOT NULL,
	audit_failure BIT NOT NULL,
	CONSTRAINT unique_acl_entry UNIQUE (acl_object_identity, ace_order),
	CONSTRAINT fk_acl_entry_object FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id),
	CONSTRAINT fk_acl_entry_acl FOREIGN KEY (sid) REFERENCES acl_sid (id)
);
