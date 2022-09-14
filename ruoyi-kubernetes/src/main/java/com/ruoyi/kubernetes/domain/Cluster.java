package com.ruoyi.kubernetes.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
public class Cluster extends BaseEntity {

    int clusterId;
    String clusterCode;
    String masterUrl;
    String token;
    String status;
    String creatUser;
    Timestamp createTime;
    String updateUser;
    Timestamp updateTime;
    String remark;
}