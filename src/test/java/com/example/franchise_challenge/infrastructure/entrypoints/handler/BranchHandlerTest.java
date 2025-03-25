package com.example.franchise_challenge.infrastructure.entrypoints.handler;

import com.example.franchise_challenge.domain.api.IBranchServicePort;
import com.example.franchise_challenge.domain.exceptions.BranchAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.BranchNotFoundException;
import com.example.franchise_challenge.domain.exceptions.FranchiseNotFoundException;
import com.example.franchise_challenge.domain.model.BranchModel;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.BranchRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.mapper.IBranchRequestMapper;
import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchHandlerTest {

    @Mock
    private IBranchServicePort branchServicePort;

    @Mock
    private IBranchRequestMapper branchRequestMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private BranchHandler branchHandler;

    @Test
    public void test_valid_branch_request_returns_created_status() {
        // Arrange
        BranchRequest validRequest = new BranchRequest("Test Branch", 1);
        BranchModel branchModel = new BranchModel(null, "Test Franchise", 1L);

        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(validRequest));

        Set<ConstraintViolation<BranchRequest>> emptyViolations = Collections.emptySet();

        when(validator.validate(validRequest)).thenReturn(emptyViolations);
        when(branchRequestMapper.toModel(validRequest)).thenReturn(branchModel);
        when(branchServicePort.createBranch(branchModel)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> responseMono = branchHandler.createBranch(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CREATED)
                .verifyComplete();

        verify(validator).validate(validRequest);
        verify(branchRequestMapper).toModel(validRequest);
        verify(branchServicePort).createBranch(branchModel);
    }

    @Test
    public void test_invalid_request_returns_bad_request() {
        // Arrange
        BranchRequest invalidRequest = new BranchRequest("", 1);

        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(invalidRequest));

        Set<ConstraintViolation<BranchRequest>> violations = new HashSet<>();
        ConstraintViolation<BranchRequest> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("name");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn(InputConstants.IS_REQUIRED);
        violations.add(violation);

        when(validator.validate(invalidRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> responseMono = branchHandler.createBranch(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST;
                })
                .verifyComplete();

        verify(validator).validate(invalidRequest);
        verify(branchRequestMapper, never()).toModel(any());
        verify(branchServicePort, never()).createBranch(any());
    }

    @Test
    public void test_franchise_not_found_exception_returns_not_found_status() {
        // Arrange
        BranchRequest request = new BranchRequest("Existing Branch", 1);
        BranchModel branchModel = new BranchModel(null, "Existing Franchise", 1L);

        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(request));

        Set<ConstraintViolation<BranchRequest>> emptyViolations = Collections.emptySet();

        when(validator.validate(request)).thenReturn(emptyViolations);
        when(branchRequestMapper.toModel(request)).thenReturn(branchModel);
        when(branchServicePort.createBranch(branchModel))
                .thenReturn(Mono.error(new FranchiseNotFoundException("Franchise not found")));

        // Act
        Mono<ServerResponse> responseMono = branchHandler.createBranch(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(validator).validate(request);
        verify(branchRequestMapper).toModel(request);
        verify(branchServicePort).createBranch(branchModel);
    }

    @Test
    public void test_branch_already_exists_exception_returns_conflict_status() {
        // Arrange
        BranchRequest request = new BranchRequest("Existing Branch", 1);
        BranchModel branchModel = new BranchModel(null, "Existing Franchise", 1L);

        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(request));

        Set<ConstraintViolation<BranchRequest>> emptyViolations = Collections.emptySet();

        when(validator.validate(request)).thenReturn(emptyViolations);
        when(branchRequestMapper.toModel(request)).thenReturn(branchModel);
        when(branchServicePort.createBranch(branchModel))
                .thenReturn(Mono.error(new BranchAlreadyExistsException("Branch already exists")));

        // Act
        Mono<ServerResponse> responseMono = branchHandler.createBranch(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CONFLICT)
                .verifyComplete();

        verify(validator).validate(request);
        verify(branchRequestMapper).toModel(request);
        verify(branchServicePort).createBranch(branchModel);
    }

    @Test
    public void test_unexpected_exception_returns_internal_server_error_create_branch() {
        // Arrange
        BranchRequest validRequest = new BranchRequest("Test Branch", 1);
        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(validRequest));

        Set<ConstraintViolation<BranchRequest>> emptyViolations = Collections.emptySet();

        when(validator.validate(validRequest)).thenReturn(emptyViolations);
        when(branchRequestMapper.toModel(validRequest)).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        Mono<ServerResponse> responseMono = branchHandler.createBranch(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();

        verify(validator).validate(validRequest);
        verify(branchRequestMapper).toModel(validRequest);
    }

    @Test
    public void test_valid_branch_id_and_name_returns_ok_response() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("New Branch Name");

        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(branchServicePort.updateName(1L, "New Branch Name")).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = branchHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(branchServicePort).updateName(1L, "New Branch Name");
    }

    @Test
    public void test_empty_name_triggers_validation_error() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("");

        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Set.of(mock(ConstraintViolation.class)));

        // Act
        Mono<ServerResponse> result = branchHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }

    @Test
    public void test_non_existent_branch_id_throws_branch_not_found_exception() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("New Branch Name");

        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn("999");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(branchServicePort.updateName(999L, "New Branch Name"))
                .thenReturn(Mono.error(new BranchNotFoundException("Branch not found")));

        // Act
        Mono<ServerResponse> result = branchHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(branchServicePort).updateName(999L, "New Branch Name");
    }

    @Test
    public void test_branch_already_exists_exception() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("Existing Branch Name");

        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(branchServicePort.updateName(1L, "Existing Branch Name"))
                .thenReturn(Mono.error(new BranchAlreadyExistsException("Branch already exists")));

        // Act
        Mono<ServerResponse> result = branchHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CONFLICT)
                .verifyComplete();

        verify(branchServicePort).updateName(1L, "Existing Branch Name");
    }

    @Test
    public void test_unexpected_exception_returns_internal_server_error_update_name() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("New Branch Name");

        when(request.pathVariable(InputConstants.BRANCH_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(branchServicePort.updateName(1L, "New Branch Name")).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        Mono<ServerResponse> result = branchHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();
    }
}
