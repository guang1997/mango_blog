import request from '@/utils/request'

export function getWebConfig() {
    return request({
      url: '/web/webConfig/getWebConfig',
      method: 'post'
    })
  }

  export default { getWebConfig }  