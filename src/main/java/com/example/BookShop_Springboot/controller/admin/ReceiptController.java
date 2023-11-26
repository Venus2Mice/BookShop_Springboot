package com.example.BookShop_Springboot.controller.admin;

import com.example.BookShop_Springboot.model.Receipt;
import com.example.BookShop_Springboot.model.Supplier;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BookShop_Springboot.dto.ReceiptDto;
import com.example.BookShop_Springboot.service.SupplierService;
import com.example.BookShop_Springboot.utils.ReceiptExcel;
import com.example.BookShop_Springboot.service.AdminService;
import com.example.BookShop_Springboot.service.ReceiptService;

import java.io.ByteArrayInputStream;
import org.springframework.http.HttpHeaders;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ReceiptController {
    private final String redirectLoginPath = "redirect:/admin/login";
    private final String receiptsPath = "/admin/receipts";
    private final String receiptDetailPath = "/admin/receipt-detail";
    private final String redirectReceiptsPath = "redirect:/admin/receipts/0";
    private final String redirectReceiptDetailWithIdParamPath = "redirect:/admin/receipt-detail?id=";

    private final ReceiptService receiptService;

    private final SupplierService supplierService;

    private final AdminService adminService;

    private final ReceiptExcel receiptExcel;

    @GetMapping("/receipts")
    public String receipts(Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        }
        return receiptsPath;
    }

    @GetMapping("/receipts/{pageNo}")
    public String allReceipts(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        }
        Page<ReceiptDto> receipts = receiptService.getAllReceipts(pageNo);
        model.addAttribute("title", "Manage Receipts");
        model.addAttribute("size", receipts.getSize());
        model.addAttribute("receipts", receipts);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", receipts.getTotalPages());
        return receiptsPath;
    }

    // @GetMapping("/search-receipts/{pageNo}")
    // public String searchReceipt(@PathVariable("pageNo") int pageNo,
    // @RequestParam(value = "keyword") String keyword,
    // Model model, Principal principal) {
    // if (principal == null) {
    // return redirectLoginPath;
    // }
    // Page<ReceiptDto> receipts = receiptService.searchReceipts(pageNo, keyword);
    // model.addAttribute("title", "Result Search Receipts");
    // model.addAttribute("size", receipts.getSize());
    // model.addAttribute("receipts", receipts);
    // model.addAttribute("currentPage", pageNo);
    // model.addAttribute("totalPages", receipts.getTotalPages());
    // return "receipts-result";

    // }

    @GetMapping("/add-receipt")
    public String addReceipt(RedirectAttributes redirectAttributes, Principal principal, Model model) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            Receipt receiptDefault = receiptService.initDefault(adminService.findByEmail(principal.getName()),
                    adminService.findByEmail(principal.getName()));
            return redirectReceiptDetailWithIdParamPath + receiptDefault.getId();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new receipt!");
        }
        return redirectReceiptsPath;
    }

    @GetMapping("/receipt-detail")
    public String receiptDetail(@RequestParam(name = "id") Long id, RedirectAttributes redirectAttributes,
            Principal principal, Model model) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            List<Supplier> suppliers = supplierService.findAllByActivatedTrue();
            Receipt receipt = receiptService.findById(id);
            model.addAttribute("title", "Receipt Detail");
            model.addAttribute("suppliers", suppliers);
            model.addAttribute("receipt", receipt);
            model.addAttribute("size", receipt.getReceiptDetails().size());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update receipt!");
        }
        return receiptDetailPath;
    }

    @PostMapping("/receipt-detail/import")
    @CacheEvict(cacheNames = "products", allEntries = true)
    public String importReceipt(@RequestParam(name = "file") MultipartFile file, @RequestParam(name = "id") Long id,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            receiptService.saveReceiptDetail(adminService.findByEmail(principal.getName()),
                    receiptExcel.importExcel(file), id);
            redirectAttributes.addFlashAttribute("success", "Add new product successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add product !");
        }
        return redirectReceiptDetailWithIdParamPath + id;
    }

    @PostMapping("/receipt/update")
    public String updateReceipt(@ModelAttribute("receipt") Receipt receipt,
            RedirectAttributes redirectAttributes, Principal principal, @RequestParam("id") Long id) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            receipt.setAdminUpdate(adminService.findByEmail(principal.getName()));
            Receipt receiptFound = receiptService.findById(id);
            receiptService.update(receipt, receiptFound);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
            return redirectReceiptDetailWithIdParamPath + receiptFound.getId();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
        }
        return redirectReceiptsPath;
    }

    @GetMapping("/receipt/export-default")
    public ResponseEntity<InputStreamResource> excelReceiptReport() throws Exception {

        ByteArrayInputStream in = receiptExcel.exportExcelDefault();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=receipt-default.xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));

    }

    @RequestMapping(value = "/enable-receipt", method = { RequestMethod.PUT, RequestMethod.GET })
    @CacheEvict(cacheNames = "products", allEntries = true)
    public String enabledReceipt(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            receiptService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return redirectReceiptsPath;
    }

    @RequestMapping(value = "/delete-receipt", method = { RequestMethod.PUT, RequestMethod.GET })
    public String deletedReceipt(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            receiptService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return redirectReceiptsPath;
    }

    @RequestMapping(value = "/receiptDetails/delete-receiptDetail", method = { RequestMethod.PUT,
            RequestMethod.GET })
    public String deleteProductInReceiptDetail(@RequestParam("id") Long id, @RequestParam("idReceipt") Long idReceipt,
            RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            receiptService.removeProductFromReceiptDetail(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return redirectReceiptDetailWithIdParamPath + idReceipt;
    }
}
