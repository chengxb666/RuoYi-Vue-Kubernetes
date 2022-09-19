package com.ruoyi.kubernetes.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class GeneralUtils {

    private static final Logger log = LoggerFactory.getLogger(GeneralUtils.class);

    public Map<String,String> resourceInitialResolver(String yamlContent) throws Exception {
        Yaml yaml = new Yaml();
        Map<String,String> result = new HashMap<>();
        Map<String, Object> loadedYamlContent = (Map<String, Object>) yaml.load(yamlContent);
        Set<String> yamlKeys = loadedYamlContent.keySet();
        if(yamlKeys.contains("kind")){
            String resourceKind = (String) loadedYamlContent.get("kind");
            result.put("kind",resourceKind);
        }else {
            log.error("The yaml has no key named kind");
        }
        if(yamlKeys.contains("metadata")){
            Map<String, Object> metadata = (Map<String, Object>)loadedYamlContent.get("metadata");
            log.info("metadata is: {}",metadata);
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
}
