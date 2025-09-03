package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByDeliveryManAndOrderStatus(DeliveryMan deliveryMan, String orderStatus);

    List<Order> findAllById(Long id);

    List<Order> findByCustomerIdOrderByOrderDateDesc(Long customerId);
}


