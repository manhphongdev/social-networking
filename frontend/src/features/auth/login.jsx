import React, {useEffect, useState} from "react";
import "./auth.css";

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");


    useEffect(() => {
        document.title = "StudySocial - Đăng nhập";

        // Load Google Sign-In script
        const script = document.createElement("script");
        script.src = "https://accounts.google.com/gsi/client";
        script.async = true;
        script.defer = true;
        document.body.appendChild(script);

        // Define callback function globally
        window.handleCredentialResponse = (response) => {
            console.log("Google response:", response);
            // TODO: Gửi token tới backend để xác thực
        };

        return () => {
            document.body.removeChild(script);
        };
    }, []);

    const handleLogin =async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("http://localhost:8081/auth/log-in", {
                method: "POST",
                body: JSON.stringify({email, password}),
                headers: {"Content-Type": "application/json"}
            });
            const data = await response.json();
            if (response.ok) {
                // Ví dụ: lưu token và chuyển hướng
                localStorage.setItem("token", data.token);
                window.location.href = "https://docs.google.com/document/d/1V4Ff7hB63txjZUSrBtWBKDOVVdwu19Xt/edit";
            } else {
                alert(data.message || "Đăng nhập thất bại");
            }
        }
        catch (error) {
            console.log(error);
        }
    }

    return (
        <div className="login-container">
            <div className="login-card">
                <h2>Đăng Nhập</h2>

                <form onSubmit={handleLogin}>
                    <input
                        type="text"
                        className="form-control"
                        name="identifier"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Email hoặc tên đăng nhập"
                        required
                    />
                    <input
                        type="password"
                        className="form-control"
                        name="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Mật khẩu"
                        required
                    />
                    <button type="submit" className="btn btn-primary">
                        Đăng nhập
                    </button>
                    <div className="text-center mt-2">
                        <a href="#" data-bs-toggle="modal" data-bs-target="#forgotPasswordModal">
                            Quên mật khẩu?
                        </a>
                    </div>
                </form>

                <div className="text-center mt-3">
                    <p>Hoặc đăng nhập bằng:</p>
                    <div
                        id="g_id_onload"
                        data-client_id="YOUR_GOOGLE_CLIENT_ID"
                        data-callback="handleCredentialResponse"
                    ></div>
                    <div
                        className="g_id_signin"
                        data-type="standard"
                        data-size="large"
                        data-theme="outline"
                        data-text="sign_in_with"
                        data-shape="rectangular"
                    ></div>
                </div>

                <div className="text-center mt-3">
                    <p>
                        Chưa có tài khoản? <a href="/register">Đăng ký</a>
                    </p>
                </div>
            </div>

            {/* Modal Quên mật khẩu */}
            <div className="modal fade" id="forgotPasswordModal" tabIndex="-1">
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">Đặt Lại Mật Khẩu</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div className="modal-body">
                            <p>Nhập email của bạn để nhận liên kết đặt lại mật khẩu.</p>
                            <form>
                                <input
                                    type="email"
                                    className="form-control"
                                    name="email"
                                    placeholder="Email"
                                    required
                                />
                                <button type="submit" className="btn btn-primary mt-3">
                                    Gửi Liên Kết
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
