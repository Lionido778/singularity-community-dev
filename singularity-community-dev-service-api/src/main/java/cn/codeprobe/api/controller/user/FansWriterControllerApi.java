package cn.codeprobe.api.controller.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 创作中心：我的粉丝
 *
 * @author Lionido
 */
@Api(value = "我的所有粉丝信息相关接口", tags = "我的所有粉丝信息相关接口")
@RequestMapping("/writer/fans/")
public interface FansWriterControllerApi {

    /**
     * 查询作家的所有粉丝
     *
     * @param writerId 作者ID
     * @param page 当前页
     * @param pageSize 每页查询数量
     * @return JsonResult
     */
    @ApiOperation(value = "查询作家的所有粉丝", notes = "查询作家的所有粉丝", httpMethod = "POST")
    @PostMapping("/queryPageListFans")
    JsonResult queryPageListFans(@RequestParam String writerId, Integer page, Integer pageSize);

    /**
     * 根据性别查询作家的所有粉丝数
     *
     * @param writerId 作者ID
     * @return JsonResult
     */
    @ApiOperation(value = "根据性别查询作家的所有粉丝", notes = "根据性别查询作家的所有粉丝", httpMethod = "POST")
    @PostMapping("/queryRatioBySex")
    JsonResult queryRatioBySex(@RequestParam String writerId);

    /**
     * 根据地域查询作家的所有粉丝数
     *
     * @param writerId 作者ID
     * @return JsonResult
     */
    @ApiOperation(value = "根据地域查询作家的所有粉丝", notes = "根据地域查询作家的所有粉丝", httpMethod = "POST")
    @PostMapping("/queryRatioByRegion")
    JsonResult queryRatioByRegion(@RequestParam String writerId);

}
