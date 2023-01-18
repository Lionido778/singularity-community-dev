package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.AdminPassportService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.AdminUserDO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.utils.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Lionido
 */
@Service
public class AdminPassportServiceImpl extends AdminBaseService implements AdminPassportService {

    @Override
    public void loginByUsernameAndPwd(String username, String password) {
        AdminUserDO adminUserDO = queryAdminByUsername(username);
        if (adminUserDO != null) {
            String encryptedPwd = adminUserDO.getPassword();
            if (Boolean.TRUE.equals(isPasswordMatched(password, encryptedPwd))) {
                // 登陆成功，进行token、cookie配置
                adminLoggedSetting(adminUserDO);
            } else {
                GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    @Override
    public void loginByFace(String username, String img64Face) {
        AdminUserDO adminUserDO = queryAdminByUsername(username);
        if (adminUserDO != null) {
            String faceId = adminUserDO.getFaceId();
            if (CharSequenceUtil.isNotBlank(faceId)) {
                // 暂时使用 RestTemplate 调用 file 服务的下载保存在GridFS中的人脸数据（Base64）
                String accessFileServerUrl = FILE_SERVER_URL + faceId;
                ResponseEntity<JsonResult> responseEntity = restTemplate.getForEntity(accessFileServerUrl, JsonResult.class);
                JsonResult body = responseEntity.getBody();
                if (body != null) {
                    String img64FaceGridFs = (String) body.getData();
                    FileUtil.base64ToFile(FACE_TEMP_DIR + "/" + username, img64Face, LOGIN_FACE_NAME + EXTEND_NAME);
                    FileUtil.base64ToFile(FACE_TEMP_DIR + "/" + username, img64FaceGridFs, FACE_DATA_NAME + EXTEND_NAME);
                    String faceFile = FACE_TEMP_DIR + "/" + username + "/" + LOGIN_FACE_NAME + EXTEND_NAME;
                    String dataFile = FACE_TEMP_DIR + "/" + username + "/" + FACE_DATA_NAME + EXTEND_NAME;
                    try {
                        // 调用阿里云人脸识别接口，进行人脸识别登录
                        Boolean pass = faceVerifyUtil.verifyFace(TARGET_CONFIDENCE, faceFile, dataFile);
                        if (Boolean.TRUE.equals(pass)) {
                            // 人脸识别登陆成功，设置管理员登陆参数
                            adminLoggedSetting(adminUserDO);
                        } else {
                            // 人脸识别失败
                            GlobalExceptionManage.internal(ResponseStatusEnum.FACE_VERIFY_TYPE_ERROR);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    GlobalExceptionManage.internal(ResponseStatusEnum.FILE_DOWNLOAD_ERROR);
                }
            } else {
                GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_FACE_NOT_EXIST_ERROR);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_NOT_EXIT_FACE_ERROR);
        }
    }

    @Override
    public void adminLogout(String adminId) {
        // 删除 redis 中的token
        redisUtil.del(REDIS_ADMIN_TOKEN + ":" + adminId);
        // 删除 cookie
        setCookie(COOKIE_NAME_ADMIN_ID, "", COOKIE_TIME_OUT, domainName);
        setCookie(COOKIE_NAME_ADMIN_TOKEN, "", COOKIE_TIME_OUT, domainName);
        setCookie(COOKIE_NAME_ADMIN_NAME, "", COOKIE_TIME_OUT, domainName);
    }
}
