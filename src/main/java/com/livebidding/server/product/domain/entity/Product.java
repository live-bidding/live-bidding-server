package com.livebidding.server.product.domain.entity;

import com.livebidding.server.product.domain.type.ProductStatus;
import com.livebidding.server.product.domain.vo.AuctionPeriod;
import com.livebidding.server.product.domain.vo.Price;
import com.livebidding.server.product.domain.vo.ProductDescription;
import com.livebidding.server.product.domain.vo.ProductName;
import com.livebidding.server.user.domain.entity.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName name;

    @Embedded
    private ProductDescription description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "start_price", nullable = false))
    private Price startPrice;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "current_price", nullable = false))
    private Price currentPrice;

    @Embedded
    private AuctionPeriod auctionPeriod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    private Product(ProductName name, ProductDescription description, Price startPrice, AuctionPeriod auctionPeriod, User seller) {
        this.name = name;
        this.description = description;
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
        this.auctionPeriod = auctionPeriod;
        this.status = ProductStatus.PREPARED;
        this.seller = seller;
    }

    public static Product of(String name, String description, Long startPrice, LocalDateTime startTime, LocalDateTime endTime, User seller) {
        return new Product(
                ProductName.from(name),
                ProductDescription.from(description),
                Price.from(startPrice),
                AuctionPeriod.of(startTime, endTime),
                seller
        );
    }
}
