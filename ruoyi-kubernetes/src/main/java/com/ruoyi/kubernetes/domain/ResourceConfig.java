package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ResourceConfig extends BaseEntity {

    int resourceConfigId;
    String resourceConfigName;
    String resourceName;
    String resourceKind;
    String namespaceCode;
    String clusterCode;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}
