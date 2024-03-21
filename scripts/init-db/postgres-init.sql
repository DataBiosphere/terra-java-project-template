CREATE ROLE dbuser WITH LOGIN ENCRYPTED PASSWORD 'dbpwd';
CREATE DATABASE javatemplate_db OWNER dbuser;
-- GRANT CREATE ON DATABASE javatemplate_db TO dbuser;
-- GRANT ALL PRIVILEGES ON DATABASE javatemplate_db TO dbuser;
