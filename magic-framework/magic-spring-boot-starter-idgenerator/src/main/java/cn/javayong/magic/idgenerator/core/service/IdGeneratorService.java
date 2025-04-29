package cn.javayong.magic.idgenerator.core.service;

/**
 *  编号生成器
 */
public interface IdGeneratorService {

    Long createUniqueId(String shardingKey);

}
