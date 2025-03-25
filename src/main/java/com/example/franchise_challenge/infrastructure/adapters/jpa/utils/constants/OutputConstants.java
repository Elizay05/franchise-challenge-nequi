package com.example.franchise_challenge.infrastructure.adapters.jpa.utils.constants;

public class OutputConstants {

    public static final String QUERY_TO_GET_MOST_STOCKED_PRODUCTS_BY_FRANCHISE_ID =
            """
            SELECT p.name, pb.stock, b.name AS branch_name
            FROM product_branch pb
            JOIN product p ON pb.product_id = p.id
            JOIN branch b ON pb.branch_id = b.id
            WHERE pb.stock = (
                SELECT MAX(stock) FROM product_branch WHERE branch_id = b.id
            )
            AND b.franchise_id = :franchiseId
            """;
    public static final String QUERY_TO_UPDATE_PRODUCT_NAME =
            "UPDATE product p SET p.name = :name WHERE p.id = :productId";
    public static final String QUERY_TO_UPDATE_FRANCHISE_NAME =
            "UPDATE franchise f SET f.name = :name WHERE f.id = :franchiseId";
    public static final String QUERY_TO_UPDATE_BRANCH_NAME =
            "UPDATE branch b SET b.name = :name WHERE b.id = :branchId";
    public static final String QUERY_TO_UPDATE_STOCK =
            "UPDATE product_branch SET stock = :stock WHERE product_id = :productId AND branch_id = :branchId";
    public static final String PARAM_QUERY_TO_UPDATE_STOCK_PRODUCT_ID = "productId";
    public static final String PARAM_QUERY_TO_UPDATE_STOCK_BRANCH_ID = "branchId";
    public static final String PARAM_QUERY_TO_UPDATE_STOCK_STOCK = "stock";
}
