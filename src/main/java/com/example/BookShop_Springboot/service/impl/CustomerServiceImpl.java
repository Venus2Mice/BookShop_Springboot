package com.example.BookShop_Springboot.service.impl;

import com.example.BookShop_Springboot.dto.CustomerDto;
import com.example.BookShop_Springboot.model.Customer;
import com.example.BookShop_Springboot.repository.CustomerRepository;
import com.example.BookShop_Springboot.service.CustomerService;
import com.example.BookShop_Springboot.utils.ImageUpload;

import lombok.RequiredArgsConstructor;

import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    private final ImageUpload imageUpload;

    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setUsername(customerDto.getUsername());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public CustomerDto getCustomer(String username) {
        CustomerDto customerDto = new CustomerDto();
        Customer customer = customerRepository.findByEmail(username);
        customerDto.setUsername(customer.getUsername());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPassword(customer.getPassword());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPhone(customer.getPhone());
        customerDto.setImage(customer.getImg_avt());
        return customerDto;
    }

    @Override
    public Customer changePass(CustomerDto customerDto) {
        Customer customer = customerRepository.findByEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(CustomerDto dto) {
        Customer customer = customerRepository.findByEmail(dto.getEmail());
        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            customer.setUsername(dto.getUsername());
        }

        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            customer.setPhone(dto.getPhone());
        }
        if (dto.getAddress() != null && !dto.getAddress().isEmpty()) {
            customer.setAddress(dto.getAddress());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            customer.setEmail(dto.getEmail());
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void update(CustomerDto customerDto, MultipartFile img_avt) {
        try {
            Customer customer = customerRepository.findByEmail(customerDto.getEmail());
            if (img_avt.getBytes().length > 0) {
                if (imageUpload.checkExist(img_avt)) {
                    customer.setImg_avt((customer.getImg_avt()));
                } else {
                    imageUpload.uploadFile(img_avt);
                    customer.setImg_avt(Base64.getEncoder().encodeToString(img_avt.getBytes()));
                }
            }
            if (customerDto.getUsername() != null && !customerDto.getUsername().isEmpty()) {
                customer.setUsername(customerDto.getUsername());
            }

            if (customerDto.getPhone() != null && !customerDto.getPhone().isEmpty()) {
                customer.setPhone(customerDto.getPhone());
            }
            if (customerDto.getAddress() != null && !customerDto.getAddress().isEmpty()) {
                customer.setAddress(customerDto.getAddress());
            }

            if (customerDto.getEmail() != null && !customerDto.getEmail().isEmpty()) {
                customer.setEmail(customerDto.getEmail());
            }
            customerRepository.save(customer);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
