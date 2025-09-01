package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.Order;

import java.util.List;

public interface OrderService {
    public List<Order> listOrdersByDeliveryMan(Long deliveryMan);
    List<Order> listAllOrders();
}
