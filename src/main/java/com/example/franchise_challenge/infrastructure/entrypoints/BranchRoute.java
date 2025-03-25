package com.example.franchise_challenge.infrastructure.entrypoints;

import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.BranchRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.infrastructure.entrypoints.handler.BranchHandler;
import com.example.franchise_challenge.infrastructure.entrypoints.util.constants.InputConstants;
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
public class BranchRoute {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = InputConstants.BRANCHES_PATH,
                    beanClass = BranchHandler.class,
                    beanMethod = InputConstants.METHOD_CREATE_BRANCH,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = InputConstants.OP_CREATE_BRANCH,
                            summary = InputConstants.SUMMARY_CREATE_BRANCH,
                            description = InputConstants.DESC_CREATE_BRANCH,
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = BranchRequest.class),
                                            examples = @ExampleObject(
                                                    name = InputConstants.EXAMPLE_NAME_CREATE_BRANCH,
                                                    value = InputConstants.EXAMPLE_CREATE_BRANCH
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_201,
                                            description = InputConstants.RESP_BRANCH_CREATED
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
                                            description = InputConstants.RESP_ERROR_DUPLICATE_BRANCH,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_DUPLICATE_BRANCH_JSON
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = InputConstants.BRANCHES_PATH_UPDATE_NAME,
                    beanClass = BranchHandler.class,
                    beanMethod = InputConstants.METHOD_UPDATE_BRANCH_NAME,
                    method = RequestMethod.PUT,
                    operation = @Operation(
                            operationId = InputConstants.OP_UPDATE_BRANCH_NAME,
                            summary = InputConstants.SUMMARY_UPDATE_BRANCH_NAME,
                            description = InputConstants.DESC_UPDATE_BRANCH_NAME,
                            parameters = {
                                    @Parameter(
                                            name = InputConstants.BRANCH_ID,
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = InputConstants.DESC_PARAM_BRANCH_ID_UPDATE_NAME,
                                            schema = @Schema(type = InputConstants.TYPE_INTENGER, format = InputConstants.TYPE_INT64)
                                    )
                            },
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = NameUpdateRequest.class),
                                            examples = @ExampleObject(
                                                    name = InputConstants.EXAMPLE_NAME_UPDATE_BRANCH,
                                                    value = InputConstants.EXAMPLE_UPDATE_BRANCH
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_200,
                                            description = InputConstants.RESP_BRANCH_NAME_UPDATED
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
                                            description = InputConstants.RESP_ERROR_BRANCH_NOT_FOUND,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_BRANCH_NOT_FOUND_JSON
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_409,
                                            description = InputConstants.RESP_ERROR_DUPLICATE_BRANCH,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_DUPLICATE_BRANCH_JSON
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return RouterFunctions.route()
                .POST(InputConstants.BRANCHES_PATH, handler::createBranch)
                .PUT(InputConstants.BRANCHES_PATH_UPDATE_NAME, handler::updateName)
                .build();
    }
}
