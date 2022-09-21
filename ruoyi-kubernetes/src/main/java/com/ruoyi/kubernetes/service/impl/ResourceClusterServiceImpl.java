package com.ruoyi.kubernetes.service.impl;

import com.ruoyi.kubernetes.domain.ResourceCluster;
import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.domain.YamlTemplate;
import com.ruoyi.kubernetes.mapper.ResourceClusterMapper;
import com.ruoyi.kubernetes.service.ResourceClusterService;
import com.ruoyi.kubernetes.service.ResourceInfoService;
import com.ruoyi.kubernetes.service.YamlTemplateService;
import com.ruoyi.kubernetes.utils.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class ResourceClusterServiceImpl implements ResourceClusterService {

    private static final Logger log = LoggerFactory.getLogger(ResourceClusterServiceImpl.class);

    @Autowired
    private ResourceClusterMapper resourceClusterMapper;

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Autowired
    private YamlTemplateService yamlTemplateService;

    @Autowired
    private Commit commit;

    @Value("${ruoyi.profile}")
    private String AbsoluteFilePathPrefix;

    @Override
    @Transactional
    public int createResource(ResourceCluster resourceCluster) throws Exception {

        List<ResourceCluster> queriedResourceCluster = this.queryResourceClusterByNameAndKind(resourceCluster.getResourceName(), resourceCluster.getResourceKind());
        if(!CollectionUtils.isEmpty(queriedResourceCluster)){
            log.error("Resource named {} is existed", resourceCluster.getResourceName());
            throw new Exception("Resource named " + resourceCluster.getResourceName() + "is existed");
        }
        YamlTemplate yamlTemplate = yamlTemplateService.queryYamlByName(resourceCluster.getYamlContentName());
        if(null != yamlTemplate){
            yamlTemplate.setStatus("commited");
            int i = yamlTemplateService.updateYamlTemplate(yamlTemplate);
            log.info("during create, " + i + " yamltemplate is commited");
        }
        ResourceInfo resourceInfo = resourceInfoService.queryResourceInfoByNameAndKind(resourceCluster.getResourceName(), resourceCluster.getResourceKind());
        if(null != resourceInfo){
            resourceInfo.setStatus("commited");
            int i = resourceInfoService.updateResource(resourceInfo);
            log.info("during create, " + i + " resourceinfo is commited");
        }
        return resourceClusterMapper.addResource(resourceCluster);
    }

    @Override
    @Transactional
    public int updateResource(ResourceCluster resourceCluster) throws Exception {

        List<ResourceCluster> queriedResourceCluster = this.queryResourceClusterByNameAndKind(resourceCluster.getResourceName(), resourceCluster.getResourceKind());
        if(CollectionUtils.isEmpty(queriedResourceCluster)){
            log.error("Resource named {} is not existed", resourceCluster.getResourceName());
            throw new Exception("Resource named " + resourceCluster.getResourceName() + "is not existed");
        }

        return resourceClusterMapper.updateResourceByNameAndKind(resourceCluster);
    }

    @Override
    @Transactional
    public int deleteResource(ResourceCluster resourceCluster) throws Exception {

        List<ResourceCluster> queriedResourceCluster = this.queryResourceClusterByNameAndKind(resourceCluster.getResourceName(), resourceCluster.getResourceKind());
        if(CollectionUtils.isEmpty(queriedResourceCluster)){
            log.error("Resource named {} is not existed", resourceCluster.getResourceName());
            throw new Exception("Resource named " + resourceCluster.getResourceName() + "is not existed");
        }

        YamlTemplate yamlTemplate = yamlTemplateService.queryYamlByName(resourceCluster.getYamlContentName());
        if(null != yamlTemplate){
            yamlTemplate.setStatus("Deleted");
            int i = yamlTemplateService.updateYamlTemplate(yamlTemplate);
            log.info("during delete, " + i + " yamltemplate is deleted");
        }
        ResourceInfo resourceInfo = resourceInfoService.queryResourceInfoByNameAndKind(resourceCluster.getResourceName(), resourceCluster.getResourceKind());
        if(null != resourceInfo){
            resourceInfo.setStatus("Deleted");
            int i = resourceInfoService.updateResource(resourceInfo);
            log.info("during delete, " + i + " resourceinfo is deleted");
        }
        return resourceClusterMapper.deleteResourceByNameAndKind(resourceCluster.getResourceName(),
                resourceCluster.getResourceKind(),resourceCluster.getNamespaceCode());
    }

    @Override
    public List<ResourceCluster> queryResourceClusterByNameAndKind(String resourceName, String resourceKind) {
        return resourceClusterMapper.queryResourceByNameAndKind(resourceKind,resourceName);
    }

    @Override
    public List<ResourceCluster> query() {
        return resourceClusterMapper.query();
    }

    @Override
    public List<ResourceCluster> queryByResourceKind(String resourceKind) {
        return resourceClusterMapper.queryResourceByKind(resourceKind);
    }

    @Override
    public boolean commitResource(ResourceCluster resourceCluster, String action) throws FileNotFoundException {
        String clusterCode = resourceCluster.getClusterCode();
        String namespaceCode = resourceCluster.getNamespaceCode();
        String yamlPath = AbsoluteFilePathPrefix+resourceCluster.getYamlContentName();
        Boolean commited = commit.DoCommit(yamlPath, namespaceCode, clusterCode, action);
        return commited;
    }
}
