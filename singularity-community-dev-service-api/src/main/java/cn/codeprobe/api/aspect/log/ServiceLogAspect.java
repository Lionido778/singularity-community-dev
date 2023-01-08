package cn.codeprobe.api.aspect.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 记录 所有cn.codeprobe包下的 service包的所有 方法执行时间，并当执行时间大于限制值时，在控制台答应警告或错误日志
 *
 * @author Lionido
 */
@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    private static final String DESCRIBE = "当前方法 {} 执行耗时：{}";
    private static final int MAX_LIMIT = 3000;
    private static final int MEDIUM_LIMIT = 2000;

    /**
     * @Description: 环绕 切面表达式   (*任意返回参数  主包.所有微服务.微服务.Impl..所有包.所有类.所有方法（所有参数）)
     */
    @Around("execution(* cn.codeprobe.*.service.impl..*.*(..))")
    public Object recordExecTimeOfService(ProceedingJoinPoint joinPoint) throws Throwable {

        logger.info("==== 开始执行 {} 类下的 {} 方法 ====", joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        long start = System.currentTimeMillis();
        // 执行原方法并返回结果
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long consumedTime = end - start;
        if (consumedTime >= MAX_LIMIT) {
            logger.error(DESCRIBE, joinPoint.getSignature().getName(), consumedTime);
        } else if (consumedTime >= MEDIUM_LIMIT) {
            logger.error(DESCRIBE, joinPoint.getSignature().getName(), consumedTime);
        } else {
            logger.error(DESCRIBE, joinPoint.getSignature().getName(), consumedTime);
        }
        return result;
    }

}
