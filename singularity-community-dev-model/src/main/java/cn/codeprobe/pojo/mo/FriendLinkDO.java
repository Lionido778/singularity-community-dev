package cn.codeprobe.pojo.mo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * MongoDB 对应 PO
 *
 * @author Lionido
 */
@Document(value = "friend_link")
public class FriendLinkDO {

    private String id;
    @Field(value = "link_name")
    private String linkName;
    @Field(value = "link_url")
    private String linkUrl;
    @Field(value = "is_delete")
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
