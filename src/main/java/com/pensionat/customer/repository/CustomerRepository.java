// package com.pensionat.customer.repository;
// 
// import com.pensionat.customer.model.CustomerEntity;
// import org.springframework.data.jpa.repository.JpaRepository;
// 
// import java.util.Optional;
// 
// public interface CustomerRepository
//         extends JpaRepository<CustomerEntity, Long> {
//     boolean existsByEmailAndIdNot(String email, Long id);
// 
//     Optional<CustomerEntity> findByEmail(String email);
// }