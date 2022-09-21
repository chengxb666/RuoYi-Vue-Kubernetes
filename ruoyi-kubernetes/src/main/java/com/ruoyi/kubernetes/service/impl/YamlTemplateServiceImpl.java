package com.ruoyi.kubernetes.service.impl;

import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.domain.YamlTemplate;
import com.ruoyi.kubernetes.mapper.YamlTemplateMapper;
import com.ruoyi.kubernetes.service.ResourceInfoService;
import com.ruoyi.kubernetes.service.YamlTemplateService;
import com.ruoyi.kubernetes.utils.GeneralUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class YamlTemplateServiceImpl implements YamlTemplateService {

    private static final Logger log = LoggerFactory.getLogger(YamlTemplateServiceImpl.class);

    @Autowired
    private YamlTemplateMapper yamlTemplateMapper;

    @Autowired
    private ResourceInfoService resourceInfoService;

    @Autowired
    private GeneralUtils generalUtils;

    @Value("${ruoyi.profile}")
    private String AbsoluteFilePathPrefix;

    @Override
    public int addYamlTemplate(YamlTemplate yamlTemplate) {
        return yamlTemplateMapper.addYamlTemplate(yamlTemplate);
    }

    @Override
    public int deleteYamlTemplate(int yamlTemplateid) {
        return yamlTemplateMapper.deleteYamlTemplate(yamlTemplateid);
    }

    @Override
    public int deleteYamlTemplateByName(String yamlName) {
        return yamlTemplateMapper.deleteYamlTemplateByName(yamlName);
    }

    @Override
    public YamlTemplate queryYamlByid(int yamlTemplateid) {
        return yamlTemplateMapper.queryYamlById(yamlTemplateid);
    }

    @Override
    public YamlTemplate queryYamlByName(String yamlTemplateCode) {
        return yamlTemplateMapper.queryYamlByName(yamlTemplateCode);
    }

    @Override
    public int updateYamlTemplate(YamlTemplate yamlTemplate) {
        return yamlTemplateMapper.updateYamlTemplate(yamlTemplate);
    }

    @Override
    public List<YamlTemplate> query() {
        return yamlTemplateMapper.query();
    }

    @Override
    @Transactional
    public String uploadYaml(MultipartFile file){

        try {
            String fileName = file.getOriginalFilename();
            StringBuilder yamlContent = new StringBuilder();
            try (Scanner in = new Scanner(file.getInputStream(),"UTF-8")){
                while(in.hasNext()){
                    yamlContent.append(in.nextLine()).append("\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Map<String, String> kindAndNameMap = generalUtils.resourceInitialResolver(yamlContent.toString());
            String yamlName = kindAndNameMap.get("name")+"-"+kindAndNameMap.get("kind")+".yaml";
            String savedYamlContentPath = AbsoluteFilePathPrefix+yamlName;
            File storeYaml = new File(savedYamlContentPath);
            try(FileOutputStream fileOutputStream = new FileOutputStream(storeYaml)){
                FileUtils.writeBytesToFile(yamlContent.toString().getBytes(),fileOutputStream);
            }
            YamlTemplate yamlTemplate = new YamlTemplate();
            yamlTemplate.setYamlName(yamlName);
            yamlTemplate.setYamlContent(yamlContent.toString());
            yamlTemplate.setStatus("uploaded");
            int addCount = yamlTemplateMapper.addYamlTemplate(yamlTemplate);
            ResourceInfo resourceInfo = resourceInfoService.declearResource(yamlTemplate);
            resourceInfoService.createResource(resourceInfo);
            return "上传成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }


    }
}
