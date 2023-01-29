package cn.codeprobe.pojo.bo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.codeprobe.validate.CheckUrl;

/**
 * @author Lionido
 */
public class NewFriendLinkBO {

    private String id;
    @NotBlank(message = "链接名称不可以为空！")
    private String linkName;
    @NotBlank(message = "链接地址不可以为空！")
    @CheckUrl
    private String linkUrl;
    @NotNull(message = "请选择一个性别")
    @Min(value = 0, message = "状态选择不正确")
    @Max(value = 1, message = "状态选择不正确")
    private Integer isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "FriendLinkMO{" + "id='" + id + '\'' + ", linkName='" + linkName + '\'' + ", linkUrl='" + linkUrl + '\''
            + ", isDelete=" + isDelete + '}';
    }
}
