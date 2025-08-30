package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.AppUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaSpecificationRepository<AppUser, Long> {
    Optional<AppUser> findByEmailAndPassword(String email, String password);
}
