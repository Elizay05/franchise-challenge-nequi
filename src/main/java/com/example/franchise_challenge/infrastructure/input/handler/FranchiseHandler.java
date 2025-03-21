package com.example.franchise_challenge.infrastructure.input.handler;

import com.example.franchise_challenge.application.dto.request.FranchiseRequest;
import com.example.franchise_challenge.application.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.application.handler.IFranchiseRestHandler;
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
public class FranchiseHandler {

    private final IFranchiseRestHandler franchiseRestHandler;
    private final Validator validator;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(franchiseRequest -> {
                    Set<ConstraintViolation<FranchiseRequest>> violations = validator.validate(franchiseRequest);

                    if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.joining(", "));

                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{" + "\"" + InputConstants.ERROR + "\": \"" + errorMessage + "\"}");
                    }

                    return franchiseRestHandler.createFranchise(franchiseRequest)
                            .then(ServerResponse.status(HttpStatus.CREATED).build());
                });
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long franchiseId = Long.parseLong(request.pathVariable(InputConstants.FRANCHISE_ID));

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

                    return franchiseRestHandler.updateName(franchiseId, nameRequest.getName())
                            .then(ServerResponse.noContent().build());
                });
    }
}
