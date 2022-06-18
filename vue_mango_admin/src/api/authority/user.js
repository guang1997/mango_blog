import request from '@/utils/request'

export function getUserByPage(data) {
  return request({
    url: '/admin/user/getUserByPage',
    method: 'post',
    data
  })
}
export function edit(data) {
  return request({
    url: '/admin/user/editUser',
    method: 'put',
    data
  })
}
export default { getUserByPage,edit }