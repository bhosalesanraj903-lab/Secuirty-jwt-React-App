import React, { useState } from "react";
import { register } from "../services/authService";

function Register() {

    const [user, setUser] = useState({
        username: "",
        password: "",
        roles: []
    });

    const handleChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    };

    const handleRole = (role) => {
        setUser({
            ...user,
            roles: [{ name: role }]
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await register(user);
        alert("User Registered!");
    };

    return (
        <div>
            <h2>Register</h2>
            <form onSubmit={handleSubmit}>
                <input name="username" placeholder="Username" onChange={handleChange} />
                <input name="password" type="password" placeholder="Password" onChange={handleChange} />

                <button type="button" onClick={() => handleRole("ROLE_USER")}>
                    Register as USER
                </button>

                <button type="button" onClick={() => handleRole("ROLE_ADMIN")}>
                    Register as ADMIN
                </button>

                <button type="submit">Register</button>
            </form>
        </div>
    );
}

export default Register;