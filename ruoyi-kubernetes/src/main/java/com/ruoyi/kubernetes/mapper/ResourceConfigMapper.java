package com.ruoyi.kubernetes.mapper;

import com.ruoyi.kubernetes.domain.ResourceConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceConfigMapper {

    ResourceConfig queryResourceConfigById(int resourceConfigId);
    ResourceConfig queryResourceConfigByName(String ResourceConfigName);
    List<ResourceConfig> queryResourceConfigByNameAndKind(@Param("resourceName") String resourceName, @Param("resourceKind") String resourceKind);
    List<ResourceConfig> query();
    int addResourceConfig(ResourceConfig resourceConfig);
    int updateResourceConfigById(ResourceConfig resourceConfig);
    int updateResourceConfigByName(ResourceConfig resourceConfig);
    int deleteResourceConfig(int resourceConfigId);
    int deleteResourceConfigByName(String resourceConfigName);
}
