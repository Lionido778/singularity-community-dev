package cn.codeprobe.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 用户注册登录 BO
 * 不使用 Lomback 的原因是，有可能与其他组件发生小bug，所以这里手动生成get set方法
 * 使用Validate 对前端数据进行校验
 */

@ApiModel
public class RegisterLoginBO {

    @ApiModelProperty(notes = "手机号")
    @NotBlank(message = "手机号不能为空！")
    private String mobile;

    @ApiModelProperty(notes = "短信验证码")
    @NotBlank(message = "短信验证码不可以为空！")
    private String smsCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "RegisterLoginBO{" +
                "mobile='" + mobile + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }
}
