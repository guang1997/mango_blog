import request from '@/utils/request'

export function getWebConfig() {
    return request({
      url: '/admin/webConfig/getWebConfig',
      method: 'post'
    })
  }

  export function edit(data) {
    return request({
      url: '/admin/webConfig/editWebConfig',
      method: 'post',
      data
    })
  }

  export default {getWebConfig, edit}