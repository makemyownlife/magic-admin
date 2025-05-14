package cn.javayong.magic.module.ai.domain.enums;

import cn.javayong.magic.framework.common.exception.ErrorCode;

/**
 * AI 错误码枚举类
 * <p>
 * ai 系统，使用 1-040-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== API 密钥 1-040-000-000 ==========
    ErrorCode API_KEY_NOT_EXISTS = new ErrorCode(1_040_000_000, "API 密钥不存在");
    ErrorCode API_KEY_DISABLE = new ErrorCode(1_040_000_001, "API 密钥已禁用！");

    // ========== API 模型 1-040-001-000 ==========
    ErrorCode MODEL_NOT_EXISTS = new ErrorCode(1_040_001_000, "模型不存在!");
    ErrorCode MODEL_DISABLE = new ErrorCode(1_040_001_001, "模型({})已禁用!");
    ErrorCode MODEL_DEFAULT_NOT_EXISTS = new ErrorCode(1_040_001_002, "操作失败，找不到默认模型");
    ErrorCode MODEL_USE_TYPE_ERROR = new ErrorCode(1_040_001_003, "操作失败，该模型的模型类型不正确");

    // ========== 平台 1-040-002-000 ==========
    ErrorCode PLATFORM_NOT_EXISTS = new ErrorCode(1_040_002_000, "平台不存在!");

}
