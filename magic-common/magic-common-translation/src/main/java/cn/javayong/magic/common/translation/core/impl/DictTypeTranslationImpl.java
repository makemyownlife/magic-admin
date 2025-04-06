package cn.javayong.magic.common.translation.core.impl;

import cn.javayong.magic.common.core.service.DictService;
import cn.javayong.magic.common.core.utils.StringUtils;
import cn.javayong.magic.common.translation.annotation.TranslationType;
import cn.javayong.magic.common.translation.constant.TransConstant;
import cn.javayong.magic.common.translation.core.TranslationInterface;
import lombok.AllArgsConstructor;

/**
 * 字典翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
public class DictTypeTranslationImpl implements TranslationInterface<String> {

    private final DictService dictService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof String dictValue && StringUtils.isNotBlank(other)) {
            return dictService.getDictLabel(other, dictValue);
        }
        return null;
    }
}
