package com.example.BookShop_Springboot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    // Phương thức tĩnh để tạo và định dạng ngày hiện tại
    public static Date getCurrentDateFormatted() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        try {
            return dateFormat.parse(formattedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
