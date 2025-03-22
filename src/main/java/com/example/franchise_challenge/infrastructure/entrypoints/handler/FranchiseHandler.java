package com.example.franchise_challenge.infrastructure.entrypoints.handler;

import com.example.franchise_challenge.domain.api.IFranchiseServicePort;
import com.example.franchise_challenge.domain.exceptions.FranchiseAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.FranchiseNotFoundException;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.FranchiseRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.mapper.IFranchiseRequestMapper;
import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import com.example.franchise_challenge.infrastructure.entrypoints.util.models.ExceptionResponse;
import jakarta.validation.ConstraintViolation;
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
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final IFranchiseServicePort franchiseServicePort;
    private final IFranchiseRequestMapper franchiseRequestMapper;
    private final Validator validator;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(franchiseRequest -> {
                    Set<ConstraintViolation<FranchiseRequest>> violations = validator.validate(franchiseRequest);

                    return violations.isEmpty()
                            ? Mono.just(franchiseRequestMapper.toModel(franchiseRequest))
                            : Mono.error(new ConstraintViolationException(violations));
                })
                .flatMap(franchiseServicePort::createFranchise)
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
                .onErrorResume(FranchiseAlreadyExistsException.class, ex -> {
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

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long franchiseId = Long.parseLong(request.pathVariable(InputConstants.FRANCHISE_ID));

        return request.bodyToMono(NameUpdateRequest.class)
                .flatMap(nameRequest -> {
                    Set<ConstraintViolation<NameUpdateRequest>> violations = validator.validate(nameRequest);

                    return violations.isEmpty()
                            ? Mono.just(nameRequest.getName())
                            : Mono.error(new ConstraintViolationException(violations));
                })
                .flatMap(name -> franchiseServicePort.updateName(franchiseId, name)
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
                .onErrorResume(FranchiseAlreadyExistsException.class, ex -> {
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
