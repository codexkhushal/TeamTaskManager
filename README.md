# Team Task Manager

Team Task Manager is a full-stack collaboration app for small teams. Admins can create projects, add members, and assign tasks. Members can track their work and update progress. The stack is React on the frontend, Spring Boot on the backend, and MySQL for persistence.

## What it does

- Secure signup and login with JWT authentication
- Role-based access with `ADMIN` and `MEMBER`
- Project creation with team member assignment
- Task assignment with deadlines and status updates
- Dashboard counts for total, completed, pending, and overdue tasks

The first account created in a fresh deployment becomes the admin account. Later signups are regular members.

## Stack

- React + Vite
- Spring Boot 3
- Spring Security + JPA
- MySQL
- Railway

## Local setup

### Backend

```bash
cd backend
mvn spring-boot:run
```

Default environment:

```env
DATABASE_URL=jdbc:mysql://localhost:3306/team_tasker?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DATABASE_USERNAME=root
DATABASE_PASSWORD=password
JWT_SECRET=change-this-development-secret-change-this-development-secret
CORS_ALLOWED_ORIGINS=http://localhost:5173
JPA_DDL_AUTO=update
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Optional frontend environment:

```env
VITE_API_URL=http://localhost:8080/api
```
