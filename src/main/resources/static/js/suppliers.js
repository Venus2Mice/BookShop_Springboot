$('document').ready(function() {
    $('table #editButton').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (supplier, status) {
            $('#idEdit').val(supplier.id);
            $('#nameEdit').val(supplier.name);
            $('#phoneEdit').val(supplier.phoneNumber);
            $('#emailEdit').val(supplier.email);
            $('#addressEdit').val(supplier.address);
        });
        $('#editModal').modal();
    });
});