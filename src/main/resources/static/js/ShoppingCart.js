function editQuantity(e, id) {
    e.preventDefault();
    var elementInput = document.getElementById('quantityInput');
    if (elementInput !== null) {
        var quantity = elementInput.value;
    }
    else {
        var quantity = 1;
    }
    var xhr = new XMLHttpRequest();
    var url = '/update-cart';

    // Xác định các thông tin cần gửi đi
    var params = 'action=update' + '&id=' + encodeURIComponent(id) + '&quantity=' + encodeURIComponent(quantity);

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
                    var successModal = document.getElementById('successModal');
                    successModal.show();
                }
            } else {
                console.error('Đã có lỗi trong quá trình gửi request.');
            }
        }
    };

    // Gửi request với các tham số đã chuẩn bị
    xhr.send(params);
}
function removeItem(e, id) {
    e.preventDefault();
    var xhr = new XMLHttpRequest();
    var url = '/update-cart';

    // Xác định các thông tin cần gửi đi
    var params = 'action=delete' + '&id=' + encodeURIComponent(id) ;

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
                    successModal.show();
                }
            } else {
                console.error('Đã có lỗi trong quá trình gửi request.');
            }
        }
    };

    // Gửi request với các tham số đã chuẩn bị
    xhr.send(params);
}