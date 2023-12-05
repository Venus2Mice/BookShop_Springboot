document.addEventListener('DOMContentLoaded', function () {
    const formBtn = document.querySelector('.changepass');

    formBtn.addEventListener('click', function (event) {
        event.preventDefault();

        const oldPassword = document.getElementById('oldPasswordInput').value;
        const newPassword = document.getElementById('newPasswordInput').value;
        const confirmPassword = document.getElementById('confirmPasswordInput').value;

        if(!checkPass(newPassword)){
            displayModal('Mật khẩu cần chứa ít nhất 1 chữ cái, 1 số, 1 ký tự đặc biệt và dài ít nhất 6 ký tự.', false);
            return;
        }

        if (!oldPassword || !newPassword || !confirmPassword) {
            displayModal('Vui lòng điền đầy đủ thông tin.', false);
            return;
        }

        if (newPassword !== confirmPassword) {
            displayModal('Mật khẩu mới không khớp vui lòng nhập lại', false);
            return;
        }

        const xhr = new XMLHttpRequest();

        xhr.open('POST', '/change-password', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onload = function () {
            if (xhr.status === 200) {
                const response = xhr.responseText;
                if (response === 'success') {
                    displayModal('Thay đổi mật khẩu thành công!', true);
                } else if (response === 'failure') {
                    displayModal('Sai mật khẩu cũ!', false);
                } else {
                    // Xử lý các trường hợp khác nếu cần thiết
                }
            } else {
                console.error('Failed to change password');
            }
        };

        const params = new URLSearchParams();
        params.append('oldPassword', oldPassword);
        params.append('newPassword', newPassword);
        params.append('repeatNewPassword', confirmPassword);

        xhr.send(params);
    });

    const closeModalBtn = document.getElementById('closeModalBtn');
    closeModalBtn.addEventListener('click', function () {
        $('#resultModal').modal('hide'); // Đóng modal khi nhấn nút "Đóng"
    });
});

// Hàm hiển thị modal
function displayModal(message, isSuccess) {
    const modal = document.getElementById('resultModal');
    const modalBody = modal.querySelector('.modal-body');
    const modalTitle = modal.querySelector('.modal-title');

    modalBody.textContent = message;
    modalTitle.textContent = isSuccess ? 'Thành công' : 'Thay đổi mật khẩu thất bại';

    $(modal).modal('show'); // Sử dụng jQuery để hiển thị modal

    setTimeout(function () {
        $(modal).modal('hide');
    }, 3500); // Thay số 3000 bằng thời gian đóng modal (milliseconds)
}

function checkPass(password) {
    // Biểu thức chính quy để kiểm tra mật khẩu
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
    return regex.test(password);
}

const phoneNumber = document.getElementById('phoneNumber');
phoneNumber.addEventListener('input', () => {
    // Xóa mọi ký tự không phải số khỏi giá trị của input
    phoneNumber.value = phoneNumber.value.replace(/\D/g, '');
});





















