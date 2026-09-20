package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.model.BillingPayment;
import com.project.smartinsurance.billingService.model.PremiumRefund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Soft GL posting for premium collections/refunds.
 * Posts are logged and best-effort; full COA mapping can be configured later.
 */
@Service
@Slf4j
public class PremiumGlPostingService {

    public void postPremiumCollection(BillingPayment payment) {
        log.info("GL POST COLLECTION ref={} amount={} policy={} mode={}",
                payment.getPaymentRef(), payment.getAmount(), payment.getPolicyNumber(), payment.getPaymentMode());
    }

    public void postPremiumRefund(PremiumRefund refund) {
        log.info("GL POST REFUND no={} amount={} policy={} reason={}",
                refund.getRefundNo(), refund.getAmount(), refund.getPolicyNumber(), refund.getReasonCode());
    }
}
