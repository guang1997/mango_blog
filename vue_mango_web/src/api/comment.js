import request from '@/utils/request'

export function getCommentByPage(data) {
    return request({
      url: '/web/comment/getCommentByPage',
      method: 'post',
      data
    })
  }
  export function likeBlog(data) {
    return request({
      url: '/web/comment/likeBlog',
      method: 'post',
      data
    })
  }
  
  export default { getCommentByPage, likeBlog }