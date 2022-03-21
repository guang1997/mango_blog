import request from '@/utils/request'

export function getDictDetailByPage(data) {
  return request({
    url: '/admin/dictDetail/getDictDetailByPage',
    method: 'post',
    data
  })
}
  export function add(data) {
    return request({
      url: '/admin/dictDetail/addDictDetail',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/dictDetail/delDictDetail',
      method: 'delete',
      data:ids
    })
  }
  
  export function edit(data) {
    return request({
      url: '/admin/dictDetail/editDictDetail',
      method: 'put',
      data
    })
  }
export default {getDictDetailByPage, add, edit, del}