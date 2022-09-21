package com.ruoyi.web.controller.kubernetes;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.kubernetes.domain.ResourceConfig;
import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.service.ResourceConfigService;
import com.ruoyi.kubernetes.service.ResourceInfoService;
import com.ruoyi.kubernetes.service.YamlTemplateService;
import com.ruoyi.kubernetes.utils.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resourceconfig")
public class ResourceConfigController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ResourceConfigController.class);

    @Autowired
    private ResourceConfigService resourceConfigService;

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Autowired
    private ClientUtils clientUtils;

    @GetMapping
    public TableDataInfo findAll(){
        startPage();
        List<ResourceConfig> list = resourceConfigService.query();
        return getDataTable(list);
    }

    @PostMapping
    public  AjaxResult create(@RequestBody ResourceConfig resourceConfig) throws Exception {
        return toAjax(resourceConfigService.addResourceConfig(resourceConfig));
    }

    @PutMapping
    public AjaxResult update(@RequestBody ResourceConfig resourceConfig){
        return toAjax(resourceConfigService.updateResourceConfigByName(resourceConfig));
    }

    @DeleteMapping("/{name}")
    public AjaxResult remove(@PathVariable("name") String name){
        return toAjax(resourceConfigService.deleteResourceConfigByName(name));
    }
}
