package xyz.soulspace.restore.api;

public enum ResultCode implements IErrorCode {
    SUCCESS(0, ""),
    WRONG_PASSWORD(1, "密码错误"),
    NO_USERNAME(2, "用户名不存在");
    private final int code;
    private final String msg;

    ResultCode(int i, String s) {
        this.code = i;
        this.msg = s;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
