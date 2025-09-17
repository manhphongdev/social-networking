import React, { useEffect } from "react";
import "./auth.css";

function Register() {
    useEffect(() => {
        document.title = "Đăng ký";

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

    return (
        <div className="register-container">
            <div className="register-card">
                <h2>Đăng Ký</h2>
                <div className="error-message" id="errorMessage"></div>
                <div className="success-message" id="successMessage"></div>
                <form id="registerForm" action="/register" method="POST">
                    <div className="form-group">
                        <input type="text" className="form-control" name="name" placeholder=" " required />
                        <label>Họ và tên</label>
                    </div>
                    <div className="form-group">
                        <input type="email" className="form-control" name="email" placeholder=" " required />
                        <label>Email</label>
                    </div>
                    <div className="form-group">
                        <input type="password" className="form-control" name="password" placeholder=" " required />
                        <label>Mật khẩu</label>
                    </div>
                    <div className="form-group">
                        <input type="password" className="form-control" name="confirm_password" placeholder=" " required />
                        <label>Xác nhận mật khẩu</label>
                    </div>
                    <div className="form-group">
                        <input type="date" className="form-control" name="dateOfBirth" placeholder=" " />
                        <label>Ngày sinh</label>
                    </div>
                    <div className="form-group">
                        <select className="form-select" name="gender">
                            <option value="">Chọn giới tính</option>
                            <option value="male">Nam</option>
                            <option value="female">Nữ</option>
                            <option value="other">Khác</option>
                        </select>
                        <label>Giới tính</label>
                    </div>
                    <div className="form-group">
                        <textarea className="form-control" name="bio" placeholder=" " rows="3" maxLength="255"></textarea>
                        <label>Giới thiệu bản thân (tối đa 255 ký tự)</label>
                    </div>
                    <button type="submit" className="btn btn-primary">Đăng ký</button>
                </form>

                <div className="text-center mt-3">
                    <p>Hoặc đăng ký bằng:</p>
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
                        data-text="sign_up_with"
                        data-shape="rectangular"
                    ></div>
                </div>

                <div className="text-center mt-3">
                    <p>Đã có tài khoản? <a href="/log-in">Đăng nhập</a></p>
                </div>
            </div>
        </div>
    );
}

export default Register;
