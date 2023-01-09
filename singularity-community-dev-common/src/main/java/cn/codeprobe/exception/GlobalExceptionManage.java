package cn.codeprobe.exception;

import cn.codeprobe.enums.ResponseStatusEnum;

/**
 * 管理全局异常
 * 目的：解耦
 *
 * @author Lionido
 */
public class GlobalExceptionManage {

    public static void internal(ResponseStatusEnum responseStatusEnum) {
        throw new CustomException(responseStatusEnum);
    }

}
