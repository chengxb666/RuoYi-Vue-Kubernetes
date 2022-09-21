import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

export function listResourceCluster(query) {
  return request({
    url: '/resourcecluster',
    method: 'get',
    params: query
  })
}

export function getResourceCluster(data) {
  return request({
    url: '/resourcecluster/cluster',
    method: 'post',
    data: data
  })
}

export function createResourceCluster(data) {
    return request({
      url: '/resourcecluster',
      method: 'post',
      data: data
    })
  }

  export function commit(data) {
    return request({
      url: '/resourcecluster/commit',
      method: 'post',
      data: data
    })
  }  

export function update(data) {
  return request({
    url: '/resourcecluster',
    method: 'put',
    data: data
  })
}

// 删除
export function delResourceCluster(data) {
  return request({
    url: '/resourcecluster',
    method: 'delete',
    data: data
  })
}