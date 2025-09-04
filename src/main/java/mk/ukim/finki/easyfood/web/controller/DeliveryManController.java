package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.model.enumerations.ORDER_STATUS;
import mk.ukim.finki.easyfood.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/DeliveryMan")
public class DeliveryManController {
    private final OrderService orderService;

    public DeliveryManController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public String deliveryManShow(@PathVariable Long id, Model model) {
        model.addAttribute("pendingOrders", orderService.listOrdersByDeliveryManAndOrderStatus(id, "PENDING"));
        model.addAttribute("processingOrders", orderService.listOrdersByDeliveryManAndOrderStatus(id, "OUT_FOR_DELIVERY"));
        return "deliveryman_dash";
    }

    @PostMapping("/accept/{orderId}")
    public String acceptOrder(@PathVariable Long orderId) {
        orderService.updateOrderStatus(orderId, ORDER_STATUS.OUT_FOR_DELIVERY);

        // Retrieve the updated order using orElseThrow() to get the Order object
        Order updatedOrder = orderService.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        Long deliveryManId = updatedOrder.getDeliveryMan().getId();

        return "redirect:/DeliveryMan/" + deliveryManId;
    }

    @PostMapping("/deliver/{orderId}")
    public String deliverOrder(@PathVariable Long orderId) {
        orderService.updateOrderStatus(orderId, ORDER_STATUS.DELIVERED);

        // Retrieve the updated order using orElseThrow() to get the Order object
        Order updatedOrder = orderService.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        Long deliveryManId = updatedOrder.getDeliveryMan().getId();

        return "redirect:/DeliveryMan/" + deliveryManId;
    }
}