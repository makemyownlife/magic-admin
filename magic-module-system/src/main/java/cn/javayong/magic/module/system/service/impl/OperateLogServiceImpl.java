package cn.javayong.magic.module.system.service.impl;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.system.domain.vo.OperateLogPageReqVO;
import cn.javayong.magic.module.system.domain.dataobject.OperateLogDO;
import cn.javayong.magic.module.system.mapper.OperateLogMapper;
import cn.javayong.magic.module.system.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 操作日志 Service 实现类
 *

 */
@Service
@Validated
@Slf4j
public class OperateLogServiceImpl implements OperateLogService {

    @Resource
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO) {
        return operateLogMapper.selectPage(pageReqVO);
    }

}
