import request from '@/utils/request'

// 销售订单统计列表
export function listSaleOrder(query) {
  return request({
    url: '/report/saleOrderList',
    method: 'post',
    params: query
  })
}

// 销售执行统计列表
export function listSaleExecute(query) {
  return request({
    url: '/report/saleExcuteList',
    method: 'post',
    params: query
  })
}

// 用户账户余额统计列表
export function listAccountBanlance(query) {
  return request({
    url: '/report/accountBanlanceList',
    method: 'post',
    params: query
  })
}
// 用户存储金对账明细（时间段）列表
export function listAccountCheck(query) {
  return request({
    url: '/report/accountCheckList',
    method: 'post',
    params: query
  })
}

// 提现明细（时间段）列表
export function listAccountWithdraw(query) {
  return request({
    url: '/report/accountWithdraw',
    method: 'post',
    params: query
  })
}
