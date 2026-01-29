
package com.example.inventory.product;

import com.example.inventory.common.BadRequestException;
import com.example.inventory.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(Product product) {
        validateBusinessRules(product);
        return repository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> list(Category category, BigDecimal maxPrice) {
        return repository.findByOptionalFilters(category, maxPrice);
    }

    @Transactional(readOnly = true)
    public Product get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    public Product update(Long id, Product updated) {
        Product existing = get(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setQuantity(updated.getQuantity());
        existing.setCategory(updated.getCategory());
        validateBusinessRules(existing);
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Product not found: " + id);
        }
        repository.deleteById(id);
    }

    private void validateBusinessRules(Product product) {
        if (product.getQuantity() != null && product.getQuantity() < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }
    }
}
