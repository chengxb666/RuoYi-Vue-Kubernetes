package com.ruoyi.kubernetes.service;

import com.ruoyi.kubernetes.domain.ResourceCluster;
import com.ruoyi.kubernetes.domain.ResourceInfo;

import java.io.FileNotFoundException;
import java.util.List;

public interface ResourceClusterService {
    public int createResource(ResourceCluster resourceCluster) throws Exception;
    public int updateResource(ResourceCluster resourceCluster) throws Exception;
    public int deleteResource(ResourceCluster resourceCluster) throws Exception;
    ResourceCluster queryResourceClusterByNameAndKind(String resourceName,String resourceKind);
    List<ResourceCluster> query();
    List<ResourceCluster> queryByResourceKind(String resourceKind);
    boolean commitResource(ResourceCluster resourceCluster,String action) throws Exception;

}
