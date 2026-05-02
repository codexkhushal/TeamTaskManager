const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

export async function apiRequest(path, options = {}) {
  const token = localStorage.getItem("team-task-token");
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers
  });

  if (!response.ok) {
    const data = await response.json().catch(() => ({ message: "Request failed" }));
    throw new Error(data.message || "Request failed");
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}
