import request from '@/utils/request'

export function getAllRole(data) {
  return request({
    url: '/admin/role/getAllRole',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/admin/role/addRole',
    method: 'post',
    data
  })
}
export function del(ids) {
  return request({
    url: '/admin/role/delRole',
    method: 'delete',
    data:ids
  })
}

export function edit(data) {
  return request({
    url: '/admin/role/editRole',
    method: 'put',
    data
  })
}
export default { getAllRole, add, del, edit}