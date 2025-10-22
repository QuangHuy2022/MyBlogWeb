# Sử dụng image Java chính thức
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build ứng dụng
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Expose port 8080 cho Render
EXPOSE 8080

# Chạy file jar khi container khởi động
CMD ["java", "-jar", "target/blog-app.jar"]
