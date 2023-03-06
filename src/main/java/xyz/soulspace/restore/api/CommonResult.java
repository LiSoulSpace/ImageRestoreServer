package xyz.soulspace.restore.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResult<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(0, "", data);
    }

    public static <T> CommonResult<T> success(String msg, T data) {
        return new CommonResult<>(0, msg, data);
    }

    public static <T> CommonResult<T> failed(int code, String msg, T data) {
        return new CommonResult<>(code, msg, data);
    }

    public boolean isSuccess() {
        return code == 0;
    }

}
