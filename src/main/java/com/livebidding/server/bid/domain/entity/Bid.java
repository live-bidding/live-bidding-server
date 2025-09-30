package com.livebidding.server.bid.domain.entity;

import com.livebidding.server.bid.exception.BidErrorCode;
import com.livebidding.server.bid.exception.BidException;
import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.vo.Price;
import com.livebidding.server.user.domain.entity.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Price price;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime bidTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id", nullable = false)
    private User bidder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Bid(Price price, User bidder, Product product) {
        this.price = price;
        this.bidder = bidder;
        this.product = product;
    }

    public static Bid of(Long price, User bidder, Product product) {
        if (bidder == null) {
            throw new BidException(BidErrorCode.BIDDER_CANNOT_BE_NULL);
        }
        if (product == null) {
            throw new BidException(BidErrorCode.PRODUCT_CANNOT_BE_NULL);
        }
        return new Bid(
                Price.from(price),
                bidder,
                product
        );
    }
}
