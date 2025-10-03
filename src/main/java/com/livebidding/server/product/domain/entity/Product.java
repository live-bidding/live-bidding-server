package com.livebidding.server.product.domain.entity;

import com.livebidding.server.product.domain.type.AuctionStatus;
import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import com.livebidding.server.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal startPrice;

    @Column(nullable = false)
    private BigDecimal currentPrice;

    private LocalDateTime auctionStartTime;

    private LocalDateTime auctionEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuctionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Product(String name, String description, BigDecimal startPrice,
                    LocalDateTime auctionStartTime, LocalDateTime auctionEndTime,
                    User seller) {
        validate(name, description, startPrice, auctionStartTime, auctionEndTime);

        this.name = name;
        this.description = description;
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
        this.auctionStartTime = auctionStartTime;
        this.auctionEndTime = auctionEndTime;
        this.seller = seller;
        this.status = determineStatus(LocalDateTime.now(), auctionStartTime, auctionEndTime);
    }

    public static Product of(String name, String description, BigDecimal startPrice,
                             LocalDateTime auctionStartTime, LocalDateTime auctionEndTime,
                             User seller) {
        return new Product(name, description, startPrice, auctionStartTime, auctionEndTime, seller);
    }

    private void validate(String name, String description, BigDecimal startPrice,
                          LocalDateTime auctionStartTime, LocalDateTime auctionEndTime) {
        validateBasic(name, description, startPrice, auctionStartTime, auctionEndTime);

        if (auctionStartTime.isBefore(LocalDateTime.now())) {
            throw new ProductException(ProductErrorCode.INVALID_AUCTION_TIME);
        }
    }

    private void validateBasic(String name, String description, BigDecimal startPrice,
                               LocalDateTime auctionStartTime, LocalDateTime auctionEndTime) {
        if (!StringUtils.hasText(name)) {
            throw new ProductException(ProductErrorCode.BLANK_PRODUCT_NAME);
        }
        if (!StringUtils.hasText(description)) {
            throw new ProductException(ProductErrorCode.EMPTY_PRODUCT_DESCRIPTION);
        }
        if (startPrice == null || startPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductException(ProductErrorCode.INVALID_PRICE);
        }
        if (auctionStartTime == null || auctionEndTime == null) {
            throw new ProductException(ProductErrorCode.INVALID_AUCTION_TIME);
        }
        if (!auctionEndTime.isAfter(auctionStartTime)) {
            throw new ProductException(ProductErrorCode.INVALID_AUCTION_PERIOD);
        }
    }

    private AuctionStatus determineStatus(LocalDateTime now, LocalDateTime startTime, LocalDateTime endTime) {
        if (now.isBefore(startTime)) {
            return AuctionStatus.SCHEDULED;
        }
        if (now.isBefore(endTime)) {
            return AuctionStatus.IN_PROGRESS;
        }
        return AuctionStatus.ENDED;
    }
}
