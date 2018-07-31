import request from '@/utils/request'

/* 权限添加 */
export function updateRoleMenu(query) {
  return request({
    url: '/role/insertRoleMenu',
    method: 'post',
    params: query
  })
}

/* 查询权限 */
export function getRoleMenu(query) {
  return request({
    url: '/role/selectMenuByRoleId',
    method: 'post',
    params: query
  })
}

/* 添加角色 */
export function addRole(query) {
  return request({
    url: '/role/insertSelective',
    method: 'post',
    params: query
  })
}

/* 修改角色名称 */
export function updateRoleName(query) {
  return request({
    url: '/role/updateByPrimaryKeySelective',
    method: 'post',
    params: query
  })
}

/* 角色列表 */
export function roleList(query) {
  return request({
    url: '/role/selectByRoleName',
    method: 'post',
    params: query
  })
}

/* 查询用户拥有的角色 */
export function selectAdminRole(query) {
  return request({
    url: '/role/selectAdminRole',
    method: 'post',
    params: query
  })
}

/* 添加用户与角色关联 */
export function addAdminRole(query) {
  return request({
    url: '/role/insertAdminRole',
    method: 'post',
    params: query
  })
}

/* 删除角色 */
export function removeRole(query) {
  return request({
    url: '/role/removetMenuByRoleId',
    method: 'post',
    params: query
  })
}

/* 查询当前用户权限列表 */
// export function addAdminRole(data) {
//   return request({
//     url: 'role/selectAdminMenu',
//     method: 'post',
//     data
//   })
// }
