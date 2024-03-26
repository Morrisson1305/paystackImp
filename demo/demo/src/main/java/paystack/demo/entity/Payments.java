package paystack.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customers customers;
    private String reference;
    private BigDecimal amount;
    private String gateWayResponse;
    private String paidAt;
    private String createdAt;
    private  String channel;
    private String currency;
    private String ipAddress;
    @Enumerated(EnumType.STRING)
    private PricingPlanType planType;
    private Date createdOn;

}
