package com.livebidding.server.product.api.dto.response;

import com.livebidding.server.user.domain.entity.User;

public record SellerResponse(
        Long id,
        String name
) {
    public static SellerResponse from(User seller) {
        return new SellerResponse(seller.getId(), seller.getName().getValue());
    }
}
