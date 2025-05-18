package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiModelRespVO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenPageReqVO;
import cn.javayong.magic.module.ai.service.AiModelService;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.javayong.magic.framework.common.pojo.CommonResult.success;

/**
 * 类似 oneapi 提供 token 管理的接口
 */
@Tag(name = "类似 oneapi 提供 token 管理的接口")
@RestController("AdminAiTokenContoller")
@RequestMapping("/ai/oneapitoken/")
@Slf4j
public class AdminAiTokenContoller {

    @Resource
    private AiOneApiTokenService oneApiTokenService;

    @GetMapping("/page")
    @Operation(summary = "获得 oneapi token 分页")
    @PreAuthorize("@ss.hasPermission('ai:oneapitoken:query')")
    public CommonResult<PageResult> getOneApiTokenPage(@Valid AiOneApiTokenPageReqVO pageReqVO) {
        PageResult<AiOneApiTokenDO> pageResult = oneApiTokenService.getOneApiTokenPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AiModelRespVO.class));
    }
    
}
