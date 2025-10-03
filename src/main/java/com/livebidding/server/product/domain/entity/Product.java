package com.livebidding.server.product.domain.entity;

import com.livebidding.server.product.domain.type.AuctionStatus;
import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import com.livebidding.server.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
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

    @Column(nullable = false)
    private LocalDateTime auctionStartTime;

    @Column(nullable = false)
    private LocalDateTime auctionEndTime;

    @Transient
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
        validate(name, startPrice, auctionStartTime, auctionEndTime);

        this.name = name;
        this.description = description;
        this.startPrice = startPrice;
        this.currentPrice = startPrice;
        this.auctionStartTime = auctionStartTime;
        this.auctionEndTime = auctionEndTime;
        this.seller = seller;
    }

    public static Product of(String name, String description, BigDecimal startPrice,
                             LocalDateTime auctionStartTime, LocalDateTime auctionEndTime,
                             User seller) {
        return new Product(name, description, startPrice, auctionStartTime, auctionEndTime, seller);
    }

    public AuctionStatus getStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(auctionStartTime)) {
            return AuctionStatus.SCHEDULED;
        }
        if (now.isBefore(auctionEndTime)) {
            return AuctionStatus.IN_PROGRESS;
        }
        return AuctionStatus.ENDED;
    }

    private void validate(String name, BigDecimal startPrice,
                          LocalDateTime auctionStartTime, LocalDateTime auctionEndTime) {
        validateBasic(name, startPrice, auctionStartTime, auctionEndTime);

        if (auctionStartTime.isBefore(LocalDateTime.now())) {
            throw new ProductException(ProductErrorCode.INVALID_AUCTION_TIME);
        }
    }

    private void validateBasic(String name, BigDecimal startPrice,
                               LocalDateTime auctionStartTime, LocalDateTime auctionEndTime) {
        if (!StringUtils.hasText(name)) {
            throw new ProductException(ProductErrorCode.BLANK_PRODUCT_NAME);
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
}
