// package com.pensionat.customer.model;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Pattern;
// import lombok.Getter;
// import lombok.Setter;


// @Entity
// @Getter
// @Setter
// @Table(name = "Customer")

// public class CustomerEntity {

//     public CustomerEntity() {
//     }

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @NotBlank(message = "Invalid first name")
//     @Column(nullable = false, length = 50)
//     private String firstName;

//     @NotBlank(message = "Invalid last name")
//     @Column(nullable = false, length = 50)

//     private String lastName;

//     @NotBlank(message = "Invalid email")
//     @Column(nullable = false, unique = true, length = 100)
//     @Email(message = "Invalid email format")
//     private String email;

//     @NotBlank(message = "Invalid password")
//     @Column(nullable = false, length = 100)
//     private String hashedPassword;

//     @NotBlank(message = "Invalid phone number")
//     @Column(nullable = false, unique = true, length = 20)
//     @Pattern(
//             regexp = "^\\+?[0-9]{7,15}$",
//             message = "Invalid phone number"
//     )
//     private String phoneNumber;

// }

