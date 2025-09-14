package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.DeliveryMan;
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
@RequestMapping("/deliveryMan")
public class DeliveryManController {
    private final OrderService orderService;
    private final UserService userService;

    public DeliveryManController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String getDashboard(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        DeliveryMan deliveryMan = userService.findByEmailDM(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Delivery man not found"));

        Long deliveryManId = deliveryMan.getId();

        model.addAttribute("deliveryMan", deliveryMan);
        model.addAttribute("pendingOrders", orderService.listOrdersByDeliveryManAndOrderStatus(deliveryManId, "PENDING"));
        model.addAttribute("processingOrders", orderService.listOrdersByDeliveryManAndOrderStatus(deliveryManId, "OUT_FOR_DELIVERY"));

        return "deliveryman";
    }

    @GetMapping("/{id}")
    public String deliveryManShow(@PathVariable Long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        DeliveryMan loggedInUser = userService.findByEmailDM(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Delivery man not found"));

        if (!id.equals(loggedInUser.getId())) {
            return "redirect:/deliveryMan/" + loggedInUser.getId();
        }

        return "redirect:/deliveryMan";
    }

    @PostMapping("/accept/{orderId}")
    public String acceptOrder(@PathVariable Long orderId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        DeliveryMan deliveryMan = userService.findByEmailDM(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Delivery man not found"));

        orderService.updateOrderStatus(orderId, ORDER_STATUS.OUT_FOR_DELIVERY);
        return "redirect:/deliveryMan";
    }

    @PostMapping("/deliver/{orderId}")
    public String deliverOrder(@PathVariable Long orderId, Authentication authentication) {
        // Verify the delivery man is authorized for this order
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        DeliveryMan deliveryMan = userService.findByEmailDM(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Delivery man not found"));

        orderService.updateOrderStatus(orderId, ORDER_STATUS.DELIVERED);
        return "redirect:/deliveryMan";
    }
}