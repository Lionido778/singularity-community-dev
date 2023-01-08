package cn.codeprobe.exception;

import cn.codeprobe.enums.ResponseStatusEnum;

/**
 * 统一全局异常
 * 目的：解耦
 *
 * @author Lionido
 */
public class GlobalException {

    public static void Internal(ResponseStatusEnum responseStatusEnum) {
        throw new CustomException(responseStatusEnum);
    }

}
