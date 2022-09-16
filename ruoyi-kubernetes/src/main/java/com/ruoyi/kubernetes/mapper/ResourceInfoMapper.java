package com.ruoyi.kubernetes.mapper;

import com.ruoyi.kubernetes.domain.ResourceInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ResourceInfoMapper {
    public int addResource(ResourceInfo resourceInfo);
    public int deleteResource(int resourceInfoId);
    public int deleteResourceByName(String resourceName);
    public ResourceInfo queryResourceById(int resourceInfoId);
    public ResourceInfo queryResourceByName(String resourceName);
    public int updateResource(ResourceInfo resourceInfo);
    List<ResourceInfo> query();
    List<ResourceInfo> queryResourceByKind(String resourceKind);
    ResourceInfo queryResourceByNameAndKind(@Param("resourceName") String resourceName, @Param("resourceKind") String resourceKind);
}
