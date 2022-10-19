CREATE ROLE dbuser WITH LOGIN ENCRYPTED PASSWORD 'dbpwd';
CREATE DATABASE catalog_db OWNER dbuser;
\c catalog_db
CREATE EXTENSION pgcrypto;
