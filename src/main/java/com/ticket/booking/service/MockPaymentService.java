package com.ticket.booking.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MockPaymentService {
    
    // Myanmar payment methods
    private static final List<String> PAYMENT_METHODS = Arrays.asList(
        "KBZ_PAY", "WAVE_MONEY", "AYA_PAY", "CB_PAY", "ONEPAY", "MPU",
        "CASH", "CREDIT_CARD", "DEBIT_CARD", "BANK_TRANSFER"
    );
    
    // Payment statuses
    private static final List<String> PAYMENT_STATUSES = Arrays.asList(
        "PENDING", "PROCESSING", "SUCCESS", "FAILED", "REFUNDED", "CANCELLED"
    );
    
    // Myanmar bank names
    private static final List<String> MYANMAR_BANKS = Arrays.asList(
        "KBZ Bank", "CB Bank", "AYA Bank", "UAB Bank", "Yoma Bank", 
        "AGD Bank", "MAB Bank", "Myanma Apex Bank"
    );
    
    // Payment gateway names
    private static final List<String> PAYMENT_GATEWAYS = Arrays.asList(
        "Myanmar Payment Gateway", "Asia Payment", "Golden Gate", 
        "Wave Money Gateway", "KBZ Pay Gateway", "Digital Myanmar"
    );
    
    /**
     * Initialize a mock payment
     */
    public Map<String, Object> initializePayment(double amount, String currency, 
                                                String paymentMethod, String bookingReference) {
        Map<String, Object> paymentInit = new HashMap<>();
        Random random = new Random();
        
        // Generate payment ID
        String paymentId = "PAY" + System.currentTimeMillis() + random.nextInt(1000);
        
        // Generate transaction reference
        String transactionRef = "TXN" + System.currentTimeMillis() + random.nextInt(10000);
        
        paymentInit.put("paymentId", paymentId);
        paymentInit.put("transactionReference", transactionRef);
        paymentInit.put("amount", amount);
        paymentInit.put("currency", currency);
        paymentInit.put("paymentMethod", paymentMethod);
        paymentInit.put("bookingReference", bookingReference);
        paymentInit.put("status", "PENDING");
        paymentInit.put("initiatedAt", new Date());
        paymentInit.put("expiresAt", new Date(System.currentTimeMillis() + 30 * 60 * 1000)); // 30 minutes
        
        // Generate payment URL for redirection
        paymentInit.put("paymentUrl", generatePaymentUrl(paymentId));
        
        // QR code data for mobile payments
        paymentInit.put("qrCodeData", generateQRCodeData(amount, paymentId, transactionRef));
        
        return paymentInit;
    }
    
    /**
     * Process a mock payment
     */
    public Map<String, Object> processPayment(String paymentId, String paymentMethod, 
                                             Map<String, Object> paymentDetails) {
        Map<String, Object> paymentResult = new HashMap<>();
        Random random = new Random();
        
        // Simulate processing delay
        try {
            Thread.sleep(1000 + random.nextInt(2000)); // 1-3 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Determine success rate (85% success for demo)
        boolean isSuccess = random.nextDouble() < 0.85;
        
        paymentResult.put("paymentId", paymentId);
        paymentResult.put("transactionId", "TXN" + System.currentTimeMillis());
        paymentResult.put("paymentMethod", paymentMethod);
        paymentResult.put("processedAt", new Date());
        
        if (isSuccess) {
            paymentResult.put("status", "SUCCESS");
            paymentResult.put("message", "Payment processed successfully");
            paymentResult.put("confirmationNumber", generateConfirmationNumber());
            paymentResult.put("receiptUrl", generateReceiptUrl(paymentId));
            
            // Add bank/gateway details
            paymentResult.put("gateway", PAYMENT_GATEWAYS.get(random.nextInt(PAYMENT_GATEWAYS.size())));
            paymentResult.put("bank", MYANMAR_BANKS.get(random.nextInt(MYANMAR_BANKS.size())));
            paymentResult.put("authCode", "AUTH" + random.nextInt(1000000));
            
        } else {
            paymentResult.put("status", "FAILED");
            paymentResult.put("message", getFailureReason(random));
            paymentResult.put("errorCode", "ERR" + random.nextInt(100));
        }
        
        return paymentResult;
    }
    
    /**
     * Verify payment status
     */
    public Map<String, Object> verifyPayment(String paymentId, String transactionReference) {
        Map<String, Object> verification = new HashMap<>();
        Random random = new Random();
        
        verification.put("paymentId", paymentId);
        verification.put("transactionReference", transactionReference);
        verification.put("verifiedAt", new Date());
        
        // Most payments are successful in verification
        boolean isVerified = random.nextDouble() < 0.9;
        
        if (isVerified) {
            verification.put("status", "VERIFIED");
            verification.put("isSuccessful", true);
            verification.put("verificationCode", "VER" + random.nextInt(1000000));
        } else {
            verification.put("status", "NOT_VERIFIED");
            verification.put("isSuccessful", false);
            verification.put("verificationCode", null);
        }
        
        return verification;
    }
    
    /**
     * Refund a payment
     */
    public Map<String, Object> refundPayment(String paymentId, double amount, String reason) {
        Map<String, Object> refund = new HashMap<>();
        Random random = new Random();
        
        // Simulate processing delay
        try {
            Thread.sleep(2000 + random.nextInt(3000)); // 2-5 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        refund.put("refundId", "REF" + System.currentTimeMillis());
        refund.put("originalPaymentId", paymentId);
        refund.put("amount", amount);
        refund.put("reason", reason);
        refund.put("requestedAt", new Date());
        refund.put("processedAt", new Date());
        
        // Refund usually succeeds (90% success rate)
        boolean isSuccess = random.nextDouble() < 0.9;
        
        if (isSuccess) {
            refund.put("status", "REFUNDED");
            refund.put("message", "Refund processed successfully");
            refund.put("estimatedDays", 3 + random.nextInt(7)); // 3-10 days
            
            // Generate refund reference
            refund.put("refundReference", "RFR" + random.nextInt(1000000));
        } else {
            refund.put("status", "REFUND_FAILED");
            refund.put("message", "Refund processing failed. Please contact customer support.");
            refund.put("errorCode", "REFUND_ERR" + random.nextInt(100));
        }
        
        return refund;
    }
    
    /**
     * Get available payment methods for Myanmar
     */
    public List<Map<String, Object>> getAvailablePaymentMethods() {
        List<Map<String, Object>> methods = new ArrayList<>();
        
        Map<String, String> methodDetails = Map.of(
            "KBZ_PAY", "KBZ Pay - Mobile Payment",
            "WAVE_MONEY", "Wave Money - Mobile Money",
            "AYA_PAY", "AYA Pay - Bank Transfer",
            "CB_PAY", "CB Pay - Mobile Banking",
            "ONEPAY", "OnePay - Digital Wallet",
            "MPU", "MPU - Card Payment",
            "CASH", "Cash Payment at Station",
            "CREDIT_CARD", "Credit Card (Visa/MasterCard)",
            "DEBIT_CARD", "Debit Card",
            "BANK_TRANSFER", "Bank Transfer"
        );
        
        Map<String, String> methodLogos = Map.of(
            "KBZ_PAY", "https://cdn.kbzpay.com/logo.png",
            "WAVE_MONEY", "https://cdn.wavemoney.com/logo.png",
            "AYA_PAY", "https://cdn.ayabank.com/logo.png",
            "CB_PAY", "https://cdn.cbbank.com/logo.png"
        );
        
        for (String method : PAYMENT_METHODS) {
            Map<String, Object> methodInfo = new HashMap<>();
            methodInfo.put("code", method);
            methodInfo.put("name", methodDetails.getOrDefault(method, method));
            methodInfo.put("description", getMethodDescription(method));
            methodInfo.put("supported", true);
            methodInfo.put("processingTime", getProcessingTime(method));
            methodInfo.put("feePercentage", getFeePercentage(method));
            
            if (methodLogos.containsKey(method)) {
                methodInfo.put("logoUrl", methodLogos.get(method));
            }
            
            methods.add(methodInfo);
        }
        
        return methods;
    }
    
    /**
     * Get payment status history
     */
    public List<Map<String, Object>> getPaymentStatusHistory(String paymentId) {
        List<Map<String, Object>> history = new ArrayList<>();
        Random random = new Random();
        
        // Generate status timeline
        String[] statusSequence = {"CREATED", "PENDING", "PROCESSING", "SUCCESS"};
        long baseTime = System.currentTimeMillis() - 3600000; // 1 hour ago
        
        for (int i = 0; i < statusSequence.length; i++) {
            Map<String, Object> status = new HashMap<>();
            status.put("status", statusSequence[i]);
            status.put("timestamp", new Date(baseTime + (i * 600000))); // 10-minute intervals
            status.put("description", getStatusDescription(statusSequence[i]));
            
            if (i == statusSequence.length - 1) {
                status.put("isCurrent", true);
            }
            
            history.add(status);
        }
        
        return history;
    }
    
    // Helper methods
    
    private String generatePaymentUrl(String paymentId) {
        return "https://payment.myanmarrailways.gov.mm/pay/" + paymentId;
    }
    
    private String generateQRCodeData(double amount, String paymentId, String transactionRef) {
        return String.format("mmrpayment://pay?amount=%.2f&id=%s&ref=%s", amount, paymentId, transactionRef);
    }
    
    private String generateConfirmationNumber() {
        Random random = new Random();
        return "CONF" + random.nextInt(1000000) + System.currentTimeMillis();
    }
    
    private String generateReceiptUrl(String paymentId) {
        return "https://receipt.myanmarrailways.gov.mm/" + paymentId + "/receipt.pdf";
    }
    
    private String getFailureReason(Random random) {
        String[] reasons = {
            "Insufficient funds",
            "Bank declined transaction",
            "Network error",
            "Payment gateway timeout",
            "Invalid payment details",
            "Daily limit exceeded",
            "Card expired",
            "Security verification failed"
        };
        return reasons[random.nextInt(reasons.length)];
    }
    
    private String getMethodDescription(String method) {
        Map<String, String> descriptions = Map.of(
            "KBZ_PAY", "Pay using KBZ Pay mobile app with secure authentication",
            "WAVE_MONEY", "Use Wave Money wallet for quick payments",
            "AYA_PAY", "AYA Bank mobile banking transfer",
            "CB_PAY", "CB Bank mobile payment service",
            "ONEPAY", "OnePay digital wallet for Myanmar",
            "MPU", "Myanmar Payment Union card payment",
            "CASH", "Pay in cash at any Myanmar Railways station",
            "CREDIT_CARD", "International and local credit cards accepted",
            "DEBIT_CARD", "Local and international debit cards",
            "BANK_TRANSFER", "Direct bank transfer to Myanmar Railways account"
        );
        return descriptions.getOrDefault(method, "Secure payment method");
    }
    
    private String getProcessingTime(String method) {
        Map<String, String> times = Map.of(
            "KBZ_PAY", "Instant",
            "WAVE_MONEY", "Instant",
            "AYA_PAY", "1-2 minutes",
            "CB_PAY", "Instant",
            "ONEPAY", "Instant",
            "MPU", "30-60 seconds",
            "CASH", "Immediate",
            "CREDIT_CARD", "10-30 seconds",
            "DEBIT_CARD", "10-30 seconds",
            "BANK_TRANSFER", "1-2 business days"
        );
        return times.getOrDefault(method, "Varies");
    }
    
    private double getFeePercentage(String method) {
        Map<String, Double> fees = Map.of(
            "KBZ_PAY", 1.5,
            "WAVE_MONEY", 2.0,
            "AYA_PAY", 1.0,
            "CB_PAY", 1.5,
            "ONEPAY", 2.5,
            "MPU", 2.0,
            "CASH", 0.0,
            "CREDIT_CARD", 3.0,
            "DEBIT_CARD", 2.0,
            "BANK_TRANSFER", 0.5
        );
        return fees.getOrDefault(method, 1.5);
    }
    
    private String getStatusDescription(String status) {
        Map<String, String> descriptions = Map.of(
            "CREATED", "Payment request created",
            "PENDING", "Awaiting payment initiation",
            "PROCESSING", "Payment being processed by gateway",
            "SUCCESS", "Payment completed successfully",
            "FAILED", "Payment failed",
            "REFUNDED", "Payment refunded",
            "CANCELLED", "Payment cancelled by user"
        );
        return descriptions.getOrDefault(status, "Payment status");
    }
    
    /**
     * Calculate payment breakdown with fees
     */
    public Map<String, Object> calculatePaymentBreakdown(double baseAmount, String paymentMethod) {
        Map<String, Object> breakdown = new HashMap<>();
        
        double feePercentage = getFeePercentage(paymentMethod);
        double feeAmount = (baseAmount * feePercentage) / 100.0;
        double totalAmount = baseAmount + feeAmount;
        
        breakdown.put("baseAmount", baseAmount);
        breakdown.put("paymentMethod", paymentMethod);
        breakdown.put("feePercentage", feePercentage);
        breakdown.put("feeAmount", Math.round(feeAmount * 100.0) / 100.0);
        breakdown.put("totalAmount", Math.round(totalAmount * 100.0) / 100.0);
        breakdown.put("currency", "MMK");
        
        return breakdown;
    }
}