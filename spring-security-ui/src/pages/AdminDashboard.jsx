import React, { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../services/authService";

function AdminDashboard() {
  const [dashboardMessage, setDashboardMessage] = useState("");
  const [usersMessage, setUsersMessage] = useState("");
  const [createMessage, setCreateMessage] = useState("");
  const [deleteMessage, setDeleteMessage] = useState("");
  const [deleteId, setDeleteId] = useState("");
  const [error, setError] = useState("");
  const authConfig = () => ({
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });

  const loadDashboard = async () => {
    try {
      setError("");
      const res = await axios.get("http://localhost:8181/admin/dashboard", authConfig());
      setDashboardMessage(res.data);
    } catch (err) {
      setError("Access denied for admin dashboard.");
    }
  };

  const loadUsers = async () => {
    try {
      setError("");
      const res = await axios.get("http://localhost:8181/admin/users", authConfig());
      setUsersMessage(res.data);
    } catch (err) {
      setError("Could not fetch users.");
    }
  };

  const createResource = async () => {
    try {
      setError("");
      const res = await axios.post("http://localhost:8181/admin/create", {}, authConfig());
      setCreateMessage(res.data);
    } catch (err) {
      setError("Could not create resource.");
    }
  };

  const deleteUser = async () => {
    if (!deleteId) {
      setError("Enter a user ID to delete.");
      return;
    }

    try {
      setError("");
      const res = await axios.delete(`http://localhost:8181/admin/user/${deleteId}`, authConfig());
      setDeleteMessage(res.data);
    } catch (err) {
      setError("Could not delete user.");
    }
  };

  useEffect(() => {
    loadDashboard();
  }, []);

  return (
    <div style={{ padding: "24px" }}>
      <h2>Admin Dashboard</h2>
      {error && <p style={{ color: "crimson" }}>{error}</p>}

      <div style={{ marginBottom: "12px" }}>
        <button type="button" onClick={loadDashboard}>
          Load Dashboard
        </button>
        <p>{dashboardMessage}</p>
      </div>

      <div style={{ marginBottom: "12px" }}>
        <button type="button" onClick={loadUsers}>
          Get All Users
        </button>
        <p>{usersMessage}</p>
      </div>

      <div style={{ marginBottom: "12px" }}>
        <button type="button" onClick={createResource}>
          Create Resource
        </button>
        <p>{createMessage}</p>
      </div>

      <div>
        <input
          type="number"
          placeholder="User ID"
          value={deleteId}
          onChange={(e) => setDeleteId(e.target.value)}
          style={{ marginRight: "8px" }}
        />
        <button type="button" onClick={deleteUser}>
          Delete User
        </button>
        <p>{deleteMessage}</p>
      </div>
    </div>
  );
}

export default AdminDashboard;
