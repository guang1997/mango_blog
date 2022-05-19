import request from '@/utils/request'

export function getTagByPage(data) {
    return request({
      url: '/web/tag/getTagByPage',
      method: 'post',
      data
    })
  }

  export default { getTagByPage }