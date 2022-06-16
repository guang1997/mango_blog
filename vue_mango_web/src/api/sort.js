import request from '@/utils/request'

export function initSortByPage(data) {
  return request({
    url: '/web/sort/initSortByPage',
    method: 'post',
    data
  })
}

export function getSortByPage(data) {
    return request({
      url: '/web/sort/getSortByPage',
      method: 'post',
      data
    })
  }

  export default { getSortByPage, initSortByPage }