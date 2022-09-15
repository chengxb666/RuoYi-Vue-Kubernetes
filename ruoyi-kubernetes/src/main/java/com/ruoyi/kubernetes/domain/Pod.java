package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.sql.Timestamp;

public class Pod extends BaseEntity {

    int podId;
    String podName;
    String namespaceCode;
    String status;
    String repositoryName;
    String imageTag;
    String yamlName;
    String actionName;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}
