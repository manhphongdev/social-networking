# VN SocialNet Backend

Backend cho nền tảng mạng xã hội VN SocialNet, cung cấp các API cho tính năng đăng bài, nhắn tin, theo dõi, và xác thực người dùng.

## 🛠 Công nghệ sử dụng

- **Java 17**
- **Spring Boot 3.5.5**
- **MySQL**: Cơ sở dữ liệu chính.
- **Spring Security & JWT**: Xác thực và phân quyền.
- **OAuth2 Client**: Đăng nhập bằng Google.
- **WebSocket (STOMP)**: Tính năng chat và thông báo thời gian thực.
- **Spring Data JPA**: Tương tác với cơ sở dữ liệu.
- **AWS S3**: Lưu trữ hình ảnh và tệp tin.
- **SendGrid**: Gửi email xác nhận, thông báo.
- **OpenAPI (Swagger)**: Tài liệu hóa API.
- **Lombok**: Giảm thiểu boilerplate code.

## ⚙️ Yêu cầu hệ thống

- JDK 17 trở lên
- Maven 3.x
- MySQL 8.x

## 🚀 Cài đặt và Chạy ứng dụng

1. **Clone dự án:**
   ```bash
   git clone <repository-url>
   cd backend
   ```

2. **Cấu hình cơ sở dữ liệu:**
   Tạo cơ sở dữ liệu MySQL (ví dụ: `social_network`).

3. **Cấu hình biến môi trường:**
   Để chạy ứng dụng, bạn cần thiết lập các biến môi trường sau (dựa trên file `application-dev.yml`):

   | Biến | Mô tả | Mặc định (nếu có) |
   |------|-------|-------------------|
   | `DB_HOST` | Host của MySQL | `localhost` |
   | `DB_PORT` | Port của MySQL | `3306` |
   | `DB_NAME` | Tên database | `social_network` |
   | `DB_USER` | Username MySQL | |
   | `DB_PASS` | Password MySQL | |
   | `GOOGLE_CLIENT_ID` | Client ID cho Google OAuth2 | |
   | `GOOGLE_CLIENT_SECRET` | Client Secret cho Google OAuth2 | |
   | `SENDGRID_API_KEY` | API Key của SendGrid | |
   | `AWS_ACCESS_KEY` | Access Key của AWS | |
   | `AWS_SECRET_KEY` | Secret Key của AWS | |

4. **Chạy ứng dụng:**
   Sử dụng Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   Hoặc nếu đã cài Maven:
   ```bash
   mvn spring-boot:run
   ```

   Ứng dụng sẽ khởi chạy tại cổng **8081**.

## 📚 Tài liệu API (Swagger)

Sau khi ứng dụng chạy thành công, bạn có thể truy cập tài liệu API tại:
- **Swagger UI:** [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

## 🔐 Tài khoản Test (Môi trường Dev)

Dựa trên cấu hình mặc định:
- **Admin:** `admin@test.com` / `12345678`
- **User:** `user@test.com` / `12345678`

## 📂 Cấu trúc thư mục

- `src/main/java`: Mã nguồn Java (Controller, Service, Repository, Entity...).
- `src/main/resources`: File cấu hình (`application.yaml`, `application-dev.yml`) và templates.
- `pom.xml`: Quản lý dependencies của Maven.
