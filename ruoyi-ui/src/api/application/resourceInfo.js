import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询资源信息列表
export function listResourceInfo(query) {
  return request({
    url: '/resourceinfo',
    method: 'get',
    params: query
  })
}

// 查询资源信息详细---todo
export function getResource(resourceName) {
  return request({
    url: '/resourceinfo/name/' + parseStrEmpty(resourceName),
    method: 'get'
  })
}

// 修改
export function update(data) {
  return request({
    url: '/resourceinfo',
    method: 'put',
    data: data
  })
}

// 删除
export function delResourceInfo(resourceName) {
  return request({
    url: '/resourceinfo/' + parseStrEmpty(resourceName),
    method: 'delete'
  })
}