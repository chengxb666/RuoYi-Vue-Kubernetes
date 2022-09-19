package com.ruoyi.kubernetes.service.impl;

import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.domain.ResourceKind;
import com.ruoyi.kubernetes.domain.YamlTemplate;
import com.ruoyi.kubernetes.mapper.ResourceInfoMapper;
import com.ruoyi.kubernetes.service.ResourceInfoService;
import com.ruoyi.kubernetes.utils.GeneralUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

@Service
public class ResourceInfoServiceImpl implements ResourceInfoService {

    private static final Logger log = LoggerFactory.getLogger(ResourceInfoServiceImpl.class);

    @Autowired
    private ResourceInfoMapper resourceInfoMapper;

    @Autowired
    private GeneralUtils generalUtils;

    @Override
    public ResourceInfo declearResource(YamlTemplate yamlTemplate) throws Exception {
        Map<String, String> resourceInitialResult = generalUtils.resourceInitialResolver(yamlTemplate.getYamlContent());
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setResourceKind(resourceInitialResult.get("kind"));
        log.info("kind is: {}",resourceInitialResult.get("kind"));
        resourceInfo.setResourceName(resourceInitialResult.get("name"));
        log.info("name is: {}",resourceInitialResult.get("name"));
        if(resourceInitialResult.containsKey("namespace")){
            resourceInfo.setNamespaceCode(resourceInitialResult.get("namespace"));
        }
        resourceInfo.setYamlContent(yamlTemplate.getYamlContent());
        resourceInfo.setYamlName(yamlTemplate.getYamlName());
        resourceInfo.setStatus("Initialized");
        return resourceInfo;
    }

    @Override
    public int createResource(ResourceInfo resourceInfo) {
        return resourceInfoMapper.addResource(resourceInfo);
    }

    @Override
    public int updateResource(ResourceInfo resourceInfo) {

        return 0;
    }

    @Override
    public int deleteResource(String resourceInfoName) {

        return resourceInfoMapper.deleteResourceByName(resourceInfoName);
    }

    @Override
    public ResourceInfo queryResourceInfoByNameAndKind(String resourceName,String resourceKind) {
        return resourceInfoMapper.queryResourceByNameAndKind(resourceName,resourceKind);
    }

    @Override
    public List<ResourceInfo> queryResourceInfoByKind(String resourceKind) throws Exception {
        List<ResourceKind> resourceKinds = Arrays.asList(ResourceKind.values());
        List<ResourceInfo> queryResult = new LinkedList<>();
        for (ResourceKind item : resourceKinds) {
            if(item.toString().equals(resourceKind)) {
                List<ResourceInfo> resourceInfos = resourceInfoMapper.queryResourceByKind(resourceKind);
                if(CollectionUtils.isEmpty(resourceInfos)){
                    log.info("No resourceinfo with the specified kind ccreated");
                }
                queryResult.addAll(resourceInfos);
            }else{
                throw new Exception("The resourceKind: " + resourceKind + " is wrong,check it");
            }
        }
        return queryResult;
    }
}
