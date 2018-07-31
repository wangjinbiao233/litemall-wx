import request from '@/utils/request'

export function couponList(query) {
  return request({
    url: '/discount/selectDiscountList',
    method: 'post',
    params: query
  })
}


export function CreateCoupon(data) {
  debugger;
  return request({
    url: '/discount/createDiscount',
    method: 'post',
    params: data
  })
}

export function updateCoupon(data) {
  debugger;
  return request({
    url: '/discount/updateDiscount',
    method: 'post',
    params: data
  })
}

