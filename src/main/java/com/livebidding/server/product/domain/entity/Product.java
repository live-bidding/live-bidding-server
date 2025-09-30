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

    /**
     * Constructs a Product with the provided value objects and seller, initializing the current price
     * to the start price and setting the product status to PREPARED.
     *
     * @param name the product's name value object
     * @param description the product's description value object
     * @param startPrice the starting price value object
     * @param auctionPeriod the auction period value object defining start and end times
     * @param seller the user who lists the product for auction
     */
    private Product(ProductName name, ProductDescription description, Price startPrice, AuctionPeriod auctionPeriod, User seller) {
        this.name = name;
        this.description = description;
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
        this.auctionPeriod = auctionPeriod;
        this.status = ProductStatus.PREPARED;
        this.seller = seller;
    }

    /**
     * Create a new Product with the given name, description, starting price, auction period, and seller.
     *
     * The created Product's current price is initialized from the provided startPrice and its status is set to PREPARED.
     *
     * @param name the product name
     * @param description the product description
     * @param startPrice the starting price value for the product
     * @param startTime the auction start time
     * @param endTime the auction end time
     * @param seller the user who is selling the product
     * @return a newly constructed Product populated with the provided attributes
     */
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
