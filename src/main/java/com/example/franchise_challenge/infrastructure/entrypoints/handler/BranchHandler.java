package com.example.franchise_challenge.infrastructure.entrypoints.handler;

import com.example.franchise_challenge.domain.api.IBranchServicePort;
import com.example.franchise_challenge.domain.exceptions.BranchAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.BranchNotFoundException;
import com.example.franchise_challenge.domain.exceptions.FranchiseNotFoundException;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.BranchRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.mapper.IBranchRequestMapper;
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
public class BranchHandler {

    private final IBranchServicePort branchServicePort;
    private final IBranchRequestMapper branchRequestMapper;
    private final Validator validator;

    public Mono<ServerResponse> createBranch(ServerRequest request) {
        return request.bodyToMono(BranchRequest.class)
                .flatMap(branchRequest -> Mono.fromCallable(() -> validator.validate(branchRequest))
                        .flatMap(violations -> violations.isEmpty()
                                ? Mono.just(branchRequestMapper.toModel(branchRequest))
                                : Mono.error(new ConstraintViolationException(violations)))
                )
                .flatMap(branchServicePort::createBranch)
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
                .onErrorResume(BranchAlreadyExistsException.class, ex -> {
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
        Long branchId = Long.parseLong(request.pathVariable(InputConstants.BRANCH_ID));

        return request.bodyToMono(NameUpdateRequest.class)
                .flatMap(nameRequest -> Mono.fromCallable(() -> validator.validate(nameRequest))
                        .flatMap(violations -> violations.isEmpty()
                                ? Mono.just(nameRequest.getName())
                                : Mono.error(new ConstraintViolationException(violations)))
                )
                .flatMap(name -> branchServicePort.updateName(branchId, name)
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
                .onErrorResume(BranchAlreadyExistsException.class, ex -> {
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
