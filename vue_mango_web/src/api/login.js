import request from '@/utils/request'

export function sendCode(data) {
  return request({
    url: '/web/login/sendCode',
    method: 'post',
    data
  })
}

export function doLogin(data) {
  return request({
    url: '/web/login/doLogin',
    method: 'post',
    data
  })
}

export default {
  sendCode,
  doLogin
}
