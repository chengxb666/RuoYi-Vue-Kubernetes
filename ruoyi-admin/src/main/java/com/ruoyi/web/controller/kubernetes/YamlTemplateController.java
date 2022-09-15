package com.ruoyi.web.controller.kubernetes;

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
public class YamlTemplateController {

    private static final Logger log = LoggerFactory.getLogger(YamlTemplateController.class);

    @Autowired
    private YamlTemplateService yamlTemplateService;

    @Autowired
    private ClientUtils clientUtils;

    @GetMapping
    public List<YamlTemplate> findAll(){
        return yamlTemplateService.query();
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
    public Object upload(@RequestParam("file")MultipartFile file) {
        if(file.isEmpty()){
            return "未选择文件";
        }

        String result = yamlTemplateService.uploadYaml(file);
        return result;
    }

    @PutMapping
    public int updateYamlTemplate(@RequestBody YamlTemplate yamlTemplate){
        return yamlTemplateService.updateYamlTemplate(yamlTemplate);
    }

    @DeleteMapping("/{name}")
    public int removeYamlTemplate(@PathVariable("name") String name){
        return yamlTemplateService.deleteYamlTemplateByName(name);
    }
}
