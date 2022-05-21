import request from '@/utils/request'

export function getArchives(data) {
    return request({
      url: '/web/blog/getBlogByPage',
      method: 'post',
      data
    })
  }

  export default { getArchives }