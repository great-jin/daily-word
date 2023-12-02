package xyz.ibudai.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultData<T> {

    private int code;

    private String msg;

    private T data;

}
