import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./features/auth/login";
import Register from "./features/auth/register";
import logo from "./logo.svg";
import "./App.css";

function Home() {
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <p>
                    Đi đến trang đăng ký.
                </p>
                <a
                    className="App-link"
                    href= "/log-in"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Home
                </a>
            </header>
        </div>
    );
}

function App() {
    return (
        <BrowserRouter>
            <Routes>

                <Route path="/" element={<Home />} />

                {/* Login page */}
                <Route path="/log-in" element={<Login />} />

                <Route path="/register" element={<Register />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
