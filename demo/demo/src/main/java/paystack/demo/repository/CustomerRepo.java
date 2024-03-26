package paystack.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paystack.demo.entity.Customers;

public interface CustomerRepo extends JpaRepository<Customers, Long> {
}
