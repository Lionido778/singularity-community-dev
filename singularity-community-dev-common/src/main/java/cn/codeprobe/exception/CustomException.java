package cn.codeprobe.exception;

import cn.codeprobe.enums.ResponseStatusEnum;

/**
 * @author Lionido
 * 自定义异常：来自服务内部的异常信息
 */
public class CustomException extends RuntimeException {

    private ResponseStatusEnum responseStatusEnum;

    public CustomException(ResponseStatusEnum responseStatusEnum) {
        super("异常状态码：" + responseStatusEnum.status() + "; 异常状态信息: " + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
