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

    @Query("SELECT p FROM Product p WHERE :now < p.auctionStartTime")
    Page<Product> findScheduled(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE :now BETWEEN p.auctionStartTime AND p.auctionEndTime")
    Page<Product> findInProgress(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE :now > p.auctionEndTime")
    Page<Product> findEnded(@Param("now") LocalDateTime now, Pageable pageable);
}
