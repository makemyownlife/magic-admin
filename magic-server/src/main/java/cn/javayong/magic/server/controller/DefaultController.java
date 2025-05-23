package cn.javayong.magic.server.controller;

import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.util.servlet.ServletUtils;
import cn.javayong.magic.idgenerator.core.service.IdGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import static cn.javayong.magic.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_IMPLEMENTED;

/**
 * 默认 Controller，解决部分 module 未开启时的 404 提示。
 * 例如说，/bpm/** 路径，工作流
 *
 * @author zhangyong
 */
@RestController
@Slf4j
public class DefaultController {

    @Autowired
    private IdGeneratorService idGeneratorService;

    /**
     * 测试接口：打印 query、header、body
     */
    @RequestMapping(value = {"/test"})
    @PermitAll
    public CommonResult<Boolean> test(HttpServletRequest request) {
        // 打印查询参数
        log.info("Query: {}", ServletUtils.getParamMap(request));
        // 打印请求头
        log.info("Header: {}", ServletUtils.getHeaderMap(request));
        // 打印请求体
        log.info("Body: {}", ServletUtils.getBody(request));
        // 生成 id
        Long id = idGeneratorService.createUniqueId("mytable");
        log.info("ID 生成器创建新序号: {}", id);
        return CommonResult.success(true);
    }

}
