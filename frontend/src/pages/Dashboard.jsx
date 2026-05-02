import { useEffect, useState } from "react";
import { apiRequest } from "../api/client";
import { useAuth } from "../context/AuthContext";
import SummaryCards from "../components/SummaryCards";
import ProjectPanel from "../components/ProjectPanel";
import TaskPanel from "../components/TaskPanel";

export default function Dashboard() {
  const { user, logout } = useAuth();
  const [summary, setSummary] = useState({});
  const [projects, setProjects] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [users, setUsers] = useState([]);
  const [error, setError] = useState("");

  const isAdmin = user?.role === "ADMIN";

  async function loadData() {
    try {
      const [summaryData, projectData, taskData, userData] = await Promise.all([
        apiRequest("/dashboard"),
        apiRequest("/projects"),
        apiRequest("/tasks"),
        apiRequest("/users")
      ]);
      setSummary(summaryData);
      setProjects(projectData);
      setTasks(taskData);
      setUsers(userData);
      setError("");
    } catch (err) {
      setError(err.message);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  async function handleCreateProject(payload) {
    await apiRequest("/projects", { method: "POST", body: JSON.stringify(payload) });
    await loadData();
  }

  async function handleCreateTask(payload) {
    await apiRequest("/tasks", { method: "POST", body: JSON.stringify(payload) });
    await loadData();
  }

  async function handleUpdateTaskStatus(taskId, status) {
    await apiRequest(`/tasks/${taskId}/status`, {
      method: "PATCH",
      body: JSON.stringify({ status })
    });
    await loadData();
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">Workspace dashboard</p>
          <h1>Welcome back, {user?.name}</h1>
        </div>
        <div className="topbar-actions">
          <div className="identity-chip">
            <span>{user?.role}</span>
            <strong>{user?.email}</strong>
          </div>
          <button className="ghost-button" onClick={logout} type="button">Logout</button>
        </div>
      </header>

      {error ? <p className="error-banner">{error}</p> : null}

      <SummaryCards summary={summary} />
      <ProjectPanel projects={projects} users={users} isAdmin={isAdmin} onCreateProject={handleCreateProject} />
      <TaskPanel
        tasks={tasks}
        projects={projects}
        users={users}
        isAdmin={isAdmin}
        onCreateTask={handleCreateTask}
        onUpdateTaskStatus={handleUpdateTaskStatus}
      />
    </div>
  );
}
