package cn.javayong.magic.module.infra.service;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.infra.domain.vo.FileCreateReqVO;
import cn.javayong.magic.module.infra.domain.vo.FilePageReqVO;
import cn.javayong.magic.module.infra.domain.vo.FilePresignedUrlRespVO;
import cn.javayong.magic.module.infra.domain.dataobject.FileDO;

/**
 * 文件 Service 接口
 */
public interface FileService {

    /**
     * 获得文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO);

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name    文件名称
     * @param path    文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    String createFile(String name, String path, byte[] content);

    /**
     * 创建文件
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFile(FileCreateReqVO createReqVO);

    /**
     * 删除文件
     *
     * @param id 编号
     */
    void deleteFile(Long id) throws Exception;

    /**
     * 获得文件内容
     *
     * @param configId 配置编号
     * @param path     文件路径
     * @return 文件内容
     */
    byte[] getFileContent(Long configId, String path) throws Exception;

    /**
     * 生成文件预签名地址信息
     *
     * @param path 文件路径
     * @return 预签名地址信息
     */
    FilePresignedUrlRespVO getFilePresignedUrl(String path) throws Exception;

}
