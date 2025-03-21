package com.example.franchise_challenge.infrastructure.input.routes;

import com.example.franchise_challenge.application.dto.request.FranchiseRequest;
import com.example.franchise_challenge.application.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.input.handler.FranchiseHandler;
import com.example.franchise_challenge.infrastructure.input.utils.constants.InputConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FranchiseRoute {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = InputConstants.FRANCHISES_PATH,
                    beanClass = FranchiseHandler.class,
                    beanMethod = InputConstants.METHOD_CREATE_FRANCHISE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = InputConstants.OP_CREATE_FRANCHISE,
                            summary = InputConstants.SUMMARY_CREATE_FRANCHISE,
                            description = InputConstants.DESC_CREATE_FRANCHISE,
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = FranchiseRequest.class),
                                            examples = @ExampleObject(
                                                    name = InputConstants.EXAMPLE_NAME_CREATE_FRANCHISE,
                                                    value = InputConstants.EXAMPLE_CREATE_FRANCHISE
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_201,
                                            description = InputConstants.RESP_FRANCHISE_CREATED
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_400,
                                            description = InputConstants.RESP_ERROR_VALIDATION,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_VALIDATION
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_409,
                                            description = InputConstants.RESP_ERROR_DUPLICATE_FRANCHISE,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_DUPLICATE_FRANCHISE_JSON
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = InputConstants.FRANCHISES_PATH_UPDATE_NAME,
                    beanClass = FranchiseHandler.class,
                    beanMethod = InputConstants.METHOD_UPDATE_FRANCHISE_NAME,
                    method = RequestMethod.PUT,
                    operation = @Operation(
                            operationId = InputConstants.OP_UPDATE_FRANCHISE_NAME,
                            summary = InputConstants.SUMMARY_UPDATE_FRANCHISE_NAME,
                            description = InputConstants.DESC_UPDATE_FRANCHISE_NAME,
                            parameters = {
                                    @Parameter(
                                            name = InputConstants.FRANCHISE_ID,
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = InputConstants.DESC_PARAM_FRANCHISE_ID_UPDATE_NAME,
                                            schema = @Schema(type = InputConstants.TYPE_INTENGER, format = InputConstants.TYPE_INT64)
                                    )
                            },
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = NameUpdateRequest.class),
                                            examples = @ExampleObject(
                                                    name = InputConstants.EXAMPLE_NAME_UPDATE_FRANCHISE,
                                                    value = InputConstants.EXAMPLE_UPDATE_FRANCHISE
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_204,
                                            description = InputConstants.RESP_FRANCHISE_NAME_UPDATED
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_400,
                                            description = InputConstants.RESP_ERROR_VALIDATION,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_VALIDATION
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_404,
                                            description = InputConstants.RESP_ERROR_FRANCHISE_NOT_FOUND,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_FRANCHISE_NOT_FOUND_JSON
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_409,
                                            description = InputConstants.RESP_ERROR_DUPLICATE_FRANCHISE,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_DUPLICATE_FRANCHISE_JSON
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions.route()
                .POST(InputConstants.FRANCHISES_PATH, handler::createFranchise)
                .PUT(InputConstants.FRANCHISES_PATH_UPDATE_NAME, handler::updateName)
                .build();
    }
}
