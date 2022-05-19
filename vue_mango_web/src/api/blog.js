import request from '@/utils/request'

export function getBlogByPage(data) {
    return request({
      url: '/web/blog/getBlogByPage',
      method: 'post',
      data
    })
  }

  export default { getBlogByPage }