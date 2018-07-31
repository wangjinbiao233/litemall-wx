import request from '@/utils/request'

//管理员登录首页统计查询
export function reservationTotal(query) {
  return request({
    url: '/padReserve/reserveCount',
    method: 'get',
    params: query
  })
}

//某个门店某天所有的顾客预约信息
export function reservationStoreData(query) {
    return request({
      url: '/padReserve/reserveData',
      method: 'get',
      params: query
    })
  }

//某个门店所有美容师的信息
export function cosmeStoreData(query) {
  return request({
    url: '/padReserve/reserveDcotor',
    method: 'get',
    params: query
  })
}

//美容师和顾客关联保存操作
export function saveReserCosme(data) {
  return request({
    url: '/padReserve/inertDoctorRes',
    method: 'post',
    data
  })
}

//美容师登录 统计查询该美容师某个门店某个日期分配的顾客数和已服务的顾客数
export function doctorReserTotal(query){
  return request({
    url:  '/padReserve/doctorReserveCount',
    method: 'get',
    params: query
  })
}

//某个美容师某天所有的顾客预约信息
export function doctorReservationData(query) {
  return request({
    url: '/padReserve/doctorReserveData',
    method: 'get',
    params: query
  })
}

//美容师开始服务顾客
export function updateDoctorReser(data) {
  return request({
    url: '/padReserve/updateReserveStatus',
    method: 'post',
    data
  })
}