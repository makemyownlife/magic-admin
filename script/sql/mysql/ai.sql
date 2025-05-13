CREATE TABLE `ai_model` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
                            `name` varchar(50) DEFAULT NULL COMMENT '模型名称',
                            `model` varchar(50) DEFAULT NULL COMMENT '模型标志',
                            `platform` varchar(20) DEFAULT NULL COMMENT '平台',
                            `type` int DEFAULT NULL COMMENT '类型',
                            `sort` int DEFAULT NULL COMMENT '排序值',
                            `status` int DEFAULT NULL COMMENT '状态',
                            `temperature` double DEFAULT NULL COMMENT '温度参数',
                            `max_tokens` int DEFAULT NULL COMMENT '单条回复的最大 Token 数量',
                            `max_contexts` int DEFAULT NULL COMMENT '上下文的最大 Message 数量',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
                            `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
                            `deleted` bit(1) DEFAULT NULL COMMENT '是否删除',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='AI模型表';

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2758, '幻视AI助手', '', 1, 400, 0, '/ai', 'fa:apple', '', '', 0, b'1', b'1', b'1', '1', '2024-05-07 15:07:56', '1', '2024-05-25 12:36:12', b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2767, '模型配置', '', 2, 0, 2758, 'model', 'fa-solid:abacus', 'ai/model/model/index.vue', 'AiModel', 0, b'1', b'1', b'1', '', '2024-05-10 14:42:48', '1', '2025-03-03 09:57:41', b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2769, '聊天模型创建', 'ai:model:create', 3, 2, 2767, '', '', '', '', 0, b'1', b'1', b'1', '', '2024-05-10 14:42:48', '1', '2025-03-03 09:20:10', b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2770, '聊天模型更新', 'ai:model:update', 3, 3, 2767, '', '', '', '', 0, b'1', b'1', b'1', '', '2024-05-10 14:42:48', '1', '2025-03-03 09:20:14', b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2771, '聊天模型删除', 'ai:model:delete', 3, 4, 2767, '', '', '', '', 0, b'1', b'1', b'1', '', '2024-05-10 14:42:48', '1', '2025-03-03 09:20:27', b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2773, '平台配置', '', 2, 0, 2758, 'platform', 'fa:user-secret', 'ai/model/platform/index.vue', 'AiPlatform', 0, b'1', b'1', b'1', '', '2024-05-13 12:39:28', '1', '2024-05-13 20:41:45', b'0');

CREATE TABLE `ai_platform` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `platform` varchar(255) DEFAULT NULL COMMENT '平台代码/标识',
                               `name` varchar(50) NOT NULL DEFAULT '' COMMENT '平台显示名称',
                               `base_url` varchar(512) DEFAULT NULL COMMENT '平台基础API地址',
                               `model_mapping_json` json DEFAULT NULL COMMENT '模型映射配置(JSON格式)',
                               `sort` int DEFAULT NULL COMMENT '排序值',
                               `status` int DEFAULT NULL COMMENT '状态',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
                               `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
                               `deleted` bit(1) DEFAULT NULL COMMENT '是否删除 (0-未删除 1-已删除)',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='AI平台表';

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2774, '平台创建', 'ai:platform:create', 3, 2, 2773, '', '', '', '', 0, b'1', b'1', b'1', '', '2024-05-10 14:42:48', '1', '2025-03-03 09:20:10', b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2775, '平台更新', 'ai:platform:update', 3, 3, 2773, '', '', '', '', 0, b'1', b'1', b'1', '', '2024-05-10 14:42:48', '1', '2025-03-03 09:20:14', b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2776, '平台删除', 'ai:platform:delete', 3, 4, 2773, '', '', '', '', 0, b'1', b'1', b'1', '', '2024-05-10 14:42:48', '1', '2025-03-03 09:20:27', b'0');