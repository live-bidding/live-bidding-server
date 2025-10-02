package com.livebidding.server.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.livebidding.server.product.api.dto.request.ProductCreateRequest;
import com.livebidding.server.product.api.dto.response.ProductCreateResponse;
import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.repository.ProductRepository;
import com.livebidding.server.support.TestFixture;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import com.livebidding.server.user.exception.UserException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 단위 테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록에 성공한다")
    void createProductSuccess() {
        Long sellerId = 1L;
        User mockSeller = TestFixture.createUser("seller@test.com");
        given(userRepository.findById(sellerId)).willReturn(Optional.of(mockSeller));

        Product mockProduct = TestFixture.createProduct(mockSeller);
        given(productRepository.save(any(Product.class))).willReturn(mockProduct);

        ProductCreateRequest request = new ProductCreateRequest(
                "테스트 상품", "상품 설명", new BigDecimal("100000"),
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
        );

        ProductCreateResponse response = productService.createProduct(request, sellerId);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("테스트 상품");
        verify(userRepository, times(1)).findById(sellerId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("존재하지 않는 판매자 ID로 상품 등록 시 예외 발생")
    void createProductFailWhenSellerNotFound() {
        Long nonExistentSellerId = 999L;
        given(userRepository.findById(nonExistentSellerId)).willReturn(Optional.empty());

        ProductCreateRequest request = new ProductCreateRequest(
                "테스트 상품", "상품 설명", new BigDecimal("100000"),
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
        );

        assertThatThrownBy(() -> productService.createProduct(request, nonExistentSellerId))
                .isInstanceOf(UserException.class);
    }
}
