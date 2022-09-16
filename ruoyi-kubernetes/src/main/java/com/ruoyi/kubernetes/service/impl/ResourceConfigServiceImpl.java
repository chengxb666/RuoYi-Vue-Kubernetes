package com.ruoyi.kubernetes.service.impl;

import com.ruoyi.kubernetes.domain.ResourceCluster;
import com.ruoyi.kubernetes.domain.ResourceConfig;
import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.mapper.ResourceConfigMapper;
import com.ruoyi.kubernetes.service.ResourceClusterService;
import com.ruoyi.kubernetes.service.ResourceConfigService;
import com.ruoyi.kubernetes.service.ResourceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceConfigServiceImpl implements ResourceConfigService {

    @Autowired
    private ResourceConfigMapper resourceConfigMapper;

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Autowired
    private ResourceClusterService resourceClusterService;

    @Override
    public ResourceConfig queryResourceConfigById(int resourceConfigId) {
        return resourceConfigMapper.queryResourceConfigById(resourceConfigId);
    }

    @Override
    public ResourceConfig queryResourceConfigByName(String resourceConfigName) {
        return resourceConfigMapper.queryResourceConfigByName(resourceConfigName);
    }

    @Override
    public List<ResourceConfig> query() {
        return resourceConfigMapper.query();
    }

    @Override
    public int addResourceConfig(ResourceConfig resourceConfig) throws Exception {
        String resourceName = resourceConfig.getResourceName();
        String resourceKind = resourceConfig.getResourceKind();
        ResourceInfo resourceInfo = resourceInfoService.queryResourceInfoByNameAndKind(resourceName,resourceKind);
        if(resourceInfo.equals(null)){
            throw new Exception("cannot find ResourceInfo with name: " + resourceName + " and kind: " + resourceKind);
        }
        if(resourceInfo.getNamespaceCode().equals(null)){
            resourceInfo.setNamespaceCode(resourceConfig.getNamespaceCode());
        }
        if(!resourceConfig.getNamespaceCode().equals(resourceInfo.getNamespaceCode())){
            throw new Exception("The namespaces are different, check it");
        }
        ResourceCluster resourceCluster = new ResourceCluster();
        resourceCluster.setResourceName(resourceName);
        resourceCluster.setResourceKind(resourceKind);
        resourceCluster.setClusterCode(resourceConfig.getClusterCode());
        resourceCluster.setNamespaceCode(resourceConfig.getNamespaceCode());
        resourceCluster.setStatus("waitCommit");
        resourceCluster.setYamlContent(resourceInfo.getYamlContent());
        resourceInfoService.updateResource(resourceInfo);
        resourceClusterService.createResource(resourceCluster);
        return resourceConfigMapper.addResourceConfig(resourceConfig);
    }

    @Override
    public int updateResourceConfigById(ResourceConfig resourceConfig) {
        return resourceConfigMapper.updateResourceConfigById(resourceConfig);
    }

    @Override
    public int updateResourceConfigByName(ResourceConfig resourceConfig) {
        return resourceConfigMapper.updateResourceConfigByName(resourceConfig);
    }

    @Override
    public int deleteResourceConfig(int resourceConfigId) {
        return resourceConfigMapper.deleteResourceConfig(resourceConfigId);
    }

    @Override
    public int deleteResourceConfigByName(String resourceConfigName) {
        return resourceConfigMapper.deleteResourceConfigByName(resourceConfigName);
    }
}
