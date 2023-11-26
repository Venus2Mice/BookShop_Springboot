// Validation for Add Category Modal
document.getElementById('form-category').addEventListener('submit', function (event) {
    const categoryName = document.getElementById('name').value.trim();

    if (categoryName.length > 20) {
        event.preventDefault(); 
        var exampleAddCategoryModal = document.getElementById('exampleAddCategoryModal');
        $(exampleAddCategoryModal).modal('hide');
        showErrorModal('Category name should not exceed 20 characters.');
        return;
    }
});

// Validation for Edit Category Modal
document.getElementById('form-category-edit').addEventListener('submit', function (event) {
    const editedCategoryName = document.getElementById('nameEdit').value.trim();

    if (editedCategoryName.length > 20) {
        event.preventDefault(); 
        var editModal = document.getElementById('editModal');
        $(editModal).modal('hide');
        showErrorModal('Category name should not exceed 20 characters.');
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



