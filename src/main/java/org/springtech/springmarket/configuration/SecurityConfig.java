package org.springtech.springmarket.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springtech.springmarket.filter.CustomAuthorizationFilter;
import org.springtech.springmarket.handler.CustomAccessDeniedHandler;
import org.springtech.springmarket.handler.CustomAuthenticationEntryPoint;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springtech.springmarket.constant.Constants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final CustomAuthorizationFilter customAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(configure -> configure.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(PUBLIC_URLS).permitAll()
                                .requestMatchers(OPTIONS).permitAll()



//                                .requestMatchers(PATCH, "/user/update/role//{roleName}")
//                                .hasAnyAuthority("UPDATE:USER_ROLE")
//                                .requestMatchers(PATCH, "/user/update/{userId}/settings")
//                                .hasAnyAuthority("UPDATE:USER")
//
//
//                                .requestMatchers(POST, "/agency/create")
//                                .hasAnyAuthority("CREATE:AGENCY")
//                                .requestMatchers(PUT, "/agency/update")
//                                .hasAnyAuthority("UPDATE:AGENCY")
//                                .requestMatchers(DELETE, "/agency/delete/{id}")
//                                .hasAnyAuthority("DELETE:AGENCY")
//
//
//                                .requestMatchers(PUT, "/category/update")
//                                .hasAnyAuthority("UPDATE:CATEGORY")
//                                .requestMatchers(DELETE, "/category/delete/{id}")
//                                .hasAnyAuthority("DELETE:CATEGORY")
//
//
//
//                                .requestMatchers(POST, "/customer/create")
//                                .hasAnyAuthority("CREATE:CUSTOMER")
//                                .requestMatchers(PUT, "/customer/update")
//                                .hasAnyAuthority("UPDATE:CUSTOMER")
//                                .requestMatchers(DELETE, "/customer/delete/{id}")
//                                .hasAnyAuthority("DELETE:CUSTOMER")
//
//
//                                .requestMatchers(POST, "/fournisseur/create")
//                                .hasAnyAuthority("CREATE:SUPPLIER")
//                                .requestMatchers(PUT, "/fournisseur/update")
//                                .hasAnyAuthority("UPDATE:SUPPLIER")
//                                .requestMatchers(DELETE, "/fournisseur/delete/{id}")
//                                .hasAnyAuthority("DELETE:SUPPLIER")
//
//
//
//                                .requestMatchers(POST, "/invoice/create")
//                                .hasAnyAuthority("CREATE:INVOICE")
//                                .requestMatchers(PUT, "/invoice/update")
//                                .hasAnyAuthority("UPDATE:INVOICE")
//                                .requestMatchers(DELETE, "/invoice/delete/{id}")
//                                .hasAnyAuthority("DELETE:INVOICE")
//                                .requestMatchers(POST, "/invoice/addtocustomer/{id}")
//                                .hasAnyAuthority("CREATE:INVOICE")
//
//
//                                .requestMatchers(POST, "/ligne/create")
//                                .hasAnyAuthority("CREATE:ORDER")
//                                .requestMatchers(DELETE, "/ligne/delete/{id}")
//                                .hasAnyAuthority("DELETE:ORDER")
//                                .requestMatchers(DELETE, "/ligne/deleteLcDetail/{id}")
//                                .hasAnyAuthority("DELETE:ORDER")
//                                .requestMatchers(POST, "/ligne/addto/{invoiceId}/{productId}")
//                                .hasAnyAuthority("CREATE:ORDER")
//
//                                .requestMatchers(POST, "/product/create/")
//                                .hasAnyAuthority("CREATE:PRODUCT")
//                                .requestMatchers(PATCH, "/product/update")
//                                .hasAnyAuthority("UPDATE:PRODUCT")
//                                .requestMatchers(DELETE, "/product/delete/{id}")
//                                .hasAnyAuthority("DELETE:PRODUCT")
//                                .requestMatchers(POST, "/product/addto/{agencyId}/{fournisseurId}/{categoryId}")
//                                .hasAnyAuthority("CREATE:PRODUCT")
//
//
//                                .requestMatchers(POST, "/stock/create")
//                                .hasAnyAuthority("CREATE:INVENTORY")
//                                .requestMatchers(POST, "/stock/addtoproduct/{id}")
//                                .hasAnyAuthority("CREATE:INVENTORY")

                                // User related endpoints
                                .requestMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("READ:USER", "READ:USERS")
                                .requestMatchers(HttpMethod.PATCH, "/user/update/role/{roleName}").hasAuthority("UPDATE:USER_ROLE")
                                .requestMatchers(HttpMethod.PATCH, "/user/update/{userId}/settings").hasAuthority("UPDATE:USER")
                                // Agency related endpoints
                                .requestMatchers(HttpMethod.GET, "/agency/**").hasAuthority("READ:AGENCY")
                                .requestMatchers(HttpMethod.POST, "/agency/create").hasAuthority("CREATE:AGENCY")
                                .requestMatchers(HttpMethod.PUT, "/agency/update").hasAuthority("UPDATE:AGENCY")
                                .requestMatchers(HttpMethod.DELETE, "/agency/delete/{id}").hasAuthority("DELETE:AGENCY")
                                // Category related endpoints
                                .requestMatchers(HttpMethod.GET, "/category/**").hasAuthority("READ:CATEGORY")
                                .requestMatchers(HttpMethod.POST, "/category/create").hasAuthority("CREATE:CATEGORY")
                                .requestMatchers(HttpMethod.PUT, "/category/update").hasAuthority("UPDATE:CATEGORY")
                                .requestMatchers(HttpMethod.DELETE, "/category/delete/{id}").hasAuthority("DELETE:CATEGORY")
                                // Customer related endpoints
                                .requestMatchers(HttpMethod.GET, "/customer/**").hasAuthority("READ:CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/customer/create").hasAuthority("CREATE:CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/customer/update").hasAuthority("UPDATE:CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/customer/delete/{id}").hasAuthority("DELETE:CUSTOMER")
                                // Supplier related endpoints
                                .requestMatchers(HttpMethod.GET, "/supplier/**").hasAuthority("READ:SUPPLIER")
                                .requestMatchers(HttpMethod.POST, "/supplier/create").hasAuthority("CREATE:SUPPLIER")
                                .requestMatchers(HttpMethod.PUT, "/supplier/update").hasAuthority("UPDATE:SUPPLIER")
                                .requestMatchers(HttpMethod.DELETE, "/supplier/delete/{id}").hasAuthority("DELETE:SUPPLIER")
                                // Invoice related endpoints
                                .requestMatchers(HttpMethod.GET, "/invoice/**").hasAuthority("READ:INVOICE")
                                .requestMatchers(HttpMethod.POST, "/invoice/create").hasAuthority("CREATE:INVOICE")
                                .requestMatchers(HttpMethod.PUT, "/invoice/update").hasAuthority("UPDATE:INVOICE")
                                .requestMatchers(HttpMethod.DELETE, "/invoice/delete/{id}").hasAuthority("DELETE:INVOICE")
                                .requestMatchers(HttpMethod.POST, "/invoice/addtocustomer/{id}").hasAuthority("CREATE:INVOICE")
                                // Order related endpoints
                                .requestMatchers(HttpMethod.GET, "/order/**").hasAuthority("READ:ORDER")
                                .requestMatchers(HttpMethod.POST, "/order/create").hasAuthority("CREATE:ORDER")
                                .requestMatchers(HttpMethod.DELETE, "/order/delete/{id}").hasAuthority("DELETE:ORDER")
                                .requestMatchers(HttpMethod.DELETE, "/order/deleteLcDetail/{id}").hasAuthority("DELETE:ORDER")
                                .requestMatchers(HttpMethod.POST, "/order/addto/{invoiceId}/{productId}").hasAuthority("CREATE:ORDER")
                                // Product related endpoints
                                .requestMatchers(HttpMethod.GET, "/product/**").hasAuthority("READ:PRODUCT")
                                .requestMatchers(HttpMethod.POST, "/product/create").hasAuthority("CREATE:PRODUCT")
                                .requestMatchers(HttpMethod.PATCH, "/product/update").hasAuthority("UPDATE:PRODUCT")
                                .requestMatchers(HttpMethod.DELETE, "/product/delete/{id}").hasAuthority("DELETE:PRODUCT")
                                .requestMatchers(HttpMethod.POST, "/product/addto/{agencyId}/{supplierId}/{categoryId}").hasAuthority("CREATE:PRODUCT")
                                // Inventory related endpoints
                                .requestMatchers(HttpMethod.GET, "/inventory/**").hasAuthority("READ:INVENTORY")
                                .requestMatchers(HttpMethod.POST, "/inventory/create").hasAuthority("CREATE:INVENTORY")
                                .requestMatchers(HttpMethod.POST, "/inventory/addtoproduct/{id}").hasAuthority("CREATE:INVENTORY")
                                // Any other requests


                                .anyRequest().authenticated())
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200", "http://192.168.56.1:4200", "https://projet-erp-frontend.vercel.app", "https://projet-erp-frontend.vercel.app/"));
        //corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin", "Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "File-Name"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }
}
