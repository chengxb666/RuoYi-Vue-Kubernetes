package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class YamlTemplate extends BaseEntity {

    int yamlTemplateId;
    String yamlName;
    String status;
    String yamlContent;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}
