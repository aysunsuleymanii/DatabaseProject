package mk.ukim.finki.easyfood.repository;

import mk.ukim.finki.easyfood.model.Costs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostsRepository extends JpaRepository<Costs, Integer> {
}
