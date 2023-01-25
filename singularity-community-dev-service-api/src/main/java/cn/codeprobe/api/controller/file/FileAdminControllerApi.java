package cn.codeprobe.api.controller.file;

import org.springframework.web.bind.annotation.*;

import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 管理中心：文件服务相关接口
 *
 * @author Lionido
 */
@Api(value = "文件服务相关接口", tags = "文件服务相关接口")
@RequestMapping("/admin/file")
public interface FileAdminControllerApi {

    /**
     * 上传用户人脸图片至 mongodb gridFS
     *
     * @param newAdminBO img64 人脸数据
     * @return faceId
     */
    @ApiOperation(value = "上传人脸图像", notes = "上传人脸图像", httpMethod = "POST")
    @PostMapping("/uploadToGridFS")
    JsonResult uploadToGridFs(@RequestBody NewAdminBO newAdminBO);

    /**
     * 从GridFS中读取人脸头像
     *
     * @param faceId 人脸识别ID
     */
    @ApiOperation(value = "查看用户头像", notes = "查看用户头像", httpMethod = "GET")
    @GetMapping("/readFromGridFS")
    void readFaceFromGridFs(@RequestParam String faceId);

    /**
     * 从GridFS中读取人脸头像，并转换为img64格式
     *
     * @param faceId 人脸ID
     * @return file
     */
    @ApiOperation(value = "获取用户头像Base64格式", notes = "获取用户头像Base64格式", httpMethod = "GET")
    @GetMapping("/readBase64FromGridFS")
    JsonResult readBase64FaceFromGridFs(@RequestParam String faceId);

}
