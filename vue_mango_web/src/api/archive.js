import request from '@/utils/request'

  export function getArchives(data) {
    return request({
      url: '/web/archive/getArchives',
      method: 'post',
      data
    })
  }
  export default { getArchives }