// package com.pensionat.customer;

// import com.pensionat.booking.model.BookingStatus;
// import com.pensionat.booking.repository.BookingRepository;
// import com.pensionat.customer.dto.CreateCustomerRequest;
// import com.pensionat.customer.dto.UpdateCustomerRequest;
// import com.pensionat.customer.model.CustomerEntity;
// import com.pensionat.customer.repository.CustomerRepository;
// import com.pensionat.customer.service.CustomerService;
// import com.pensionat.exception.BadRequestException;
// import com.pensionat.exception.NotFoundException;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;


// @ExtendWith(MockitoExtension.class)
// class CustomerServiceTest {

//     @Mock
//     private CustomerRepository customerRepository;
//     @Mock
//     private BookingRepository bookingRepository;
//     @Mock
//     private PasswordEncoder passwordEncoder;
//     @InjectMocks
//     private CustomerService customerService;


//     @Test
//     void createCustomer_ShouldSaveAndReturnCustomer() {

//         CreateCustomerRequest request = new CreateCustomerRequest(
//                 "Daniel",
//                 "Lyytikäinen",
//                 "Test@test.com",
//                 "Password123",
//                 "+46701234567");

//         when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
//         CustomerEntity testResult = customerService.createCustomer(request);

//         assertNotNull(testResult);
//         assertEquals("Daniel", testResult.getFirstName());
//         assertEquals("Lyytikäinen", testResult.getLastName());
//         assertEquals("Test@test.com", testResult.getEmail());
//         assertEquals("+46701234567", testResult.getPhoneNumber());
//         assertNotEquals("Patric", testResult.getFirstName());

//         verify(customerRepository, times(1)).save(any(CustomerEntity.class));
//     }

//     @Test
//     void givenRepositoryFails_WhenCreateCustomer_ThenThrowException() {
//         CreateCustomerRequest request = new CreateCustomerRequest(
//                 "Daniel",
//                 "Lyytikäinen",
//                 "Test@test.com",
//                 "Password123",
//                 "+46701234567");
//         when(customerRepository.save(any(CustomerEntity.class))).thenThrow(new RuntimeException("Database error!"));
//         when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
//         assertThrows(RuntimeException.class, () -> customerService.createCustomer(request));
//     }

//     @Test
//     void givenInvalidId_WhenDeleteCustomer_ThenThrowNotFoundException() {
//         Long customerId = 1L;
//         when(customerRepository.existsById(customerId)).thenReturn(false);

//         assertThrows(NotFoundException.class, () -> customerService.deleteCustomer(customerId));
//         verify(customerRepository, never()).deleteById(any());
//     }

//     @Test
//     void givenCustomerWithActiveBooking_WhenDeleteCustomer_ThenThrowBadRequestException() {
//         Long customerId = 1L;
//         when(customerRepository.existsById(customerId)).thenReturn(true);
//         when(bookingRepository.existsByCustomerIdAndBookingStatus(customerId, BookingStatus.ACTIVE)).thenReturn(true);

//         assertThrows(BadRequestException.class, () -> customerService.deleteCustomer(customerId));
//         verify(customerRepository, never()).deleteById(customerId);
//     }

//     @Test
//     void givenValidRequest_WhenUpdateCustomer_ThenCustomerIsUpdated() {
//         Long customerId = 1L;
//         UpdateCustomerRequest request = new UpdateCustomerRequest(
//                 "Daniel",
//                 "Lyytikäinen",
//                 "UpdatedMail@mail.com",
//                 "NewPassword123",
//                 "+46701234567"

//         );

//         CustomerEntity existingCustomer = new CustomerEntity();

//         existingCustomer.setFirstName("Igor");
//         existingCustomer.setLastName("Gomes");
//         existingCustomer.setEmail("OldMail@mail.com");
//         existingCustomer.setHashedPassword("OldPassword123");
//         existingCustomer.setPhoneNumber("+46707654321");

//         when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
//         when(customerRepository.existsByEmailAndIdNot(request.email(), customerId)).thenReturn(false);

//         CustomerEntity result = customerService.updateCustomer(customerId, request);

//         assertNotNull(result);
//         assertEquals("Daniel", result.getFirstName());
//         assertEquals("UpdatedMail@mail.com", result.getEmail());
//         verify(customerRepository).save(result);
//     }

//     @Test
//     void givenInvalidRequest_WhenUpdateCustomer_ThenThrowNotFoundException() {
//         Long customerId = 1L;
//         UpdateCustomerRequest request = new UpdateCustomerRequest(
//                 "Daniel",
//                 "Lyytikäinen",
//                 "UpdatedMail@mail.com",
//                 "NewPassword123",
//                 "+46701234567"

//         );
//         when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
//         assertThrows(NotFoundException.class, () -> customerService.updateCustomer(customerId, request));
//         verify(customerRepository, never()).save(any(CustomerEntity.class));
//     }

//     @Test
//     void givenEmailAlreadyInUse_WhenUpdateCustomer_ThenThrowBadRequestException() {
//         Long customerId = 1L;
//         UpdateCustomerRequest request = new UpdateCustomerRequest(
//                 "Daniel",
//                 "Lyytikäinen",
//                 "UpdatedMail@mail.com",
//                 "NewPassword123",
//                 "+46701234567"

//         );

//         CustomerEntity existingCustomer = new CustomerEntity();
//         when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
//         when(customerRepository.existsByEmailAndIdNot(request.email(), customerId)).thenReturn(true);

//         assertThrows(BadRequestException.class, () -> customerService.updateCustomer(customerId, request));
//         verify(customerRepository, never()).save(any(CustomerEntity.class));
//     }
// }
