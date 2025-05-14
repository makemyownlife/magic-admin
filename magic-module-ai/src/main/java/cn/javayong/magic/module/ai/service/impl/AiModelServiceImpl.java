package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.enums.AiPlatformEnum;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiModelSaveReqVO;
import cn.javayong.magic.module.ai.mapper.AiModelMapper;
import cn.javayong.magic.module.ai.service.AiModelService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.javayong.magic.module.ai.domain.enums.ErrorCodeConstants.MODEL_NOT_EXISTS;

/**
 * AI 模型 Service 接口实现
 */
@Service
@Validated
public class AiModelServiceImpl implements AiModelService {

    @Resource
    private AiModelMapper modelMapper;

    @Override
    public PageResult<AiModelDO> getModelPage(AiModelPageReqVO pageReqVO) {
        return modelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AiModelDO> getModelListByStatusAndType(Integer status, Integer type, String platform) {
        return modelMapper.selectListByStatusAndType(status, type, platform);
    }

    @Override
    public Long createModel(AiModelSaveReqVO createReqVO) {
        // 1. 校验
        AiPlatformEnum.validatePlatform(createReqVO.getPlatform());

        // 2. 插入
        AiModelDO model = BeanUtils.toBean(createReqVO, AiModelDO.class);
        model.setDeleted(false);
        modelMapper.insert(model);
        return model.getId();
    }

    @Override
    public void updateModel(AiModelSaveReqVO updateReqVO) {
        // 1. 校验
        validateModelExists(updateReqVO.getId());
        AiPlatformEnum.validatePlatform(updateReqVO.getPlatform());

        // 2. 更新
        AiModelDO updateObj = BeanUtils.toBean(updateReqVO, AiModelDO.class);
        modelMapper.updateById(updateObj);
    }

    @Override
    public void deleteModel(Long id) {
        // 校验存在
        validateModelExists(id);
        // 删除
        modelMapper.deleteById(id);
    }

    private AiModelDO validateModelExists(Long id) {
        AiModelDO model = modelMapper.selectById(id);
        if (model == null) {
            throw ServiceExceptionUtil.exception(MODEL_NOT_EXISTS);
        }
        return model;
    }

    @Override
    public AiModelDO getModel(Long id) {
        return modelMapper.selectById(id);
    }

}
