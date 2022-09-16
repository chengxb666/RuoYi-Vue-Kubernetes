package com.ruoyi.kubernetes.service;

import com.ruoyi.kubernetes.domain.ResourceConfig;

import java.util.List;

public interface ResourceConfigService {
    ResourceConfig queryResourceConfigById(int resourceConfigId);
    ResourceConfig queryResourceConfigByName(String resourceConfigName);
    List<ResourceConfig> query();
    int addResourceConfig(ResourceConfig resourceConfig) throws Exception;
    int updateResourceConfigById(ResourceConfig resourceConfig);
    int updateResourceConfigByName(ResourceConfig resourceConfig);
    int deleteResourceConfig(int resourceConfigId);
    int deleteResourceConfigByName(String resourceConfigName);
}
