package com.livebidding.server.product.application;

import com.livebidding.server.global.error.GlobalErrorCode;
import com.livebidding.server.global.error.GlobalException;
import com.livebidding.server.product.api.dto.request.ProductCreateRequest;
import com.livebidding.server.product.api.dto.response.ProductCreateResponse;
import com.livebidding.server.product.api.dto.response.ProductDetailResponse;
import com.livebidding.server.product.api.dto.response.ProductSummaryResponse;
import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.repository.ProductRepository;
import com.livebidding.server.product.domain.type.AuctionStatus;
import com.livebidding.server.product.exception.ProductErrorCode;
import com.livebidding.server.product.exception.ProductException;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import com.livebidding.server.user.exception.UserErrorCode;
import com.livebidding.server.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private static final int MAX_PAGE_SIZE = 100;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Product product = Product.of(
                request.name(),
                request.description(),
                request.startPrice(),
                request.auctionStartTime(),
                request.auctionEndTime(),
                seller
        );
        Product savedProduct = productRepository.save(product);
        return ProductCreateResponse.from(savedProduct);
    }

    public Page<ProductSummaryResponse> getProducts(AuctionStatus status, Pageable pageable) {
        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new GlobalException(GlobalErrorCode.PAGE_SIZE_EXCEEDED);
        }

        if (status != null) {
            return productRepository.findAllByStatus(status, pageable)
                    .map(ProductSummaryResponse::from);
        }

        return productRepository.findAll(pageable)
                .map(ProductSummaryResponse::from);
    }

    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = productRepository.findByIdWithSeller(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return ProductDetailResponse.from(product);
    }
}
