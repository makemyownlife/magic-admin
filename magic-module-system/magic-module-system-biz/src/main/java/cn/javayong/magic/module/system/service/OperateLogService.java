package cn.javayong.magic.module.system.service;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.javayong.magic.module.system.api.logger.dto.OperateLogPageReqDTO;
import cn.javayong.magic.module.system.domain.vo.OperateLogPageReqVO;
import cn.javayong.magic.module.system.domain.OperateLogDO;

/**
 * 操作日志 Service 接口
 *

 */
public interface OperateLogService {

    /**
     * 记录操作日志
     *
     * @param createReqDTO 创建请求
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqDTO pageReqVO);

}
