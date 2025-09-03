package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findByOrder(Order order);

    @Query("SELECT oi FROM OrderItems oi WHERE oi.order.id = :orderId")
    List<OrderItems> findByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Query("UPDATE OrderItems SET item = null WHERE item.id = :itemId")
    void setItemIdToNull(@Param("itemId") Long itemId);


}
