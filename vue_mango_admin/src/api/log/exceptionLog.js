import request from '@/utils/request'

export function getExceptionByPage(data) {
    return request({
      url: '/admin/exceptionLog/getExceptionByPage',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/exceptionLog/delExceptionLog',
      method: 'delete',
      data:ids
    })
  }
  export default { getExceptionByPage, del}