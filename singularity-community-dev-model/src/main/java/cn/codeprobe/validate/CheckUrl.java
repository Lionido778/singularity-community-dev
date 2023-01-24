package cn.codeprobe.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解：检查url地址
 *
 * @author Lionido
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckUrlValidate.class)
public @interface CheckUrl {

    String message() default "url格式不正确！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}