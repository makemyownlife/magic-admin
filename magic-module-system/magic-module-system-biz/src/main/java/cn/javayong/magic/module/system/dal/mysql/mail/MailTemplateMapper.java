package cn.javayong.magic.module.system.dal.mysql.mail;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.framework.mybatis.core.query.QueryWrapperX;
import cn.javayong.magic.module.system.controller.admin.mail.vo.template.MailTemplatePageReqVO;
import cn.javayong.magic.module.system.dal.dataobject.mail.MailTemplateDO;
import cn.javayong.magic.module.system.dal.dataobject.sms.SmsTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface MailTemplateMapper extends BaseMapperX<MailTemplateDO> {

    default PageResult<MailTemplateDO> selectPage(MailTemplatePageReqVO pageReqVO){
        return selectPage(pageReqVO , new LambdaQueryWrapperX<MailTemplateDO>()
                .eqIfPresent(MailTemplateDO::getStatus, pageReqVO.getStatus())
                .likeIfPresent(MailTemplateDO::getCode, pageReqVO.getCode())
                .likeIfPresent(MailTemplateDO::getName, pageReqVO.getName())
                .eqIfPresent(MailTemplateDO::getAccountId, pageReqVO.getAccountId())
                .betweenIfPresent(MailTemplateDO::getCreateTime, pageReqVO.getCreateTime()));
    }

    default Long selectCountByAccountId(Long accountId) {
        return selectCount(MailTemplateDO::getAccountId, accountId);
    }

    default MailTemplateDO selectByCode(String code) {
        return selectOne(MailTemplateDO::getCode, code);
    }

}
