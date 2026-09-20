package com.project.smartinsurance.billingService.mapper;

import com.project.smartinsurance.billingService.dto.BankAccountDto;
import com.project.smartinsurance.billingService.dto.InvoiceDto;
import com.project.smartinsurance.billingService.dto.OnlinePaymentDto;
import com.project.smartinsurance.billingService.dto.PaymentGatewayDto;
import com.project.smartinsurance.billingService.dto.ReceiptDto;
import com.project.smartinsurance.billingService.dto.RefundDto;
import com.project.smartinsurance.billingService.model.BankAccount;
import com.project.smartinsurance.billingService.model.Invoice;
import com.project.smartinsurance.billingService.model.OnlinePayment;
import com.project.smartinsurance.billingService.model.PaymentGateway;
import com.project.smartinsurance.billingService.model.Receipt;
import com.project.smartinsurance.billingService.model.Refund;
import org.springframework.stereotype.Component;

@Component
public class BillingMapper {

    public BankAccountDto toDto(BankAccount entity) {
        if (entity == null) return null;
        BankAccountDto dto = new BankAccountDto();
        dto.setId(entity.getId());
        dto.setAccountName(entity.getAccountName());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setBankName(entity.getBankName());
        dto.setBranchName(entity.getBranchName());
        dto.setSwiftCode(entity.getSwiftCode());
        dto.setCurrency(entity.getCurrency());
        dto.setAccountHolder(entity.getAccountHolder());
        dto.setNotes(entity.getNotes());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public BankAccount toEntity(BankAccountDto dto) {
        if (dto == null) return null;
        BankAccount entity = new BankAccount();
        entity.setAccountName(dto.getAccountName());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setBankName(dto.getBankName());
        entity.setBranchName(dto.getBranchName());
        entity.setSwiftCode(dto.getSwiftCode());
        entity.setCurrency(dto.getCurrency());
        entity.setAccountHolder(dto.getAccountHolder());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    public void updateEntity(BankAccountDto dto, BankAccount entity) {
        if (dto.getAccountName() != null) entity.setAccountName(dto.getAccountName());
        if (dto.getAccountNumber() != null) entity.setAccountNumber(dto.getAccountNumber());
        if (dto.getBankName() != null) entity.setBankName(dto.getBankName());
        if (dto.getBranchName() != null) entity.setBranchName(dto.getBranchName());
        if (dto.getSwiftCode() != null) entity.setSwiftCode(dto.getSwiftCode());
        if (dto.getCurrency() != null) entity.setCurrency(dto.getCurrency());
        if (dto.getAccountHolder() != null) entity.setAccountHolder(dto.getAccountHolder());
        if (dto.getNotes() != null) entity.setNotes(dto.getNotes());
    }

    public InvoiceDto toDto(Invoice entity) {
        if (entity == null) return null;
        InvoiceDto dto = new InvoiceDto();
        dto.setId(entity.getId());
        dto.setInvoiceNo(entity.getInvoiceNo());
        dto.setPolicyId(entity.getPolicyId());
        dto.setPolicyNumber(entity.getPolicyNumber());
        dto.setCustomerId(entity.getCustomerId());
        dto.setBranchId(entity.getBranchId());
        dto.setInstallmentId(entity.getInstallmentId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setCustomerEmail(entity.getCustomerEmail());
        dto.setAmount(entity.getAmount());
        dto.setTaxAmount(entity.getTaxAmount());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setPaidAmount(entity.getPaidAmount());
        dto.setInvoiceDate(entity.getInvoiceDate());
        dto.setDueDate(entity.getDueDate());
        dto.setDescription(entity.getDescription());
        dto.setInvoiceStatus(entity.getInvoiceStatus());
        return dto;
    }

    public Invoice toEntity(InvoiceDto dto) {
        if (dto == null) return null;
        Invoice entity = new Invoice();
        entity.setInvoiceNo(dto.getInvoiceNo());
        entity.setPolicyId(dto.getPolicyId());
        entity.setPolicyNumber(dto.getPolicyNumber());
        entity.setCustomerId(dto.getCustomerId());
        entity.setBranchId(dto.getBranchId());
        entity.setInstallmentId(dto.getInstallmentId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setCustomerEmail(dto.getCustomerEmail());
        entity.setAmount(dto.getAmount());
        entity.setTaxAmount(dto.getTaxAmount());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setPaidAmount(dto.getPaidAmount());
        entity.setInvoiceDate(dto.getInvoiceDate());
        entity.setDueDate(dto.getDueDate());
        entity.setDescription(dto.getDescription());
        entity.setInvoiceStatus(dto.getInvoiceStatus());
        return entity;
    }

    public void updateEntity(InvoiceDto dto, Invoice entity) {
        if (dto.getCustomerName() != null) entity.setCustomerName(dto.getCustomerName());
        if (dto.getCustomerEmail() != null) entity.setCustomerEmail(dto.getCustomerEmail());
        if (dto.getAmount() != null) entity.setAmount(dto.getAmount());
        if (dto.getTaxAmount() != null) entity.setTaxAmount(dto.getTaxAmount());
        if (dto.getTotalAmount() != null) entity.setTotalAmount(dto.getTotalAmount());
        if (dto.getInvoiceDate() != null) entity.setInvoiceDate(dto.getInvoiceDate());
        if (dto.getDueDate() != null) entity.setDueDate(dto.getDueDate());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
    }

    public OnlinePaymentDto toDto(OnlinePayment entity) {
        if (entity == null) return null;
        OnlinePaymentDto dto = new OnlinePaymentDto();
        dto.setId(entity.getId());
        dto.setTransactionId(entity.getTransactionId());
        dto.setGatewayName(entity.getGatewayName());
        dto.setPayerName(entity.getPayerName());
        dto.setPayerEmail(entity.getPayerEmail());
        dto.setAmount(entity.getAmount());
        dto.setCurrency(entity.getCurrency());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setGatewayResponse(entity.getGatewayResponse());
        dto.setInvoiceNo(entity.getInvoiceNo());
        dto.setPolicyId(entity.getPolicyId());
        dto.setInstallmentId(entity.getInstallmentId());
        return dto;
    }

    public OnlinePayment toEntity(OnlinePaymentDto dto) {
        if (dto == null) return null;
        OnlinePayment entity = new OnlinePayment();
        entity.setTransactionId(dto.getTransactionId());
        entity.setGatewayName(dto.getGatewayName());
        entity.setPayerName(dto.getPayerName());
        entity.setPayerEmail(dto.getPayerEmail());
        entity.setAmount(dto.getAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setGatewayResponse(dto.getGatewayResponse());
        entity.setInvoiceNo(dto.getInvoiceNo());
        entity.setPolicyId(dto.getPolicyId());
        entity.setInstallmentId(dto.getInstallmentId());
        return entity;
    }

    public void updateEntity(OnlinePaymentDto dto, OnlinePayment entity) {
        if (dto.getGatewayName() != null) entity.setGatewayName(dto.getGatewayName());
        if (dto.getPayerName() != null) entity.setPayerName(dto.getPayerName());
        if (dto.getPayerEmail() != null) entity.setPayerEmail(dto.getPayerEmail());
        if (dto.getAmount() != null) entity.setAmount(dto.getAmount());
        if (dto.getCurrency() != null) entity.setCurrency(dto.getCurrency());
        if (dto.getGatewayResponse() != null) entity.setGatewayResponse(dto.getGatewayResponse());
        if (dto.getInvoiceNo() != null) entity.setInvoiceNo(dto.getInvoiceNo());
        if (dto.getPaymentStatus() != null) entity.setPaymentStatus(dto.getPaymentStatus());
        if (dto.getPolicyId() != null) entity.setPolicyId(dto.getPolicyId());
        if (dto.getInstallmentId() != null) entity.setInstallmentId(dto.getInstallmentId());
    }

    public PaymentGatewayDto toDto(PaymentGateway entity) {
        if (entity == null) return null;
        PaymentGatewayDto dto = new PaymentGatewayDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setProvider(entity.getProvider());
        dto.setMerchantId(entity.getMerchantId());
        dto.setApiKey(entity.getApiKey());
        dto.setSecretKey(entity.getSecretKey());
        dto.setWebhookUrl(entity.getWebhookUrl());
        dto.setGatewayType(entity.getGatewayType());
        dto.setActive(entity.getActive());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public PaymentGateway toEntity(PaymentGatewayDto dto) {
        if (dto == null) return null;
        PaymentGateway entity = new PaymentGateway();
        entity.setName(dto.getName());
        entity.setProvider(dto.getProvider());
        entity.setMerchantId(dto.getMerchantId());
        entity.setApiKey(dto.getApiKey());
        entity.setSecretKey(dto.getSecretKey());
        entity.setWebhookUrl(dto.getWebhookUrl());
        entity.setGatewayType(dto.getGatewayType());
        entity.setActive(dto.getActive());
        return entity;
    }

    public void updateEntity(PaymentGatewayDto dto, PaymentGateway entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getProvider() != null) entity.setProvider(dto.getProvider());
        if (dto.getMerchantId() != null) entity.setMerchantId(dto.getMerchantId());
        if (dto.getApiKey() != null) entity.setApiKey(dto.getApiKey());
        if (dto.getSecretKey() != null) entity.setSecretKey(dto.getSecretKey());
        if (dto.getWebhookUrl() != null) entity.setWebhookUrl(dto.getWebhookUrl());
        if (dto.getGatewayType() != null) entity.setGatewayType(dto.getGatewayType());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
    }

    public ReceiptDto toDto(Receipt entity) {
        if (entity == null) return null;
        ReceiptDto dto = new ReceiptDto();
        dto.setId(entity.getId());
        dto.setReceiptNo(entity.getReceiptNo());
        dto.setCustomerName(entity.getCustomerName());
        dto.setCustomerEmail(entity.getCustomerEmail());
        dto.setAmount(entity.getAmount());
        dto.setReceiptDate(entity.getReceiptDate());
        dto.setPaymentMode(entity.getPaymentMode());
        dto.setReferenceNo(entity.getReferenceNo());
        dto.setDescription(entity.getDescription());
        dto.setReceiptStatus(entity.getReceiptStatus());
        return dto;
    }

    public Receipt toEntity(ReceiptDto dto) {
        if (dto == null) return null;
        Receipt entity = new Receipt();
        entity.setReceiptNo(dto.getReceiptNo());
        entity.setCustomerName(dto.getCustomerName());
        entity.setCustomerEmail(dto.getCustomerEmail());
        entity.setAmount(dto.getAmount());
        entity.setReceiptDate(dto.getReceiptDate());
        entity.setPaymentMode(dto.getPaymentMode());
        entity.setReferenceNo(dto.getReferenceNo());
        entity.setDescription(dto.getDescription());
        entity.setReceiptStatus(dto.getReceiptStatus());
        return entity;
    }

    public void updateEntity(ReceiptDto dto, Receipt entity) {
        if (dto.getCustomerName() != null) entity.setCustomerName(dto.getCustomerName());
        if (dto.getCustomerEmail() != null) entity.setCustomerEmail(dto.getCustomerEmail());
        if (dto.getAmount() != null) entity.setAmount(dto.getAmount());
        if (dto.getReceiptDate() != null) entity.setReceiptDate(dto.getReceiptDate());
        if (dto.getPaymentMode() != null) entity.setPaymentMode(dto.getPaymentMode());
        if (dto.getReferenceNo() != null) entity.setReferenceNo(dto.getReferenceNo());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getReceiptStatus() != null) entity.setReceiptStatus(dto.getReceiptStatus());
    }

    public RefundDto toDto(Refund entity) {
        if (entity == null) return null;
        RefundDto dto = new RefundDto();
        dto.setId(entity.getId());
        dto.setRefundNo(entity.getRefundNo());
        dto.setInvoiceNo(entity.getInvoiceNo());
        dto.setCustomerName(entity.getCustomerName());
        dto.setAmount(entity.getAmount());
        dto.setRefundDate(entity.getRefundDate());
        dto.setReason(entity.getReason());
        dto.setRefundStatus(entity.getRefundStatus());
        return dto;
    }

    public Refund toEntity(RefundDto dto) {
        if (dto == null) return null;
        Refund entity = new Refund();
        entity.setRefundNo(dto.getRefundNo());
        entity.setInvoiceNo(dto.getInvoiceNo());
        entity.setCustomerName(dto.getCustomerName());
        entity.setAmount(dto.getAmount());
        entity.setRefundDate(dto.getRefundDate());
        entity.setReason(dto.getReason());
        entity.setRefundStatus(dto.getRefundStatus());
        return entity;
    }

    public void updateEntity(RefundDto dto, Refund entity) {
        if (dto.getInvoiceNo() != null) entity.setInvoiceNo(dto.getInvoiceNo());
        if (dto.getCustomerName() != null) entity.setCustomerName(dto.getCustomerName());
        if (dto.getAmount() != null) entity.setAmount(dto.getAmount());
        if (dto.getRefundDate() != null) entity.setRefundDate(dto.getRefundDate());
        if (dto.getReason() != null) entity.setReason(dto.getReason());
        if (dto.getRefundStatus() != null) entity.setRefundStatus(dto.getRefundStatus());
    }
}
