package com.app.service;

import com.app.dto.SignupRequest;
import com.app.entity.LoginStatus;
import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.util.PasswordPolicy;
import com.app.util.PasswordUtils;

import jakarta.persistence.EntityManager;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private javax.sql.DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Đăng nhập và xác thực thông tin đăng nhập.
     */
    public LoginStatus loginUser(String username, String password) {

        // Kiểm tra null TRƯỚC khi gọi .trim()
        if (username == null) {
            System.err.println("Error: USER_NAME IS NULL!");
            return LoginStatus.USER_NOT_FOUND;
        }

        String cleanUsername = username.trim();
        System.out.println("Đang xử lý login cho: " + cleanUsername);
        // Ghi chú: Kiểm tra xem Java đang kết nối vào file nào (Rất quan trọng!)
        try (java.sql.Connection conn = dataSource.getConnection()) {
            System.out.println("--- DB LOCATION: " + conn.getMetaData().getURL() + " ---");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ghi chú: In ra chính xác những gì Thunder Client gửi lên
        System.out.println("DEBUG: Searching for username -> [" + username + "]");

        // Ghi chú: Liệt kê TẤT CẢ username đang có trong DB mà Java thấy
        System.out.print("DEBUG: All usernames currently in Java's DB: ");
        userRepository.findAll().forEach(u -> System.out.print("[" + u.getUsername() + "] "));
        System.out.println();

        Optional<User> userOpt = userRepository.findByUsername(username.trim());

        if (userOpt.isEmpty()) {
            System.err.println("RESULT: USER NOT FOUND!");
            return LoginStatus.USER_NOT_FOUND;
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.err.println("RESULT: WRONG PASSWORD!");
            return LoginStatus.WRONG_PASSWORD;
        }

        System.out.println("RESULT: SUCCESS!");
        return LoginStatus.SUCCESS;
    }

    /**
     * Đăng kí, xác thực thông tin đăng kí.
     */
    public String registerUser(SignupRequest request) {

        // 0. Kiểm tra trùng MSSV (Cực kỳ quan trọng)
        if (userRepository.existsByStudentId(request.getStudentId())) {
            throw new RuntimeException("Error: Student ID " + request.getStudentId() + " is already registered!");
        }

        // 1. Kiểm tra Username đã tồn tại chưa
        // Note: Nếu trùng username, ném lỗi ngay lập tức để dừng luồng xử lý
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(
                    "Registration Error: Username '" + request.getUsername() + "' is already taken.");
        }

        // 2. Kiểm tra Email (Bước cực kỳ quan trọng cho tính năng Reset Password)
        // Note: Đảm bảo một email chỉ được đăng ký một tài khoản duy nhất
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Registration Error: Email is required.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Registration Error: Email '" + request.getEmail() + "' is already in use.");
        }

        // 3. Kiểm tra các thông tin bắt buộc khác
        // Note: Sử dụng .trim() để loại bỏ khoảng trắng dư thừa ở hai đầu
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Registration Error: Full Name cannot be blank.");
        }

        if (request.getRole() == null) {
            throw new RuntimeException("Registration Error: User role is missing.");
        }

        // 4. Khởi tạo đối tượng User và nạp dữ liệu
        User user = new User();
        user.setStudentId(request.getStudentId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail().trim());
        user.setFullName(request.getFullName().trim());
        user.setRole(request.getRole());

        // 5. MÃ HÓA MẬT KHẨU (Bảo mật tuyệt đối)
        // Note: Biến 'encodedPassword' dùng thì quá khứ để chỉ mật khẩu đã qua xử lý
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        // 6. Lưu xuống Database
        userRepository.save(user);

        // 7. Ghi log kiểm tra trên Console (Chỉ in thông tin không nhạy cảm)
        System.out.println("LOG: Registration successful for user: " + request.getUsername() + " with Email: "
                + request.getEmail());

        return "SUCCESS: User registered successfully!";
    }

    /**
     * Hàm quên mật khẩu (cần email).
     */
    public String processForgotPassword(String email) {
        // Sửa lỗi Optional: Nếu không tìm thấy sẽ gán user = null
        // Note: Tìm kiếm người dùng trong Database bằng Email
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // Log tiếng Anh để dễ debug trên server
            System.out.println("ERROR: Reset password failed. Email not found: " + email);
            return null; // Trả về null thay vì false
        }

        // 1. Tạo mật khẩu thô (Plain text)
        // Note: Sử dụng Utility đã tạo trước đó
        String rawPassword = PasswordUtils.generateRandomPassword(10);

        // 2. Mã hóa mật khẩu trước khi lưu vào Database
        String hashedPass = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPass);
        userRepository.save(user);

        // 3. Log thông tin ra Console bằng Tiếng Anh
        // Note: Dòng này giúp bạn thấy mật khẩu ở màn hình đen của IntelliJ/Eclipse
        System.out.println("SUCCESS: Temporary password generated for " + email);
        System.out.println("DEBUG: New Raw Password: " + rawPassword);

        // TRẢ VỀ mật khẩu thô để Controller có thể lấy và hiển thị ra màn hình
        return rawPassword;
    }

    /**
     * Hàm đổi mật khẩu.
     */
    public String resetPassword(String email, String oldPassword, String newPassword, String confirmPassword) {
        // Tìm user theo email.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: Email address does not exist."));

        // Xét mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return "[401] Error: Wrong Password!";
        }

        // Xem mật khẩu mới dùng đúng form chưa
        if (newPassword == null || !validatePassword(newPassword)) {
            return "[403] Error: Incorrect Password Form.";
        }

        // Xác nhận lại mật khẩu
        if (!newPassword.equals(confirmPassword)) {
            return "[405] Error: Confirmation Failed.";
        }

        // Mã hóa mật khẩu mới trước khi lưu
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Log ra Console để copy mật khẩu đi test Login
        System.out.println("-----> RESET SUCCESS <-----");
        System.out.println("User: " + user.getUsername());
        System.out.println("Mật khẩu mới (thô): " + newPassword);

        return "[200] SUCCESS: Password Reset";
    }

    /**
     * Lấy user từ db qua username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Lưu về db chứa user
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Hàm xác thực dạng mật khẩu.
     */
    public boolean validatePassword(String rawPassword) {
        PasswordValidator validator = PasswordPolicy.getValidator();
        PasswordData data = new PasswordData(rawPassword);
        RuleResult result = validator.validate(data);

        return result.isValid();
    }

}
