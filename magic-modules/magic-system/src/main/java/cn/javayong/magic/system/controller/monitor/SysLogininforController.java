package cn.javayong.magic.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import cn.javayong.magic.common.core.constant.CacheConstants;
import cn.javayong.magic.common.core.domain.R;
import cn.javayong.magic.common.excel.utils.ExcelUtil;
import cn.javayong.magic.common.log.annotation.Log;
import cn.javayong.magic.common.log.enums.BusinessType;
import cn.javayong.magic.common.mybatis.core.page.PageQuery;
import cn.javayong.magic.common.mybatis.core.page.TableDataInfo;
import cn.javayong.magic.common.redis.utils.RedisUtils;
import cn.javayong.magic.common.web.core.BaseController;
import cn.javayong.magic.system.domain.bo.SysLogininforBo;
import cn.javayong.magic.system.domain.vo.SysLogininforVo;
import cn.javayong.magic.system.service.ISysLogininforService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController {

    private final ISysLogininforService logininforService;

    /**
     * 获取系统访问记录列表
     */
    @SaCheckPermission("monitor:logininfor:list")
    @GetMapping("/list")
    public TableDataInfo<SysLogininforVo> list(SysLogininforBo logininfor, PageQuery pageQuery) {
        return logininforService.selectPageLogininforList(logininfor, pageQuery);
    }

    /**
     * 导出系统访问记录列表
     */
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @SaCheckPermission("monitor:logininfor:export")
    @PostMapping("/export")
    public void export(SysLogininforBo logininfor, HttpServletResponse response) {
        List<SysLogininforVo> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil.exportExcel(list, "登录日志", SysLogininforVo.class, response);
    }

    /**
     * 批量删除登录日志
     * @param infoIds 日志ids
     */
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public R<Void> remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    /**
     * 清理系统访问记录
     */
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R<Void> clean() {
        logininforService.cleanLogininfor();
        return R.ok();
    }

    @SaCheckPermission("monitor:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public R<Void> unlock(@PathVariable("userName") String userName) {
        String loginName = CacheConstants.PWD_ERR_CNT_KEY + userName;
        if (RedisUtils.hasKey(loginName)) {
            RedisUtils.deleteObject(loginName);
        }
        return R.ok();
    }

}
