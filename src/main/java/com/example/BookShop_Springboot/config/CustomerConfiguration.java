// package com.example.BookShop_Springboot.config;

// import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// @Configuration
// @EnableWebSecurity
// @Order(2)
// public class CustomerConfiguration {
//     @Bean
//     public BCryptPasswordEncoder passwordEncoder2() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public UserDetailsService userDetailsServiceCustomer() {
//         return new CustomerServiceConfig();
//     }

//     @Bean
//     public SecurityFilterChain filterChainCustomer(HttpSecurity http) throws Exception {
//         AuthenticationManagerBuilder authenticationManagerBuilder = http
//                 .getSharedObject(AuthenticationManagerBuilder.class);

//         authenticationManagerBuilder
//                 .userDetailsService(userDetailsServiceCustomer())
//                 .passwordEncoder(passwordEncoder2());

//         AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
//         http
//                 .csrf(AbstractHttpConfigurer::disable)
//                 .authorizeHttpRequests(author -> author
//                         .requestMatchers(PathRequest.toStaticResources()
//                                 .atCommonLocations())
//                         .permitAll()
//                         .requestMatchers("/*", "/product-detail/**").permitAll()
//                         .requestMatchers("/shop/**", "/find-products/**")
//                         .hasAuthority("CUSTOMER")

//                 )
//                 .formLogin(login -> login.loginPage("/login")
//                         .loginProcessingUrl("/do-login")
//                         .defaultSuccessUrl("/index", true)
//                         .permitAll())
//                 .logout(logout -> logout.invalidateHttpSession(true)
//                         .clearAuthentication(true)
//                         .logoutRequestMatcher(
//                                 new AntPathRequestMatcher("/logout"))
//                         .logoutSuccessUrl("/login?logout")
//                         .permitAll())
//                 .authenticationManager(authenticationManager)
//                 .sessionManagement(session -> session
//                         .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
//         return http.build();
//     }
// }
