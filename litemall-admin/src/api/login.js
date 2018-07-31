import request from '@/utils/request'

export function loginByUsername(username, password,browserEquipment) {
  const data = {
    username,
    password,
    browserEquipment
  }
  return request({
    url: '/login/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/login/logout',
    method: 'post'
  })
}

export function getUserInfo(token) {
  return request({
    url: '/admin/info',
    method: 'get',
    params: { token }
  })
}

export function getUserMenu(token) {
  return request({
    url: '/role/selectAdminMenu',
    method: 'post',
    params: { token }
  })
}

