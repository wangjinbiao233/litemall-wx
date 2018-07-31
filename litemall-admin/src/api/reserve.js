import request from '@/utils/request'

export function listReserve(query) {
  return request({
    url: '/reserve/reserveList',
    method: 'post',
    params: query
  })
}

export function updateReserve(data) {
  return request({
    url: '/reserve/update',
    method: 'post',
    data
  })
}


export function cancelReserve(data) {
  return request({
    url: '/reserve/cancel',
    method: 'post',
    data
  })
}

export function listStore(query) {
  return request({
    url: '/store/storeDataList',
    method: 'post',
    params: query
  })
}



