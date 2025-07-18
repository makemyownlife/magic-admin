package cn.javayong.magic.module.system.service.client;

import java.util.*;
import javax.validation.*;
import cn.javayong.magic.module.system.dal.dataobject.client.SystemClientDO;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.system.domain.vo.SystemClientPageReqVO;
import cn.javayong.magic.module.system.domain.vo.SystemClientSaveReqVO;

/**
 * 系统客户端 Service 接口
 *
 * @author admin
 */
public interface SystemClientService {

    /**
     * 创建系统客户端
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createClient(@Valid SystemClientSaveReqVO createReqVO);

    /**
     * 更新系统客户端
     *
     * @param updateReqVO 更新信息
     */
    void updateClient(@Valid SystemClientSaveReqVO updateReqVO);

    /**
     * 删除系统客户端
     *
     * @param id 编号
     */
    void deleteClient(Long id);

    /**
    * 批量删除系统客户端
    *
    * @param ids 编号
    */
    void deleteClientListByIds(List<Long> ids);

    /**
     * 获得系统客户端
     *
     * @param id 编号
     * @return 系统客户端
     */
    SystemClientDO getClient(Long id);

    /**
     * 获得系统客户端分页
     *
     * @param pageReqVO 分页查询
     * @return 系统客户端分页
     */
    PageResult<SystemClientDO> getClientPage(SystemClientPageReqVO pageReqVO);

}