package com.example.franchise_challenge.infrastructure.input.handler;

import com.example.franchise_challenge.application.dto.request.BranchRequest;
import com.example.franchise_challenge.application.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.application.handler.IBranchRestHandler;
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
public class BranchHandler {

    private final IBranchRestHandler branchRestHandler;
    private final Validator validator;

    public Mono<ServerResponse> createBranch(ServerRequest request) {
        return request.bodyToMono(BranchRequest.class)
                .flatMap(branchRequest -> {
                    Set<ConstraintViolation<BranchRequest>> violations = validator.validate(branchRequest);

                    if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.joining(", "));

                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{" + "\"" + InputConstants.ERROR + "\": \"" + errorMessage + "\"}");
                    }

                    return branchRestHandler.createBranch(branchRequest)
                            .then(ServerResponse.status(HttpStatus.CREATED).build());
                });
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long branchId = Long.parseLong(request.pathVariable(InputConstants.BRANCH_ID));

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

                    return branchRestHandler.updateName(branchId, nameRequest.getName())
                            .then(ServerResponse.noContent().build());
                });
    }
}
