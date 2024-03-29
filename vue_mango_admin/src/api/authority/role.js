import request from '@/utils/request'

export function getRoleByPage(data) {
  return request({
    url: '/admin/role/getRoleByPage',
    method: 'post',
    data
  })
}

export function getRoleById(id) {
  return request({
    url: '/admin/role/getRoleById?id=' + id,
    method: 'get'
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
export function editMenu(data) {
  return request({
    url: '/admin/role/menu',
    method: 'post',
    data
  })
}
export default { getRoleByPage, add, del, edit, editMenu, getRoleById}