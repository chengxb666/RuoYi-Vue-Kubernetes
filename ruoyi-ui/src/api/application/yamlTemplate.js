import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询Yaml文件列表
export function listYaml(query) {
  return request({
    url: '/yaml',
    method: 'get',
    params: query
  })
}

// 查询Yaml文件详细
export function getYaml(yamlName) {
  return request({
    url: '/yaml/name/' + parseStrEmpty(yamlName),
    method: 'get'
  })
}

export function getYamlById(yamlId) {
    return request({
      url: '/yaml/id/' + parseStrEmpty(yamlId),
      method: 'get'
    })
  }

// 新增Yaml文件
export function addYaml(data) {
  return request({
    url: '/yaml',
    method: 'post',
    data: data
  })
}

// 修改Yaml文件
export function updateYaml(data) {
  return request({
    url: '/yaml',
    method: 'put',
    data: data
  })
}

// 删除Yaml文件
export function delYaml(yamlName) {
  return request({
    url: '/yaml/' + yamlName,
    method: 'delete'
  })
}
