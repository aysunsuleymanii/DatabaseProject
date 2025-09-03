package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.Address;
import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.repository.AddressRepository;
import mk.ukim.finki.easyfood.repository.CustomerRepository;
import mk.ukim.finki.easyfood.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public AddressServiceImpl(AddressRepository addressRepository, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Address> findAllByUserId(Long id) {
        return this.addressRepository.findAllById(id);
    }


    @Transactional
    @Override
    public Address addAddressToUser(Customer customer, Address address) {
        Address savedAddress = addressRepository.save(address);

        customer.getAddresses().add(savedAddress);

        customerRepository.save(customer);

        return savedAddress;
    }

    @Override
    @Transactional
    public void removeAddressFromUser(Customer customer, Long addressId) {
        Address addressToRemove = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));

        customer.getAddresses().removeIf(address -> address.getId().equals(addressId));

        customerRepository.save(customer);

        if (addressToRemove.getUsers() == null || addressToRemove.getUsers().isEmpty() ||
                (addressToRemove.getUsers().size() == 1 && addressToRemove.getUsers().contains(customer))) {
            addressRepository.delete(addressToRemove);
        }
    }

    @Override
    public Address findById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));
    }

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }


}
