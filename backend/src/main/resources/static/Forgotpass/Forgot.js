/**
 * File: forgot-password.js (Phiên bản dùng async/await)
 * Chức năng: Gửi yêu cầu lấy mật khẩu mới một cách tuần tự, dễ đọc.
 */

document.addEventListener("DOMContentLoaded", function () {
    const forgotForm = document.getElementById("forgotPasswordForm");
    const emailInput = document.getElementById("email");
    const messageBox = document.getElementById("responseMessage");

    // Thêm từ khóa 'async' trước function để cho phép dùng 'await' bên trong
    forgotForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const emailValue = emailInput.value.trim();

        // Hiển thị trạng thái chờ
        messageBox.style.display = "block";
        messageBox.innerHTML = "Đang xử lý...";
        messageBox.className = "message-box processing";

        // Sử dụng cấu trúc try...catch để bắt lỗi (thay cho .catch)
        try {
            // Gửi yêu cầu và đợi phản hồi trả về (await)
            const response = await fetch("/api/auth/forgot-password", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ email: emailValue })
            });

            // Lấy nội dung chữ từ phản hồi
            const resultText = await response.text();

            if (response.ok) {
                // Trường hợp thành công (Status 200)
                localStorage.setItem("email", email);
                messageBox.style.color = "#155724";
                messageBox.style.backgroundColor = "#d4edda";
                messageBox.innerHTML = `<strong>Thành công!</strong> <br> ${resultText}`;
                emailInput.value = "";
            } else {
                // Trường hợp lỗi từ Server (404, 500...)
                throw new Error(resultText);
            }

        } catch (error) {
            // Xử lý lỗi kết nối hoặc lỗi được throw ở trên
            console.error("Lỗi:", error);
            messageBox.style.color = "#721c24";
            messageBox.style.backgroundColor = "#f8d7da";
            messageBox.innerText = "Lỗi: " + error.message;
        }
    });
});