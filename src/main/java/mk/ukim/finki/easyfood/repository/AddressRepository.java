package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllById(Long id);

    @Query("SELECT a FROM Address a JOIN a.users u WHERE u.id = :userId")
    List<Address> findAllByUserId(@Param("userId") Long userId);

}
