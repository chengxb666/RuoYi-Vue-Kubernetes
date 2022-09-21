package com.ruoyi.web.controller.kubernetes;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.domain.YamlTemplate;
import com.ruoyi.kubernetes.service.ResourceInfoService;
import com.ruoyi.kubernetes.service.YamlTemplateService;
import com.ruoyi.kubernetes.utils.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/resourceinfo")
public class ResourceInfoController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ResourceInfoController.class);

    @Autowired
    private YamlTemplateService yamlTemplateService;

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Autowired
    private ClientUtils clientUtils;

    @GetMapping
    public TableDataInfo findAll(){
        startPage();
        List<ResourceInfo> list = resourceInfoService.query();
        return getDataTable(list);
    }

    /*@GetMapping()
    public  queryByNameAndKind(){

    }*/

    /*@PostMapping
    public int create(@RequestBody YamlTemplate yamlTemplate){
        return yamlTemplateService.addYamlTemplate(yamlTemplate);
    }*/

    @PutMapping
    public AjaxResult update(@RequestBody ResourceInfo resourceInfo){
        return toAjax(resourceInfoService.updateResource(resourceInfo));
    }

    @DeleteMapping("/{name}")
    public AjaxResult remove(@PathVariable("name") String name){
        return toAjax(resourceInfoService.deleteResource(name));
    }
}
