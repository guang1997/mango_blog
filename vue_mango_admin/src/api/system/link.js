import request from '@/utils/request'

export function getLinkByPage(data) {
  return request({
    url: '/admin/link/getLinkByPage',
    method: 'post',
    data
  })
}

  export function add(data) {
    return request({
      url: '/admin/link/addLink',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/link/delLink',
      method: 'delete',
      data:ids
    })
  }
  
  export function edit(data) {
    return request({
      url: '/admin/link/editLink',
      method: 'put',
      data
    })
  }
export default { getLinkByPage, add, edit, del}