package com.example.BookShop_Springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.BookShop_Springboot.model.Customer;
import com.example.BookShop_Springboot.repository.CustomerRepository;
//import com.example.BookShop_Springboot.service.CustomerUserDetailService;

public class CustomerServiceConfig implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username);
        if (customer == null) {
            throw new UsernameNotFoundException("Could not find email");
        }

        return new CustomerDetail(customer);
    }
}
