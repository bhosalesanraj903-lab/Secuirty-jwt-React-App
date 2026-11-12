import React, { useEffect, useState } from "react";
import axios from "axios";
import { getToken } from "../services/authService";

function UserDashboard() {
  const [dashboardMessage, setDashboardMessage] = useState("");
  const [ordersMessage, setOrdersMessage] = useState("");
  const [profileMessage, setProfileMessage] = useState("");
  const [error, setError] = useState("");
  const authConfig = () => ({
    headers: {
      Authorization: `Bearer ${getToken()}`,
    },
  });

  const loadDashboard = async () => {
    try {
      setError("");
      const res = await axios.get("http://localhost:8181/user/dashboard", authConfig());
      setDashboardMessage(res.data);
    } catch (err) {
      setError(`Access denied for dashboard (${err?.response?.status || "request failed"}).`);
    }
  };

  const loadOrders = async () => {
    try {
      setError("");
      const res = await axios.get("http://localhost:8181/user/orders", authConfig());
      setOrdersMessage(res.data);
    } catch (err) {
      setError(`Could not load orders (${err?.response?.status || "request failed"}).`);
    }
  };

  const updateProfile = async () => {
    try {
      setError("");
      const res = await axios.put("http://localhost:8181/user/profile", {}, authConfig());
      setProfileMessage(res.data);
    } catch (err) {
      setError(`Profile update failed (${err?.response?.status || "request failed"}).`);
    }
  };

  useEffect(() => {
    loadDashboard();
  }, []);

  return (
    <div style={{ padding: "24px" }}>
      <h2>User Dashboard</h2>
      {error && <p style={{ color: "crimson" }}>{error}</p>}

      <div style={{ marginBottom: "12px" }}>
        <button type="button" onClick={loadDashboard}>
          Load Dashboard
        </button>
        <p>{dashboardMessage}</p>
      </div>

      <div style={{ marginBottom: "12px" }}>
        <button type="button" onClick={loadOrders}>
          Get Orders
        </button>
        <p>{ordersMessage}</p>
      </div>

      <div>
        <button type="button" onClick={updateProfile}>
          Update Profile
        </button>
        <p>{profileMessage}</p>
      </div>
    </div>
  );
}

export default UserDashboard;
