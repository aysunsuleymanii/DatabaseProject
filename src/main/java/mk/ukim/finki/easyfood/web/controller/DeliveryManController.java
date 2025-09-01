package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/DeliveryMan")
public class DeliveryManController {
    private final OrderService orderService;

    public DeliveryManController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public String deliveryManShow(@PathVariable Long id, Model model) {

        List<Order> orders = orderService.listOrdersByDeliveryMan(id);
        List<Order> orders1 = orderService.listAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("o", orders1);

        return "deliveryman_dash";
    }
}
