// package com.pensionat.customer.service;


// import com.pensionat.booking.model.BookingStatus;
// import com.pensionat.booking.repository.BookingRepository;
// import com.pensionat.customer.dto.CreateCustomerRequest;
// import com.pensionat.customer.dto.LoginRequest;
// import com.pensionat.customer.dto.LoginResponse;
// import com.pensionat.customer.dto.UpdateCustomerRequest;
// import com.pensionat.customer.model.CustomerEntity;
// import com.pensionat.customer.repository.CustomerRepository;
// import com.pensionat.exception.BadRequestException;
// import com.pensionat.exception.NotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;

// @Service
// public class CustomerService {
//     private final CustomerRepository customerRepository;
//     private final BookingRepository bookingRepository;
//     private final PasswordEncoder passwordEncoder;


//     public CustomerService(CustomerRepository customerRepository, BookingRepository bookingRepository, PasswordEncoder passwordEncoder) {
//         this.customerRepository = customerRepository;
//         this.bookingRepository = bookingRepository;
//         this.passwordEncoder = passwordEncoder;
//     }

//     public List<CustomerEntity> getAllCustomers() {
//         return customerRepository.findAll();
//     }

//     public CustomerEntity createCustomer(CreateCustomerRequest request) {
//         CustomerEntity customer = new CustomerEntity();
//         customer.setFirstName(request.firstName());
//         customer.setLastName(request.lastName());
//         customer.setEmail(request.email());
//         customer.setHashedPassword(passwordEncoder.encode(request.hashedPassword()));
//         customer.setPhoneNumber(request.phone());
//         customerRepository.save(customer);
//         return customer;
//     }

//     @Transactional
//     public void deleteCustomer(Long id) {
//         if (!customerRepository.existsById(id)) {
//             throw new NotFoundException("Customer not found");
//         }
//         if (bookingRepository.existsByCustomerIdAndBookingStatus(id, BookingStatus.ACTIVE)) {
//             throw new BadRequestException("Customer cannot be deleted with an active booking");
//         }
//         bookingRepository.deleteByCustomerIdAndBookingStatus(id, BookingStatus.CANCELLED);
//         customerRepository.deleteById(id);
//     }

//     public CustomerEntity updateCustomer(Long id, UpdateCustomerRequest request) {
//         CustomerEntity customer = customerRepository.findById(id)
//                 .orElseThrow(() -> new NotFoundException("Customer not found"));

//         if (customerRepository.existsByEmailAndIdNot(request.email(), id)) {
//             throw new BadRequestException("Email is already in use");
//         }
//         customer.setFirstName(request.firstName());
//         customer.setLastName(request.lastName());
//         customer.setEmail(request.email());
//         if (request.hashedPassword() != null && !request.hashedPassword().isBlank()) {
//             customer.setHashedPassword(passwordEncoder.encode(request.hashedPassword()));
//         }
//         customer.setPhoneNumber(request.phone());
//         customerRepository.save(customer);
//         return customer;
//     }

//     public LoginResponse login(LoginRequest request) {
//         CustomerEntity customer = customerRepository.findByEmail(request.email())
//                 .orElseThrow(() -> new NotFoundException("Customer not found"));
//         if (!passwordEncoder.matches(request.password(), customer.getHashedPassword())) {
//             throw new BadRequestException("Invalid password");
//         }
//         return new LoginResponse(
//                 customer.getId(),
//                 customer.getFirstName(),
//                 customer.getLastName(),
//                 customer.getEmail(),
//                 customer.getPhoneNumber()
//         );
//     }
// }
