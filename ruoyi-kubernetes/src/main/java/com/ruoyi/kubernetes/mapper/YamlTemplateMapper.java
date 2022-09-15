package com.ruoyi.kubernetes.mapper;

import com.ruoyi.kubernetes.domain.YamlTemplate;

import java.util.List;

public interface YamlTemplateMapper {
    public int addYamlTemplate(YamlTemplate yamlTemplate);
    public int deleteYamlTemplate(int yamlTemplateid);
    public int deleteYamlTemplateByName(String yamlName);
    public YamlTemplate queryYamlByid(int yamlTemplateid);
    public YamlTemplate queryYamlByName(String yamlTemplateCode);
    public int updateYamlTemplate(YamlTemplate yamlTemplate);
    List<YamlTemplate> query();
}
