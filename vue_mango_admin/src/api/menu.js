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

export function getMenusByPid(data) {
  return request({
    url: '/admin/menu/getMenusByPid',
    method: 'get',
    data
  })
}
export default { getAllMenu, getMenuById, getMenusByPid}
