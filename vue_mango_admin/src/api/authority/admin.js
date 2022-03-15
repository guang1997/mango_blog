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
  export function add(data) {
    return request({
      url: '/admin/manager/addAdmin',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/manager/delAdmin',
      method: 'delete',
      data:ids
    })
  }
  
  export function edit(data) {
    return request({
      url: '/admin/manager/editAdmin',
      method: 'put',
      data
    })
  }
export default { getAdminByPage, getAdminByIds, add, edit, del}