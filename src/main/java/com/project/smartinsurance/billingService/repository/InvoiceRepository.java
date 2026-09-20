package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.Invoice;
import com.project.smartinsurance.billingService.model.Invoice.InvoiceStatus;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> findAllByStatus(Status status);
    Optional<Invoice> findByInvoiceNo(String invoiceNo);
    List<Invoice> findByStatusAndInvoiceStatus(Status entityStatus, InvoiceStatus invoiceStatus);

    long countByStatusAndInvoiceStatus(Status entityStatus, InvoiceStatus invoiceStatus);

    @Query("SELECT COALESCE(SUM(COALESCE(i.totalAmount, i.amount)), 0) FROM Invoice i WHERE i.status = :status AND i.invoiceStatus = :invoiceStatus")
    BigDecimal sumAmountByStatusAndInvoiceStatus(@Param("status") Status status, @Param("invoiceStatus") InvoiceStatus invoiceStatus);

    @Query("SELECT COALESCE(SUM(COALESCE(i.totalAmount, i.amount)), 0) FROM Invoice i WHERE i.status = :status AND i.invoiceStatus IN :invoiceStatuses")
    BigDecimal sumAmountByStatusAndInvoiceStatuses(@Param("status") Status status, @Param("invoiceStatuses") Collection<InvoiceStatus> invoiceStatuses);

    List<Invoice> findByStatusAndInvoiceStatusInOrderByDueDateAsc(Status status, Collection<InvoiceStatus> invoiceStatuses, Pageable pageable);
}
