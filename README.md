# Team Task Manager

Team Task Manager is a full-stack project management app for teams. It includes JWT authentication, role-based access control, project ownership, member assignment, task tracking, and dashboard summaries.

## Stack

- Frontend: React + Vite
- Backend: Spring Boot 3 + Spring Security + JPA
- Database: MySQL
- Deployment target: Railway

## Features

- Signup and login with JWT auth
- First registered user becomes `ADMIN`
- Later registered users become `MEMBER`
- Admin-only project creation and task assignment
- Member task status updates
- Dashboard metrics for total, completed, pending, and overdue tasks
- Railway-ready health checks and service config

## Local run

### Backend

```bash
cd backend
mvn spring-boot:run
```

Default backend environment:

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

## Railway-ready files

- Backend deploy config: [backend/railway.toml](/Users/khushal/Desktop/C-tasker/backend/railway.toml)
- Frontend deploy config: [frontend/railway.toml](/Users/khushal/Desktop/C-tasker/frontend/railway.toml)
- Backend Railway env example: [backend/.env.railway.example](/Users/khushal/Desktop/C-tasker/backend/.env.railway.example)
- Frontend Railway env example: [frontend/.env.railway.example](/Users/khushal/Desktop/C-tasker/frontend/.env.railway.example)

The backend exposes a public healthcheck endpoint at `/api/health` for Railway deployments.

## Push to GitHub

If this folder is not already a git repository:

```bash
cd /Users/khushal/Desktop/C-tasker
git init
git add .
git commit -m "Initial Team Task Manager setup"
```

Create an empty GitHub repository first, then connect it:

```bash
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
git branch -M main
git push -u origin main
```

If you prefer GitHub CLI:

```bash
gh repo create YOUR_REPO --public --source=. --remote=origin --push
```

## Railway deployment

### 1. Create the project

1. Open Railway.
2. Create a new empty project.
3. Add three services:
   - `MySQL`
   - `backend`
   - `frontend`

### 2. Add MySQL

1. Click `+ New`.
2. Choose `Database` -> `MySQL`.
3. Wait for the MySQL service to deploy.
4. Keep it in the same project as the app services.

Railway provides these variables from the MySQL service:

- `MYSQLHOST`
- `MYSQLPORT`
- `MYSQLUSER`
- `MYSQLPASSWORD`
- `MYSQLDATABASE`
- `MYSQL_URL`

### 3. Connect the backend service

1. Open the `backend` service.
2. Connect your GitHub repository.
3. In `Settings`, set:
   - Root Directory: `/backend`
   - Config as Code path: `/backend/railway.toml`
4. Deploy the service.

### 4. Set backend variables

In the backend service `Variables` tab, add:

```env
DATABASE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DATABASE_USERNAME=${{MySQL.MYSQLUSER}}
DATABASE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
JWT_SECRET=replace-with-a-long-random-secret
JPA_DDL_AUTO=update
```

Deploy or redeploy the backend after saving the variables.

### 5. Generate the backend public domain

1. Open the backend service.
2. Go to `Settings` -> `Networking`.
3. Click `Generate Domain`.
4. Copy the resulting URL.

Example:

```text
https://backend-production-xxxx.up.railway.app
```

### 6. Connect the frontend service

1. Open the `frontend` service.
2. Connect the same GitHub repository.
3. In `Settings`, set:
   - Root Directory: `/frontend`
   - Config as Code path: `/frontend/railway.toml`

### 7. Set frontend variables

In the frontend service `Variables` tab, add:

```env
VITE_API_URL=https://your-backend-domain.up.railway.app/api
```

Deploy the frontend service.

### 8. Generate the frontend public domain

1. Open the frontend service.
2. Go to `Settings` -> `Networking`.
3. Click `Generate Domain`.
4. Copy the frontend URL.

Example:

```text
https://frontend-production-xxxx.up.railway.app
```

### 9. Finish backend CORS

Go back to backend `Variables` and add:

```env
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.up.railway.app
```

Redeploy the backend service after saving it.

### 10. Verify the app

1. Open the frontend domain.
2. Click `Sign up`.
3. Create the first account.
4. The first account becomes `ADMIN`.
5. Login and start creating projects and tasks.

## Redeploy flow after changes

After making code changes locally:

```bash
git add .
git commit -m "Describe your change"
git push
```

If Railway GitHub autodeploy is enabled, new deployments start automatically after the push.

## Notes

- Railway monorepo deployments require per-service root directories.
- Railway config-as-code files must be referenced by absolute repo path, such as `/backend/railway.toml`.
- The frontend is configured to build static files and serve them with `serve` in production.
- The backend is configured with a public health endpoint for Railway healthchecks.
