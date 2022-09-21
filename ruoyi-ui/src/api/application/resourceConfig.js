import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询列表
export function listResourceConfigs(query) {
  return request({
    url: '/resourceconfig',
    method: 'get',
    params: query
  })
}

//添加
export function createResourceConfig(data) {
    return request({
      url: '/resourceconfig',
      method: 'post',
      data: data
    })
  }

// 修改
export function updateResourceConfig(data) {
  return request({
    url: '/resourceconfig',
    method: 'put',
    data: data
  })
}

// 删除
export function delResourceConfigs(resourceConfigName) {
  return request({
    url: '/resourceconfig/' + parseStrEmpty(resourceconfigName),
    method: 'delete'
  })
}