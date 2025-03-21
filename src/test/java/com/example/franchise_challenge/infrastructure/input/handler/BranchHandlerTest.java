package com.example.franchise_challenge.infrastructure.input.handler;

import com.example.franchise_challenge.application.dto.request.BranchRequest;
import com.example.franchise_challenge.application.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.application.handler.IBranchRestHandler;
import com.example.franchise_challenge.application.utils.constants.ApplicationConstants;
import com.example.franchise_challenge.infrastructure.input.utils.constants.InputConstants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class BranchHandlerTest {

    @Mock
    private IBranchRestHandler branchRestHandler;

    @Mock
    private Validator validator;

    @InjectMocks
    private BranchHandler branchHandler;

    @Test
    public void test_valid_branch_request_returns_created_status() {
        // Arrange
        BranchRequest validRequest = new BranchRequest("Test Branch", 1);
        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(validRequest));

        Mockito.when(validator.validate(validRequest)).thenReturn(Collections.emptySet());
        Mockito.when(branchRestHandler.createBranch(validRequest)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> responseMono = branchHandler.createBranch(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CREATED)
                .verifyComplete();

        Mockito.verify(branchRestHandler).createBranch(validRequest);
    }

    @Test
    public void test_missing_name_field_triggers_validation_error() {
        // Arrange
        BranchRequest invalidRequest = new BranchRequest(null, 1);
        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(invalidRequest));

        Set<ConstraintViolation<BranchRequest>> violations = new HashSet<>();
        ConstraintViolation<BranchRequest> violation = Mockito.mock(ConstraintViolation.class);
        Path path = Mockito.mock(Path.class);

        Mockito.when(path.toString()).thenReturn("name");
        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("is required");
        violations.add(violation);

        Mockito.when(validator.validate(invalidRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> responseMono = branchHandler.createBranch(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST;
                })
                .verifyComplete();

        Mockito.verify(validator).validate(invalidRequest);
        Mockito.verify(branchRestHandler, Mockito.never()).createBranch(invalidRequest);
    }

    @Test
    public void test_update_name_success() {
        // Arrange
        Long branchId = 1L;
        String newName = "New Branch Name";
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest(newName);

        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.BRANCH_ID, branchId.toString())
                .body(Mono.just(nameUpdateRequest));


        Mockito.when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        Mockito.when(branchRestHandler.updateName(branchId, newName)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = branchHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();

        Mockito.verify(branchRestHandler).updateName(branchId, newName);
    }

    @Test
    public void test_update_name_validation_failure() {
        // Arrange
        Long branchId = 1L;
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("");

        ServerRequest request = MockServerRequest.builder()
                .pathVariable(InputConstants.BRANCH_ID, branchId.toString())
                .body(Mono.just(nameUpdateRequest));


        Set<ConstraintViolation<NameUpdateRequest>> violations = new HashSet<>();
        ConstraintViolation<NameUpdateRequest> violation = Mockito.mock(ConstraintViolation.class);
        violations.add(violation);

        Mockito.when(violation.getPropertyPath()).thenReturn(Mockito.mock(Path.class));
        Mockito.when(violation.getMessage()).thenReturn(ApplicationConstants.IS_REQUIRED);
        Mockito.when(validator.validate(nameUpdateRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> result = branchHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST;
                })
                .verifyComplete();

        Mockito.verify(validator).validate(nameUpdateRequest);
        Mockito.verifyNoInteractions(branchRestHandler);
    }
}
