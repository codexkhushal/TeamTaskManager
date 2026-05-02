import { createContext, useContext, useEffect, useState } from "react";
import { apiRequest } from "../api/client";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("team-task-token");
    if (!token) {
      setLoading(false);
      return;
    }

    apiRequest("/auth/me")
      .then((data) => setUser(data))
      .catch(() => {
        localStorage.removeItem("team-task-token");
        setUser(null);
      })
      .finally(() => setLoading(false));
  }, []);

  const authenticate = (payload) => {
    localStorage.setItem("team-task-token", payload.token);
    setUser(payload);
  };

  const logout = () => {
    localStorage.removeItem("team-task-token");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, authenticate, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
