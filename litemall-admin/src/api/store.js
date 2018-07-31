import request from '@/utils/request'

export function listStore(query) {
  return request({
    url: '/store/list',
    method: 'post',
    params: query
  })
}

export function listAdminStore(query){
  return request({
    url: '/store/adminStorelist',
    method: 'post',
    params: query
  })

}

export function createStore(data) {
  return request({
    url: '/store/create',
    method: 'post',
    data
  })
}

export function createManage(data) {
  return request({
    url: '/store/createManage',
    method: 'post',
    data
  })
}

export function createDoctor(data) {
  return request({
    url: '/store/createDoctor',
    method: 'post',
    data
  })
}



export function updateStore(data) {
  return request({
    url: '/store/update',
    method: 'post',
    data
  })
}

export function deleteStore(data) {
  return request({
    url: '/store/delete',
    method: 'post',
    data
  })
}
export function deleteStoreGoods(data) {
  return request({
    url: '/store/deleteStoreGoods',
    method: 'post',
    data
  })
}

export function deleteStoreDoctor(data) {
  return request({
    url: '/store/deleteStoreUser',
    method: 'post',
    data
  })
}



export function listUser(query) {
  return request({
    url: '/store/user',
    method: 'post',
    params: query
  })
}

export function listGoods(query) {
  return request({
    url: '/store/goodslist',
    method: 'post',
    params: query
  })
}

export function listOptions() {
  return request({
    url: '/store/goodsOptions',
    method: 'post'

  })
}


export function listProvinceOptions() {
  return request({
    url: '/store/provinceOptions',
    method: 'post'

  })
}

export function listCityOptions(data) {
  return request({
    url: '/store/cityOptions',
    method: 'post',
    data
  })
}

export function addProject(data) {
  return request({
    url: '/store/createGoods',
    method: 'post',
    data
  })
}

export function listDoctor(query) {
  return request({
    url: '/store/doctor',
    method: 'post',
    params: query
  })
}

export function addStore(data) {
  return request({
    url: '/store/addUserStore',
    method: 'post',
    params: data
  })
}


/*export function readGoods(data) {
  return request({
    url: '/goods/read',
    method: 'get',
    data
  })
}
*/

export function storeDataList(query) {
  return request({
    url: '/store/storeDataList',
    method: 'post',
    params: query
  })
}

export function getGoodsStoreList(query) {
  return request({
    url: '/store/getGoodsStoreList',
    method: 'post',
    params: query
  })
}