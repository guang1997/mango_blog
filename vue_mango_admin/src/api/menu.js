import request from '@/utils/request'

export function getAllMenu(data) {
  return request({
    url: '/admin/menu/getAllMenu',
    method: 'post',
    data
  })
}

export function getMenuById(data) {
  return request({
    url: '/admin/menu/getMenuById',
    method: 'get',
    data
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

export default { getAllMenu, getMenuById, getMenusByPid, add }