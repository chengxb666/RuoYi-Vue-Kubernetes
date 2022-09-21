package com.ruoyi.web.controller.kubernetes;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.kubernetes.domain.ResourceCluster;
import com.ruoyi.kubernetes.domain.ResourceConfig;
import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.service.ResourceClusterService;
import com.ruoyi.kubernetes.service.ResourceConfigService;
import com.ruoyi.kubernetes.service.ResourceInfoService;
import com.ruoyi.kubernetes.service.YamlTemplateService;
import com.ruoyi.kubernetes.utils.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/resourcecluster")
public class ResourceClusterController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ResourceClusterController.class);

    @Autowired
    private YamlTemplateService yamlTemplateService;

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Autowired
    private ResourceClusterService resourceClusterService;

    @Autowired
    private ResourceConfigService resourceConfigService;

    @Autowired
    private ClientUtils clientUtils;

    @GetMapping
    public TableDataInfo findAll(){
        startPage();
        List<ResourceCluster> list = resourceClusterService.query();
        return getDataTable(list);
    }

    @PostMapping("/cluster")
    public TableDataInfo queryByResourceInfo(@RequestBody ResourceInfo resourceInfo){
        if(resourceInfo.equals(null)){
            log.error("参数为空");
            return null;
        }
        List<ResourceCluster> resourceClusterList = resourceClusterService.queryResourceClusterByNameAndKind(resourceInfo.getResourceName(),
                resourceInfo.getResourceKind());
        return getDataTable(resourceClusterList);
    }

    @PostMapping
    public AjaxResult create(@RequestBody ResourceInfo resourceInfo) throws Exception {
        if(resourceInfo.equals(null)){
            log.error("参数为空");
            return null;
        }
        List<ResourceConfig> resourceConfigList = resourceConfigService.queryResourceConfigByNameAndKind(resourceInfo.getResourceName(),
                resourceInfo.getResourceKind());
        if(CollectionUtils.isEmpty(resourceConfigList)){
            log.error("This resource has not been configed,plseae goto 资源部署集群页面");
            return toAjax(false);
        }
        List<Integer> results = new ArrayList<>();
        for (ResourceConfig item: resourceConfigList) {
            ResourceCluster resourceCluster = new ResourceCluster();
            resourceCluster.setClusterCode(item.getClusterCode());
            resourceCluster.setResourceKind(item.getResourceKind());
            resourceCluster.setResourceName(item.getResourceName());
            resourceCluster.setNamespaceCode(item.getNamespaceCode());
            resourceCluster.setYamlContentName(resourceInfo.getYamlName());
            resourceCluster.setStatus("Commited");
            int createResult = resourceClusterService.createResource(resourceCluster);
            if(createResult > 0){
                boolean created = resourceClusterService.commitResource(resourceCluster, "update");
                log.info(resourceCluster.getResourceName() + " is " + created);
            }
            results.add(createResult);
        }
        for (int num : results) {
            if(num != 1){
                return toAjax(false);
            }
        }
        return toAjax(true);
    }

    @PostMapping("/commit")
    public AjaxResult commit(@RequestBody ResourceCluster resourceCluster) throws Exception {
        if(resourceCluster.equals(null)){
            log.error("参数为空");
            return null;
        }
        resourceCluster.setStatus("Commited");
        ResourceInfo resourceInfo = resourceInfoService.queryResourceInfoByNameAndKind(resourceCluster.getResourceName(),
                resourceCluster.getResourceKind());
        resourceInfo.setStatus("Commited");
        resourceInfoService.updateResource(resourceInfo);
        int updateResourceResult = resourceClusterService.updateResource(resourceCluster);
        boolean created = resourceClusterService.commitResource(resourceCluster, "update");
        log.info(resourceCluster.getResourceName() + " is " + created);
        return toAjax(created);
    }

    @PutMapping
    public AjaxResult update(@RequestBody ResourceCluster resourceCluster) throws Exception {
        int updateResource = resourceClusterService.updateResource(resourceCluster);
        if(updateResource>0){
            resourceClusterService.commitResource(resourceCluster,"update");
        }
        return toAjax(updateResource);
    }

    @DeleteMapping
    public AjaxResult remove(@RequestBody ResourceCluster resourceCluster) throws Exception {
        int deleteResource = resourceClusterService.deleteResource(resourceCluster);
        if(deleteResource>0){
            resourceClusterService.commitResource(resourceCluster,"delete");
        }
        return toAjax(deleteResource);
    }
}
