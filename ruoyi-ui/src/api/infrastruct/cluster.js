import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询集群列表
export function listClusters(query) {
  return request({
    url: '/cluster',
    method: 'get',
    params: query
  })
}

// 查询全部集群名称列表
export function getClusterCodes() {
  return request({
    url: '/cluster/codes/',
    method: 'get'
  })
}

//添加新集群
export function createCluster(data) {
    return request({
      url: '/cluster',
      method: 'post',
      data: data
    })
  }

// 修改
export function updateCluster(data) {
  return request({
    url: '/cluster',
    method: 'put',
    data: data
  })
}

// 删除
export function delCluster(clusterCode) {
  return request({
    url: '/cluster/' + clusterCode,
    method: 'delete'
  })
}