import request from '@/utils/request'

export function getDictByPage(data) {
  return request({
    url: '/admin/dict/getDictByPage',
    method: 'post',
    data
  })
}
  export function add(data) {
    return request({
      url: '/admin/dict/addDict',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/dict/delDict',
      method: 'delete',
      data:ids
    })
  }
  
  export function edit(data) {
    return request({
      url: '/admin/dict/editDict',
      method: 'put',
      data
    })
  }
export default { getDictByPage, add, edit, del}