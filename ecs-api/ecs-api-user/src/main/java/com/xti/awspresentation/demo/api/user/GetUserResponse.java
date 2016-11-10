package com.xti.awspresentation.demo.api.user;

public class GetUserResponse {

    private String taskId;
    private String userId;

    public GetUserResponse() {
    }

    public GetUserResponse(String taskId, String userId) {
        this.taskId = taskId;
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
