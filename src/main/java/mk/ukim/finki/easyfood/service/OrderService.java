package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.Order;

import java.util.List;

public interface OrderService {
    public List<Order> listOrdersByDeliveryManAndOrderStatus(Long deliveryMan, String orderStatus);
    List<Order> listAllOrders();
}
