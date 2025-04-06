package cn.javayong.magic.common.translation.core.impl;

import cn.javayong.magic.common.core.service.DeptService;
import cn.javayong.magic.common.translation.annotation.TranslationType;
import cn.javayong.magic.common.translation.constant.TransConstant;
import cn.javayong.magic.common.translation.core.TranslationInterface;
import lombok.AllArgsConstructor;

/**
 * 部门翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.DEPT_ID_TO_NAME)
public class DeptNameTranslationImpl implements TranslationInterface<String> {

    private final DeptService deptService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof String ids) {
            return deptService.selectDeptNameByIds(ids);
        } else if (key instanceof Long id) {
            return deptService.selectDeptNameByIds(id.toString());
        }
        return null;
    }
}
