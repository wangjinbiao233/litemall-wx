import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/label/list',
    method: 'get',
    params: query
  })
}

export function createLabel(data) {
  return request({
    url: '/label/create',
    method: 'post',
    data
  })
}

export function updateLabel(data) {
  return request({
    url: '/label/update',
    method: 'post',
    data
  })
}

export function removeLabel(data) {
  return request({
    url: '/label/delete',
    method: 'post',
    data
  })
}
