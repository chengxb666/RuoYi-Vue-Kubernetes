package com.ruoyi.kubernetes.service;

import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.domain.YamlTemplate;

import java.util.List;

public interface ResourceInfoService {

    public ResourceInfo declearResource(YamlTemplate yamlTemplate) throws Exception;
    public int createResource(ResourceInfo resourceInfo);
    public int updateResource(ResourceInfo resourceInfo);
    public int deleteResource(String resourceInfoName);
    ResourceInfo queryResourceInfoByNameAndKind(String resourceName,String resourceKind);
    List<ResourceInfo> queryResourceInfoByKind(String resourceKind) throws Exception;
    public List<ResourceInfo> query();
}
