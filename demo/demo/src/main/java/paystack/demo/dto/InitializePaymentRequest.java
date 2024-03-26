package paystack.demo.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitializePaymentRequest {

    @JsonProperty("amount")
    private String amount;
    @JsonProperty("email")
    private String email;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("plan")
    private String plan;
    @JsonProperty("channels")
    private String[] channels;
}
