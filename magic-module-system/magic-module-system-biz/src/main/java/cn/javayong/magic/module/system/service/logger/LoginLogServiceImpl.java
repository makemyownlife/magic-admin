package cn.javayong.magic.module.system.service.logger;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.javayong.magic.module.system.domain.vo.LoginLogPageReqVO;
import cn.javayong.magic.module.system.domain.LoginLogDO;
import cn.javayong.magic.module.system.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 登录日志 Service 实现
 */
@Service
@Validated
public class LoginLogServiceImpl implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public PageResult<LoginLogDO> getLoginLogPage(LoginLogPageReqVO pageReqVO) {
        return loginLogMapper.selectPage(pageReqVO);
    }

    @Override
    public void createLoginLog(LoginLogCreateReqDTO reqDTO) {
        LoginLogDO loginLog = BeanUtils.toBean(reqDTO, LoginLogDO.class);
        loginLogMapper.insert(loginLog);
    }

}
