package com.ruoyi.kubernetes.service;

import com.ruoyi.kubernetes.domain.YamlTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface YamlTemplateService {
    public int addYamlTemplate(YamlTemplate yamlTemplate);
    public int deleteYamlTemplate(int yamlTemplateid);
    public int deleteYamlTemplateByName(String yamlName);
    public YamlTemplate queryYamlByid(int yamlTemplateid);
    public YamlTemplate queryYamlByName(String yamlTemplateCode);
    public int updateYamlTemplate(YamlTemplate yamlTemplate);
    List<YamlTemplate> query();
    public String uploadYaml(MultipartFile file);
}
