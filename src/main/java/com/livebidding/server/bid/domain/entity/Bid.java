package com.livebidding.server.bid.domain.entity;

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

    /**
     * Creates a Bid with the specified price, bidder, and product.
     *
     * @param price  the bid amount as a Price value object
     * @param bidder the user placing the bid
     * @param product the product being bid on
     */
    private Bid(Price price, User bidder, Product product) {
        this.price = price;
        this.bidder = bidder;
        this.product = product;
    }

    /**
     * Create a Bid for a product by a bidder using the provided price.
     *
     * @param price  numeric price value used to construct the Bid's Price value object
     * @param bidder the user placing the bid; must not be null
     * @param product the product being bid on; must not be null
     * @return the newly created Bid
     * @throws IllegalArgumentException if {@code bidder} is null ("[ERROR] 입찰자는 null일 수 없습니다.") or if {@code product} is null ("[ERROR] 입찰 상품은 null일 수 없습니다.")
     */
    public static Bid of(Long price, User bidder, Product product) {
        if (bidder == null) {
            throw new IllegalArgumentException("[ERROR] 입찰자는 null일 수 없습니다.");
        }
        if (product == null) {
            throw new IllegalArgumentException("[ERROR] 입찰 상품은 null일 수 없습니다.");
        }
        return new Bid(
                Price.from(price),
                bidder,
                product
        );
    }
}
