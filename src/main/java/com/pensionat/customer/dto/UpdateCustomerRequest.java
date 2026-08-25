// package com.pensionat.customer.dto;

// import jakarta.annotation.Nullable;
// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Pattern;

// public record UpdateCustomerRequest(
//         @NotBlank(message = "Invalid first name")
//         String firstName,

//         @NotBlank(message = "Invalid last name")
//         String lastName,

//         @NotBlank(message = "Invalid email")
//         @Email(message = "Invalid email format")
//         String email,

//         @Nullable
//         String hashedPassword,

//         @NotBlank(message = "Invalid phone number")
//         @Pattern(
//                 regexp = "^\\+?[0-9]{7,15}$",
//                 message = "Invalid phone number"
//         )
//         String phone

// ) {
// }
