package com.livebidding.server.support;

import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.type.AuctionStatus;
import com.livebidding.server.user.domain.entity.User;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestFixture {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User createUser() {
        return createUser("test" + System.nanoTime() + "@test.com");
    }

    public static User createUser(String email) {
        String encodedPassword = passwordEncoder.encode("Password123!");
        return User.of(email, encodedPassword, "테스트사용자");
    }

    public static Product createProduct(User seller) {
        return Product.of(
                "테스트 상품",
                "상품 설명",
                new BigDecimal("100000"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                seller
        );
    }

    public static Product createInProgressProduct(User seller) {
        return createProductWithReflection(
                "진행중 상품",
                "경매 진행중인 상품",
                new BigDecimal("100000"),
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusDays(1),
                seller
        );
    }

    public static Product createScheduledProduct(User seller) {
        return Product.of(
                "예정 상품",
                "경매 예정 상품",
                new BigDecimal("100000"),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                seller
        );
    }

    public static Product createEndedProduct(User seller) {
        return createProductWithReflection(
                "종료된 상품",
                "경매 종료된 상품",
                new BigDecimal("100000"),
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                seller
        );
    }

    public static Product createProduct(
            String name,
            String description,
            BigDecimal startPrice,
            LocalDateTime auctionStartTime,
            LocalDateTime auctionEndTime,
            User seller
    ) {
        if (auctionStartTime.isBefore(LocalDateTime.now())) {
            return createProductWithReflection(name, description, startPrice, auctionStartTime, auctionEndTime, seller);
        }
        return Product.of(name, description, startPrice, auctionStartTime, auctionEndTime, seller);
    }

    private static Product createProductWithReflection(
            String name, String description, BigDecimal startPrice,
            LocalDateTime auctionStartTime, LocalDateTime auctionEndTime, User seller) {
        try {
            Constructor<Product> constructor = Product.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Product product = constructor.newInstance();
            setField(product, "name", name);
            setField(product, "description", description);
            setField(product, "startPrice", startPrice);
            setField(product, "currentPrice", startPrice);
            setField(product, "auctionStartTime", auctionStartTime);
            setField(product, "auctionEndTime", auctionEndTime);
            setField(product, "status", determineStatus(auctionStartTime, auctionEndTime));
            setField(product, "seller", seller);
            return product;
        } catch (Exception e) {
            throw new RuntimeException("테스트용 Product 생성 실패", e);
        }
    }

    private static void setField(Product product, String fieldName, Object value) throws Exception {
        Field field = Product.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(product, value);
    }

    private static AuctionStatus determineStatus(LocalDateTime auctionStartTime, LocalDateTime auctionEndTime) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(auctionStartTime)) {
            return AuctionStatus.SCHEDULED;
        }
        if (now.isBefore(auctionEndTime)) {
            return AuctionStatus.IN_PROGRESS;
        }
        return AuctionStatus.ENDED;
    }
}
