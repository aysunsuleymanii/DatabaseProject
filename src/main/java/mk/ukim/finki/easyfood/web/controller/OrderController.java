package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.*;
import mk.ukim.finki.easyfood.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final UserService userService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final ShoppingCartService cartService;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;

    public OrderController(UserService userService, AddressService addressService,
                           OrderService orderService, ShoppingCartService cartService,
                           RestaurantService restaurantService, MenuItemService menuItemService) {
        this.userService = userService;
        this.addressService = addressService;
        this.orderService = orderService;
        this.cartService = cartService;
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            String email = authentication.getName();
            Optional<Customer> customerOptional = userService.findByEmail(email);

            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();

                List<CartItems> cartItems = cartService.getCartItems(customer.getId());

                if (cartItems.isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty!");
                    return "redirect:/cart";
                }

                // Calculate total amount
                BigDecimal totalAmount = cartItems.stream()
                        .map(item -> item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Get customer addresses
                List<Address> addresses = customer.getAddresses();

                // Get restaurant (assuming all cart items are from same restaurant)
                Restaurant restaurant = null;
                if (!cartItems.isEmpty()) {
                    Optional<Restaurant> restaurantOpt = menuItemService.getRestaurantByItem(cartItems.get(0).getItem());
                    restaurant = restaurantOpt.orElse(null);
                }

                Map<Long, BigDecimal> itemTotals = cartItems.stream()
                        .collect(Collectors.toMap(
                                item -> item.getItem().getId(),
                                item -> item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                        ));
                model.addAttribute("itemTotals", itemTotals);
                model.addAttribute("customer", customer);
                model.addAttribute("addresses", addresses);
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("totalAmount", totalAmount);
                model.addAttribute("restaurant", restaurant);

                return "checkout";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam(required = false) Long addressId,
                             @RequestParam(required = false) String newStreet,
                             @RequestParam(required = false) String newCity,
                             @RequestParam(required = false) String newPostalCode,
                             @RequestParam(required = false) String paymentMethod,
                             @RequestParam(required = false) String comment,
                             RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            String email = authentication.getName();
            Optional<Customer> customerOptional = userService.findByEmail(email);

            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();

                try {
                    // Get cart items
                    List<CartItems> cartItems = cartService.getCartItems(customer.getId());

                    if (cartItems.isEmpty()) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty!");
                        return "redirect:/cart";
                    }

                    Address orderAddress;
                    if (addressId != null) {
                        Optional<Address> existingAddress = Optional.ofNullable(addressService.findById(addressId));
                        if (existingAddress.isPresent() && customer.getAddresses().contains(existingAddress.get())) {
                            orderAddress = existingAddress.get();
                        } else {
                            redirectAttributes.addFlashAttribute("errorMessage", "Invalid address selected!");
                            return "redirect:/order/checkout";
                        }
                    } else {
                        // Create new address
                        if (newStreet == null || newCity == null || newPostalCode == null ||
                                newStreet.trim().isEmpty() || newCity.trim().isEmpty() || newPostalCode.trim().isEmpty()) {
                            redirectAttributes.addFlashAttribute("errorMessage", "Please provide complete address information!");
                            return "redirect:/order/checkout";
                        }

                        Address newAddress = new Address();
                        newAddress.setStreet(newStreet);
                        newAddress.setCity(newCity);
                        newAddress.setPostalCode(newPostalCode);

                        // Save the address and add it to customer - assuming this returns the saved address
                        orderAddress = addressService.addAddressToUser(customer, newAddress);
                    }

                    BigDecimal totalAmount = cartItems.stream()
                            .map(item -> item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // Get restaurant from the first item
                    Restaurant restaurant = null;
                    if (!cartItems.isEmpty()) {
                        Optional<Restaurant> restaurantOpt = menuItemService.getRestaurantByItem(cartItems.get(0).getItem());
                        restaurant = restaurantOpt.orElse(null);
                    }

                    Order order = new Order();
                    order.setCustomer(customer);
                    order.setAddress(orderAddress);
                    order.setRestaurant(restaurant);
                    order.setOrderDate(LocalDateTime.now());
                    order.setComment(comment);
                    order.setOrderStatus("PENDING");
                    order.setTotalAmount(totalAmount);

                    Order savedOrder = orderService.createOrderWithItems(order, cartItems);

                    boolean paymentSuccessful = processPayment(paymentMethod, totalAmount);

                    if (paymentSuccessful) {
                        redirectAttributes.addFlashAttribute("successMessage",
                                "Order placed successfully! Order ID: " + savedOrder.getId());
                        return "redirect:/order/confirmation/" + savedOrder.getId();
                    } else {
                        savedOrder.setOrderStatus("PAYMENT_FAILED");
                        orderService.save(savedOrder);

                        redirectAttributes.addFlashAttribute("errorMessage",
                                "Payment failed! Please try again.");
                        return "redirect:/order/checkout";
                    }

                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Failed to place order. Please try again.");
                    return "redirect:/order/checkout";
                }
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/confirmation/{orderId}")
    public String showOrderConfirmation(@PathVariable Long orderId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            String email = authentication.getName();
            Optional<Customer> customerOptional = userService.findByEmail(email);

            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();

                Optional<Order> orderOptional = orderService.findById(orderId);

                if (orderOptional.isPresent() &&
                        orderOptional.get().getCustomer().getId().equals(customer.getId())) {

                    Order order = orderOptional.get();
                    List<OrderItems> orderItems = orderService.getOrderItems(orderId);

                    model.addAttribute("order", order);
                    model.addAttribute("orderItems", orderItems);

                    return "order_confirmation";
                }
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/address/add-during-checkout")
    public String addAddressDuringCheckout(@RequestParam String street,
                                           @RequestParam String city,
                                           @RequestParam String postalCode,
                                           RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            String email = authentication.getName();
            Optional<Customer> customerOptional = userService.findByEmail(email);

            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();

                try {
                    Address address = new Address();
                    address.setStreet(street);
                    address.setCity(city);
                    address.setPostalCode(postalCode);

                    addressService.addAddressToUser(customer, address);

                    redirectAttributes.addFlashAttribute("successMessage",
                            "Address added successfully!");
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Failed to add address. Please try again.");
                }
            }
        }

        return "redirect:/order/checkout";
    }

    // Simulate payment processing
    private boolean processPayment(String paymentMethod, BigDecimal amount) {
        // In a real application, this would integrate with a payment gateway
        // For now, we'll simulate successful payment
        try {
            Thread.sleep(1000); // Simulate processing time

            // Simulate payment success/failure (90% success rate)
            return Math.random() > 0.1;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}