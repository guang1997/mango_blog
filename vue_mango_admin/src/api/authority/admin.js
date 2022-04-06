import request from '@/utils/request'
import { encrypt } from '@/utils/rsaEncrypt'

export function getAdminByPage(data) {
  return request({
    url: '/admin/manager/getAdminByPage',
    method: 'post',
    data
  })
}

export function getAdminByIds(data) {
    return request({
      url: '/admin/manager/getAdminByIds',
      method: 'post',
      data
    })
  }
  export function add(data) {
    return request({
      url: '/admin/manager/addAdmin',
      method: 'post',
      data
    })
  }
  export function del(ids) {
    return request({
      url: '/admin/manager/delAdmin',
      method: 'delete',
      data:ids
    })
  }
  
  export function edit(data) {
    return request({
      url: '/admin/manager/editAdmin',
      method: 'put',
      data
    })
  }
  export function editAdminFromCenter(data) {
    return request({
      url: '/admin/manager/editAdminFromCenter',
      method: 'put',
      data
    })
  }
  export function updatePass(user) {
    const data = {
      oldPass: encrypt(user.oldPass),
      newPass: encrypt(user.newPass)
    }
    return request({
      url: '/admin/manager/updatePass',
      method: 'post',
      data
    })
  }

  export function updateEmail(form) {
    const data = {
      pass: encrypt(form.pass),
      email: form.email,
      code: form.code
    }
    return request({
      url: '/admin/manager/updateEmail',
      method: 'post',
      data
    })
  }
export default { updateEmail, editAdminFromCenter, updatePass, getAdminByPage, getAdminByIds, add, edit, del}