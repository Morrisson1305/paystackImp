package paystack.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

    @Data
    @Entity
    @Table(name = "customers")
    public class Customers  {
        @Id
        @GeneratedValue(strategy =  GenerationType.SEQUENCE, generator = "customer_id_seq")
        @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq", initialValue = 4, allocationSize = 1)
        private Long customerId;
        private String firstName;
        private String lastName;
        private String email;
        private String mobileNumber;
        private String password;
        private Timestamp createdOn;
        private Timestamp lastLogin;

    }



