package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.DeliveryMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Integer> {
}
