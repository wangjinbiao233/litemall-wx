import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/user/list',
    method: 'get',
    params: query
  })
}

export function createUser(data) {
  return request({
    url: '/user/create',
    method: 'post',
    data
  })
}

export function readUser(data) {
  return request({
    url: '/user/detail',
    method: 'get',
    params: data
  })
}

export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'post',
    data
  })
}

export function getUserRole(data) {
  return request({
    url: '/role/selectAdminRole',
    method: 'post',
    data
  })
}

export function getOrderList(data) {
  return request({
    url: '/user/findUserOrder',
    method: 'post',
    data
  })
}

export function getReserveList(data) {
  return request({
    url: '/user/findUserReserve',
    method: 'post',
    data
  })
}

export function getDiscountList(data) {
  return request({
    url: '/user/findUserDiscount',
    method: 'post',
    data
  })
}

export function selectParentUserInfo(data) {
  return request({
    url: '/distribution/selectParentUserInfoByUserId',
    method: 'get',
    params: data
  })
}

export function selectSubUserInfo(data) {
  return request({
    url: '/distribution/selectSubUserInfoByUserId',
    method: 'get',
    params: data
  })
}

export function selectSubSubUserInfo(data) {
  return request({
    url: '/distribution/selectSubSubUserInfoByUserId',
    method: 'get',
    params: data
  })
}

export function getTransactionRecordList(data) {
  return request({
    url: '/distribution/selectTransactionRecord',
    method: 'get',
    params: data
  })
}

export function createRecharge(data) {
  return request({
    url: '/distribution/recharge',
    method: 'get',
    params: data
  })
}

export function getDiscountOrderDetail(data){
  return request({
    url: '/user/getDiscountOrderDetail',
    method: 'get',
    params: data
  })
}

