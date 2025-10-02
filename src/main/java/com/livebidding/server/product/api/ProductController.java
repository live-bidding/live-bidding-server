package com.livebidding.server.product.api;

import com.livebidding.server.auth.service.UserDetailsImpl;
import com.livebidding.server.product.api.dto.request.ProductCreateRequest;
import com.livebidding.server.product.api.dto.response.ProductCreateResponse;
import com.livebidding.server.product.api.dto.response.ProductDetailResponse;
import com.livebidding.server.product.api.dto.response.ProductSummaryResponse;
import com.livebidding.server.product.application.ProductService;
import com.livebidding.server.product.domain.type.AuctionStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductCreateResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long sellerId = userDetails.getUser().getId();
        ProductCreateResponse response = productService.createProduct(request, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductSummaryResponse>> getProducts(
            @RequestParam(name = "status", required = false) AuctionStatus status,
            @PageableDefault(sort = "auctionEndTime", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ProductSummaryResponse> response = productService.getProducts(status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
            @PathVariable("productId") Long productId
    ) {
        ProductDetailResponse response = productService.getProductDetail(productId);
        return ResponseEntity.ok(response);
    }
}
