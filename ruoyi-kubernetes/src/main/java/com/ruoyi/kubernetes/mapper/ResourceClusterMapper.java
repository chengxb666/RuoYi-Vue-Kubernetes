package com.ruoyi.kubernetes.mapper;

import com.ruoyi.kubernetes.domain.ResourceCluster;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceClusterMapper {
    public int addResource(ResourceCluster resourceCluster);
    public int deleteResource(int resourceClusterId);
    public int deleteResourceByNameAndKind(@Param("resourceName") String resourceName,
                @Param("resourceKind") String resourceKind, @Param("namespaceCode") String namespaceCode);
    public ResourceCluster queryResourceById(int resourceClusterId);
    public ResourceCluster queryResourceByName(String resourceName);
    ResourceCluster queryResourceByNameAndKind(@Param("resourceKind") String resourceKind,@Param("resourceName") String resourceName);
    List<ResourceCluster> queryResourceByKind(String resourceKind);
    public int updateResource(ResourceCluster resourceCluster);
    List<ResourceCluster> query();
    int updateResourceByNameAndKind(ResourceCluster resourceCluster);
}
