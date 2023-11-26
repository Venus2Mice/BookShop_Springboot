package com.example.BookShop_Springboot.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AdminConfiguration {

        // @Bean
        // private AuthenticationManager authenticationManager(){
        // AuthenticationManagerBuilder authenticationManagerBuilder =
        // http.getSharedObject(AuthenticationManagerBuilder.class);

        // }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsServiceAdmin() {
                return new AdminServiceConfig();
        }

        @Bean
        public UserDetailsService userDetailsServiceCustomer() {
                return new CustomerServiceConfig();
        }

        @Bean
        @Order(1)
        public SecurityFilterChain filterChainAdmin(HttpSecurity http) throws Exception {

                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);

                authenticationManagerBuilder
                                .userDetailsService(userDetailsServiceAdmin())
                                .passwordEncoder(passwordEncoder());

                AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
                http.securityMatcher("/admin/**")
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(author -> author
                                                .requestMatchers(PathRequest.toStaticResources()
                                                                .atCommonLocations())
                                                .permitAll()

                                                .requestMatchers("/admin/forgot-password", "/admin/register",
                                                                "/admin/register-new")
                                                .permitAll()
                                                .requestMatchers("/admin/index").hasAuthority("ADMIN")
                                                .requestMatchers("/admin/**").authenticated()
                                // .anyRequest().permitAll()

                                )
                                .formLogin(login -> login.loginPage("/admin/login")
                                                .loginProcessingUrl("/admin/do-login")
                                                .defaultSuccessUrl("/admin/index", true)
                                                .permitAll())
                                .logout(logout -> logout.invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .logoutRequestMatcher(
                                                                new AntPathRequestMatcher("/admin/logout"))
                                                .logoutSuccessUrl("/admin/login?logout")
                                                .permitAll())
                                .authenticationManager(authenticationManager)
                                .sessionManagement(
                                                session -> session.sessionCreationPolicy(
                                                                SessionCreationPolicy.ALWAYS));
                return http.build();
        }

        @Bean

        public SecurityFilterChain filterChainCustomer(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);

                authenticationManagerBuilder
                                .userDetailsService(userDetailsServiceCustomer())
                                .passwordEncoder(passwordEncoder());

                AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(author -> author
                                                .requestMatchers(PathRequest.toStaticResources()
                                                                .atCommonLocations())
                                                .permitAll()
                                                .requestMatchers("/*", "/product-detail/**").permitAll()
                                                .requestMatchers("/shop/**", "/find-products/**")
                                                .hasAuthority("CUSTOMER")
                                                .requestMatchers("/**").permitAll()

                                )
                                .formLogin(login -> login.loginPage("/login")
                                                .loginProcessingUrl("/do-login")
                                                .defaultSuccessUrl("/index", true)
                                                .permitAll())
                                .logout(logout -> logout.invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .logoutRequestMatcher(
                                                                new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .authenticationManager(authenticationManager)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
                return http.build();
        }
}