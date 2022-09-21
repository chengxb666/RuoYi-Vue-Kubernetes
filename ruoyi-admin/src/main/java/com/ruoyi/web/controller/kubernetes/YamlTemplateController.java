package com.ruoyi.web.controller.kubernetes;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.kubernetes.domain.YamlTemplate;
import com.ruoyi.kubernetes.service.YamlTemplateService;
import com.ruoyi.kubernetes.utils.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/yaml")
public class YamlTemplateController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(YamlTemplateController.class);

    @Autowired
    private YamlTemplateService yamlTemplateService;

    @Autowired
    private ClientUtils clientUtils;

    @GetMapping
    public TableDataInfo findAll(){
        startPage();
        List<YamlTemplate> list = yamlTemplateService.query();
        return getDataTable(list);
    }

    @GetMapping("/id/{id}")
    public YamlTemplate queryYamlTemplateById(@PathVariable("id") int id){
        return yamlTemplateService.queryYamlByid(id);
    }

    @GetMapping("/name/{name}")
    public YamlTemplate queryYamlTemplateByName(@PathVariable("name") String name){
        return yamlTemplateService.queryYamlByName(name);
    }

    @PostMapping
    public int createYamlTemplate(@RequestBody YamlTemplate yamlTemplate){
        return yamlTemplateService.addYamlTemplate(yamlTemplate);
    }

    @PostMapping("upload")
    public AjaxResult upload(@RequestParam("file")MultipartFile file) {
        if(file.isEmpty()){
            log.error("no file upload,please check it");
            return toAjax(false);
        }

        String result = yamlTemplateService.uploadYaml(file);
        log.info("file {} upload",file.getName());
        return toAjax(result.equals("上传成功")?true:false);
    }

    @PutMapping
    public int updateYamlTemplate(@RequestBody YamlTemplate yamlTemplate){
        return yamlTemplateService.updateYamlTemplate(yamlTemplate);
    }

    @DeleteMapping("/{name}")
    public AjaxResult removeYamlTemplate(@PathVariable("name") String name){
        return toAjax(yamlTemplateService.deleteYamlTemplateByName(name));
    }
}
