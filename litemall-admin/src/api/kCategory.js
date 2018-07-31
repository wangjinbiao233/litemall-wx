import request from '@/utils/request'

export function listCategory(query) {
  return request({
    url: '/kCategory/list',
    method: 'post',
    params: query
  })
}

export function createCategory(data) {
  return request({
    url: '/kCategory/create',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/kCategory/update',
    method: 'post',
    data
  })
}

export function deleteCategory(data) {
  return request({
    url: '/kCategory/delete',
    method: 'post',
    data
  })
}
