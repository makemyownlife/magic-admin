package cn.iocoder.yudao.module.infra.convert.config;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.config.vo.ConfigRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.config.vo.ConfigSaveReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-08T14:24:36+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
public class ConfigConvertImpl implements ConfigConvert {

    @Override
    public PageResult<ConfigRespVO> convertPage(PageResult<ConfigDO> page) {
        if ( page == null ) {
            return null;
        }

        PageResult<ConfigRespVO> pageResult = new PageResult<ConfigRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    }

    @Override
    public List<ConfigRespVO> convertList(List<ConfigDO> list) {
        if ( list == null ) {
            return null;
        }

        List<ConfigRespVO> list1 = new ArrayList<ConfigRespVO>( list.size() );
        for ( ConfigDO configDO : list ) {
            list1.add( convert( configDO ) );
        }

        return list1;
    }

    @Override
    public ConfigRespVO convert(ConfigDO bean) {
        if ( bean == null ) {
            return null;
        }

        ConfigRespVO configRespVO = new ConfigRespVO();

        configRespVO.setKey( bean.getConfigKey() );
        configRespVO.setId( bean.getId() );
        configRespVO.setCategory( bean.getCategory() );
        configRespVO.setName( bean.getName() );
        configRespVO.setValue( bean.getValue() );
        configRespVO.setType( bean.getType() );
        configRespVO.setVisible( bean.getVisible() );
        configRespVO.setRemark( bean.getRemark() );
        configRespVO.setCreateTime( bean.getCreateTime() );

        return configRespVO;
    }

    @Override
    public ConfigDO convert(ConfigSaveReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        ConfigDO configDO = new ConfigDO();

        configDO.setConfigKey( bean.getKey() );
        configDO.setId( bean.getId() );
        configDO.setCategory( bean.getCategory() );
        configDO.setName( bean.getName() );
        configDO.setValue( bean.getValue() );
        configDO.setVisible( bean.getVisible() );
        configDO.setRemark( bean.getRemark() );

        return configDO;
    }
}
