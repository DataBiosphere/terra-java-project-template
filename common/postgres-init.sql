CREATE ROLE dbuser WITH LOGIN ENCRYPTED PASSWORD 'dbpwd';
CREATE DATABASE ${{values.projectSlug}}_db OWNER dbuser;
