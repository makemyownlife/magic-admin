package cn.javayong.magic.framework.dict.config;

import cn.javayong.magic.framework.dict.core.DictFrameworkUtils;
import cn.javayong.magic.framework.dict.core.adapter.DictDataAdapter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MagicDictAutoConfiguration {

    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public DictFrameworkUtils dictUtils(DictDataAdapter dictDataAdapter) {
        DictFrameworkUtils.init(dictDataAdapter);
        return new DictFrameworkUtils();
    }

}
