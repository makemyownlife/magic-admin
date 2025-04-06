package cn.javayong.magic.demo.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.javayong.magic.common.core.domain.R;
import cn.javayong.magic.common.core.utils.MapstructUtils;
import cn.javayong.magic.common.core.utils.ValidatorUtils;
import cn.javayong.magic.common.core.validate.AddGroup;
import cn.javayong.magic.common.core.validate.EditGroup;
import cn.javayong.magic.common.core.validate.QueryGroup;
import cn.javayong.magic.common.web.core.BaseController;
import cn.javayong.magic.demo.domain.TestDemo;
import cn.javayong.magic.demo.domain.bo.TestDemoBo;
import cn.javayong.magic.demo.domain.bo.TestDemoImportVo;
import cn.javayong.magic.demo.domain.vo.TestDemoVo;
import cn.javayong.magic.demo.service.ITestDemoService;
import cn.javayong.magic.common.idempotent.annotation.RepeatSubmit;
import cn.javayong.magic.common.mybatis.core.page.PageQuery;
import cn.javayong.magic.common.mybatis.core.page.TableDataInfo;
import cn.javayong.magic.common.excel.core.ExcelResult;
import cn.javayong.magic.common.excel.utils.ExcelUtil;
import cn.javayong.magic.common.log.annotation.Log;
import cn.javayong.magic.common.log.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 测试单表Controller
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/demo")
public class TestDemoController extends BaseController {

    private final ITestDemoService testDemoService;

    /**
     * 查询测试单表列表
     */
    @SaCheckPermission("demo:demo:list")
    @GetMapping("/list")
    public TableDataInfo<TestDemoVo> list(@Validated(QueryGroup.class) TestDemoBo bo, PageQuery pageQuery) {
        return testDemoService.queryPageList(bo, pageQuery);
    }

    /**
     * 自定义分页查询
     */
    @SaCheckPermission("demo:demo:list")
    @GetMapping("/page")
    public TableDataInfo<TestDemoVo> page(@Validated(QueryGroup.class) TestDemoBo bo, PageQuery pageQuery) {
        return testDemoService.customPageList(bo, pageQuery);
    }

    /**
     * 导入数据
     *
     * @param file 导入文件
     */
    @Log(title = "测试单表", businessType = BusinessType.IMPORT)
    @SaCheckPermission("demo:demo:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file) throws Exception {
        ExcelResult<TestDemoImportVo> excelResult = ExcelUtil.importExcel(file.getInputStream(), TestDemoImportVo.class, true);
        List<TestDemo> list = MapstructUtils.convert(excelResult.getList(), TestDemo.class);
        testDemoService.saveBatch(list);
        return R.ok(excelResult.getAnalysis());
    }

    /**
     * 导出测试单表列表
     */
    @SaCheckPermission("demo:demo:export")
    @Log(title = "测试单表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated TestDemoBo bo, HttpServletResponse response) {
        List<TestDemoVo> list = testDemoService.queryList(bo);
        // 测试雪花id导出
//        for (TestDemoVo vo : list) {
//            vo.setId(1234567891234567893L);
//        }
        ExcelUtil.exportExcel(list, "测试单表", TestDemoVo.class, response);
    }

    /**
     * 获取测试单表详细信息
     *
     * @param id 测试ID
     */
    @SaCheckPermission("demo:demo:query")
    @GetMapping("/{id}")
    public R<TestDemoVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable("id") Long id) {
        return R.ok(testDemoService.queryById(id));
    }

    /**
     * 新增测试单表
     */
    @SaCheckPermission("demo:demo:add")
    @Log(title = "测试单表", businessType = BusinessType.INSERT)
    @RepeatSubmit(interval = 2, timeUnit = TimeUnit.SECONDS, message = "{repeat.submit.message}")
    @PostMapping()
    public R<Void> add(@RequestBody TestDemoBo bo) {
        // 使用校验工具对标 @Validated(AddGroup.class) 注解
        // 用于在非 Controller 的地方校验对象
        ValidatorUtils.validate(bo, AddGroup.class);
        return toAjax(testDemoService.insertByBo(bo));
    }

    /**
     * 修改测试单表
     */
    @SaCheckPermission("demo:demo:edit")
    @Log(title = "测试单表", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TestDemoBo bo) {
        return toAjax(testDemoService.updateByBo(bo));
    }

    /**
     * 删除测试单表
     *
     * @param ids 测试ID串
     */
    @SaCheckPermission("demo:demo:remove")
    @Log(title = "测试单表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(testDemoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
