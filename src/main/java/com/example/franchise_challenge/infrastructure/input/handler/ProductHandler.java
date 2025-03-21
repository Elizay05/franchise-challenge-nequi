package com.example.franchise_challenge.infrastructure.input.handler;

import com.example.franchise_challenge.application.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.application.dto.request.ProductRequest;
import com.example.franchise_challenge.application.dto.request.StockUpdateRequest;
import com.example.franchise_challenge.application.handler.IProductRestHandler;
import com.example.franchise_challenge.infrastructure.input.utils.constants.InputConstants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final IProductRestHandler productRestHandler;
    private final Validator validator;

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .flatMap(productRequest -> {
                    Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

                    if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.joining(", "));

                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{" + "\"" + InputConstants.ERROR + "\": \"" + errorMessage + "\"}");
                    }

                    return productRestHandler.createProduct(productRequest)
                            .then(ServerResponse.status(HttpStatus.CREATED).build());
                });
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable(InputConstants.PRODUCT_ID));
        Long branchId = Long.parseLong(request.pathVariable(InputConstants.BRANCH_ID));

        return productRestHandler.deleteProduct(productId, branchId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable(InputConstants.PRODUCT_ID));
        Long branchId = Long.parseLong(request.pathVariable(InputConstants.BRANCH_ID));

        return request.bodyToMono(StockUpdateRequest.class)
                .flatMap(stockRequest -> {
                    Set<ConstraintViolation<StockUpdateRequest>> violations = validator.validate(stockRequest);

                    if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.joining(", "));
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{" + "\"" + InputConstants.ERROR + "\": \"" + errorMessage + "\"}");
                    }

                    return productRestHandler.updateStock(productId, branchId, stockRequest.getStock())
                            .then(ServerResponse.noContent().build());
                });
    }

    public Mono<ServerResponse> getProducts(ServerRequest request) {
        Long franchiseId = Long.parseLong(request.pathVariable(InputConstants.FRANCHISE_ID));

        return productRestHandler.getAllProductStockByFranchise(franchiseId)
                .flatMap(products -> ServerResponse.ok().bodyValue(products));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable(InputConstants.PRODUCT_ID));

        return request.bodyToMono(NameUpdateRequest.class)
                .flatMap(nameRequest -> {
                    Set<ConstraintViolation<NameUpdateRequest>> violations = validator.validate(nameRequest);

                    if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.joining(", "));
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{" + "\"" + InputConstants.ERROR + "\": \"" + errorMessage + "\"}");
                    }

                    return productRestHandler.updateName(productId, nameRequest.getName())
                            .then(ServerResponse.noContent().build());
                });
    }
}
