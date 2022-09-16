package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.sql.Timestamp;

public class Registry extends BaseEntity {

    int registryId;
    String registryName;
    String registryUrl;
    String userName;
    String password;
    String projectName;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}
