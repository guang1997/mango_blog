import request from '@/utils/request'

export function getSortByPage(data) {
  return request({
    url: '/admin/sort/getSortByPage',
    method: 'post',
    data
  })
}

  export function add(data) {
    return request({
      url: '/admin/sort/addSort',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/sort/delSorts',
      method: 'delete',
      data:ids
    })
  }
  
  export function edit(data) {
    return request({
      url: '/admin/sort/editSort',
      method: 'put',
      data
    })
  }
export default { getSortByPage, add, edit, del}