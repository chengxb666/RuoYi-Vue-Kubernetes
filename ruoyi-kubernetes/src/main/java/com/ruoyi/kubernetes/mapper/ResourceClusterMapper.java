package com.ruoyi.kubernetes.mapper;

import com.ruoyi.kubernetes.domain.ResourceCluster;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceClusterMapper {
    public int addResource(ResourceCluster resourceCluster);
    public int deleteResource(int resourceClusterId);
    public int deleteResourceByName(String resourceName);
    public ResourceCluster queryResourceById(int resourceClusterId);
    public ResourceCluster queryResourceByName(String resourceName);
    public int updateResource(ResourceCluster resourceCluster);
    List<ResourceCluster> query();
}
