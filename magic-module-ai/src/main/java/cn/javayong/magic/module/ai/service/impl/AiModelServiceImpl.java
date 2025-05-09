package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import cn.javayong.magic.module.ai.mapper.AiModelMapper;
import cn.javayong.magic.module.ai.service.AiModelService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * AI 模型 Service 接口实现
 */
@Service
@Validated
public class AiModelServiceImpl implements AiModelService {

    @Resource
    private AiModelMapper modelMapper;

    /**
     * 分页查询 AI 模型列表
     * <p>
     * 根据查询条件返回分页数据，可用于管理后台的列表展示
     * </p>
     *
     * @param pageReqVO 分页查询请求参数，包含以下字段：
     *                  - pageNo: 页码
     *                  - pageSize: 每页条数
     *                  - name: 模型名称(模糊查询)
     *                  - platform: 平台类型
     * @return 分页结果，包含：
     * - list: 当前页的 AI 模型数据列表
     * - total: 总记录数
     * - pages: 总页数
     * @throws IllegalArgumentException 如果分页参数不合法
     */
    @Override
    public PageResult<AiModelDO> getModelPage(AiModelPageReqVO pageReqVO) {
        return modelMapper.selectPage(pageReqVO);
    }

}
