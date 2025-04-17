# User Service

This service handles user management, including:
- User registration
- Authentication using JWT
- Role management

## Technologies
- Spring Boot
- PostgreSQL for user data storage
- Google Guava for caching


### For local launch 
1. .env file
   - Copy .env.example to .env
   - Change the values of the variables in the .env file to your local environment
     - Make sure to set your local ip address:
       - POSTGRES_HOST
       - EMAIL_HOST

2. Delete all containers and volumes (docker)
3. Run in the terminal from the root project folder
```
docker compose --env-file .env -f docker/compose.yml up -d
```
Containers with the database and admin panel are built and launched
4. Launch the application.

#### localhost:7001 - admin panel (pgAdmin) postgresql
#### localhost:8025 - mailhog (email testing tool)