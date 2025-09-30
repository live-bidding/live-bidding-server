package com.livebidding.server.product.domain.repository;

import com.livebidding.server.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
