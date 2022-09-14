package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.sql.Timestamp;

public class Configmap extends BaseEntity {
    int configmapId;
    String configmapName;
    String namespaceCode;
    String status;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}
