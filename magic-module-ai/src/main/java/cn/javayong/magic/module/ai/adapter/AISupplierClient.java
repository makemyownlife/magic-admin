package cn.javayong.magic.module.ai.adapter;

import reactor.core.publisher.Flux;

/**
 * 供应商客户端
 */
public interface AISupplierClient {

    void init(AISupplierConfig aiSupplierConfig);


    Flux<String> chatCompletion();


    void destroy();

}
