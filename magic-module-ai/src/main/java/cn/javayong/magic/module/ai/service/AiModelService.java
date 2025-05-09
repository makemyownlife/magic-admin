package cn.javayong.magic.module.ai.service;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiModelSaveReqVO;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * AI 模型 Service 接口
 */
public interface AiModelService {

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
     *         - list: 当前页的 AI 模型数据列表
     *         - total: 总记录数
     *         - pages: 总页数
     * @throws IllegalArgumentException 如果分页参数不合法
     */
    PageResult<AiModelDO> getModelPage(AiModelPageReqVO pageReqVO);

    /**
     * 获得模型列表
     *
     * @param status 状态
     * @param type 类型
     * @param platform 平台，允许空
     * @return 模型列表
     */
    List<AiModelDO> getModelListByStatusAndType(Integer status, Integer type,
                                                @Nullable String platform);

    /**
     * 创建模型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createModel(@Valid AiModelSaveReqVO createReqVO);

    /**
     * 更新模型
     *
     * @param updateReqVO 更新信息
     */
    void updateModel(@Valid AiModelSaveReqVO updateReqVO);

    /**
     * 删除模型
     *
     * @param id 编号
     */
    void deleteModel(Long id);

    /**
     * 获得模型
     *
     * @param id 编号
     * @return 模型
     */
    AiModelDO getModel(Long id);

}
