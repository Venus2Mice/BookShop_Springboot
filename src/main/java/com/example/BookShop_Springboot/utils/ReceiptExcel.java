package com.example.BookShop_Springboot.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.BookShop_Springboot.model.Product;
import com.example.BookShop_Springboot.model.ReceiptDetail;
import com.example.BookShop_Springboot.repository.ProductRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ReceiptExcel {
    @Autowired
    private ProductRepository productRepository;

    /* Cout Row of Excel Table */
    public static int CountRowExcel(Iterator<Row> iterator) {
        int size = 0;
        while (iterator.hasNext()) {
            Row row = iterator.next();
            size++;
        }
        return size;
    }

    /* Import */
    public List<ReceiptDetail> importExcel(MultipartFile file) throws Exception {

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        List<ReceiptDetail> receiptDetails = new ArrayList<>();
        for (int i = 0; i < (CountRowExcel(sheet.rowIterator())); i++) {
            if (i == 0) {
                continue;
            }

            Row row = sheet.getRow(i);

            String nameProduct = row.getCell(0).getStringCellValue();
            int quantity = (int) row.getCell(1).getNumericCellValue();
            double totalPrice = row.getCell(2).getNumericCellValue();

            Product productFound = productRepository.findByName(nameProduct);

            if (productFound != null) {
                ReceiptDetail receiptDetail = new ReceiptDetail();
                receiptDetail.setProduct(productFound);
                receiptDetail.setQuantity(quantity);
                receiptDetail.setTotalPrice(totalPrice);
                receiptDetails.add(receiptDetail);
            } else {
                Product product = new Product();
                product.setName(nameProduct);
                product.setActivated(true);
                product.setCurrentQuantity(0);
                product.setDeleted(false);
                productRepository.save(product);

                ReceiptDetail receiptDetail = new ReceiptDetail();
                receiptDetail.setProduct(product);
                receiptDetail.setQuantity(quantity);
                receiptDetail.setTotalPrice(totalPrice);
                receiptDetails.add(receiptDetail);
            }
        }
        
        return receiptDetails;
    }

    /* export */
    public ByteArrayInputStream exportExcel(List<ReceiptDetail> receiptDetails) throws Exception {
        String[] columns = { "nameProduct", "quantity", "totalPrice" };
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CreationHelper creationHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Data receipt");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row of Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (ReceiptDetail receiptDetail : receiptDetails) {
                Row row = sheet.createRow(rowIdx);

                row.createCell(0).setCellValue(receiptDetail.getProduct().getName());
                row.createCell(1).setCellValue(receiptDetail.getQuantity());
                row.createCell(2).setCellValue(receiptDetail.getTotalPrice());
                rowIdx++;
            }

            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {

        }
        return null;
    }

    /* export default */
    public ByteArrayInputStream exportExcelDefault() throws Exception {
        String[] columns = { "nameProduct", "quantity", "totalPrice" };
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CreationHelper creationHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Data receipt");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row of Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }
            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {

        }
        return null;
    }
}
