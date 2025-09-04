package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.CartItems;
import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.model.OrderItems;
import mk.ukim.finki.easyfood.model.enumerations.ORDER_STATUS;
import mk.ukim.finki.easyfood.repository.DeliveryManRepository;
import mk.ukim.finki.easyfood.repository.OrderItemsRepository;
import mk.ukim.finki.easyfood.repository.OrderRepository;
import mk.ukim.finki.easyfood.service.OrderService;
import mk.ukim.finki.easyfood.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DeliveryManRepository deliveryManRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ShoppingCartService shoppingCartService;

    public OrderServiceImpl(OrderRepository orderRepository, DeliveryManRepository deliveryManRepository, OrderItemsRepository orderItemsRepository, ShoppingCartService shoppingCartService) {
        this.orderRepository = orderRepository;
        this.deliveryManRepository = deliveryManRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.shoppingCartService = shoppingCartService;
    }
    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, ORDER_STATUS newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
        order.setOrderStatus(newStatus.toString()); // Assuming orderStatus is a String in your Order model
        return orderRepository.save(order);
    }

    @Override
    public List<Order> listAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> listOrdersByDeliveryManAndOrderStatus(Long deliveryMan, String orderStatus) {
        DeliveryMan deliveryMan1 = deliveryManRepository.findById(deliveryMan).orElse(null);
        return orderRepository.findAllByDeliveryManAndOrderStatus(deliveryMan1, orderStatus);
    }

    @Override
    public List<Order> findAllByUserId(Long id) {
        return this.orderRepository.findByCustomerIdOrderByOrderDateDesc(id);
    }

    public void saveOrderItem(OrderItems orderItem) {
        orderItemsRepository.save(orderItem);
    }

    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<OrderItems> findOrderItemsByOrderId(Long orderId) {
        return orderItemsRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public Order createOrderWithItems(Order order, List<CartItems> cartItems) {
        Order savedOrder = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItems cartItem : cartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(savedOrder);
            orderItem.setItem(cartItem.getItem());
            orderItem.setQuantity(cartItem.getQuantity());

            BigDecimal itemTotal = cartItem.getItem().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            orderItem.setTotalPrice(itemTotal);
            totalAmount = totalAmount.add(itemTotal);

            orderItemsRepository.save(orderItem);
        }

        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        // ✅ clear cart inside same transaction
        shoppingCartService.clearCart(order.getCustomer().getId());

        return savedOrder;
    }


    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<OrderItems> getOrderItems(Long orderId) {
        return orderItemsRepository.findByOrderId(orderId);
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus(status);
            orderRepository.save(order);
        }
    }

}
