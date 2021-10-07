import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/admin/auth/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/admin/auth/info',
    method: 'get',
    // params: {
    //   token
    // }
  })
}

export function logout() {
  return request({
    url: '/admin/auth/logout',
    method: 'post'
  })
}

export function getMenu() {
  return request({
    url: '/admin/auth/getMenu',
    method: 'get'
  })
}
