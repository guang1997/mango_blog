import request from '@/utils/request'

export function getCommentByPage(data) {
    return request({
      url: '/admin/comment/getCommentByPage',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/comment/delComment',
      method: 'delete',
      data:ids
    })
  }
  export default { getCommentByPage, del}