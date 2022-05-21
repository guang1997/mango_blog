import request from '@/utils/request'

export function getBlogByPage(data) {
    return request({
      url: '/web/blog/getBlogByPage',
      method: 'post',
      data
    })
  }
  export function getBlogById(data) {
    return request({
      url: '/web/blog/getBlogById',
      method: 'post',
      data
    })
  }
  export function getBlogBySortId(data) {
    return request({
      url: '/web/blog/getBlogBySortId',
      method: 'post',
      data
    })
  }
  
  export default { getBlogByPage, getBlogById, getBlogBySortId }