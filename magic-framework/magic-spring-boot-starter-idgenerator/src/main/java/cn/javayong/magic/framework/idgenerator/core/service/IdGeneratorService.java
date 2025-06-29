package cn.javayong.magic.framework.idgenerator.core.service;

/**
 *  编号生成器
 */
public interface IdGeneratorService {

    Long createUniqueId(String shardingKey);

}
