package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.AiPlatformDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelRespVO;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformRespVO;
import cn.javayong.magic.module.ai.service.AiPlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.javayong.magic.framework.common.pojo.CommonResult.success;

@Tag(name = "后台AI管理-平台配置")
@RestController("AdminAIPlatformController")
@RequestMapping("/ai/platform/")
@Slf4j
public class AdminAiPlatformController {

    @Resource
    private AiPlatformService aiPlatformService;

    @GetMapping("/page")
    @Operation(summary = "获得平台配置分页")
    @PreAuthorize("@ss.hasPermission('ai:platform:query')")
    public CommonResult<PageResult> getPlatformPage(@Valid AiPlatformPageReqVO pageReqVO) {
        PageResult<AiPlatformDO> pageResult = aiPlatformService.getModelPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AiPlatformRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ai:platform:query')")
    public CommonResult<AiPlatformRespVO> getModel(@RequestParam("id") Long id) {
        AiPlatformDO platformDO = aiPlatformService.getPlatform(id);
        return success(BeanUtils.toBean(platformDO, AiPlatformRespVO.class));
    }

}
