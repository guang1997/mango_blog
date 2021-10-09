import request from '@/utils/request'

export function getAllMenu(data) {
  return request({
    url: '/admin/auth/getAllMenu',
    method: 'post',
    data
  })
}