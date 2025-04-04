package com.example.franchise_challenge.infrastructure.entrypoints.util.constants;

import com.example.franchise_challenge.domain.utils.constants.DomainConstants;

public class InputConstants {

    // PATHS
    public static final String FRANCHISES_PATH = "/api/v1/franchises";
    public static final String BRANCHES_PATH = "/api/v1/branches";
    public static final String PRODUCTS_PATH = "/api/v1/products";
    public static final String PRODUCTS_PATH_DELETE = "/api/v1/products/{productId}/branch/{branchId}";
    public static final String PRODUCTS_PATH_UPDATE_STOCK = "/api/v1/products/{productId}/branch/{branchId}/stock";
    public static final String PRODUCTS_STOCK_PATH = "/api/v1/products/franchise/{franchiseId}";
    public static final String PRODUCTS_PATH_UPDATE_NAME = "/api/v1/products/{productId}/name";
    public static final String BRANCHES_PATH_UPDATE_NAME = "/api/v1/branches/{branchId}/name";
    public static final String FRANCHISES_PATH_UPDATE_NAME = "/api/v1/franchises/{franchiseId}/name";

    // PARAMETROS
    public static final String PRODUCT_ID = "productId";
    public static final String BRANCH_ID = "branchId";
    public static final String FRANCHISE_ID = "franchiseId";

    // DESCRIPCION PARAMETROS
    public static final String DESC_PARAM_FRANCHISE_ID_UPDATE_NAME = "ID de la franquicia a actualizar";
    public static final String DESC_PARAM_BRANCH_ID_UPDATE_NAME = "ID de la sucursal a actualizar";
    public static final String DESC_PARAM_PRODUCT_ID_DELETE_PRODUCT = "ID del producto a eliminar";
    public static final String DESC_PARAM_BRANCH_ID_DELETE_PRODUCT = "ID de la sucursal donde se encuentra el producto a eliminar";
    public static final String DESC_PARAM_PRODUCT_ID_UPDATE_STOCK_PRODUCT = "ID del producto con stock a actualizar";
    public static final String DESC_PARAM_BRANCH_ID_UPDATE_STOCK_PRODUCT = "ID de la sucursal donde se encuentra el producto con stock a actualizar";
    public static final String DESC_PARAM_FRANCHISE_ID_GET_PRODUCTS_STOCK = "ID de la franquicia a consultar";
    public static final String DESC_PARAM_PRODUCT_ID_UPDATE_NAME = "ID del producto a actualizar";


    // METODOS
    public static final String METHOD_CREATE_FRANCHISE = "createFranchise";
    public static final String METHOD_UPDATE_FRANCHISE_NAME = "updateName";
    public static final String METHOD_CREATE_BRANCH = "createBranch";
    public static final String METHOD_UPDATE_BRANCH_NAME = "updateName";
    public static final String METHOD_CREATE_PRODUCT = "createProduct";
    public static final String METHOD_DELETE_PRODUCT = "deleteProduct";
    public static final String METHOD_UPDATE_PRODUCT_STOCK = "updateStock";
    public static final String METHOD_GET_PRODUCTS_STOCK_BY_FRANCHISE = "getProducts";
    public static final String METHOD_UPDATE_PRODUCT_NAME = "updateName";

    // OPERACIONES
    public static final String OP_CREATE_FRANCHISE = "createFranchise";
    public static final String OP_UPDATE_FRANCHISE_NAME = "updateFranchiseName";
    public static final String OP_CREATE_BRANCH = "createBranch";
    public static final String OP_UPDATE_BRANCH_NAME = "updateBranchName";
    public static final String OP_CREATE_PRODUCT = "createProduct";
    public static final String OP_DELETE_PRODUCT = "deleteProduct";
    public static final String OP_UPDATE_PRODUCT_STOCK = "updateStock";
    public static final String OP_GET_PRODUCTS_STOCK_BY_FRANCHISE = "getProducts";
    public static final String OP_UPDATE_PRODUCT_NAME = "updateProductName";


    // RESÚMENES Y DESCRIPCIONES
    public static final String SUMMARY_CREATE_FRANCHISE = "Crear una nueva franquicia";
    public static final String DESC_CREATE_FRANCHISE = "Registra una nueva franquicia en el sistema si no existe.";
    public static final String SUMMARY_UPDATE_FRANCHISE_NAME = "Actualizar el nombre de una franquicia";
    public static final String DESC_UPDATE_FRANCHISE_NAME = "Modifica el nombre de una franquicia existente si esta existe y el nuevo nombre no está en uso.";
    public static final String SUMMARY_CREATE_BRANCH = "Crear una nueva sucursal en una franquicia";
    public static final String DESC_CREATE_BRANCH = "Registra una nueva sucursal en una franquicia existente.";
    public static final String SUMMARY_UPDATE_BRANCH_NAME = "Actualizar el nombre de una sucursal";
    public static final String DESC_UPDATE_BRANCH_NAME = "Modifica el nombre de una sucursal existente si esta existe y el nuevo nombre no está en uso.";
    public static final String SUMMARY_CREATE_PRODUCT = "Crear un nuevo producto";
    public static final String DESC_CREATE_PRODUCT = "Registra un nuevo producto en una sucursal si este no existe y el stock es válido.";
    public static final String SUMMARY_DELETE_PRODUCT = "Eliminar un producto de una sucursal";
    public static final String DESC_DELETE_PRODUCT = "Elimina un producto de una sucursal específica si existe en el sistema.";
    public static final String SUMMARY_UPDATE_PRODUCT_STOCK = "Actualizar el stock de un producto en una sucursal";
    public static final String DESC_UPDATE_PRODUCT_STOCK = "Modifica la cantidad en stock de un producto en una sucursal específica si ambos existen en el sistema.";
    public static final String REQ_BODY_UPDATE_PRODUCT_STOCK = "Cuerpo de la solicitud para actualizar el stock de un producto.";
    public static final String SUMMARY_GET_PRODUCTS_STOCK_BY_FRANCHISE = "Obtener los productos que mas stock tienen por sucursal de una franquicia";
    public static final String DESC_GET_PRODUCTS_STOCK_BY_FRANCHISE = "Devuelve la lista de productos que mas stock tienen por sucursal de una franquicia especifica";
    public static final String SUMMARY_UPDATE_PRODUCT_NAME = "Actualizar el nombre de un producto";
    public static final String DESC_UPDATE_PRODUCT_NAME = "Modifica el nombre de un producto existente si este existe y el nuevo nombre no está en uso.";


    // CÓDIGOS DE RESPUESTA
    public static final String CODE_201 = "201";
    public static final String CODE_400 = "400";
    public static final String CODE_409 = "409";
    public static final String CODE_204 = "204";
    public static final String CODE_404 = "404";
    public static final String CODE_200 = "200";

    // MENSAJES DE RESPUESTA
    public static final String RESP_FRANCHISE_CREATED = "Franquicia creada exitosamente";
    public static final String RESP_ERROR_VALIDATION = "Error en la validación de datos";
    public static final String RESP_ERROR_DUPLICATE_FRANCHISE = "La franquicia ya existe";
    public static final String RESP_FRANCHISE_NAME_UPDATED = "Nombre de la franquicia actualizado exitosamente";
    public static final String RESP_ERROR_FRANCHISE_NOT_FOUND = "La franquicia no existe";
    public static final String RESP_BRANCH_CREATED = "Sucursal creada exitosamente";
    public static final String RESP_ERROR_DUPLICATE_BRANCH = "La sucursal ya existe";
    public static final String RESP_BRANCH_NAME_UPDATED = "Nombre de la sucursal actualizado exitosamente";
    public static final String RESP_ERROR_BRANCH_NOT_FOUND = "La sucursal no existe";
    public static final String RESP_PRODUCT_CREATED = "Producto creado exitosamente";
    public static final String RESP_ERROR_DUPLICATE_PRODUCT = "El producto ya existe";
    public static final String RESP_PRODUCT_DELETED = "Producto eliminado exitosamente";
    public static final String RESP_ERROR_PRODUCT_NOT_FOUND = "El producto no existe en la sucursal";
    public static final String RESP_ERROR_PRODUCT_OR_BRANCH_NOT_FOUND = "La sucursal no existe o el producto no existe en la sucursal";
    public static final String RESP_SUCCESS_UPDATE_PRODUCT_STOCK = "Stock del producto actualizado exitosamente";
    public static final String RESP_ERROR_INVALID_STOCK = "La cantidad de stock proporcionada no es válida";
    public static final String RESP_GET_PRODUCTS_STOCK_BY_FRANCHISE = "Lista de productos con mayor stock por sucursal de una franquicia";
    public static final String RESP_PRODUCT_NAME_UPDATED = "Nombre de el producto actualizado exitosamente";

    // EJEMPLOS JSON
    public static final String EXAMPLE_NAME_CREATE_FRANCHISE = "Ejemplo de creación de franquicia";
    public static final String EXAMPLE_CREATE_FRANCHISE = "{ \"name\": \"Franquicia Ejemplo\" }";
    public static final String EXAMPLE_ERROR_VALIDATION = "{ \"error\": \"El nombre es requerido\" }";
    public static final String EXAMPLE_ERROR_DUPLICATE_FRANCHISE_JSON = "{\"message\":\"" + DomainConstants.FRANCHISE_ALREADY_EXISTS + "\",\"status\":\"" + CODE_409 + "\",\"timestamp\":\"" + "2022-09-30T00:00:00.000" + "\"}";
    public static final String EXAMPLE_NAME_UPDATE_FRANCHISE = "Ejemplo de actualización de nombre de franquicia";
    public static final String EXAMPLE_UPDATE_FRANCHISE = "{ \"name\": \"Nueva Franquicia\" }";
    public static final String EXAMPLE_ERROR_FRANCHISE_NOT_FOUND_JSON = "{ \"message\": \"La franquicia con ID 1 no existe\", \"status\": \"404\", \"timestamp\": \"2022-09-30T00:00:00.000\" }";
    public static final String EXAMPLE_NAME_CREATE_BRANCH = "Ejemplo de creación de sucursal";
    public static final String EXAMPLE_CREATE_BRANCH = "{ \"name\": \"Sucursal Ejemplo\", \"franchiseId\": 1 }";
    public static final String EXAMPLE_ERROR_DUPLICATE_BRANCH_JSON = "{ \"message\": \"La sucursal ya existe\", \"status\": \"409\", \"timestamp\": \"2022-09-30T00:00:00.000\" }";
    public static final String EXAMPLE_NAME_UPDATE_BRANCH = "Ejemplo de actualización de nombre de sucursal";
    public static final String EXAMPLE_UPDATE_BRANCH = "{ \"name\": \"Nueva Sucursal\" }";
    public static final String EXAMPLE_ERROR_BRANCH_NOT_FOUND_JSON = "{ \"message\": \"La sucursal con ID 1 no existe\", \"status\": \"404\", \"timestamp\": \"2022-09-30T00:00:00.000\" }";
    public static final String EXAMPLE_NAME_CREATE_PRODUCT = "Ejemplo de creación de producto";
    public static final String EXAMPLE_CREATE_PRODUCT = "{ \"name\": \"Cerveza\", \"branchId\": 1, \"stock\": 10 }";
    public static final String EXAMPLE_ERROR_DUPLICATE_PRODUCT_JSON = "{ \"message\": \"El producto ya existe\", \"status\": \"409\", \"timestamp\": \"2022-09-30T00:00:00.000\" }";
    public static final String EXAMPLE_ERROR_PRODUCT_NOT_FOUND_JSON = "{ \"message\": \"El producto no se encuentra en la sucursal\", \"status\": \"404\", \"timestamp\": \"2022-09-30T00:00:00.000\" }";
    public static final String NAME_EXAMPLE_OBJECT_BRANCH_NOT_FOUND_JSON = "Sucursal no encontrada";
    public static final String NAME_EXAMPLE_OBJECT_PRODUCT_NOT_FOUND_IN_BRANCH_JSON = "El producto no se encuentra en la sucursal";
    public static final String NAME_EXAMPLE_OBJECT_UPDATE_STOCK_JSON = "Ejemplo de actualización de stock";
    public static final String EXAMPLE_UPDATE_PRODUCT_STOCK_JSON = "{ \"stock\": 20 }";
    public static final String NAME_EXAMPLE_OBJECT_INVALID_STOCK_JSON = "Ejemplo de error de stock inválido";
    public static final String EXAMPLE_ERROR_INVALID_STOCK_JSON = "{ \"message\": \"El stock debe ser un número positivo\", \"status\": \"400\", \"timestamp\": \"2022-09-30T00:00:00.000\" }";
    public static final String EXAMPLE_NAME_GET_PRODUCTS_STOCK_BY_FRANCHISE = "Ejemplo lista de productos con mayor stock por sucursal de una franquicia";
    public static final String EXAMPLE_GET_PRODUCTS_STOCK_BY_FRANCHISE_JSON = "{ \"franchiseId\": 1, \"products\": [ { \"name\": \"Producto A\", \"branchName\": \"Sucursal X\", \"stock\": 50 } , { \"name\": \"Producto B\", \"branchName\": \"Sucursal Y\", \"stock\": 30 } ] }";
    public static final String EXAMPLE_NAME_UPDATE_PRODUCT = "Ejemplo de actualización de nombre de un producto";
    public static final String EXAMPLE_UPDATE_PRODUCT = "{ \"name\": \"Nuevo Producto\" }";

    // ERROR
    public static final String ERROR = "error";
    public static final String TYPE_INTENGER = "integer";
    public static final String TYPE_INT64 = "int64";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    // VALIDATION CONSTANTS
    public static final int MIN_STOCK = 0;
    public static final String MIN_STOCK_MESSAGE = "El stock debe ser mayor a 0";
    public static final String IS_REQUIRED = "Es un campo requerido";
}
