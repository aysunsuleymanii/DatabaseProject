package mk.ukim.finki.easyfood.service;

import jakarta.transaction.Transactional;
import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.model.OrderItems;
import mk.ukim.finki.easyfood.model.enumerations.ORDER_STATUS;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    @Transactional
    public Order updateOrderStatus(Long orderId, ORDER_STATUS newStatus);

    public List<Order> listOrdersByDeliveryManAndOrderStatus(Long deliveryMan, String orderStatus);

    List<Order> listAllOrders();

    List<Order> findAllByUserId(Long id);

    Order save(Order order);

    void saveOrderItem(OrderItems orderItem);

    Optional<Order> findOrderById(Long orderId);

    List<OrderItems> findOrderItemsByOrderId(Long orderId);

    Order createOrderWithItems(Order order, List<CartItems> cartItems);

    Optional<Order> findById(Long orderId);

    List<OrderItems> getOrderItems(Long orderId);

    List<Order> getOrdersByCustomerId(Long customerId);

    void updateOrderStatus(Long orderId, String status);
}
