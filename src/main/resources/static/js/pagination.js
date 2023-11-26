// pagination.js
document.addEventListener("DOMContentLoaded", function() {
    const currentPageElement = document.getElementById("currentPage");
    const leftArrow = document.getElementById("arrow--left");
    const rightArrow = document.getElementById("arrow--right");

    leftArrow.addEventListener("click", function(event) {
        event.preventDefault();
        // Xử lý khi nhấn nút trái
        const currentPage = parseInt(currentPageElement.textContent);
        if (currentPage > 1) {
            // Gửi request hoặc thay đổi trang ở đây
            // Ví dụ: window.location.href = "/your-url?page=" + (currentPage - 1);
            currentPageElement.textContent = currentPage - 1;
        }
    });

    rightArrow.addEventListener("click", function(event) {
        event.preventDefault();
        // Xử lý khi nhấn nút phải
        const currentPage = parseInt(currentPageElement.textContent);
        // Thay đổi điều kiện kiểm tra dựa trên số trang thực tế của bạn
        if (currentPage < 10) {
            // Gửi request hoặc thay đổi trang ở đây
            // Ví dụ: window.location.href = "/your-url?page=" + (currentPage + 1);
            currentPageElement.textContent = currentPage + 1;
        }
    });
});
