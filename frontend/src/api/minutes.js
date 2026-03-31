import request from '@/utils/request'

export function getMinutesList(params) {
  return request({
    url: '/minutes',
    method: 'get',
    params
  })
}

export function getMinuteDetail(id) {
  return request({
    url: `/minutes/${id}`,
    method: 'get'
  })
}

export function createMinute(data) {
  return request({
    url: '/minutes',
    method: 'post',
    data
  })
}

export function updateMinute(id, data) {
  return request({
    url: `/minutes/${id}`,
    method: 'put',
    data
  })
}

export function updateMinuteStatus(id, status) {
  return request({
    url: `/minutes/${id}/status`,
    method: 'put',
    data: { status }
  })
}

export function deleteMinute(id) {
  return request({
    url: `/minutes/${id}`,
    method: 'delete'
  })
}
