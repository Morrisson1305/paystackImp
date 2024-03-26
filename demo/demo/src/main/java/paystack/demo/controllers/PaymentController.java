package paystack.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import paystack.demo.dto.*;
import paystack.demo.service.PaymentService;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/paystack", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/createPlan")
    public ResponseEntity <CreatePlanResponse> createPlan (@Validated @RequestBody CreatePlanRequest planRequest) throws Exception {
        return ResponseEntity.ok(paymentService.createPlan(planRequest));
    }
    @PostMapping("/initializePayment")
    public ResponseEntity<InitializePaymentResponse> initializePayment(@Validated @RequestBody InitializePaymentRequest initializePaymentRequest){
        return ResponseEntity.ok(paymentService.initializePayment(initializePaymentRequest));
    }
    public ResponseEntity <PaymentVerificationResponse> paymentVerification (@PathVariable(value = "reference") String reference, @PathVariable(value = "plan") String plan, @PathVariable(value = "id") Long id) throws Exception {
        if(reference.isEmpty() || plan.isEmpty()){
            throw new Exception(" reference, plan and id must not be null");
        }
        return ResponseEntity.ok(paymentService.paymentVerification(reference, plan, id));
    }

}