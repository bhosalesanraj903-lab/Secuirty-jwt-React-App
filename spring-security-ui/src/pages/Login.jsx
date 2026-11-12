import React, { useState } from "react";
import { login } from "../services/authService";
import { useNavigate } from "react-router-dom";
import "./Login.css";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();

  const decodeJwtPayload = (token) => {
    const encodedPayload = token.split(".")[1];
    const base64 = encodedPayload.replace(/-/g, "+").replace(/_/g, "/");
    const padded = base64 + "=".repeat((4 - (base64.length % 4)) % 4);
    return JSON.parse(atob(padded));
  };

  const extractRoleNames = (payload) => {
    const source = payload?.roles ?? payload?.authorities ?? [];
    if (!Array.isArray(source)) {
      return [];
    }

    return source
      .map((item) => {
        if (typeof item === "string") return item;
        if (item && typeof item === "object") {
          return item.authority || item.name || "";
        }
        return "";
      })
      .filter(Boolean);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);

    try {
      const data = await login(username, password);
      const token = data?.token;

      if (!token) {
        setError("Login failed: token not received.");
        return;
      }

      const payload = decodeJwtPayload(token);
      const roles = extractRoleNames(payload);

      if (roles.includes("ROLE_ADMIN")) {
        navigate("/admin");
        return;
      }

      navigate("/user");
    } catch (err) {
      if (err?.code === "ERR_NETWORK") {
        setError("Network/CORS issue: verify backend is running and CORS allows http://localhost:5173.");
        return;
      }

      const responseData = err?.response?.data;
      const serverMessage =
        (typeof responseData === "string" && responseData) ||
        responseData?.message ||
        responseData?.error;

      setError(serverMessage || "Invalid username or password.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <main className="login-shell">
      <section className="login-hero">
        <p className="login-eyebrow">Spring Security + React</p>
        <h1 className="login-title">Secure access for your workspace</h1>
        <p className="login-subtitle">
          Sign in to continue to your dashboard with role-based access.
        </p>
      </section>

      <section className="login-card-wrap">
        <div className="login-card">
          <h2 className="login-card-title">Welcome Back</h2>
          <p className="login-card-desc">Use your username and password to continue.</p>

          <form className="login-form" onSubmit={handleSubmit}>
            <label className="login-label" htmlFor="username">
              Username
            </label>
            <input
              id="username"
              className="login-input"
              placeholder="Enter username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              autoComplete="username"
            />

            <label className="login-label" htmlFor="password">
              Password
            </label>
            <input
              id="password"
              className="login-input"
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              autoComplete="current-password"
            />

            <button className="login-button" type="submit" disabled={isSubmitting}>
              {isSubmitting ? "Signing in..." : "Login"}
            </button>
          </form>

          {error && <p className="login-error">{error}</p>}
        </div>
      </section>
    </main>
  );
}

export default Login;
