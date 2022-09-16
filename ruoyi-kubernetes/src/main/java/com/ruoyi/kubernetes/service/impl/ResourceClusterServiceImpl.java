package com.ruoyi.kubernetes.service.impl;

import com.ruoyi.kubernetes.domain.ResourceCluster;
import com.ruoyi.kubernetes.mapper.ResourceClusterMapper;
import com.ruoyi.kubernetes.service.ResourceClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceClusterServiceImpl implements ResourceClusterService {

    @Autowired
    private ResourceClusterMapper resourceClusterMapper;

    @Override
    public int createResource(ResourceCluster resourceCluster) {
        return resourceClusterMapper.addResource(resourceCluster);
    }

    @Override
    public int updateResource(ResourceCluster resourceCluster) {
        return 0;
    }

    @Override
    public int deleteResource(String resourceClusterName) {
        return 0;
    }

    @Override
    public ResourceCluster queryResourceClusterByNameAndKind(String resourceName, String resourceKind) {
        return null;
    }

    @Override
    public List<ResourceCluster> query() {
        return null;
    }

    @Override
    public List<ResourceCluster> queryByResourceKind(String resourceKind) {
        return null;
    }
}
