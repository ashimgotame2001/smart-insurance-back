package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.Receipt;
import com.project.smartinsurance.billingService.model.Receipt.ReceiptStatus;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
    List<Receipt> findAllByStatus(Status status);
    Optional<Receipt> findByReceiptNo(String receiptNo);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Receipt r WHERE r.status = :status AND r.receiptStatus = :receiptStatus AND r.receiptDate >= :from AND r.receiptDate <= :to")
    BigDecimal sumAmountBetween(@Param("status") Status status,
                                @Param("receiptStatus") ReceiptStatus receiptStatus,
                                @Param("from") LocalDate from,
                                @Param("to") LocalDate to);
}
