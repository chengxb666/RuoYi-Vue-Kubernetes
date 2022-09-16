package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.sql.Timestamp;

public class Repository extends BaseEntity {

    int repositoryId;
    String repositoryName;
    String imageTag;
    String registry;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}
