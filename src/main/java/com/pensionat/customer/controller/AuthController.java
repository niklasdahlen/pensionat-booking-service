// package com.pensionat.customer.controller;

// import com.pensionat.customer.dto.LoginRequest;
// import com.pensionat.customer.dto.LoginResponse;
// import com.pensionat.customer.service.CustomerService;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {

//     private final CustomerService customerService;

//     public AuthController(CustomerService customerService) {
//         this.customerService = customerService;
//     }

//     @PostMapping("/login")
//     public LoginResponse login(@RequestBody LoginRequest loginRequest) {
//         return customerService.login(loginRequest);
//     }


// }
