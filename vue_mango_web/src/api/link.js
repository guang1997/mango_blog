import request from '@/utils/request'

export function getFriendLink(data) {
    return request({
      url: '/web/link/getFriendLink',
      method: 'post',
      data
    })
  }

  
export function saveFriendLink(data) {
  return request({
    url: '/web/link/saveFriendLink',
    method: 'post',
    data
  })
}
  export default { getFriendLink, saveFriendLink }