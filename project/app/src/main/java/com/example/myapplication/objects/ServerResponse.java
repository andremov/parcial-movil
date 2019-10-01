package com.example.myapplication.objects;

public class ServerResponse {

    private boolean success;
    private String errorMsg;
    private String data;
    private final int status;

    public ServerResponse(boolean success, String errorMsg, String data, int status) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.data = data;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
