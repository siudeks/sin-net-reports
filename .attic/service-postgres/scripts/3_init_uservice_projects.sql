CREATE ROLE uservice_projects_role_name WITH LOGIN PASSWORD 'uservice_projects_role_password';
CREATE SCHEMA uservice_projects AUTHORIZATION uservice_projects_role_name;