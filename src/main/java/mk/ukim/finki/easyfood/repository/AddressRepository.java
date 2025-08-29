package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
