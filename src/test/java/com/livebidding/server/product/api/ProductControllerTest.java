package com.livebidding.server.product.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livebidding.server.auth.jwt.JwtTokenProvider;
import com.livebidding.server.product.api.dto.request.ProductCreateRequest;
import com.livebidding.server.product.domain.entity.Product;
import com.livebidding.server.product.domain.repository.ProductRepository;
import com.livebidding.server.support.TestFixture;
import com.livebidding.server.user.domain.entity.User;
import com.livebidding.server.user.domain.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String accessToken;
    private User seller;

    @BeforeEach
    void setUp() {
        productRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        seller = userRepository.save(TestFixture.createUser("seller@test.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(seller.getEmail().getValue(), null);
        accessToken = jwtTokenProvider.createAccessToken(authentication);
    }

    @Test
    @DisplayName("인증된 사용자는 상품 등록에 성공한다.")
    void createProductSuccess() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest("test product", "desc", BigDecimal.valueOf(1000), LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        // when & then
        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test product"));
    }

    @Test
    @DisplayName("로그인하지 않은 사용자는 상품 등록에 실패한다. (401 Unauthorized)")
    void createProductFail_Unauthorized() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest("test product", "desc", BigDecimal.valueOf(1000), LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        // when & then
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("상품 상세 조회에 성공한다.")
    void getProductDetailSuccess() throws Exception {
        // given
        Product savedProduct = productRepository.save(TestFixture.createProduct(seller));

        // when & then
        mockMvc.perform(get("/api/v1/products/{productId}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedProduct.getId()))
                .andExpect(jsonPath("$.seller.name").value(seller.getName().getValue()));
    }
}