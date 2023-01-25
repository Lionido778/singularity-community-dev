package cn.codeprobe.api.controller.file;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 创作中心：文件服务相关接口
 *
 * @author Lionido
 */
@Api(value = "文件服务相关接口", tags = "文件服务相关接口")
@RequestMapping("/writer/file")
public interface FileWriterControllerApi {

    /**
     * 用户上传头像，审核核通过后，将图像访问链接返回给前端
     *
     * @param userId 用户ID
     * @param file 用户上传的头像
     * @return face_url 图像链接
     */
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    @PostMapping("/uploadFace")
    JsonResult uploadFace(@RequestParam String userId, MultipartFile file);

    /**
     * 创作中心： 用户上传多张图片，审核通过后，将图像链接列表返回给前端
     *
     * @param userId 用户ID
     * @param files 用户上传的头像
     * @return face_url 图像链接
     */
    @ApiOperation(value = "用户上传多张图片", notes = "用户上传多张图片", httpMethod = "POST")
    @PostMapping("/uploadSerialsFiles")
    JsonResult uploadSerialsFiles(@RequestParam String userId, MultipartFile[] files);

}
