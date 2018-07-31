import request from '@/utils/request'

export function listProduct(query) {
  return request({
    url: '/product/list',
    method: 'get',
    params: query
  })
}

// add by pengxb
export function listSpecGroup(query) {
  return request({
    url: '/product/listSpecGroup',
    method: 'get',
    params: query
  })
}

// add by pengxb @2018-05-14 11:21
export function listProductSpec(query) {
  return request({
    url: '/product/listProductSpec',
    method: 'get',
    params: query
  })
}

export function createProduct(data) {
  return request({
    url: '/product/create',
    method: 'post',
    data
  })
}

export function readProduct(data) {
  return request({
    url: '/product/read',
    method: 'get',
    data
  })
}

export function updateProduct(data) {
  return request({
    url: '/product/update',
    method: 'post',
    data
  })
}

export function deleteProduct(data) {
  return request({
    url: '/product/delete',
    method: 'post',
    data
  })
}

export function saveProduct(data) {
  return request({
    url: '/product/save',
    method: 'post',
    data
  })
}
