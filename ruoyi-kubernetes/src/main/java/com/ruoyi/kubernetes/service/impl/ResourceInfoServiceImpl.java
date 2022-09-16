package com.ruoyi.kubernetes.service.impl;

import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.domain.ResourceKind;
import com.ruoyi.kubernetes.domain.YamlTemplate;
import com.ruoyi.kubernetes.mapper.ResourceInfoMapper;
import com.ruoyi.kubernetes.service.ResourceInfoService;
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

    public Map<String,String> resourceInitialResolver(String yamlContent) throws Exception {
        Yaml yaml = new Yaml();
        Map<String,String> result = new HashMap<>();
        Map<String, Object> loadedYamlContent = (Map<String, Object>) yaml.load(yamlContent);
        Set<String> yamlKeys = loadedYamlContent.keySet();
        if(yamlKeys.contains("kind")){
            String resourceKind = (String) loadedYamlContent.get("kind");
            result.put("kind",resourceKind);
        }else if(yamlKeys.contains("metadata")){
            Map<String, Object> metadata = (Map<String, Object>)loadedYamlContent.get("metadata");
            String resourceName = (String) metadata.get("name");
            result.put("name",resourceName);
            if(metadata.containsKey("namespace")){
                String resourceNamespace = (String) metadata.get("namespace");
                result.put("namespace",resourceNamespace);
            }
        }  else{
            throw new Exception("The yaml doesnot contain keyword named kind or metadata");
        }
        return result;
    }

    @Override
    public ResourceInfo declearResource(YamlTemplate yamlTemplate) throws Exception {
        Map<String, String> resourceInitialResult = resourceInitialResolver(yamlTemplate.getYamlContent());
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setResourceKind(resourceInitialResult.get("kind"));
        resourceInfo.setResourceName(resourceInitialResult.get("name"));
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
