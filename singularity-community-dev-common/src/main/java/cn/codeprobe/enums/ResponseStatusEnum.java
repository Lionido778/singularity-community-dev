package cn.codeprobe.enums;

/**
 * 响应结果枚举，用于提供给GraceJSONResult返回给前端的 本枚举类中包含了很多的不同的状态码供使用，可以自定义，便于更优雅的对状态码进行管理，一目了然
 *
 * @author Lionido
 */

public enum ResponseStatusEnum {

    /**
     * 后端接口访问成功、失败
     */
    SUCCESS(200, true, "操作成功！"), FAILED(500, false, "操作失败！"),

    /**
     * 50x
     */
    UN_LOGIN(501, false, "请登录后再继续操作！"), TICKET_INVALID(502, false, "会话失效，请重新登录！"),
    NO_AUTH(503, false, "您的权限不足，无法继续操作！"), SMS_CODE_SEND_ERROR(504, false, "短信发送失败，请稍后重试！"),
    SMS_NEED_WAIT_ERROR(505, false, "短信发送太快啦~请稍后再试！"), SMS_CODE_ERROR(506, false, "验证码过期或不匹配，请稍后再试！"),
    SMS_MOBILE_BLANK(517, false, "手机号码不可以为空！"), SMS_MOBILE_FORMAT_ERROR(519, false, "手机号码格式错误！"),
    USER_FROZEN(507, false, "用户已被冻结，请联系管理员！"), USER_UPDATE_ERROR(508, false, "用户信息更新失败，请联系管理员！"),
    USER_INACTIVE_ERROR(509, false, "请前往[账号设置]修改信息激活后再进行后续操作！"),
    FILE_UPLOAD_NULL_ERROR(510, false, "文件不能为空，请选择一个文件再上传！"), FILE_UPLOAD_FAILED(511, false, "文件上传失败！"),
    FILE_FORMATTER_FAILED(512, false, "文件图片格式不支持！"), FILE_MAX_SIZE_ERROR(513, false, "仅支持500kb大小以下的图片上传！"),
    FILE_NOT_EXIST_ERROR(514, false, "你所查看的文件不存在！"), USER_STATUS_ERROR(515, false, "用户状态参数出错！"),
    USER_NOT_EXIST_ERROR(516, false, "用户不存在！"), USER_QUERY_ERROR(517, false, "获取用户信息失败！"),
    USER_OPERATION_ERROR(518, false, "操作失败！"), USER_LOGIN_FAILED_COOKIE_ERROR(518, false, "用户登陆失败！cookie 失败！"),

    /**
     * 自定义系统级别异常 54x
     */
    SYSTEM_INDEX_OUT_OF_BOUNDS(541, false, "系统错误，数组越界！"), SYSTEM_ARITHMETIC_BY_ZERO(542, false, "系统错误，无法除零！"),
    SYSTEM_NULL_POINTER(543, false, "系统错误，空指针！"), SYSTEM_NUMBER_FORMAT(544, false, "系统错误，数字转换异常！"),
    SYSTEM_PARSE(545, false, "系统错误，解析异常！"), SYSTEM_IO(546, false, "系统错误，IO输入输出异常！"),
    SYSTEM_FILE_NOT_FOUND(547, false, "系统错误，文件未找到！"), SYSTEM_CLASS_CAST(548, false, "系统错误，类型强制转换错误！"),
    SYSTEM_PARSER_ERROR(549, false, "系统错误，解析出错！"), SYSTEM_DATE_PARSER_ERROR(550, false, "系统错误，日期解析出错！"),

    /**
     * admin 管理系统 56x
     */
    ADMIN_USERNAME_NULL_ERROR(561, false, "管理员登录名不能为空！"), ADMIN_USERNAME_EXIST_ERROR(562, false, "管理员登录名已存在！"),
    ADMIN_NAME_NULL_ERROR(563, false, "管理员负责人不能为空！"), ADMIN_PASSWORD_ERROR(564, false, "密码不能为空后者两次输入不一致！"),
    ADMIN_CREATE_ERROR(565, false, "添加管理员失败！"), ADMIN_PASSWORD_NULL_ERROR(566, false, "密码不能为空！"),
    ADMIN_NOT_EXIT_ERROR(567, false, "管理员不存在或密码错误！"), ADMIN_FACE_NULL_ERROR(568, false, "人脸信息不能为空！"),
    ADMIN_FACE_LOGIN_ERROR(569, false, "人脸识别失败，请重试！"), CATEGORY_EXIST_ERROR(570, false, "文章分类已存在，请换一个分类名！"),
    ADMIN_ALL_NULL_ERROR(571, false, "登录参数不可以为空！"), ADMIN_PAGE_NULL_ERROR(572, false, "查询分页参数不可以为空！"),
    ADMIN_QUERY_ERROR(573, false, "管理员列表查询失败！"), ADMIN_FACE_NOT_EXIST_ERROR(574, false, "该管理员未录入人脸数据！"),
    ADMIN_NOT_EXIT_FACE_ERROR(575, false, "管理员不存在或人脸识别失败！"), ADMIN_CATEGORY_ADD_FAILED(576, false, "分类添加失败！"),
    ADMIN_CATEGORY_DELETE_FAILED(577, false, "分类删除失败！"), ADMIN_CATEGORY_IS_EXISTED(578, false, "该分类已存在！"),
    ADMIN_FRIEND_LINK_IS_EXISTED(579, false, "该友情链接已存在！"),

    /**
     * 媒体中心 相关错误 58x
     */
    ARTICLE_COVER_NOT_EXIST_ERROR(590, false, "文章封面不存在，请选择一个！"),
    ARTICLE_CATEGORY_NOT_EXIST_ERROR(591, false, "请选择正确的文章领域！"), ARTICLE_CREATE_ERROR(592, false, "创建文章失败，请重试或联系管理员！"),
    ARTICLE_QUERY_PARAMS_ERROR(593, false, "文章列表查询参数错误！"), ARTICLE_DELETE_ERROR(594, false, "文章删除失败！"),
    ARTICLE_WITHDRAW_ERROR(595, false, "文章撤回失败！"), ARTICLE_REVIEW_ERROR(596, false, "文章审核出错！"),
    ARTICLE_ALREADY_READ_ERROR(597, false, "文章重复阅读！"), FANS_PARAMENT_ERROR(598, false, "查看粉丝参数出错！"),
    ARTICLE_VIEW_DETAIL_FAILED(599, false, "查看文章详细内容失败！"), ARTICLE_COMMENT_FAILED(600, false, "用户发表评论失败！"),
    ARTICLE_COMMENT_LIST_QUERY_FAILED(600, false, "文章评论列表获取失败！"),
    ARTICLE_COMMENT_DELETE_FAILED(601, false, "文章评论删除失败！"), ARTICLE_STATIC_FAILED(602, false, "文章静态化失败！"),
    ARTICLE_STATIC_PUBLISH_FAILED(603, false, "静态文章发布失败！"), ARTICLE_STATIC_DELETE_FAILED(604, false, "静态文章删除失败！"),
    RABBITMQ_ERROR(604, false, "消息队列消息出错！"), RABBITMQ_CONSUMER_DOWNLOAD_ERROR(605, false, "消费端下载失败！"),
    RABBITMQ_CONSUMER_DELETE_ERROR(606, false, "消费端删除失败！"), ARTICLE_APPOINT_PUBLISH_FAILED(607, false, "文章定时发布失败！"),

    /**
     * 人脸识别错误代码
     */
    FACE_VERIFY_TYPE_ERROR(650, false, "人脸比对验证类型不正确！"), FACE_VERIFY_LOGIN_ERROR(651, false, "人脸登录失败！"),

    /**
     * 404
     */
    NOT_FOUND_ERROR(404, false, "页面找不到！ "),
    /**
     * 系统错误，未预期的错误 555
     */
    SYSTEM_ERROR(555, false, "系统繁忙，请稍后再试！"), SYSTEM_OPERATION_ERROR(556, false, "操作失败，请重试或联系管理员"),
    SYSTEM_RESPONSE_NO_INFO(557, false, ""), SYSTEM_INTERNAL_ERROR(558, false, "系统内部错误，请联系管理员！"),
    SYSTEM_ERROR_ZUUL(559, false, "请求发送过于频繁，请稍后再试！"),

    /**
     * file system
     */
    FILE_DOWNLOAD_ERROR(701, false, "文件下载失败！"),

    /**
     * Auth
     */
    AUTH_FAILED(801, false, "登录鉴权失败！访问主体不存在"), AUTH_HEADER_ID_NULL(802, false, "登录鉴权失败！id,token为空"),

    /**
     * Auth
     */
    ARTICLE_PUBLISH_USER_ERROR(901, false, "文章用户信息获取失败"),

    /**
     * Fans
     */
    FANS_FOLLOW_PARAMENT_ERROR(1001, false, "粉丝关注参数错误！"), FANS_UN_FOLLOW_PARAMENT_ERROR(1002, false, "粉丝取消关注参数错误！"),
    FANS_FOLLOW_FAILED(1003, false, "粉丝关注失败！"), FANS_UN_FOLLOW_FAILED(1004, false, "粉丝取消关注失败！");

    /**
     * 业务响应状态码
     */
    private final Integer status;
    /**
     * 调用是否成功
     */
    private final Boolean success;
    /**
     * 响应消息
     */
    private final String msg;

    ResponseStatusEnum(Integer status, Boolean success, String msg) {
        this.status = status;
        this.success = success;
        this.msg = msg;
    }

    public Integer status() {
        return status;
    }

    public Boolean success() {
        return success;
    }

    public String msg() {
        return msg;
    }
}
