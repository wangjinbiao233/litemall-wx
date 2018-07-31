import request from '@/utils/request'

export function listKnowledge(query) {
  return request({
    url: '/knowledge/list',
    method: 'post',
    params: query
  })
}

export function createKnowledge(data) {
  return request({
    url: '/knowledge/create',
    method: 'post',
    data
  })
}

export function readKnowledge(data) {
  return request({
    url: '/knowledge/read',
    method: 'get',
    data
  })
}

export function updateKnowledge(data) {
  return request({
    url: '/knowledge/update',
    method: 'post',
    data
  })
}

export function deleteKnowledge(data) {
  return request({
    url: '/knowledge/delete',
    method: 'post',
    data
  })
}
export function getKCategory() {
  return request({
    url: '/knowledge/getKCategory',
    method: 'get'
  })
}
