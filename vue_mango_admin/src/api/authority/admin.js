import request from '@/utils/request'

export function getAdminByPage(data) {
  return request({
    url: '/admin/manager/getAdminByPage',
    method: 'post',
    data
  })
}

export function getAdminByIds(data) {
    return request({
      url: '/admin/manager/getAdminByIds',
      method: 'post',
      data
    })
  }

export default { getAdminByPage, getAdminByIds}