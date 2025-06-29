package cn.javayong.magic.server.controller;

import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.util.servlet.ServletUtils;
import cn.javayong.magic.framework.idgenerator.core.service.IdGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

/**
 * 默认 Controller 做测试用
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
