import request from '@/utils/request'

export function getAllAdmin(data) {
  return request({
    url: '/admin/menu/getAllMenu',
    method: 'post',
    data
  })
}

export function getAdminByIds(data) {
    return request({
      url: '/admin/menu/getAdminByIds',
      method: 'post',
      data
    })
  }

export default { getAllAdmin, getAdminByIds}