package cn.javayong.magic.module.system.service.client;

import cn.javayong.magic.module.system.domain.dataobject.SystemClientDO;
import cn.javayong.magic.module.system.domain.vo.SystemClientPageReqVO;
import cn.javayong.magic.module.system.domain.vo.SystemClientSaveReqVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.system.mapper.SystemClientMapper;

import static cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.javayong.magic.module.system.domain.enums.ErrorCodeConstants.CLIENT_NOT_EXISTS;
import static cn.javayong.magic.module.system.domain.enums.ErrorCodeConstants.DEFAULT_CLIENT_CANT_DELETE;

/**
 * 系统客户端 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class SystemClientServiceImpl implements SystemClientService {

    @Resource
    private SystemClientMapper clientMapper;

    @Override
    public Long createClient(SystemClientSaveReqVO createReqVO) {
        // 插入
        SystemClientDO client = BeanUtils.toBean(createReqVO, SystemClientDO.class);
        clientMapper.insert(client);

        // 返回
        return client.getId();
    }

    @Override
    public void updateClient(SystemClientSaveReqVO updateReqVO) {
        // 校验存在
        validateClientExists(updateReqVO.getId());
        // 更新
        SystemClientDO updateObj = BeanUtils.toBean(updateReqVO, SystemClientDO.class);
        clientMapper.updateById(updateObj);
    }

    @Override
    public void deleteClient(Long id) {
        if (id == 1L) {
            throw exception(DEFAULT_CLIENT_CANT_DELETE);
        }
        // 校验存在
        validateClientExists(id);
        // 删除
        clientMapper.deleteById(id);
    }

    @Override
    public void deleteClientListByIds(List<Long> ids) {
        // 删除
        clientMapper.deleteByIds(ids);
    }

    private void validateClientExists(Long id) {
        if (clientMapper.selectById(id) == null) {
            throw exception(CLIENT_NOT_EXISTS);
        }
    }

    @Override
    public SystemClientDO getClient(Long id) {
        return clientMapper.selectById(id);
    }

    @Override
    public PageResult<SystemClientDO> getClientPage(SystemClientPageReqVO pageReqVO) {
        return clientMapper.selectPage(pageReqVO);
    }

}