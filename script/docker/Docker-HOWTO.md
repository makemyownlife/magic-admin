进入 magic-server 目录 ，查看了 Dockerfile 文件：

![](https://courage-zhang.oss-cn-beijing.aliyuncs.com/custom202504/202504230958047.png)

## 执行 docker build 命令：

```bash
docker build -t magic-server:1.0.0 .
```

假如出现 build 超时，则需要配置国内镜像。

![](https://courage-zhang.oss-cn-beijing.aliyuncs.com/custom202504/202504231026422.png)

镜像 JSON ：

```
 "registry-mirrors": [
     "https://docker.m.daocloud.io",
    "https://docker.mirrors.ustc.edu.cn",
    "https://hub-mirror.c.163.com",
    "https://registry.docker-cn.com"
]
```

执行过程：

![](https://courage-zhang.oss-cn-beijing.aliyuncs.com/custom202504/202504231035355.gif)
