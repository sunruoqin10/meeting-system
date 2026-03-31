import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getUserInfo(token) {
  return request({
    url: '/auth/user/info',
    method: 'get',
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
}

export function updateUserInfo(data) {
  return request({
    url: '/auth/user/info',
    method: 'put',
    data
  })
}

export function updatePassword(data) {
  return request({
    url: '/auth/user/password',
    method: 'put',
    data
  })
}
