function formatCurrency(value) {
    // Chia nhỏ giá trị thành phần nguyên và phần thập phân
    const parts = value.toString().split('.');
    const intValue = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.'); // Thêm dấu chấm ngăn cách hàng ngàn
    
    let formattedValue = intValue + ' đ'; // Thêm ký tự 'đ' vào sau giá trị

    if (parts.length > 1) {
        formattedValue += ',' + parts[1]; // Thêm phần thập phân nếu có
    }

    return formattedValue;
}
// Lấy ra các phần tử có class "sale-price"
const salePrices = document.querySelectorAll('.product__price');

// Lặp qua từng phần tử và áp dụng định dạng tiền tệ
salePrices.forEach(priceElement => {
    const originalValue = parseFloat(priceElement.textContent);
    const formattedValue = formatCurrency(originalValue);
    priceElement.textContent = formattedValue;
});
