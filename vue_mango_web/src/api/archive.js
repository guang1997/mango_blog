import request from '@/utils/request'

export function initArchives(data) {
  return request({
    url: '/web/archive/initArchives',
    method: 'post',
    data
  })
}
  export function getArchives(data) {
    return request({
      url: '/web/archive/getArchives',
      method: 'post',
      data
    })
  }
  export default { getArchives, initArchives }