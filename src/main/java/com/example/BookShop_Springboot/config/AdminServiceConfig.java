package com.example.BookShop_Springboot.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.BookShop_Springboot.model.Admin;
import com.example.BookShop_Springboot.repository.AdminRepository;

public class AdminServiceConfig implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(username);
        if (admin == null) {
            throw new UsernameNotFoundException("could not find email");
        }
        List<SimpleGrantedAuthority> authorities = admin.getRole().getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority(admin.getRole().getName()));
        return new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    authorities);
        // admin.setStatus(true);
        // return new AdminDetail(admin);
    }

}
