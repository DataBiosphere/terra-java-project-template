CREATE DATABASE javatemplate_db;
CREATE ROLE dbuser WITH LOGIN ENCRYPTED PASSWORD 'dbpwd';
GRANT CREATE ON DATABASE javatemplate_db TO dbuser;
-- GRANT ALL PRIVILEGES ON DATABASE javatemplate_db TO dbuser;
