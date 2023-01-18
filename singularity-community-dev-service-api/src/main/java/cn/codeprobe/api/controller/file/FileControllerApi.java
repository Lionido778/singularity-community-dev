package cn.codeprobe.api.controller.file;

import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lionido
 */
@Api(value = "文件服务相关接口", tags = "文件服务相关接口")
@RequestMapping("/file")
public interface FileControllerApi {

    /**
     * 用户上传头像，核通过后，将图像链接返回给前端
     *
     * @param userId 用户ID
     * @param file   用户上传的头像
     * @return face_url 图像链接
     */
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    @PostMapping("/uploadFace")
    JsonResult uploadFace(@RequestParam String userId, MultipartFile file);

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

    /**
     * 用户上传多张图片，审核通过后，将图像链接列表返回给前端
     *
     * @param userId 用户ID
     * @param files  用户上传的头像
     * @return face_url 图像链接
     */
    @ApiOperation(value = "用户上传多张图片", notes = "用户上传多张图片", httpMethod = "POST")
    @PostMapping("/uploadSerialsFiles")
    JsonResult uploadSerialsFiles(@RequestParam String userId, MultipartFile[] files);

}
