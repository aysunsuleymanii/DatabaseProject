package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.Address;
import mk.ukim.finki.easyfood.model.Order;
import mk.ukim.finki.easyfood.model.OrderItems;
import mk.ukim.finki.easyfood.service.UserService;
import mk.ukim.finki.easyfood.service.AddressService;
import mk.ukim.finki.easyfood.service.OrderService;
import mk.ukim.finki.easyfood.repository.OrderItemsRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final OrderItemsRepository orderItemsRepository;

    public ProfileController(UserService userService, AddressService addressService,
                             OrderService orderService, OrderItemsRepository orderItemsRepository) {
        this.userService = userService;
        this.addressService = addressService;
        this.orderService = orderService;
        this.orderItemsRepository = orderItemsRepository;
    }

    @GetMapping
    public String getProfileDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            String email = authentication.getName();

            Optional<Customer> customerOptional = userService.findByEmail(email);

            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();

                // Get orders for the user
                List<Order> orders = orderService.findAllByUserId(customer.getId());

                // Load order items for each order (including deleted items)
                for (Order order : orders) {
                    List<OrderItems> orderItems = orderItemsRepository.findByOrderId(order.getId());
                    order.setOrderItems(orderItems);
                }

                List<Address> addresses = customer.getAddresses();
                model.addAttribute("user", customer);
                model.addAttribute("addresses", addresses);
                model.addAttribute("orders", orders);
                return "profile";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/address/add")
    public String addAddress(@RequestParam String street,
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

                return "redirect:/profile#addresses";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/address/delete")
    public String deleteAddress(@RequestParam Long addressId,
                                RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            String email = authentication.getName();
            Optional<Customer> customerOptional = userService.findByEmail(email);

            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();

                try {
                    addressService.removeAddressFromUser(customer, addressId);
                    redirectAttributes.addFlashAttribute("successMessage",
                            "Address deleted successfully!");
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Failed to delete address. Please try again.");
                }

                return "redirect:/profile#addresses";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/update")
    public String updateUserDetails(@RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String email,
                                    @RequestParam String phone,
                                    RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            String currentEmail = authentication.getName();
            Optional<Customer> customerOptional = userService.findByEmail(currentEmail);

            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                try {
                    customer.setFirstName(firstName);
                    customer.setLastName(lastName);
                    customer.setEmail(email);
                    customer.setPhone(phone);

                    userService.save(customer);

                    redirectAttributes.addFlashAttribute("successMessage",
                            "Profile updated successfully!");
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Failed to update profile. Please try again.");
                }
                return "redirect:/profile#details";
            }
        }
        return "redirect:/login";
    }
}