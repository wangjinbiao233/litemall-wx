import request from '@/utils/request'

export function listDistribution(query) {
  return request({
    url: '/distributionApply/distributionList',
    method: 'post',
    params: query
  })
}

export function selectDistributionTypeList(query) {
  return request({
    url: '/distributionApply/selectDistributionTypeList',
    method: 'post',
    params: query
  })
}


export function updateDistribution(data) {
  return request({
    url: '/distributionApply/update',
    method: 'post',
    data
  })
}

export function distributionReportList(query) {
  return request({
    url: '/distribution/distributionReportList',
    method: 'post',
    params: query
  })
}


