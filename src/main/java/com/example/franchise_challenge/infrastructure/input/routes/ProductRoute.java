package com.example.franchise_challenge.infrastructure.input.routes;

import com.example.franchise_challenge.application.dto.request.NameUpdateRequest;
import com.example.franchise_challenge.application.dto.request.ProductRequest;
import com.example.franchise_challenge.infrastructure.input.handler.ProductHandler;
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
public class ProductRoute {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = InputConstants.PRODUCTS_PATH,
                    beanClass = ProductHandler.class,
                    beanMethod = InputConstants.METHOD_CREATE_PRODUCT,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = InputConstants.OP_CREATE_PRODUCT,
                            summary = InputConstants.SUMMARY_CREATE_PRODUCT,
                            description = InputConstants.DESC_CREATE_PRODUCT,
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ProductRequest.class),
                                            examples = @ExampleObject(
                                                    name = InputConstants.EXAMPLE_NAME_CREATE_PRODUCT,
                                                    value = InputConstants.EXAMPLE_CREATE_PRODUCT
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_201,
                                            description = InputConstants.RESP_PRODUCT_CREATED
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
                                            description = InputConstants.RESP_ERROR_DUPLICATE_PRODUCT,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_DUPLICATE_PRODUCT_JSON
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = InputConstants.PRODUCTS_PATH_DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = InputConstants.METHOD_DELETE_PRODUCT,
                    method = RequestMethod.DELETE,
                    operation = @Operation(
                            operationId = InputConstants.OP_DELETE_PRODUCT,
                            summary = InputConstants.SUMMARY_DELETE_PRODUCT,
                            description = InputConstants.DESC_DELETE_PRODUCT,
                            parameters = {
                                    @Parameter(
                                            name = InputConstants.PRODUCT_ID,
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = InputConstants.DESC_PARAM_PRODUCT_ID_DELETE_PRODUCT,
                                            schema = @Schema(type = InputConstants.TYPE_INTENGER, format = InputConstants.TYPE_INT64)
                                    ),
                                    @Parameter(
                                            name = InputConstants.BRANCH_ID,
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = InputConstants.DESC_PARAM_BRANCH_ID_DELETE_PRODUCT,
                                            schema = @Schema(type = InputConstants.TYPE_INTENGER, format = InputConstants.TYPE_INT64)
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_204,
                                            description = InputConstants.RESP_PRODUCT_DELETED
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_404,
                                            description = InputConstants.RESP_ERROR_PRODUCT_OR_BRANCH_NOT_FOUND,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = InputConstants.NAME_EXAMPLE_OBJECT_PRODUCT_NOT_FOUND_IN_BRANCH_JSON,
                                                                    description = InputConstants.RESP_ERROR_PRODUCT_NOT_FOUND,
                                                                    value = InputConstants.EXAMPLE_ERROR_PRODUCT_NOT_FOUND_JSON
                                                            ),
                                                            @ExampleObject(
                                                                    name = InputConstants.NAME_EXAMPLE_OBJECT_BRANCH_NOT_FOUND_JSON,
                                                                    description = InputConstants.RESP_ERROR_BRANCH_NOT_FOUND,
                                                                    value = InputConstants.EXAMPLE_ERROR_BRANCH_NOT_FOUND_JSON
                                                            )
                                                    }
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = InputConstants.PRODUCTS_PATH_UPDATE_STOCK,
                    beanClass = ProductHandler.class,
                    beanMethod = InputConstants.METHOD_UPDATE_PRODUCT_STOCK,
                    method = RequestMethod.PUT,
                    operation = @Operation(
                            operationId = InputConstants.OP_UPDATE_PRODUCT_STOCK,
                            summary = InputConstants.SUMMARY_UPDATE_PRODUCT_STOCK,
                            description = InputConstants.DESC_UPDATE_PRODUCT_STOCK,
                            parameters = {
                                    @Parameter(
                                            name = InputConstants.PRODUCT_ID,
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = InputConstants.DESC_PARAM_PRODUCT_ID_UPDATE_STOCK_PRODUCT,
                                            schema = @Schema(type = InputConstants.TYPE_INTENGER, format = InputConstants.TYPE_INT64)
                                    ),
                                    @Parameter(
                                            name = InputConstants.BRANCH_ID,
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = InputConstants.DESC_PARAM_BRANCH_ID_UPDATE_STOCK_PRODUCT,
                                            schema = @Schema(type = InputConstants.TYPE_INTENGER, format = InputConstants.TYPE_INT64)
                                    )
                            },
                            requestBody = @RequestBody(
                                    description = InputConstants.REQ_BODY_UPDATE_PRODUCT_STOCK,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            examples = @ExampleObject(
                                                    name = InputConstants.NAME_EXAMPLE_OBJECT_UPDATE_STOCK_JSON,
                                                    description = InputConstants.RESP_SUCCESS_UPDATE_PRODUCT_STOCK,
                                                    value = InputConstants.EXAMPLE_UPDATE_PRODUCT_STOCK_JSON
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_204,
                                            description = InputConstants.RESP_SUCCESS_UPDATE_PRODUCT_STOCK
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_400,
                                            description = InputConstants.RESP_ERROR_INVALID_STOCK,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            name = InputConstants.NAME_EXAMPLE_OBJECT_INVALID_STOCK_JSON,
                                                            description = InputConstants.RESP_ERROR_INVALID_STOCK,
                                                            value = InputConstants.EXAMPLE_ERROR_INVALID_STOCK_JSON
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_404,
                                            description = InputConstants.RESP_ERROR_PRODUCT_OR_BRANCH_NOT_FOUND,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = InputConstants.NAME_EXAMPLE_OBJECT_PRODUCT_NOT_FOUND_IN_BRANCH_JSON,
                                                                    description = InputConstants.RESP_ERROR_PRODUCT_NOT_FOUND,
                                                                    value = InputConstants.EXAMPLE_ERROR_PRODUCT_NOT_FOUND_JSON
                                                            ),
                                                            @ExampleObject(
                                                                    name = InputConstants.NAME_EXAMPLE_OBJECT_BRANCH_NOT_FOUND_JSON,
                                                                    description = InputConstants.RESP_ERROR_BRANCH_NOT_FOUND,
                                                                    value = InputConstants.EXAMPLE_ERROR_BRANCH_NOT_FOUND_JSON
                                                            )
                                                    }
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = InputConstants.PRODUCTS_STOCK_PATH,
                    beanClass = ProductHandler.class,
                    beanMethod = InputConstants.METHOD_GET_PRODUCTS_STOCK_BY_FRANCHISE,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = InputConstants.OP_GET_PRODUCTS_STOCK_BY_FRANCHISE,
                            summary = InputConstants.SUMMARY_GET_PRODUCTS_STOCK_BY_FRANCHISE,
                            description = InputConstants.DESC_GET_PRODUCTS_STOCK_BY_FRANCHISE,
                            parameters = {
                                    @Parameter(
                                            name = InputConstants.FRANCHISE_ID,
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = InputConstants.DESC_PARAM_FRANCHISE_ID_GET_PRODUCTS_STOCK,
                                            schema = @Schema(type = InputConstants.TYPE_INTENGER, format = InputConstants.TYPE_INT64)
                                    ),
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_200,
                                            description = InputConstants.RESP_GET_PRODUCTS_STOCK_BY_FRANCHISE,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            name = InputConstants.EXAMPLE_NAME_GET_PRODUCTS_STOCK_BY_FRANCHISE,
                                                            value = InputConstants.EXAMPLE_GET_PRODUCTS_STOCK_BY_FRANCHISE_JSON
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
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = InputConstants.PRODUCTS_PATH_UPDATE_NAME,
                    beanClass = ProductHandler.class,
                    beanMethod = InputConstants.METHOD_UPDATE_PRODUCT_NAME,
                    method = RequestMethod.PUT,
                    operation = @Operation(
                            operationId = InputConstants.OP_UPDATE_PRODUCT_NAME,
                            summary = InputConstants.SUMMARY_UPDATE_PRODUCT_NAME,
                            description = InputConstants.DESC_UPDATE_PRODUCT_NAME,
                            parameters = {
                                    @Parameter(
                                            name = InputConstants.PRODUCT_ID,
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = InputConstants.DESC_PARAM_PRODUCT_ID_UPDATE_NAME,
                                            schema = @Schema(type = InputConstants.TYPE_INTENGER, format = InputConstants.TYPE_INT64)
                                    ),
                            },
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = NameUpdateRequest.class),
                                            examples = @ExampleObject(
                                                    name = InputConstants.EXAMPLE_NAME_UPDATE_PRODUCT,
                                                    value = InputConstants.EXAMPLE_UPDATE_PRODUCT
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_204,
                                            description = InputConstants.RESP_PRODUCT_NAME_UPDATED
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
                                            description = InputConstants.RESP_ERROR_PRODUCT_NOT_FOUND,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_PRODUCT_NOT_FOUND_JSON
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = InputConstants.CODE_409,
                                            description = InputConstants.RESP_ERROR_DUPLICATE_PRODUCT,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = InputConstants.EXAMPLE_ERROR_DUPLICATE_PRODUCT_JSON
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
                .POST(InputConstants.PRODUCTS_PATH, handler::createProduct)
                .DELETE(InputConstants.PRODUCTS_PATH_DELETE, handler::deleteProduct)
                .PUT(InputConstants.PRODUCTS_PATH_UPDATE_STOCK, handler::updateStock)
                .GET(InputConstants.PRODUCTS_STOCK_PATH, handler::getProducts)
                .PUT(InputConstants.PRODUCTS_PATH_UPDATE_NAME, handler::updateName)
                .build();
    }
}