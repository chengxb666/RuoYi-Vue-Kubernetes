package com.ruoyi.kubernetes.mapper;

import com.ruoyi.kubernetes.domain.YamlTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YamlTemplateMapper {
    public int addYamlTemplate(YamlTemplate yamlTemplate);
    public int deleteYamlTemplate(int yamlTemplateId);
    public int deleteYamlTemplateByName(String yamlName);
    public YamlTemplate queryYamlById(int yamlTemplateId);
    public YamlTemplate queryYamlByName(String yamlTemplateCode);
    public int updateYamlTemplate(YamlTemplate yamlTemplate);
    List<YamlTemplate> query();
}
