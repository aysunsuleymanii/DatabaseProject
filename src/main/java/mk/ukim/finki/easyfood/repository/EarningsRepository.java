package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Earnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EarningsRepository extends JpaRepository<Earnings, Integer> {
}
