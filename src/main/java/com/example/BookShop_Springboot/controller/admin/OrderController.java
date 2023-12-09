package com.example.BookShop_Springboot.controller.admin;

import com.example.BookShop_Springboot.model.Order;
import com.example.BookShop_Springboot.model.Product;
import com.example.BookShop_Springboot.service.OrderService;
import com.itextpdf.html2pdf.HtmlConverter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class OrderController {
    private final String redirectLoginPath = "redirect:/admin/login";
    private final String redirectOrdersPath = "redirect:/admin/orders";
    private final String ordersPath = "/admin/orders";

    private final OrderService orderService;

    private final TemplateEngine templateEngine;

    @GetMapping("/orders")
    public String getAll(Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        } else {
            List<Order> orderList = orderService.findALlOrders();
            model.addAttribute("orders", orderList);
            return ordersPath;
        }
    }

    @RequestMapping(value = "/accept-order", method = { RequestMethod.PUT, RequestMethod.GET })
    public String acceptOrder(String id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        } else {
            orderService.acceptOrder(id);
            attributes.addFlashAttribute("success", "Order Accepted");
            return redirectOrdersPath;
        }
    }

    @RequestMapping(value = "/order-detail", method = { RequestMethod.GET })
    public String orderDetail(String id, Principal principal, Model model) {
        if (principal == null) {
            return redirectLoginPath;
        } else {
            Order order = orderService.getOrderById(id);
            if (order == null)
                return redirectOrdersPath;
            model.addAttribute("order", order);
            model.addAttribute("orderDetails", order.getOrderDetailList());
            model.addAttribute("size", order.getOrderDetailList().size());
            return "/admin/order-detail";
        }
    }

    @GetMapping("/pdf/export/orders")
    public void downloadGeneratedPdfOrders(Model model, HttpServletResponse response) throws IOException {
        try {
            List<Order> orders = orderService.findALlOrders();
            model.addAttribute("orders", orders);

            // Create a Thymeleaf context
            Context context = new Context();
            context.setVariables(model.asMap());

            String processedTemplate = templateEngine.process("pdf/Orders", context);

            // Create an output stream to store the PDF content
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

            // Convert HTML string to PDF using iText
            HtmlConverter.convertToPdf(processedTemplate, pdfOutputStream);

            // Set response headers for PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=generated_file.pdf");

            // Write PDF content to the response OutputStream
            response.getOutputStream().write(pdfOutputStream.toByteArray());
            response.getOutputStream().flush();
        } catch (IOException e) {
            // Handle exceptions
        }
    }

    @GetMapping("/pdf/export/order-detail")
    public void downloadGeneratedPdfOrderDetail(Model model, HttpServletResponse response,
            @RequestParam("id") String id) throws IOException {
        try {
            Order order = orderService.getOrderById(id);
            model.addAttribute("order", order);

            // Create a Thymeleaf context
            Context context = new Context();
            context.setVariables(model.asMap());

            String processedTemplate = templateEngine.process("pdf/Order-detail", context);

            // Create an output stream to store the PDF content
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

            // Convert HTML string to PDF using iText
            HtmlConverter.convertToPdf(processedTemplate, pdfOutputStream);

            // Set response headers for PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=generated_file.pdf");

            // Write PDF content to the response OutputStream
            response.getOutputStream().write(pdfOutputStream.toByteArray());
            response.getOutputStream().flush();
        } catch (IOException e) {
            // Handle exceptions
        }
    }

}
