import request from '@/utils/request'

export function getCommentByPage(data) {
    return request({
      url: '/web/comment/getCommentByPage',
      method: 'post',
      data
    })
  }

  export function getMessageBoardCommentByPage(data) {
    return request({
      url: '/web/comment/getMessageBoardCommentByPage',
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
  export function saveComment(data) {
    return request({
      url: '/web/comment/saveComment',
      method: 'post',
      data
    })
  }
  export function likeComment(data) {
    return request({
      url: '/web/comment/likeComment',
      method: 'post',
      data
    })
  }
  export default { getCommentByPage, likeBlog, saveComment, likeComment, getMessageBoardCommentByPage }