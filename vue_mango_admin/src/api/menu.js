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

export function getMenus(data) {
  return request({
    url: '/admin/menu/getMenusByPid',
    method: 'get',
    data
  })
}