package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResourceInfo extends BaseEntity {

    int resourceId;
    String resourceName;
    String resourceKind;
    String namespaceCode;
    String status;
    String YamlName;
    String yamlContent;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}
