package cn.codeprobe.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 管理员登录 BO
 *
 * @author Lionido
 */

@ApiModel
public class AdminLoginBO {

    @NotBlank(message = "登录名不可以为空！")
    @ApiModelProperty(notes = "管理员名称")
    private String username;

    @ApiModelProperty(notes = "登录密码")
    private String password;

    @ApiModelProperty(notes = "人脸识别")
    private String img64;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg64() {
        return img64;
    }

    public void setImg64(String img64) {
        this.img64 = img64;
    }

    @Override
    public String toString() {
        return "AdminLoginBO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", img64='" + img64 + '\'' +
                '}';
    }
}
