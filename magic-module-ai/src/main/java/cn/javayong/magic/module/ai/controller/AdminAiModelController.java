package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static cn.javayong.magic.framework.common.pojo.CommonResult.success;

@Tag(name = "后台AI管理-模型配置")
@RestController("AdminAIController")
@RequestMapping("/ai/model/")
@Slf4j
public class AdminAiModelController {

    @GetMapping("/page")
    @Operation(summary = "获得模型分页")
    @PreAuthorize("@ss.hasPermission('ai:model:query')")
    public CommonResult<PageResult> getModelPage(@Valid AiModelPageReqVO pageReqVO) {
        PageResult pageResult = PageResult.empty();
        return success(pageResult);
    }

}
