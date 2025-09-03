package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.DeliveryMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {
    Optional<DeliveryMan> findByEmail(String email);
}
