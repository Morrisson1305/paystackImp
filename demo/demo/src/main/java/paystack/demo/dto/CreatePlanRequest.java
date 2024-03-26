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
public class CreatePlanRequest {
   // @NotNull(message = "Field name cannot be null")
    @JsonProperty("name")
    private String name;
    @JsonProperty("interval")
    private String interval;
    @JsonProperty("amount")
    private Integer amount;
}
