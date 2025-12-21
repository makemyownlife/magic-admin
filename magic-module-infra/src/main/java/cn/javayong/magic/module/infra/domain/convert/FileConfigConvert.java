package cn.javayong.magic.module.infra.domain.convert;

import cn.javayong.magic.module.infra.domain.vo.FileConfigSaveReqVO;
import cn.javayong.magic.module.infra.domain.dataobject.FileConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 文件配置 Convert
 *

 */
@Mapper
public interface FileConfigConvert {

    FileConfigConvert INSTANCE = Mappers.getMapper(FileConfigConvert.class);

    @Mapping(target = "config", ignore = true)
    FileConfigDO convert(FileConfigSaveReqVO bean);

}
