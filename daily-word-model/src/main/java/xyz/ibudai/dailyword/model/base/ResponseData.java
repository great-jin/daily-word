package xyz.ibudai.dailyword.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * The type Response data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData {

    private int code;

    private String message;

    private Object data;


    public ResponseData(int code) {
        this.code = code;
    }

    /**
     * Success response data.
     *
     * @param data the data
     * @return the response data
     */
    public static ResponseData success(Object data) {
        ResponseData response = new ResponseData();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    /**
     * Failed response data.
     *
     * @param message the message
     * @return the response data
     */
    public static ResponseData failed(String message) {
        ResponseData response = new ResponseData();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    /**
     * Denied response data.
     *
     * @param message the message
     * @return the response data
     */
    public static ResponseData denied(String message) {
        ResponseData response = new ResponseData();
        response.setCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
        response.setMessage(message);
        response.setData(null);
        return response;
    }
}
