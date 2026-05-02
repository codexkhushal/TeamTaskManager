import { useState } from "react";
import { apiRequest } from "../api/client";
import { useAuth } from "../context/AuthContext";

const defaultSignup = {
  name: "",
  email: "",
  password: ""
};

export default function AuthScreen() {
  const { authenticate } = useAuth();
  const [mode, setMode] = useState("login");
  const [loginForm, setLoginForm] = useState({ email: "", password: "" });
  const [signupForm, setSignupForm] = useState(defaultSignup);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setError("");

    try {
      const payload = mode === "login"
        ? await apiRequest("/auth/login", { method: "POST", body: JSON.stringify(loginForm) })
        : await apiRequest("/auth/signup", { method: "POST", body: JSON.stringify(signupForm) });
      authenticate(payload);
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="auth-shell">
      <section className="auth-panel">
        <div className="brand-block">
          <p className="eyebrow">Team collaboration</p>
          <h1>Team Task Manager</h1>
          <p className="lede">
            Organize projects, assign work, and keep delivery visible without losing the human side of teamwork.
          </p>
        </div>

        <div className="mode-switch">
          <button className={mode === "login" ? "active" : ""} onClick={() => setMode("login")} type="button">
            Login
          </button>
          <button className={mode === "signup" ? "active" : ""} onClick={() => setMode("signup")} type="button">
            Sign up
          </button>
        </div>

        <form className="auth-form" onSubmit={handleSubmit}>
          {mode === "signup" && (
            <>
              <label>
                Full name
                <input
                  value={signupForm.name}
                  onChange={(event) => setSignupForm({ ...signupForm, name: event.target.value })}
                  required
                />
              </label>
            </>
          )}

          <label>
            Email
            <input
              type="email"
              value={mode === "login" ? loginForm.email : signupForm.email}
              onChange={(event) =>
                mode === "login"
                  ? setLoginForm({ ...loginForm, email: event.target.value })
                  : setSignupForm({ ...signupForm, email: event.target.value })
              }
              required
            />
          </label>

          <label>
            Password
            <input
              type="password"
              value={mode === "login" ? loginForm.password : signupForm.password}
              onChange={(event) =>
                mode === "login"
                  ? setLoginForm({ ...loginForm, password: event.target.value })
                  : setSignupForm({ ...signupForm, password: event.target.value })
              }
              required
            />
          </label>

          {error ? <p className="error-text">{error}</p> : null}

          <button className="primary-button" disabled={submitting} type="submit">
            {submitting ? "Please wait..." : mode === "login" ? "Enter workspace" : "Create account"}
          </button>
          {mode === "signup" ? (
            <p className="helper-text">
              The first account becomes the workspace admin. Later signups join as members.
            </p>
          ) : null}
        </form>
      </section>
    </div>
  );
}
