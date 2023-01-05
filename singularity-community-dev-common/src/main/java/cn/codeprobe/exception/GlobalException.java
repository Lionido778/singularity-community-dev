package cn.codeprobe.exception;

import cn.codeprobe.result.enums.ResponseStatusEnum;

/**
 * 统一全局异常
 * 目的：解耦
 */
public class GlobalException {

    public static void Internal(ResponseStatusEnum responseStatusEnum) {
        throw new CustomException(responseStatusEnum);
    }

}
