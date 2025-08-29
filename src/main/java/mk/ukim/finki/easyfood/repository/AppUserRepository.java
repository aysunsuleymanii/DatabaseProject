package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Integer> {
}
