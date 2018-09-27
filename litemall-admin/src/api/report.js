import request from '@/utils/request'

// 销售统计（产品）列表
export function listSaleProduct(query) {
  return request({
    url: '/report/saleProductList',
    method: 'get',
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
