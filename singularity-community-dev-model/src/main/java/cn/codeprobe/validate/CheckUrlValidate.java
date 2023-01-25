package cn.codeprobe.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.codeprobe.utils.UrlUtil;

/**
 * 自定义 url校验
 *
 * @author Lionido
 */
public class CheckUrlValidate implements ConstraintValidator<CheckUrl, String> {

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        return UrlUtil.verifyUrlByUrl(url.trim());
    }
}
