package cn.javayong.magic.module.infra.domain.convert;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.infra.domain.vo.ConfigRespVO;
import cn.javayong.magic.module.infra.domain.vo.ConfigSaveReqVO;
import cn.javayong.magic.module.infra.domain.ConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ConfigConvert {

    ConfigConvert INSTANCE = Mappers.getMapper(ConfigConvert.class);

    PageResult<ConfigRespVO> convertPage(PageResult<ConfigDO> page);

    List<ConfigRespVO> convertList(List<ConfigDO> list);

    @Mapping(source = "configKey", target = "key")
    ConfigRespVO convert(ConfigDO bean);

    @Mapping(source = "key", target = "configKey")
    ConfigDO convert(ConfigSaveReqVO bean);

}
