package paystack.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paystack.demo.entity.Payments;

public interface PaymentRepository extends JpaRepository<Payments, Long> {
}
