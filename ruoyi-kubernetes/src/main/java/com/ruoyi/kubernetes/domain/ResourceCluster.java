package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResourceCluster extends BaseEntity {

    int resourceId;
    String resourceName;
    String resourceKind;
    String namespaceCode;
    String clusterCode;
    String status;
    String yamlContentName;
    String yamlComitted;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}
