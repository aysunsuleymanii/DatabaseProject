package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Integer> {
}
