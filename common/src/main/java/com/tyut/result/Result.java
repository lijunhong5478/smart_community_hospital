package com.tyut.result;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static Result<String> success() {
        Result<String> result = new Result<>();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("操作成功");
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.ERROR);
        result.setMessage(message);
        return result;
    }
}
