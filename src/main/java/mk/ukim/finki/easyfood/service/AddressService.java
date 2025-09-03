package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Address;
import mk.ukim.finki.easyfood.model.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    List<Address> findAllByUserId(Long id);


    void removeAddressFromUser(Customer customer, Long addressId);

    @Transactional
    Address addAddressToUser(Customer customer, Address address);


    Address findById(Long addressId);

    Address save(Address address);

}
