import request from '@/utils/request'

export function getWebVisitByPage(data) {
    return request({
      url: '/admin/webVisit/getWebVisitByPage',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/webVisit/delWebVisit',
      method: 'delete',
      data:ids
    })
  }
  export default { getWebVisitByPage, del}