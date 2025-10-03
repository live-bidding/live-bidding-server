package com.livebidding.server.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.livebidding.server.global.error.GlobalException;
import com.livebidding.server.product.api.dto.request.ProductCreateRequest;
import com.livebidding.server.product.api.dto.response.ProductCreateResponse;
import com.livebidding.server.product.api.dto.response.ProductDetailResponse;
import com.livebidding.server.product.api.dto.response.ProductSummaryResponse;
import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.repository.ProductRepository;
import com.livebidding.server.product.domain.type.AuctionStatus;
import com.livebidding.server.product.exception.ProductException;
import com.livebidding.server.support.TestFixture;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import com.livebidding.server.user.exception.UserException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 단위 테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("상품 등록 시")
    class CreateProduct {

        @Test
        @DisplayName("성공한다")
        void success() {
            // given
            Long sellerId = 1L;
            User mockSeller = TestFixture.createUser("seller@test.com");
            given(userRepository.findById(sellerId)).willReturn(Optional.of(mockSeller));

            Product mockProduct = TestFixture.createProduct(mockSeller);
            given(productRepository.save(any(Product.class))).willReturn(mockProduct);

            ProductCreateRequest request = new ProductCreateRequest(
                    "테스트 상품", "상품 설명", new BigDecimal("100000"),
                    LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
            );

            // when
            ProductCreateResponse response = productService.createProduct(request, sellerId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.name()).isNotNull();
            assertThat(response.currentPrice()).isNotNull();
            verify(userRepository).findById(sellerId);
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("존재하지 않는 판매자 ID로 실패한다")
        void failWhenSellerNotFound() {
            // given
            Long nonExistentSellerId = 999L;
            given(userRepository.findById(nonExistentSellerId)).willReturn(Optional.empty());

            ProductCreateRequest request = new ProductCreateRequest(
                    "테스트 상품", "상품 설명", new BigDecimal("100000"),
                    LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
            );

            // when & then
            assertThatThrownBy(() -> productService.createProduct(request, nonExistentSellerId))
                    .isInstanceOf(UserException.class);
        }
    }

    @Nested
    @DisplayName("상품 목록 조회 시")
    class GetProducts {

        @Test
        @DisplayName("상태 필터 없이 전체 조회에 성공한다")
        void successWithoutStatusFilter() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            User seller = TestFixture.createUser();
            Product product = TestFixture.createProduct(seller);
            Page<Product> productPage = new PageImpl<>(List.of(product));

            given(productRepository.findAll(pageable)).willReturn(productPage);

            // when
            Page<ProductSummaryResponse> result = productService.getProducts(null, pageable);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepository).findAll(pageable);
        }

        @Test
        @DisplayName("SCHEDULED 상태 필터링에 성공한다")
        void successWithScheduledFilter() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            User seller = TestFixture.createUser();
            Product product = TestFixture.createScheduledProduct(seller);
            Page<Product> productPage = new PageImpl<>(List.of(product));

            given(productRepository.findScheduled(any(LocalDateTime.class), eq(pageable)))
                    .willReturn(productPage);

            // when
            Page<ProductSummaryResponse> result = productService.getProducts(AuctionStatus.SCHEDULED, pageable);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepository).findScheduled(any(LocalDateTime.class), eq(pageable));
        }

        @Test
        @DisplayName("IN_PROGRESS 상태 필터링에 성공한다")
        void successWithInProgressFilter() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            User seller = TestFixture.createUser();
            Product product = TestFixture.createInProgressProduct(seller);
            Page<Product> productPage = new PageImpl<>(List.of(product));

            given(productRepository.findInProgress(any(LocalDateTime.class), eq(pageable)))
                    .willReturn(productPage);

            // when
            Page<ProductSummaryResponse> result = productService.getProducts(AuctionStatus.IN_PROGRESS, pageable);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepository).findInProgress(any(LocalDateTime.class), eq(pageable));
        }

        @Test
        @DisplayName("ENDED 상태 필터링에 성공한다")
        void successWithEndedFilter() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            User seller = TestFixture.createUser();
            Product product = TestFixture.createEndedProduct(seller);
            Page<Product> productPage = new PageImpl<>(List.of(product));

            given(productRepository.findEnded(any(LocalDateTime.class), eq(pageable)))
                    .willReturn(productPage);

            // when
            Page<ProductSummaryResponse> result = productService.getProducts(AuctionStatus.ENDED, pageable);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(productRepository).findEnded(any(LocalDateTime.class), eq(pageable));
        }

        @Test
        @DisplayName("페이지 크기가 최대값을 초과하면 예외가 발생한다")
        void failWhenPageSizeExceeded() {
            // given
            Pageable pageable = PageRequest.of(0, 101);

            // when & then
            assertThatThrownBy(() -> productService.getProducts(null, pageable))
                    .isInstanceOf(GlobalException.class);
        }
    }

    @Nested
    @DisplayName("상품 상세 조회 시")
    class GetProductDetail {

        @Test
        @DisplayName("성공한다")
        void success() {
            // given
            Long productId = 1L;
            User seller = TestFixture.createUser();
            Product product = TestFixture.createProduct(seller);

            given(productRepository.findByIdWithSeller(productId))
                    .willReturn(Optional.of(product));

            // when
            ProductDetailResponse response = productService.getProductDetail(productId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.name()).isNotNull();
            assertThat(response.seller()).isNotNull();
            verify(productRepository).findByIdWithSeller(productId);
        }

        @Test
        @DisplayName("존재하지 않는 상품 ID로 실패한다")
        void failWhenProductNotFound() {
            // given
            Long nonExistentProductId = 999L;
            given(productRepository.findByIdWithSeller(nonExistentProductId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> productService.getProductDetail(nonExistentProductId))
                    .isInstanceOf(ProductException.class);
        }
    }
}
