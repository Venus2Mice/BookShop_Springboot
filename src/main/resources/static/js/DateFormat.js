function formatDate(inputString) {
    let dateObj = new Date(inputString);

    // Lấy thông tin ngày, tháng, năm
    let year = dateObj.getFullYear().toString().slice(-2);
    let month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
    let day = dateObj.getDate().toString().padStart(2, '0');

    // Lấy thông tin giờ, phút, giây
    let hour = dateObj.getHours().toString().padStart(2, '0');
    let minute = dateObj.getMinutes().toString().padStart(2, '0');
    let second = dateObj.getSeconds().toString().padStart(2, '0');

    // Tạo chuỗi định dạng theo yêu cầu
    let formattedDate = `${year}/${month}/${day} ${hour}h:${minute}p:${second}s`;

    return formattedDate;
}

let timeValues = document.getElementsByClassName('timeValue');

Array.from(timeValues).forEach(element => {
    let formattedTime = formatDate(element.textContent);
    element.textContent = formattedTime;
});
