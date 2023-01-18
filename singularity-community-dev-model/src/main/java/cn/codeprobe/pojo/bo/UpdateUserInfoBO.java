package cn.codeprobe.pojo.bo;

import cn.codeprobe.validate.CheckUrl;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 用户账户信息更新表单
 *
 * @author Lionido
 */

public class UpdateUserInfoBO {

    @NotBlank(message = "用户ID不能为空")
    private String id;

    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 12, message = "用户昵称不能超过12位")
    private String nickname;

    @NotBlank(message = "用户头像不能为空")
    @CheckUrl
    private String face;

    @NotBlank(message = "真实姓名不能为空")
    private String realname;

    @Email
    @NotBlank(message = "邮件不能为空")
    private String email;

    @NotNull(message = "请选择一个性别")
    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 1, message = "性别选择不正确")
    private Integer sex;

    /**
     * 解决前端日期传到后端后是 字符串，将其转换为Date类型
     */
    @NotNull(message = "请选择生日日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;

    @NotBlank(message = "请选择所在城市")
    private String province;

    @NotBlank(message = "请选择所在城市")
    private String city;

    @NotBlank(message = "请选择所在城市")
    private String district;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "UpdateUserInfoBO{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", face='" + face + '\'' +
                ", realname='" + realname + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
