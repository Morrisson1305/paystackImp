package paystack.demo.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import paystack.demo.dto.*;
import paystack.demo.entity.Customers;
import paystack.demo.entity.Payments;
import paystack.demo.entity.PricingPlanType;
import paystack.demo.repository.CustomerRepo;
import paystack.demo.repository.PaymentRepository;
import paystack.demo.service.PaymentService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import static paystack.demo.config.AppConstant.*;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepo customerRepo;
    @Value("${paystack.secret.key}")
    private String paystackSecretKey;

    @Override
    public CreatePlanResponse createPlan(CreatePlanRequest createPlanDto) throws Exception {
        CreatePlanResponse createPlanResponse = null;
        try {
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(createPlanDto));
            StringBuilder result;
            HttpResponse response;
            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                HttpPost post = new HttpPost(PAYSTACK_INIT);
                post.setEntity(postingString);
                post.addHeader("Content-Type", "application/json");
                post.addHeader("Authorization", "Bearer " + paystackSecretKey);
                result = new StringBuilder();
                response = client.execute(post);
            }
            if(response.getStatusLine().getStatusCode() == STATUS_CODE_CREATED){
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            createPlanResponse = mapper.readValue(result.toString(), CreatePlanResponse.class);
        } catch (Throwable ex){
            ex.printStackTrace();
        }
        return createPlanResponse;
    }

    @Override
    public InitializePaymentResponse initializePayment(InitializePaymentRequest initializePaymentDto) {
        try {
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(initializePaymentDto));
            StringBuilder result;
            HttpResponse response;
            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                HttpPost post = new HttpPost(PAYSTACK_INITIALIZE_PAY);
                post.setEntity(postingString);
                post.addHeader("Content-type", "application/json");
                post.addHeader("Authorization", "Bearer " + paystackSecretKey);
                result = new StringBuilder();
                response = client.execute(post);
            }
            if (response.getStatusLine().getStatusCode() == STATUS_CODE_OK){
                BufferedReader read = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = read.readLine()) != null){
                    result.append(line);
                }
            }else {
                throw new Exception("Paystack is unable to initialize payment at the moment.");
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result.toString(), InitializePaymentResponse.class);
        }catch (Throwable ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public PaymentVerificationResponse  paymentVerification(String reference, String plan, Long id) throws Exception {
        PaymentVerificationResponse verificationResponse;
        Payments payment;
        try {
            StringBuilder result;
            HttpResponse response;
            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                HttpGet request = new HttpGet(PAYSTACK_VERIFY + reference);
                request.addHeader("Content-type", "application/json");
                request.addHeader("Authorization", "Bearer " + paystackSecretKey);
                result = new StringBuilder();
                response = client.execute(request);
            }
            if(response.getStatusLine().getStatusCode() == STATUS_CODE_OK){
                BufferedReader read = new BufferedReader( new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = read.readLine()) != null){
                    result.append(line);
                }
            }else {
                throw new Exception("Paystack is unable to verify payment at the moment");
            }
            ObjectMapper mapper = new ObjectMapper();
            verificationResponse = mapper.readValue(result.toString(), PaymentVerificationResponse.class);
            if(verificationResponse == null || verificationResponse.getStatus().equals("false")){
                throw new Exception("An error occurred");
            }else {
                Customers customers = customerRepo.getReferenceById(id);
                payment = Payments.builder()
                        .customers(customers)
                        .reference(verificationResponse.getReference())
                        .amount(verificationResponse.getAmount())
                        .gateWayResponse(verificationResponse.getGatewayResponse())
                        .paidAt(verificationResponse.getPaidAt())
                        .createdAt(verificationResponse.getCreatedAt())
                        .channel(verificationResponse.getChannel())
                        .currency(verificationResponse.getCurrency())
                        .ipAddress(verificationResponse.getIpAddress())
                        .planType(PricingPlanType.valueOf(verificationResponse.getPricingPlanType().toUpperCase()))
                        .createdOn(new Date())
                        .build();
            }
            paymentRepository.save(payment);
        }catch (Exception e){
            throw new Exception("");
        }
        return verificationResponse;
    }
}

