import request from '@/utils/request'

export function listDictionary(query) {
  return request({
    url: '/dictionary/list',
    method: 'get',
    params: query
  })
}


