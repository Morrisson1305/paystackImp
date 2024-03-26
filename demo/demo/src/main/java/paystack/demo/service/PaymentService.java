package paystack.demo.service;


import paystack.demo.dto.*;

public interface PaymentService {

    CreatePlanResponse createPlan(CreatePlanRequest createPlanDto) throws Exception;
    InitializePaymentResponse initializePayment(InitializePaymentRequest initializePaymentDto);
    PaymentVerificationResponse paymentVerification(String reference, String plan, Long id) throws Exception;
}
