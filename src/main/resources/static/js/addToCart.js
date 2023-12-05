function addToCart(e, id,message) {
    e.preventDefault();
    var elementInput = document.getElementById('quantityInput');
    if (elementInput !== null) {
        var quantity = elementInput.value;
    }
    else {
        var quantity = 1;
    }
    var xhr = new XMLHttpRequest();
    var url = '/add-to-cart';

    // Xác định các thông tin cần gửi đi
    var params = 'id=' + encodeURIComponent(id) + '&quantity=' + encodeURIComponent(quantity);

    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var responseText = xhr.responseText;

                if (responseText.trim() === "redirect:/login") {
                    // Chuyển hướng đến trang login nếu response là "redirect:/login"
                    window.location.href = '/login';
                } else {
                    // Hiển thị modal khi không phải trang login
                    var successModal = new bootstrap.Modal(document.getElementById('successModal'));
                    var errorModalBody = document.querySelector('.modal-body');
                    errorModalBody.textContent = message;
                    successModal.show();
                    var headerTotalItems = document.getElementById('headerTotalItems');
                    headerTotalItems.textContent = parseInt(headerTotalItems.textContent) + parseInt(quantity);
                }
            } else {
                console.error('Đã có lỗi trong quá trình gửi request.');
            }
        }
    };

    // Gửi request với các tham số đã chuẩn bị
    xhr.send(params);
}