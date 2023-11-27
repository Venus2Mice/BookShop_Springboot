document.getElementById("registerForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Ngăn chặn việc submit form mặc định

    const passwordToCheck = document.getElementById("form3Example4").value;
    const isValidPassword = checkPass(passwordToCheck);

    const errorAlert = document.querySelector(".alert.alert-danger.pass");

    if (!isValidPassword) {
        // Hiển thị thông báo lỗi
        errorAlert.textContent = "Mật khẩu cần chứa ít nhất 1 chữ cái, 1 số, 1 ký tự đặc biệt và dài ít nhất 6 ký tự.";
        errorAlert.style.display = "block";
    } else {
        // Ẩn thông báo lỗi nếu mật khẩu hợp lệ
        errorAlert.style.display = "none";
        // Tiếp tục xử lý form (ví dụ: submit form)
        // this.submit(); // Bạn có thể gọi this.submit() nếu muốn tiếp tục submit form
        this.submit();
    }
});

function checkPass(password) {
    // Biểu thức chính quy để kiểm tra mật khẩu
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
    return regex.test(password);
}

