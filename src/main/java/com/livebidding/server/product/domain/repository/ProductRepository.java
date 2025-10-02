package com.livebidding.server.product.domain.repository;

import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.type.AuctionStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.seller WHERE p.id = :id")
    Optional<Product> findByIdWithSeller(@Param("id") Long id);

    Page<Product> findAllByStatus(AuctionStatus status, Pageable pageable);
}
