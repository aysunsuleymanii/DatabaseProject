package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.Menu;
import mk.ukim.finki.easyfood.repository.MenuRepository;
import mk.ukim.finki.easyfood.service.MenuService;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu getMenuByRestaurantId(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }

}
