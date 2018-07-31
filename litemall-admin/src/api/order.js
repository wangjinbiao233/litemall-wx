import request from '@/utils/request'

export function listOrder(query) {
  return request({
    url: '/order/list',
    method: 'post',
    params: query
  })
}

export function listExpress(query) {
  return request({
    url: '/express/list',
    method: 'get',
    params: query
  })
}

export function reserveDetail(query) {
  return request({
    url: '/reserve/reserveDetail',
    method: 'get',
    params: query
  })
}

export function createOrder(data) {
  return request({
    url: '/order/create',
    method: 'post',
    data
  })
}

export function readOrder(data) {
  return request({
    url: '/order/read',
    method: 'get',
    data
  })
}

export function updateOrder(data) {
  return request({
    url: '/order/update',
    method: 'post',
    data
  })
}
export function updateOrderStatus(data) {
  return request({
    url: '/order/updateOrderStatus',
    method: 'post',
    data
  })
}

export function orderReturn(data) {
  return request({
    url: '/order/orderReturn',
    method: 'post',
    data
  })
}

export function deleteOrder(data) {
  return request({
    url: '/order/delete',
    method: 'post',
    data
  })
}
