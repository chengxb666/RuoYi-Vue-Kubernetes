package com.ruoyi.kubernetes.service;

import com.ruoyi.kubernetes.domain.ResourceCluster;
import com.ruoyi.kubernetes.domain.ResourceInfo;

import java.util.List;

public interface ResourceClusterService {
    public int createResource(ResourceCluster resourceCluster);
    public int updateResource(ResourceCluster resourceCluster);
    public int deleteResource(String resourceClusterName);
    ResourceCluster queryResourceClusterByNameAndKind(String resourceName,String resourceKind);
    List<ResourceCluster> query();
    List<ResourceCluster> queryByResourceKind(String resourceKind);


}
