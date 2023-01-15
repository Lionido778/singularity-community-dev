package cn.codeprobe.pojo.mo;

import cn.codeprobe.validate.CheckUrl;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;

/**
 * @author Lionido
 */
public class FriendLinkMO {

    private String id;

    @NotBlank(message = "链接名称不可以为空！")
    @Field(value = "link_name")
    private String linkName;

    @NotBlank(message = "链接地址不可以为空！")
    @CheckUrl
    @Field(value = "link_url")
    private String linkUrl;

    @NotBlank(message = "请选择保留或者是删除！")
    @Field(value = "is_delete")
    private int isDelete;

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
        return "FriendLinkMO{" +
                "id='" + id + '\'' +
                ", linkName='" + linkName + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}
