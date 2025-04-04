package com.example.franchise_challenge.infrastructure.entrypoints.handler;

import com.example.franchise_challenge.domain.api.IFranchiseServicePort;
import com.example.franchise_challenge.domain.exceptions.FranchiseAlreadyExistsException;
import com.example.franchise_challenge.domain.exceptions.FranchiseNotFoundException;
import com.example.franchise_challenge.domain.model.FranchiseModel;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.FranchiseRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.mapper.IFranchiseRequestMapper;
import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.Validator;
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
public class FranchiseHandlerTest {


    @Mock
    private IFranchiseServicePort franchiseServicePort;

    @Mock
    private IFranchiseRequestMapper franchiseRequestMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private FranchiseHandler franchiseHandler;

    @Test
    public void test_valid_franchise_request_returns_created_status() {
        // Arrange
        FranchiseRequest validRequest = new FranchiseRequest("Test Franchise");
        FranchiseModel franchiseModel = new FranchiseModel(null, "Test Franchise");

        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(validRequest));

        Set<ConstraintViolation<FranchiseRequest>> emptyViolations = Collections.emptySet();

        when(validator.validate(validRequest)).thenReturn(emptyViolations);
        when(franchiseRequestMapper.toModel(validRequest)).thenReturn(franchiseModel);
        when(franchiseServicePort.createFranchise(franchiseModel)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> responseMono = franchiseHandler.createFranchise(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CREATED)
                .verifyComplete();

        verify(validator).validate(validRequest);
        verify(franchiseRequestMapper).toModel(validRequest);
        verify(franchiseServicePort).createFranchise(franchiseModel);
    }

    @Test
    public void test_invalid_request_returns_bad_request() {
        // Arrange
        FranchiseRequest invalidRequest = new FranchiseRequest("");

        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(invalidRequest));

        Set<ConstraintViolation<FranchiseRequest>> violations = new HashSet<>();
        ConstraintViolation<FranchiseRequest> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("name");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn(InputConstants.IS_REQUIRED);
        violations.add(violation);

        when(validator.validate(invalidRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> responseMono = franchiseHandler.createFranchise(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST;
                })
                .verifyComplete();

        verify(validator).validate(invalidRequest);
        verify(franchiseRequestMapper, never()).toModel(any());
        verify(franchiseServicePort, never()).createFranchise(any());
    }

    @Test
    public void test_franchise_already_exists_exception_returns_conflict_status() {
        // Arrange
        FranchiseRequest request = new FranchiseRequest("Existing Franchise");
        FranchiseModel franchiseModel = new FranchiseModel(null, "Existing Franchise");

        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(request));

        Set<ConstraintViolation<FranchiseRequest>> emptyViolations = Collections.emptySet();

        when(validator.validate(request)).thenReturn(emptyViolations);
        when(franchiseRequestMapper.toModel(request)).thenReturn(franchiseModel);
        when(franchiseServicePort.createFranchise(franchiseModel))
                .thenReturn(Mono.error(new FranchiseAlreadyExistsException("Franchise already exists")));

        // Act
        Mono<ServerResponse> responseMono = franchiseHandler.createFranchise(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CONFLICT)
                .verifyComplete();

        verify(validator).validate(request);
        verify(franchiseRequestMapper).toModel(request);
        verify(franchiseServicePort).createFranchise(franchiseModel);
    }

    @Test
    public void test_unexpected_exception_returns_internal_server_error_create_franchise() {
        // Arrange
        FranchiseRequest validRequest = new FranchiseRequest("Test Franchise");
        ServerRequest mockRequest = MockServerRequest.builder()
                .body(Mono.just(validRequest));

        Set<ConstraintViolation<FranchiseRequest>> emptyViolations = Collections.emptySet();

        when(validator.validate(validRequest)).thenReturn(emptyViolations);
        when(franchiseRequestMapper.toModel(validRequest)).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        Mono<ServerResponse> responseMono = franchiseHandler.createFranchise(mockRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();

        verify(validator).validate(validRequest);
        verify(franchiseRequestMapper).toModel(validRequest);
    }

    @Test
    public void test_valid_franchise_id_and_name_returns_ok_response() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("New Franchise Name");

        when(request.pathVariable(InputConstants.FRANCHISE_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(franchiseServicePort.updateName(1L, "New Franchise Name")).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = franchiseHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(franchiseServicePort).updateName(1L, "New Franchise Name");
    }

    @Test
    public void test_empty_name_triggers_validation_error() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("");

        when(request.pathVariable(InputConstants.FRANCHISE_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Set.of(mock(ConstraintViolation.class)));

        // Act
        Mono<ServerResponse> result = franchiseHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }

    @Test
    public void test_non_existent_franchise_id_throws_franchise_not_found_exception() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("New Franchise Name");

        when(request.pathVariable(InputConstants.FRANCHISE_ID)).thenReturn("999");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(franchiseServicePort.updateName(999L, "New Franchise Name"))
                .thenReturn(Mono.error(new FranchiseNotFoundException("Franchise not found")));

        // Act
        Mono<ServerResponse> result = franchiseHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(franchiseServicePort).updateName(999L, "New Franchise Name");
    }

    @Test
    public void test_franchise_already_exists_exception() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("Existing Franchise Name");

        when(request.pathVariable(InputConstants.FRANCHISE_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(franchiseServicePort.updateName(1L, "Existing Franchise Name"))
                .thenReturn(Mono.error(new FranchiseAlreadyExistsException("Franchise already exists")));

        // Act
        Mono<ServerResponse> result = franchiseHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CONFLICT)
                .verifyComplete();

        verify(franchiseServicePort).updateName(1L, "Existing Franchise Name");
    }

    @Test
    public void test_unexpected_exception_returns_internal_server_error_update_name() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("New Franchise Name");

        when(request.pathVariable(InputConstants.FRANCHISE_ID)).thenReturn("1");
        when(request.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(franchiseServicePort.updateName(1L, "New Franchise Name")).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        Mono<ServerResponse> result = franchiseHandler.updateName(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();
    }
}
