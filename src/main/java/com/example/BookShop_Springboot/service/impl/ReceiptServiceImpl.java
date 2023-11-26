package com.example.BookShop_Springboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.BookShop_Springboot.dto.ReceiptDto;
import com.example.BookShop_Springboot.model.Admin;
import com.example.BookShop_Springboot.model.Product;
import com.example.BookShop_Springboot.model.Receipt;
import com.example.BookShop_Springboot.model.ReceiptDetail;
import com.example.BookShop_Springboot.repository.ProductRepository;
import com.example.BookShop_Springboot.repository.ReceiptDetailRepository;
import com.example.BookShop_Springboot.repository.ReceiptRepository;
import com.example.BookShop_Springboot.repository.SupplierRepository;
import com.example.BookShop_Springboot.service.ReceiptService;
import com.example.BookShop_Springboot.utils.DateFormatter;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;

    private final ReceiptDetailRepository receiptDetailRepository;

    private final SupplierRepository supplierRepository;

    private final ProductRepository productRepository;

    @Override
    public List<ReceiptDetail> findByReceiptId(Long id) {
        return receiptDetailRepository.findByReceiptId(id);
    }

    @Override
    public List<ReceiptDto> allReceipt() {
        List<Receipt> receipts = receiptRepository.findAll();
        List<ReceiptDto> receiptDtos = transferData(receipts);
        return receiptDtos;
    }

    @Override
    public Receipt save(ReceiptDto receiptDto) {
        Receipt receipt = new Receipt();
        receipt.setName(receiptDto.getName());
        receipt.setCreateDate(receiptDto.getCreateDate());
        receipt.setTotalPrice(receiptDto.getTotalPrice());
        receipt.setAdminCreate(receiptDto.getAdminCreate());
        receipt.setAdminUpdate(receiptDto.getAdminUpdate());
        receipt.setSupplier(receiptDto.getSupplier());
        receipt.setReceiptDetails(receiptDto.getReceiptDetails());
        return receiptRepository.save(receipt);
    }

    @Override
    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    @Override
    public Receipt initDefault(Admin adminCreate, Admin adminUpdate) {
        Receipt receipt = new Receipt();
        receipt.setName("Need edit");
        receipt.setCreateDate(DateFormatter.getCurrentDateFormatted());
        receipt.setTotalPrice(0);
        receipt.setCheckOut(false);
        receipt.setAdminCreate(adminCreate);
        receipt.setAdminUpdate(adminUpdate);
        receipt.setSupplier(supplierRepository.findAllByActivatedTrue().get(0));
        receipt.setReceiptDetails(new ArrayList<ReceiptDetail>());
        return receiptRepository.save(receipt);
    }

    @Override
    public Receipt update(ReceiptDto receiptDto) {
        try {
            Receipt receiptUpdate = receiptRepository.getReferenceById(receiptDto.getId());
            receiptUpdate.setName(receiptDto.getName());
            receiptUpdate.setCreateDate(receiptDto.getCreateDate());
            receiptUpdate.setTotalPrice(receiptDto.getTotalPrice());
            receiptUpdate.setAdminCreate(receiptDto.getAdminCreate());
            receiptUpdate.setAdminUpdate(receiptDto.getAdminUpdate());
            receiptUpdate.setSupplier(receiptDto.getSupplier());
            receiptUpdate.setReceiptDetails(receiptDto.getReceiptDetails());
            return receiptRepository.save(receiptUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Receipt update(Receipt receiptNew, Receipt receiptOld) {
        receiptOld.setAdminUpdate(receiptNew.getAdminUpdate());
        receiptOld.setName(receiptNew.getName());
        receiptOld.setSupplier(receiptNew.getSupplier());
        return receiptRepository.save(receiptOld);
    }

    @Override
    public void enableById(Long id) {
        Receipt receipt = receiptRepository.getReferenceById(id);
        receipt.setCheckOut(true);
        double totalPrice = 0;
        for (ReceiptDetail receiptDetail : receipt.getReceiptDetails()) {
            Product product = productRepository.getReferenceById(receiptDetail.getProduct().getId());
            product.setCurrentQuantity(receiptDetail.getQuantity() + product.getCurrentQuantity());
            productRepository.save(product);
            totalPrice += receiptDetail.getTotalPrice();
        }
        receipt.setTotalPrice(totalPrice);
        receiptRepository.save(receipt);
    }

    @Override
    public void deleteById(Long id) {
        Receipt receipt = receiptRepository.findById(id).get();
        if (!receipt.isCheckOut()) {
            receiptRepository.delete(receipt);
        }
    }

    @Override
    public ReceiptDto getById(Long id) {
        ReceiptDto receiptDto = new ReceiptDto();
        Receipt receipt = receiptRepository.getReferenceById(id);
        receiptDto.setId(receipt.getId());
        receiptDto.setName(receipt.getName());
        receiptDto.setCreateDate(receipt.getCreateDate());
        receiptDto.setTotalPrice(receipt.getTotalPrice());
        receiptDto.setAdminUpdate(receipt.getAdminUpdate());
        receiptDto.setAdminCreate(receipt.getAdminCreate());
        receiptDto.setSupplier(receipt.getSupplier());
        receiptDto.setReceiptDetails(receipt.getReceiptDetails());
        return receiptDto;
    }

    @Override
    public Receipt findById(Long id) {
        return receiptRepository.findById(id).get();
    }

    @Override
    public Page<ReceiptDto> searchReceipts(int pageNo, String keyword) {
        List<Receipt> receipts = receiptRepository.searchReceipts(keyword);
        List<ReceiptDto> receiptDtoList = transferData(receipts);
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ReceiptDto> dtoPage = toPage(receiptDtoList, pageable);
        return dtoPage;
    }

    @Override
    public Page<ReceiptDto> getAllReceipts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ReceiptDto> receiptDtoLists = this.allReceipt();
        Page<ReceiptDto> receiptDtoPage = toPage(receiptDtoLists, pageable);
        return receiptDtoPage;
    }

    @Override
    public List<ReceiptDto> findAllBySupplier(String supplier) {
        return transferData(receiptRepository.findAllBySupplier(supplier));
    }

    @Override
    public List<ReceiptDto> findBySupplierId(Long id) {
        return transferData(receiptRepository.getReceiptBySupplierId(id));
    }

    @Override
    public List<ReceiptDto> searchReceipts(String keyword) {
        return transferData(receiptRepository.searchReceipts(keyword));
    }

    private Page toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    private List<ReceiptDto> transferData(List<Receipt> receipts) {
        List<ReceiptDto> receiptDtos = new ArrayList<>();
        for (Receipt receipt : receipts) {
            ReceiptDto receiptDto = new ReceiptDto();
            receiptDto.setId(receipt.getId());
            receiptDto.setName(receipt.getName());
            receiptDto.setCreateDate(receipt.getCreateDate());
            receiptDto.setTotalPrice(receipt.getTotalPrice());
            receiptDto.setAdminUpdate(receipt.getAdminUpdate());
            receiptDto.setAdminCreate(receipt.getAdminCreate());
            receiptDto.setSupplier(receipt.getSupplier());
            receiptDto.setCheckOut(receipt.isCheckOut());
            receiptDto.setReceiptDetails(receipt.getReceiptDetails());
            receiptDtos.add(receiptDto);
        }
        return receiptDtos;
    }

    // @Override
    // public void removeProductFromReceiptDetail(Long id, Long productId) {
    // Receipt receipt = receiptRepository.findById(id).get();
    // List<ReceiptDetail> receiptDetails = receipt.getReceiptDetails();
    // ReceiptDetail receiptDetailFound = null;
    // for (ReceiptDetail receiptDetail : receiptDetails) {
    // if (receiptDetail.getProduct().getId().equals(productId)) {
    // receiptDetailFound = receiptDetail;
    // }
    // }
    // receiptDetails.remove(receiptDetailFound);
    // receipt.setReceiptDetails(receiptDetails);
    // receiptDetailRepository.delete(receiptDetailFound);
    // receiptRepository.save(receipt);
    // }

    @Override
    public void saveReceiptDetail(Admin findByEmail, List<ReceiptDetail> receiptDetails, Long id) {
        Receipt receipt = receiptRepository.findById(id).orElse(null); // Lấy thông tin receipt theo ID
        
        if (receipt != null) {
            List<ReceiptDetail> existingDetails = receipt.getReceiptDetails(); // Danh sách chi tiết phiếu hiện tại của receipt
    
            for (ReceiptDetail newDetail : receiptDetails) {
                // Kiểm tra xem sản phẩm của chi tiết phiếu mới đã tồn tại trong danh sách cũ chưa
                boolean found = false;
                for (ReceiptDetail existingDetail : existingDetails) {
                    if (existingDetail.getProduct().getId().equals(newDetail.getProduct().getId())) {
                        // Nếu đã tồn tại, cộng dồn quantity và totalPrice vào chi tiết phiếu cũ tương ứng
                        existingDetail.setQuantity(existingDetail.getQuantity() + newDetail.getQuantity());
                        existingDetail.setTotalPrice(existingDetail.getTotalPrice() + newDetail.getTotalPrice());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // Nếu không tìm thấy sản phẩm trong danh sách cũ, thêm chi tiết phiếu mới vào danh sách
                    newDetail.setReceipt(receipt);
                    existingDetails.add(newDetail);
                }
            }
    
            // Cập nhật thông tin người quản trị và lưu lại receipt
            receipt.setReceiptDetails(existingDetails);
            receipt.setAdminUpdate(findByEmail);
            receiptRepository.save(receipt);
        }
    }
     

    @Override
    public void removeProductFromReceiptDetail(Long id) {
        ReceiptDetail receiptDetail = receiptDetailRepository.findById(id).get();
        receiptDetailRepository.delete(receiptDetail);
    }

}
