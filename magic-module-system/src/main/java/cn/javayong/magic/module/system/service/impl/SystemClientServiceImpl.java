package cn.javayong.magic.module.system.service.impl;

import cn.javayong.magic.module.system.domain.dataobject.SystemClientDO;
import cn.javayong.magic.module.system.domain.enums.RedisKeyConstants;
import cn.javayong.magic.module.system.domain.vo.SystemClientPageReqVO;
import cn.javayong.magic.module.system.domain.vo.SystemClientSaveReqVO;
import cn.javayong.magic.module.system.service.SystemClientService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.system.mapper.SystemClientMapper;

import static cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.javayong.magic.framework.common.util.collection.CollectionUtils.convertList;
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
    @CacheEvict(value = RedisKeyConstants.SYSTEM_CLIENT_LIST, allEntries = true)
    public Long createClient(SystemClientSaveReqVO createReqVO) {
        // 插入
        SystemClientDO client = BeanUtils.toBean(createReqVO, SystemClientDO.class);
        clientMapper.insert(client);

        // 返回
        return client.getId();
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.SYSTEM_CLIENT_LIST,
            allEntries = true)
    public void updateClient(SystemClientSaveReqVO updateReqVO) {
        // 校验存在
        validateClientExists(updateReqVO.getId());
        // 更新
        SystemClientDO updateObj = BeanUtils.toBean(updateReqVO, SystemClientDO.class);
        clientMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.SYSTEM_CLIENT_LIST, allEntries = true)
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
    @CacheEvict(value = RedisKeyConstants.SYSTEM_CLIENT_LIST, allEntries = true)
    public void deleteClientListByIds(List<Long> ids) {
        // 删除
        clientMapper.deleteByIds(ids);
    }

    @Cacheable(value = RedisKeyConstants.SYSTEM_CLIENT_LIST,
            key = "'cacheKey:' + #clientKey",
            unless = "#clientKey == null")  // 显式排除null情况
    public SystemClientDO getSystemClientByClientKeyFromCache(String clientKey) {
        return clientMapper.selectOne(SystemClientDO::getClientKey, clientKey);
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
    @Cacheable(value = RedisKeyConstants.SYSTEM_CLIENT_LIST, key = "'id:' + #id" ,unless = "#id == null")
    public SystemClientDO getClientByIdFromCache(Long id) {
        return clientMapper.selectById(id);
    }

    @Override
    public PageResult<SystemClientDO> getClientPage(SystemClientPageReqVO pageReqVO) {
        return clientMapper.selectPage(pageReqVO);
    }

}