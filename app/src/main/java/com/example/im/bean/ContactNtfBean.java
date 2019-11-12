package com.example.im.bean;

public class ContactNtfBean {
    /**
     * 三种选择:"Request", "AcceptResponse", "RejectResponse"
     */
    private String operation;
    /**
     * 发出通知的用户 Id
     */
    private String sourceUserId;
    /**
     * 接收通知的id
     */
    private String targetUserId;
    /**
     * 表示请求或者响应消息，如添加理由或拒绝理由
     */
    private String message;

    public ContactNtfBean(String operation, String sourceUserId, String targetUserId, String message) {
        this.operation = operation;
        this.sourceUserId = sourceUserId;
        this.targetUserId = targetUserId;
        this.message = message;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
