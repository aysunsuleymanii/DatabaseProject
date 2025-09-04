package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.model.enumerations.ORDER_STATUS;
import mk.ukim.finki.easyfood.service.OrderService;
import mk.ukim.finki.easyfood.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService; // Inject the user service

    public DeliveryManController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String deliveryManShow(@PathVariable Long id, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AppUser loggedInUser = userService.findByEmailDM(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // CRUCIAL SECURITY CHECK: Make sure the URL ID matches the logged-in user's ID
        if (!id.equals(loggedInUser.getId())) {
            // Redirect to their own page if they try to access another user's page
            return "redirect:/DeliveryMan/" + loggedInUser.getId();
        }

        model.addAttribute("pendingOrders", orderService.listOrdersByDeliveryManAndOrderStatus(id, "PENDING"));
        model.addAttribute("processingOrders", orderService.listOrdersByDeliveryManAndOrderStatus(id, "OUT_FOR_DELIVERY"));
        return "deliveryman_dash";
    }

    @PostMapping("/accept/{orderId}")
    public String acceptOrder(@PathVariable Long orderId) {
        orderService.updateOrderStatus(orderId, ORDER_STATUS.OUT_FOR_DELIVERY);

        Order updatedOrder = orderService.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        Long deliveryManId = updatedOrder.getDeliveryMan().getId();

        return "redirect:/DeliveryMan/" + deliveryManId;
    }

    @PostMapping("/deliver/{orderId}")
    public String deliverOrder(@PathVariable Long orderId) {
        orderService.updateOrderStatus(orderId, ORDER_STATUS.DELIVERED);

        Order updatedOrder = orderService.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        Long deliveryManId = updatedOrder.getDeliveryMan().getId();

        return "redirect:/DeliveryMan/" + deliveryManId;
    }
}