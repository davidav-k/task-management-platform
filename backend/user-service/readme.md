# User Service

This service handles user management, including:
- User registration
- Authentication using JWT
- Role management

## Technologies
- Spring Boot
- PostgreSQL for user data storage
- Redis for storing access tokens


### For local dev launch (first launch)
1. Delete all containers and volumes (docker)
2. Run 'docker compose up -d' in the terminal from the docker folder.
   Containers with the database and admin panel are built and launched
3. Enable database initialization in application.yml:
     sql.init.mode: always
4. Change for local ip address following properties in application-dev.yml:
   - POSTGRESQL_HOST: 
   - EMAIL_HOST: 
5. Launch the application.
   Database is initialized using the db_init.sql file:
   - users, roles, user_roles, credentials, confirmations tables are created
   - SYSTEM user with id = 0 is added to the users table
   - ADMIN and USER roles with id = 1 and 2 respectively are added to the roles table
6. Stop the application
7. Disable database initialization in application.yml:
      sql.init.mode: never


#### localhost:7001 - admin panel (pgAdmin) postgresql
#### localhost:8025 - mailhog (email testing tool)