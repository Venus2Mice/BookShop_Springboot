$("textarea").val().replace(/\r?\n/g, "\\n")

document.getElementById('form-product').addEventListener('submit', function (event) {
    var discountInput = document.getElementById('discount');
    var priceInput = document.getElementById('price');
    var productNameInput = document.getElementById('product_name');
    var authorsInput = document.getElementById('authors');
    var publisherInput = document.getElementById('publisher');
    var descriptionTextarea = document.getElementById('description');


    // Kiểm tra giá trị discount
    var discountValue = parseFloat(discountInput.value);
    if (isNaN(discountValue) || discountValue < 0 || discountValue > 99) {
        event.preventDefault();
        showErrorModal('Discount must be a number between 0 and 99.');
        return;
    } else {
        discountInput.value = discountValue.toFixed(0);
    }

    // Kiểm tra giá trị price
    var priceValue = parseFloat(priceInput.value);
    if (isNaN(priceValue) || priceValue <= 0) {
        event.preventDefault();
        showErrorModal('Price must be a valid number greater than 0.');
        return;
    }

    // Kiểm tra độ dài của product name
    if (productNameInput.value.length > 50) {
        event.preventDefault();
        showErrorModal('Product name cannot exceed 50 characters.');
        return;
    }

    // Kiểm tra độ dài của authors
    if (authorsInput.value.length > 40) {
        event.preventDefault();
        showErrorModal('Authors field cannot exceed 40 characters.');
        return;
    }

    // Kiểm tra độ dài của publisher
    if (publisherInput.value.length > 40) {
        event.preventDefault();
        showErrorModal('Publisher field cannot exceed 40 characters.');
        return;
    }

    // Kiểm tra độ dài của description
    if (descriptionTextarea.value.length > 10000) {
        event.preventDefault();
        showErrorModal('Description cannot exceed 10000 characters.');
        return;
    }
});

function showErrorModal(errorMessage) {
    var errorModal = document.getElementById('errorModal');
    var errorModalBody = errorModal.querySelector('.modal-body');
    errorModalBody.textContent = errorMessage;
    $(errorModal).modal('show');

    // Tự động đóng modal sau 3 giây
    setTimeout(function () {
        $(errorModal).modal('hide');
    }, 3000);
}



