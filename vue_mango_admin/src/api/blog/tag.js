import request from '@/utils/request'

export function getTagByPage(data) {
  return request({
    url: '/admin/tag/getTagByPage',
    method: 'post',
    data
  })
}

  export function add(data) {
    return request({
      url: '/admin/tag/addTag',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/tag/delTags',
      method: 'delete',
      data:ids
    })
  }
  
  export function edit(data) {
    return request({
      url: '/admin/tag/editTag',
      method: 'put',
      data
    })
  }
export default { getTagByPage, add, edit, del}