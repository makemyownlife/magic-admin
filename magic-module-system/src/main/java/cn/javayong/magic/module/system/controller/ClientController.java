package cn.javayong.magic.module.system.controller;

import cn.javayong.magic.module.system.domain.vo.SystemClientPageReqVO;
import cn.javayong.magic.module.system.domain.vo.SystemClientRespVO;
import cn.javayong.magic.module.system.domain.vo.SystemClientSaveReqVO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.javayong.magic.framework.common.pojo.PageParam;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import static cn.javayong.magic.framework.common.pojo.CommonResult.success;

import cn.javayong.magic.framework.excel.core.util.ExcelUtils;
import cn.javayong.magic.module.system.dal.dataobject.client.SystemClientDO;
import cn.javayong.magic.module.system.service.client.SystemClientService;

@Tag(name = "管理后台 - 系统客户端")
@RestController
@RequestMapping("/system/client")
@Validated
public class ClientController {

    @Resource
    private SystemClientService clientService;

    @PostMapping("/create")
    @Operation(summary = "创建系统客户端")
    @PreAuthorize("@ss.hasPermission('system:client:create')")
    public CommonResult<Long> createClient(@Valid @RequestBody SystemClientSaveReqVO createReqVO) {
        return success(clientService.createClient(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新系统客户端")
    @PreAuthorize("@ss.hasPermission('system:client:update')")
    public CommonResult<Boolean> updateClient(@Valid @RequestBody SystemClientSaveReqVO updateReqVO) {
        clientService.updateClient(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除系统客户端")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:client:delete')")
    public CommonResult<Boolean> deleteClient(@RequestParam("id") Long id) {
        clientService.deleteClient(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除系统客户端")
                @PreAuthorize("@ss.hasPermission('system:client:delete')")
    public CommonResult<Boolean> deleteClientList(@RequestParam("ids") List<Long> ids) {
        clientService.deleteClientListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得系统客户端")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:client:query')")
    public CommonResult<SystemClientRespVO> getClient(@RequestParam("id") Long id) {
        SystemClientDO client = clientService.getClient(id);
        return success(BeanUtils.toBean(client, SystemClientRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得系统客户端分页")
    @PreAuthorize("@ss.hasPermission('system:client:query')")
    public CommonResult<PageResult<SystemClientRespVO>> getClientPage(@Valid SystemClientPageReqVO pageReqVO) {
        PageResult<SystemClientDO> pageResult = clientService.getClientPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SystemClientRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出系统客户端 Excel")
    @PreAuthorize("@ss.hasPermission('system:client:export')")
    public void exportClientExcel(@Valid SystemClientPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SystemClientDO> list = clientService.getClientPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "系统客户端.xls", "数据", SystemClientRespVO.class,
                        BeanUtils.toBean(list, SystemClientRespVO.class));
    }

}