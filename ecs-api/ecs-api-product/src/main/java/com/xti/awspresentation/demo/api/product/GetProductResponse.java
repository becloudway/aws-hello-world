package com.xti.awspresentation.demo.api.product;

public class GetProductResponse {

    private String taskId;
    private String productId;

    public GetProductResponse() {
    }

    public GetProductResponse(String taskId, String productId) {
        this.taskId = taskId;
        this.productId = productId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
