import request from '@/utils/request'

export function listAnalyse(query) {
  return request({
    url: '/analyse/analyseList',
    method: 'post',
    params: query
  })
}

export function listAnalyseDetail(query) {
  return request({
    url: '/analyse/analyseDetailList',
    method: 'post',
    params: query
  })
}






