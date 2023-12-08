package com.example.BookShop_Springboot.controller.admin;

import com.example.BookShop_Springboot.model.Order;
import com.example.BookShop_Springboot.model.Product;
import com.example.BookShop_Springboot.service.OrderService;
import com.lowagie.text.pdf.BaseFont;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
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

    @GetMapping("/pdf/export/order")
    public void pdftReport(Model model, HttpServletResponse response) throws Exception {

        List<Order> orders = orderService.findALlOrders();
        model.addAttribute("orders", orders);
        // Create a Thymeleaf context
        Context context = new Context();
        context.setVariables(model.asMap());

        String htmlContent = templateEngine.process("pdf/Orders", context);
        try {
            // Generate PDF from HTML content
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

            // Set font resolver to use Times New Roman
            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(
                    new ClassPathResource("static/fonts/vuArial.ttf").getFile().getAbsolutePath(),
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();

            // Set response content type
            response.setContentType("application/pdf");

            // Set content disposition to force download if needed
            response.setHeader("Content-Disposition", "attachment; filename=Order-List.pdf");
            response.setHeader("Content-Encoding", "UTF-8");

            // Write the output stream to the response's output stream
            OutputStream out = response.getOutputStream();
            outputStream.writeTo(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }

}
