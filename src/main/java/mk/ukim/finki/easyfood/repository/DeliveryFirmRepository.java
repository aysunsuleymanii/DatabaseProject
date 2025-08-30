package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.DeliveryFirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryFirmRepository extends JpaRepository<DeliveryFirm, Long> {
}
