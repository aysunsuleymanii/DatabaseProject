package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.Category;
import mk.ukim.finki.easyfood.repository.CategoryRepository;
import mk.ukim.finki.easyfood.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> create(String name, String description) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> update(Long id, String name, String description) {
        return Optional.empty();
    }

    @Override
    public void delete(String name) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Category> searchCategories(String text) {
        return List.of();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.empty();
    }
}
