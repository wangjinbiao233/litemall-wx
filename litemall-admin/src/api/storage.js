import axios from 'axios'

// create an axios instance
const service = axios.create({
  // baseURL: process.env.OS_API, // api的base_url
  baseURL: process.env.BASE_API,
  timeout: 5000 // request timeout
})

export function listStorage(query) {
  return service({
    url: '/storage/list',
    method: 'get',
    params: query
  })
}

export function createStorage(data) {
  return service({
    url: '/storage/create',
    method: 'post',
    data
  })
}
export function uploadPic(data) {
  return service({
    url: '/storage/uploadPic',
    method: 'post',
    data
  })
}

export function readStorage(data) {
  return service({
    url: '/storage/read',
    method: 'get',
    data
  })
}

export function updateStorage(data) {
  return service({
    url: '/storage/update',
    method: 'post',
    params: data
  })
}

export function deleteStorage(data) {
  debugger;
  return service({
    url: '/storage/delete',
    method: 'post',
    params: data
  })
}
