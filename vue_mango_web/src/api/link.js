import request from '@/utils/request'

export function getFriendLink(data) {
    return request({
      url: '/web/link/getFriendLink',
      method: 'post',
      data
    })
  }
  export default { getFriendLink }