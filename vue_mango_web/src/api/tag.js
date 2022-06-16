import request from '@/utils/request'

export function initTagByPage(data) {
  return request({
    url: '/web/tag/initTagByPage',
    method: 'post',
    data
  })
}

export function getTagByPage(data) {
    return request({
      url: '/web/tag/getTagByPage',
      method: 'post',
      data
    })
  }

  export default { getTagByPage, initTagByPage }