package cn.javayong.magic.module.system.framework.web.adapter;

import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.framework.dict.core.adapter.DictDataAdapter;
import cn.javayong.magic.framework.dict.core.dto.DictDataRespDTO;
import cn.javayong.magic.module.system.domain.dataobject.DictDataDO;
import cn.javayong.magic.module.system.service.DictDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 字典数据 API 实现类
 *

 */
@Service
public class SystemDictDataAdapter implements DictDataAdapter {

    @Resource
    private DictDataService dictDataService;

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        dictDataService.validateDictDataList(dictType, values);
    }

    @Override
    public DictDataRespDTO getDictData(String dictType, String value) {
        DictDataDO dictData = dictDataService.getDictData(dictType, value);
        return BeanUtils.toBean(dictData, DictDataRespDTO.class);
    }

    @Override
    public DictDataRespDTO parseDictData(String dictType, String label) {
        DictDataDO dictData = dictDataService.parseDictData(dictType, label);
        return BeanUtils.toBean(dictData, DictDataRespDTO.class);
    }

    @Override
    public List<DictDataRespDTO> getDictDataList(String dictType) {
        List<DictDataDO> list = dictDataService.getDictDataListByDictType(dictType);
        return BeanUtils.toBean(list, DictDataRespDTO.class);
    }

}
