// Lấy danh sách các input cần xử lý
const quantityInputs = document.querySelectorAll('.quantity-input');

quantityInputs.forEach(quantityInput => {
    quantityInput.addEventListener('input', () => {
        // Xóa mọi ký tự không phải số khỏi giá trị của input
        quantityInput.value = quantityInput.value.replace(/\D/g, '');

        let value = parseInt(quantityInput.value);
        if (value > 100000) {
            quantityInput.value = 100000;
        }
        if (isNaN(value)) {
            quantityInput.value = 1;
        }
    });
});


function editQuantity(e, id) {
    e.preventDefault();
    var trElement = document.getElementById(id);
    var elementInput = trElement.querySelector('input.quantity-input');
    
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
                var responseData = JSON.parse(xhr.responseText);

                if (responseText.trim() === "redirect:/login") {
                    // Chuyển hướng đến trang login nếu response là "redirect:/login"
                    window.location.href = '/login';
                } else {
                    // Hiển thị modal khi không phải trang login
                    showModal("Thay đổi thành công");
                    var totalItems = responseData[0];
                    var totalPrice = responseData[1];

                    var pTotalItems = document.getElementById('totalItems');
                    var headerTotalItems = document.getElementById('headerTotalItems');
                    var grandTotal = document.getElementById('grandTotal');
                    pTotalItems.textContent = totalItems;
                    headerTotalItems.textContent = totalItems;

                    const originalValue = parseFloat(totalPrice);
                    const formattedValue = formatCurrency(originalValue);
                    grandTotal.textContent = formattedValue;
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
    var params = 'action=delete' + '&id=' + encodeURIComponent(id);

    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var responseText = xhr.responseText;
                var responseData = JSON.parse(xhr.responseText);

                if (responseText.trim() === "redirect:/login") {
                    // Chuyển hướng đến trang login nếu response là "redirect:/login"
                    window.location.href = '/login';
                } else {
                    // Hiển thị modal khi không phải trang login
                    showModal("Xóa thành công");
                    var totalItems = responseData[0];
                    var totalPrice = responseData[1];

                    var pTotalItems = document.getElementById('totalItems');
                    var headerTotalItems = document.getElementById('headerTotalItems');
                    var grandTotal = document.getElementById('grandTotal');
                    pTotalItems.textContent = totalItems;
                    headerTotalItems.textContent = totalItems;

                    const originalValue = parseFloat(totalPrice);
                    const formattedValue = formatCurrency(originalValue);
                    grandTotal.textContent = formattedValue;

                }
            } else {
                console.error('Đã có lỗi trong quá trình gửi request.');
            }
        }
    };

    // Gửi request với các tham số đã chuẩn bị
    xhr.send(params);
    setTimeout(function () {
        const teToRemove = document.getElementById(id);
        teToRemove.remove();
    }, 3000);
}

const closeModalBtn = document.getElementById('closeModalBtn');
closeModalBtn.addEventListener('click', function () {
    $('#successModal').modal('hide'); // Đóng modal khi nhấn nút "Đóng"
});

function showModal(Message) {
    var successModal = document.getElementById('successModal');
    var successModalBody = successModal.querySelector('.modal-body');
    successModalBody.textContent = Message;
    $(successModal).modal('show');

    // Tự động đóng modal sau 3 giây
    setTimeout(function () {
        $(successModal).modal('hide');
    }, 3000);
}