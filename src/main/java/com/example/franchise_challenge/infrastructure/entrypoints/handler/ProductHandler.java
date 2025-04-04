package com.example.franchise_challenge.infrastructure.entrypoints.handler;

import com.example.franchise_challenge.domain.api.IProductServicePort;
import com.example.franchise_challenge.domain.exceptions.*;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.ProductRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.StockUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.mapper.IFranchiseStockResponseMapper;
import com.example.franchise_challenge.infrastructure.entrypoints.mapper.IProductRequestMapper;
import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import com.example.franchise_challenge.infrastructure.entrypoints.util.models.ExceptionResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final IProductServicePort productServicePort;
    private final IProductRequestMapper productRequestMapper;
    private final IFranchiseStockResponseMapper franchiseStockResponseMapper;
    private final Validator validator;

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .flatMap(productRequest -> Mono.fromCallable(() -> validator.validate(productRequest))
                        .flatMap(violations -> violations.isEmpty()
                                ? Mono.just(productRequestMapper.toModel(productRequest))
                                : Mono.error(new ConstraintViolationException(violations)))
                )
                .flatMap(productServicePort::createProduct)
                .then(ServerResponse.status(HttpStatus.CREATED).build())
                .onErrorResume(ConstraintViolationException.class, e -> {
                    String errorMessage = e.getConstraintViolations().stream()
                            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                            .collect(Collectors.joining(", "));
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            errorMessage,
                            HttpStatus.BAD_REQUEST.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(BranchNotFoundException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ProductAlreadyExistsException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.CONFLICT.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.CONFLICT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(InvalidStockException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.BAD_REQUEST.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            InputConstants.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable(InputConstants.PRODUCT_ID));
        Long branchId = Long.parseLong(request.pathVariable(InputConstants.BRANCH_ID));

        return productServicePort.deleteProduct(productId, branchId)
                .then(ServerResponse.noContent().build())
                .onErrorResume(BranchNotFoundException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ProductNotFoundInBranchException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            InputConstants.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable(InputConstants.PRODUCT_ID));
        Long branchId = Long.parseLong(request.pathVariable(InputConstants.BRANCH_ID));

        return request.bodyToMono(StockUpdateRequest.class)
                .flatMap(stockRequest -> Mono.fromCallable(() -> validator.validate(stockRequest))
                        .flatMap(violations -> violations.isEmpty()
                                ? Mono.just(stockRequest.getStock())
                                : Mono.error(new ConstraintViolationException(violations)))
                )
                .flatMap(stock -> productServicePort.updateStock(productId, branchId, stock))
                .then(ServerResponse.status(HttpStatus.OK).build())
                .onErrorResume(ConstraintViolationException.class, e -> {
                    String errorMessage = e.getConstraintViolations().stream()
                            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                            .collect(Collectors.joining(", "));
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            errorMessage,
                            HttpStatus.BAD_REQUEST.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(BranchNotFoundException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ProductNotFoundInBranchException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(InvalidStockException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.BAD_REQUEST.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            InputConstants.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });

    }

    public Mono<ServerResponse> getProducts(ServerRequest request) {
        Long franchiseId = Long.parseLong(request.pathVariable(InputConstants.FRANCHISE_ID));

        return productServicePort.getProductsByFranchise(franchiseId)
                .map(franchiseStockResponseMapper::toResponse)
                .flatMap(products -> ServerResponse.ok().bodyValue(products))
                .onErrorResume(FranchiseNotFoundException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            InputConstants.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long productId = Long.parseLong(request.pathVariable(InputConstants.PRODUCT_ID));

        return request.bodyToMono(NameUpdateRequest.class)
                .flatMap(nameRequest -> Mono.fromCallable(() -> validator.validate(nameRequest))
                        .flatMap(violations -> violations.isEmpty()
                                ? Mono.just(nameRequest.getName())
                                : Mono.error(new ConstraintViolationException(violations)))
                )
                .flatMap(name -> productServicePort.updateName(productId, name)
                        .then(ServerResponse.status(HttpStatus.OK).build()))
                .onErrorResume(ConstraintViolationException.class, e -> {
                    String errorMessage = e.getConstraintViolations().stream()
                            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                            .collect(Collectors.joining(", "));
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            errorMessage,
                            HttpStatus.BAD_REQUEST.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ProductNotFoundException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ProductAlreadyExistsException.class, ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            ex.getMessage(),
                            HttpStatus.CONFLICT.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.CONFLICT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                })
                .onErrorResume(ex -> {
                    ExceptionResponse errorResponse = new ExceptionResponse(
                            InputConstants.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                            LocalDateTime.now()
                    );
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorResponse);
                });
    }
}
