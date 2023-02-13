import request from '@/utils/request'

export function getSortByPage(data) {
    return request({
      url: '/web/sort/getSortByPage',
      method: 'post',
      data
    })
  }

  export default { getSortByPage }