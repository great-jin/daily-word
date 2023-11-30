package xyz.ibudai.common;

import lombok.Data;

@Data
public class ResponseData {

    private int code;

    private String message;

    private Object data;

    public static ResponseData success(Object data) {
        ResponseData response = new ResponseData();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static ResponseData failed(String message) {
        ResponseData response = new ResponseData();
        response.setCode(500);
        response.setMessage(message);
        response.setData(null);
        return response;
    }
}
