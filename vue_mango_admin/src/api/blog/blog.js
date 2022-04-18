import request from '@/utils/request'

export function getBlogByPage(data) {
  return request({
    url: '/admin/blog/getBlogByPage',
    method: 'post',
    data
  })
}

  export function add(data) {
    return request({
      url: '/admin/blog/addBlog',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/blog/delBlog',
      method: 'delete',
      data:ids
    })
  }
  
  export function edit(data) {
    return request({
      url: '/admin/blog/editBlog',
      method: 'put',
      data
    })
  }
export default { getBlogByPage, add, edit, del}