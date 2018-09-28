import request from '@/utils/request'

// 销售订单统计列表
export function listSaleOrder(query) {
  return request({
    url: '/report/saleOrderList',
    method: 'post',
    params: query
  })
}
// 销售统计（门店）列表
export function listSaleStore(query) {
  return request({
    url: '/report/saleStoreList',
    method: 'get',
    params: query
  })
}
// 销售统计（顾问）列表
export function listSaleCounselor(query) {
  return request({
    url: '/report/saleCounselorList',
    method: 'get',
    params: query
  })
}
// 销售统计（时间段）列表
export function listSaleTime(query) {
  return request({
    url: '/report/saleTimeList',
    method: 'get',
    params: query
  })
}
