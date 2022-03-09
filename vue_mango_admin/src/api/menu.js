import request from '@/utils/request'

export function getAllMenu(data) {
  return request({
    url: '/admin/menu/getAllMenu',
    method: 'post',
    data
  })
}

export function getMenuById(id) {
  return request({
    url: '/admin/menu/getMenuById?id=' + id,
    method: 'get'
  })
}

export function getMenusByPid(pid) {
  return request({
    url: '/admin/menu/getMenusByPid?pid=' + pid,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/admin/menu/addMenu',
    method: 'post',
    data
  })
}
export function del(ids) {
  return request({
    url: '/admin/menu/delMenu',
    method: 'delete',
    data:ids
  })
}

export function edit(data) {
  return request({
    url: '/admin/menu/editMenu',
    method: 'put',
    data
  })
}
export default { getAllMenu, getMenuById, getMenusByPid, add, del, edit}