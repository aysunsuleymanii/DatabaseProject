package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.repository.DeliveryManRepository;
import mk.ukim.finki.easyfood.repository.OrderRepository;
import mk.ukim.finki.easyfood.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DeliveryManRepository deliveryManRepository;

    public OrderServiceImpl(OrderRepository orderRepository, DeliveryManRepository deliveryManRepository) {
        this.orderRepository = orderRepository;
        this.deliveryManRepository = deliveryManRepository;
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
        return this.orderRepository.findAllById(id);
    }
}
