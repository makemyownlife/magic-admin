package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelRespVO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenRespVO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenSaveReqVO;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import static cn.javayong.magic.framework.common.pojo.CommonResult.success;

/**
 * 类似 oneapi 提供 token 管理的接口
 */
@Tag(name = "类似 oneapi 提供 token 管理的接口")
@RestController("AdminOneApiTokenContoller")
@RequestMapping("/ai/oneapitoken/")
@Slf4j
public class AdminOneApiTokenContoller {

    @Resource
    private AiOneApiTokenService oneApiTokenService;

    @GetMapping("/page")
    @Operation(summary = "获得 oneapi token 分页")
    @PreAuthorize("@ss.hasPermission('ai:oneapitoken:query')")
    public CommonResult<PageResult> getOneApiTokenPage(@Valid AiOneApiTokenPageReqVO pageReqVO) {
        PageResult<AiOneApiTokenDO> pageResult = oneApiTokenService.getOneApiTokenPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AiOneApiTokenRespVO.class));
    }

    @PostMapping("/create")
    @Operation(summary = "创建 oneapi token")
    @PreAuthorize("@ss.hasPermission('ai:oneapitoken:create')")
    public CommonResult<Long> createOneApiToken(@Valid @RequestBody AiOneApiTokenSaveReqVO saveReqVO) {
        return success(oneApiTokenService.createOneApiToken(saveReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新oneapi token")
    @PreAuthorize("@ss.hasPermission('ai:oneapitoken:update')")
    public CommonResult<Boolean> updateOneApiToken(@Valid @RequestBody AiOneApiTokenSaveReqVO saveReqVO) {
        oneApiTokenService.updateOneApiToken(saveReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得oneapi token")
    @PreAuthorize("@ss.hasPermission('ai:oneapitoken:query')")
    public CommonResult<AiOneApiTokenRespVO> getOneApiToken(@RequestParam("id") Long id) {
        AiOneApiTokenDO aiOneApiTokenDO = oneApiTokenService.getOneApiTokenById(id);
        return success(BeanUtils.toBean(aiOneApiTokenDO, AiOneApiTokenRespVO.class));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 oneapi token")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ai:oneapitoken:delete')")
    public CommonResult<Boolean> deleteOneApiToken(@RequestParam("id") Long id) {
        oneApiTokenService.deleteOneApiToken(id);
        return success(true);
    }

}
