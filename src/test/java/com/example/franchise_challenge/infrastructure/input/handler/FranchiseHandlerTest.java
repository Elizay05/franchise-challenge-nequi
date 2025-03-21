package com.example.franchise_challenge.infrastructure.input.handler;

import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.FranchiseRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.application.handler.IFranchiseRestHandler;
import com.example.franchise_challenge.infrastructure.entrypoints.handler.FranchiseHandler;
import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
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
import org.springframework.http.MediaType;
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
    private IFranchiseRestHandler franchiseRestHandler;

    @Mock
    private Validator validator;

    @InjectMocks
    private FranchiseHandler franchiseHandler;

    @Test
    public void test_valid_franchise_request_returns_created_status() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        FranchiseRequest franchiseRequest = new FranchiseRequest("Test Franchise");

        when(request.bodyToMono(FranchiseRequest.class)).thenReturn(Mono.just(franchiseRequest));
        when(validator.validate(franchiseRequest)).thenReturn(Collections.emptySet());
        when(franchiseRestHandler.createFranchise(franchiseRequest)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = franchiseHandler.createFranchise(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.CREATED)
                .verifyComplete();

        verify(franchiseRestHandler).createFranchise(franchiseRequest);
    }

    @Test
    public void test_invalid_franchise_request_returns_bad_request() {
        // Arrange
        ServerRequest request = mock(ServerRequest.class);
        FranchiseRequest franchiseRequest = new FranchiseRequest("");

        Set<ConstraintViolation<FranchiseRequest>> violations = new HashSet<>();
        ConstraintViolation<FranchiseRequest> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("name");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("is required");
        violations.add(violation);

        when(request.bodyToMono(FranchiseRequest.class)).thenReturn(Mono.just(franchiseRequest));
        when(validator.validate(franchiseRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> result = franchiseHandler.createFranchise(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST &&
                            response.headers().getContentType().equals(MediaType.APPLICATION_JSON);
                })
                .verifyComplete();

        verify(franchiseRestHandler, Mockito.never()).createFranchise(Mockito.any());
    }

    @Test
    public void test_valid_name_update_returns_no_content() {
        // Arrange
        Long franchiseId = 1L;
        String validName = "New Franchise Name";
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest(validName);

        ServerRequest mockRequest = mock(ServerRequest.class);

        when(mockRequest.pathVariable(InputConstants.FRANCHISE_ID)).thenReturn(franchiseId.toString());
        when(mockRequest.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(Collections.emptySet());
        when(franchiseRestHandler.updateName(franchiseId, validName)).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> result = franchiseHandler.updateName(mockRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.statusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();

        verify(franchiseRestHandler).updateName(franchiseId, validName);
    }

    @Test
    public void test_empty_name_returns_bad_request() {
        // Arrange
        Long franchiseId = 1L;
        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest("");

        ServerRequest mockRequest = mock(ServerRequest.class);

        Set<ConstraintViolation<NameUpdateRequest>> violations = new HashSet<>();
        ConstraintViolation<NameUpdateRequest> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn("name");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn(ApplicationConstants.IS_REQUIRED);
        violations.add(violation);

        when(mockRequest.pathVariable(InputConstants.FRANCHISE_ID)).thenReturn(franchiseId.toString());
        when(mockRequest.bodyToMono(NameUpdateRequest.class)).thenReturn(Mono.just(nameUpdateRequest));
        when(validator.validate(nameUpdateRequest)).thenReturn(violations);

        // Act
        Mono<ServerResponse> result = franchiseHandler.updateName(mockRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return response.statusCode() == HttpStatus.BAD_REQUEST &&
                            response.headers().getContentType().equals(MediaType.APPLICATION_JSON);
                })
                .verifyComplete();

        verify(franchiseRestHandler, never()).updateName(anyLong(), anyString());
    }
}
