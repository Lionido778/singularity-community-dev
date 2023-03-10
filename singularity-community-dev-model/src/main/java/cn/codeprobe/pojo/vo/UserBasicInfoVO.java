package cn.codeprobe.pojo.vo;

/**
 * 用户基本信息 VO
 *
 * @author Lionido
 */
public class UserBasicInfoVO {

    private String id;
    private String nickname;
    private String face;
    private Integer activeStatus;
    /** 关注数 */
    private String followCounts;
    /** 粉丝数 */
    private String fansCounts;

    public UserBasicInfoVO() {}

    public String getFollowCounts() {
        return followCounts;
    }

    public void setFollowCounts(String followCounts) {
        this.followCounts = followCounts;
    }

    public String getFansCounts() {
        return fansCounts;
    }

    public void setFansCounts(String fansCounts) {
        this.fansCounts = fansCounts;
    }

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

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    @Override
    public String toString() {
        return "UserInfoVO{" + "id='" + id + '\'' + ", nickname='" + nickname + '\'' + ", face='" + face + '\''
            + ", activeStatus=" + activeStatus + '}';
    }
}