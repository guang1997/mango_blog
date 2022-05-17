import request from '@/utils/request'

export function getBlogs(data) {
    return request({
      url: '/web/blog/getBlogs',
      method: 'post',
      data
    })
  }

  export default { getBlogs }