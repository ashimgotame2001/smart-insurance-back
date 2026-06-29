package com.project.smartinsurance.customerService.repository;

import com.project.smartinsurance.customerService.model.Customer;
import com.project.smartinsurance.customerService.model.IndividualCustomer;
import com.project.smartinsurance.customerService.model.enums.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByCustomerCode(String customerCode);
    Optional<Customer> findByCustomerCode(String customerCode);

    Page<Customer> findByCustomerType(CustomerType customerType, Pageable pageable);

    @Query("SELECT c FROM IndividualCustomer c WHERE c.identityNumber IS NOT NULL AND c.identityNumber <> ''")
    Page<IndividualCustomer> findIndividualCustomersWithIdentityDocs(Pageable pageable);

    @Query("SELECT c FROM IndividualCustomer c WHERE c.identityNumber IS NOT NULL AND c.identityNumber <> ''")
    List<IndividualCustomer> findIndividualCustomersWithIdentityDocs();
}
