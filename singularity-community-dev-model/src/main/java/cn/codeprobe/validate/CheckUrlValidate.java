package cn.codeprobe.validate;


import cn.codeprobe.utils.UrlUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


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
