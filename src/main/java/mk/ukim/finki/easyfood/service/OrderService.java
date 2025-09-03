package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.model.OrderItems;

import java.util.List;
import java.util.Optional;

public interface OrderService {
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
